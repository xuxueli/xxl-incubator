package com.xxl.util.core.util;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import org.apache.commons.exec.*;

import java.io.*;

/**
 *  1、内嵌编译器如"PythonInterpreter"无法引用扩展包，因此推荐使用java调用控制台进程方式"Runtime.getRuntime().exec()"来运行脚本(shell或python)；
 *  2、因为通过java调用控制台进程方式实现，需要保证目标机器PATH路径正确配置对应编译器；
 *  3、暂时脚本执行日志只能在脚本执行结束后一次性获取，无法保证实时性；因此为确保日志实时性，可改为将脚本打印的日志存储在指定的日志文件上；
 *
 *  知识点：
 *      1、日志输出到日志文件：[>>logfile 2>&1]：将错误输出2以及标准输出1都一起以附加写方式导入logfile文件
 *      2、python 异常输出优先级高于标准输出，体现在Log文件中，因此推荐通过logging方式打日志保持和异常信息一致；否则用prinf日志顺序会错乱
 *
 * Created by xuxueli on 17/2/25.
 */
public class ScriptUtil {

    private static String pyCmd = "python";
    private static String shllCmd = "bash";
    private static String pyFile = "/Users/xuxueli/workspaces/idea-git-workspace/github/xxl-incubator/xxl-util/src/main/resources/script/pytest.py";
    private static String shellFile = "/Users/xuxueli/workspaces/idea-git-workspace/github/xxl-incubator/xxl-util/src/main/resources/script/shelltest.sh";
    private static String pyLogFile = "/Users/xuxueli/Downloads/tmp/pylog.log";
    private static String shLogFile = "/Users/xuxueli/Downloads/tmp/shlog.log";

    public static void main(String[] args) {

        String command = pyCmd;
        String filename = pyFile;
        String logFile = pyLogFile;
        if (false) {
            command = shllCmd;
            filename = shellFile;
            logFile = shLogFile;
        }

        if (true) {
            execToFile(command, filename, logFile);
        }

        if (false) {
            System.out.println("---------execByShRunner----------");
            execByShRunner(command, filename, logFile);
            return;
        }
        if (false) {
            System.out.println("---------originExec----------");
            originExec(command, filename, logFile);
        }
        if (false) {
            System.out.println("---------commonExec----------");
            commonExec(command, filename, logFile);
        }
        if (false) {
            System.out.println("---------commonExecAsync----------");
            commonExecAsync(command, filename, logFile);
        }

    }

