package com.xxl.util.core.util;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import org.apache.commons.exec.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

/**
 * Created by xuxueli on 17/2/25.
 */
public class ScriptUtil {

    // 假定这个test.py里面使用了拓展的包，使得pythoninterpreter之类内嵌的编译器无法使用，那么只能采用java调用控制台进程，即 Runtime.getRuntime().exec()，来运行这个python脚本。
    private static String pyFile = "/Users/xuxueli/workspaces/idea-git-workspace/github/xxl-incubator/xxl-util/src/main/resources/script/pytest.py";
    private static String shellFile = "/Users/xuxueli/workspaces/idea-git-workspace/github/xxl-incubator/xxl-util/src/main/resources/script/shelltest.sh";


    public static void main(String[] args) {
        String command = "python";  // python 异常输出优先级高于标准输出，体现在Log文件中，因此推荐通过logging方式打日志保持和异常信息一致；否则用prinf日志顺序会错乱
        String filename = pyFile;
        String logCommand = ">>/Users/xuxueli/Downloads/tmp/pylog.log 2>&1";
        if (false) {
            command = "bash";
            filename = shellFile;
            logCommand = ">>/Users/xuxueli/Downloads/tmp/shlog.log 2>&1";       // [>>logfile 2>&1]：将错误输出2以及标准输出1都一起以附加写方式导入logfile文件
        }

        System.out.println("---------originExec----------");
        //originExec(command, filename);
        System.out.println("---------commonExec----------");
        commonExec(command, filename);
        System.out.println("---------commonExecAsync----------");
        //commonExecAsync(command, filename);
    }

    public static void test(String command) {
    }

    /**
     * commons-exec 异步方式
     *
     * 优点：
     *      可区分显示 "标准输出、错误输出和标准输入"
     *      支持脚本process异步执行，自定义wait时间，支持主动销毁
     * 缺点：输出必须等待脚本执行结束一次性返回;
     *
     * @param command
     * @param scriptFile
     */
    public static void commonExecAsync(String command, String scriptFile) {
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

            // command
            CommandLine commandline = new CommandLine(command);
            commandline.addArgument(scriptFile);

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
     * 缺点：输出必须等待脚本执行结束一次性返回;
     *
     * @param command
     * @param scriptFile
     */
    public static void commonExec(String command, String scriptFile) {
        try {
            // input + output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();   // 标准输出：print （null if watchdog timeout）
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();    // 错误输出：logging + 异常 （still exists if watchdog timeout）
            ByteArrayInputStream inputStream = null;                            // 标准输出
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);

            // command
            //CommandLine commandline = CommandLine.parse(command + " " + scriptFile);
            CommandLine commandline = new CommandLine(command);
            commandline.addArgument(scriptFile);

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
     *      输出必须等待脚本执行结束一次性返回;
     *      只支持数据print内容;
     *
     * @param command
     * @param scriptFile
     */
    public static void originExec(String command, String scriptFile){
        try {
            String[] cmdarray = new String[]{command, scriptFile};
            Process pr = Runtime.getRuntime().exec(cmdarray);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
