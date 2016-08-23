package com.xxl.app.util;

import javax.swing.*;

/**
 * Created by xuxueli on 16/8/23.
 */
public class TiminalUtil {

    private static final String TERMINAL_BASE = "/usr/bin/open -a Terminal ";

    public static void sshLogin() throws Exception {
        OSUtil.OSEnum os = OSUtil.os();
        switch (os) {
            case Windows:{
                JOptionPane.showMessageDialog(null, "暂未支持", null,JOptionPane.PLAIN_MESSAGE);
                break;
            }
            case Mac:{
                //Runtime.getRuntime().exec(TERMINAL_BASE + "/data").waitFor();

                Runtime.getRuntime().exec(TERMINAL_BASE + "/data").waitFor();

                /*Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(TERMINAL_BASE + "/data");

                DataOutputStream dos = new DataOutputStream(process.getOutputStream());
                dos.writeBytes("ls \n");
                //dos.writeBytes("ls \n");
                //dos.writeBytes("exit \n");
                dos.flush();*/


                break;
            }
            case LinuxOrUnix:{
                JOptionPane.showMessageDialog(null, "暂未支持", null,JOptionPane.PLAIN_MESSAGE);
                break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            sshLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