    /**
     * 日志文件输出方式
     *
     * 优点：支持将目标数据实时输出到指定日志文件中去
     * 缺点：
     *      标准输出和错误输出优先级固定，可能和脚本中顺序不一致
     *      Java无法实时获取
     *
     * @param command
     * @param scriptFile
     * @param logFile
     */
    public static void execToFile(String command, String scriptFile, String logFile){
        try {
            // 标准输出：print （null if watchdog timeout）
            // 错误输出：logging + 异常 （still exists if watchdog timeout）
            // 标准输出
            FileOutputStream fileOutputStream = new FileOutputStream(logFile);
            PumpStreamHandler streamHandler = new PumpStreamHandler(fileOutputStream, fileOutputStream, null);

            // command
            CommandLine commandline = new CommandLine(command);
            commandline.addArgument(scriptFile);

            // exec
            DefaultExecutor exec = new DefaultExecutor();
            exec.setExitValues(null);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Process process = Runtime.getRuntime().exec(cmdarray);
        IOUtils.copy(process.getInputStream(), out);
        IOUtils.copy(process.getErrorStream(), out);*/
    }

    /**
     * 中间shell方式
     *
     *  优点：中间shell调用目标脚本，支持将目标脚本日志输出到指定日志文件中
     *  缺点：中间shell调用的目标shell，本质上是新的进程，调用方无法关闭该shell，目标进程不再受控制，隐患非常大，该方式废弃；
     *
     * @param command
     * @param scriptFile
     * @param logFile
     */
    public static void execByShRunner(String command, String scriptFile, String logFile){
        // formet[$1 $2 >>$3 2>&1]: format：bash($1) xxx.sh($2) >>xxx.log($3) 2>&1  或者 python xxx.py >>xxx.log 2>&1
        String runnerFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "script/script-runner.sh";

        try {
            // input + output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();   // 标准输出：print （null if watchdog timeout）
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();    // 错误输出：logging + 异常 （still exists if watchdog timeout）
            ByteArrayInputStream inputStream = null;                            // 标准出入
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);

            ExecuteWatchdog watchdog = new ExecuteWatchdog(1000);

            // command
            CommandLine commandline = new CommandLine(shllCmd);
            commandline.addArgument(runnerFile);
            commandline.addArgument(command);
            commandline.addArgument(scriptFile);
            commandline.addArgument(logFile);

            // exec
            DefaultExecutor exec = new DefaultExecutor();
            exec.setExitValues(null);
            exec.setWatchdog(watchdog);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandline);

            // parse output
            String out = outputStream.toString(OutputFormat.Defaults.Encoding);
            String error = errorStream.toString(OutputFormat.Defaults.Encoding);

            System.out.println(out);
            System.out.println("---");
            System.out.println(error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * commons-exec 异步方式
     *
     * 优点：
     *      可区分显示 "标准输出、错误输出和标准输入"
     *      支持脚本process异步执行，自定义wait时间，支持主动销毁
     * 缺点：
     *      结束后一次性返回Log；
     *      不支持脚本日志输出到日志文件
     *
     * @param command
     * @param scriptFile
     * @param logFile    脚本日志路径
     */
    public static void commonExecAsync(String command, String scriptFile, String logFile) {
        try {
            // input + output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();   // 标准输出：print （watchdog timeout, print is null）
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();    // 错误输出：logging + 异常 （watchdog timeout, is ok）
            ByteArrayInputStream inputStream = null;                            // 标准输出
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);

            // timeout watchdog
            ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);

            // async result
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

            // log command
            String locCommand = ">>"+ logFile +" 2>&1";

            // command
            CommandLine commandline = new CommandLine(command);
            commandline.addArgument(scriptFile);
            commandline.addArgument(locCommand);

            // exec
            DefaultExecutor exec = new DefaultExecutor();
            exec.setExitValues(null);
            exec.setWatchdog(watchdog);
            exec.setStreamHandler(streamHandler);
            exec.execute(commandline, resultHandler);

            // wait until destory process
            resultHandler.waitFor(5000);
            if (watchdog.isWatching()) {
                watchdog.destroyProcess();
                if (watchdog.killedProcess()) {
                    System.out.println("--> wait result is : " + resultHandler.hasResult());
                    System.out.println("--> exit value is : " + resultHandler.getExitValue());
                    System.out.println("--> exception is : " + resultHandler.getException());
                }
            }

            // parse output
            String out = outputStream.toString(OutputFormat.Defaults.Encoding);
            String error = errorStream.toString(OutputFormat.Defaults.Encoding);

            System.out.println(out);
            System.out.println("---");
            System.out.println(error);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * commons-exec 普通方式
     *
     * 优点：可区分显示 "标准输出、错误输出和标准输入"
     * 缺点：
     *      阻塞执行，结束后一次性返回Log；
     *      不支持脚本日志输出到日志文件
     *
     * @param command       命令
     * @param scriptFile    脚本文件路径
     * @param logFile       脚本日志路径
     */
    public static void commonExec(String command, String scriptFile, String logFile) {
        try {
            // input + output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();   // 标准输出：print （null if watchdog timeout）
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();    // 错误输出：logging + 异常 （still exists if watchdog timeout）
            ByteArrayInputStream inputStream = null;                            // 标准输出
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);

            // log command
            String locCommand = ">>"+ logFile +" 2>&1";

            // command
            //CommandLine commandline = CommandLine.parse(command + " " + scriptFile);
            CommandLine commandline = new CommandLine(command);
            commandline.addArgument(scriptFile);
            commandline.addArgument(locCommand);

            // exec
            DefaultExecutor exec = new DefaultExecutor();
            exec.setExitValues(null);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandline);

            // parse output
            String out = outputStream.toString(OutputFormat.Defaults.Encoding);
            String error = errorStream.toString(OutputFormat.Defaults.Encoding);

            System.out.println(out);
            System.out.println("---");
            System.out.println(error);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 原始方法
     *
     * 缺点：
     *      阻塞执行，结束后一次性返回Log；
     *      只支持标准输出print内容;
     *      不支持脚本日志输出到日志文件
     *
     * @param command       命令
     * @param scriptFile    脚本文件路径
     * @param logFile       脚本日志路径
     */
    public static void originExec(String command, String scriptFile, String logFile){
        try {
            String locCommand = ">>"+ logFile +" 2>&1";
            String[] cmdarray = new String[]{command, scriptFile, locCommand};
            Process pr = Runtime.getRuntime().exec(cmdarray);

            SequenceInputStream sequenceInputStream = new SequenceInputStream(pr.getInputStream(), pr.getErrorStream());

            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sequenceInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
