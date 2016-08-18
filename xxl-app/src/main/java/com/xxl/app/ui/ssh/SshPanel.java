package com.xxl.app.ui.ssh;

import java.awt.*;

/**
 * Created by xuxueli on 16/8/18.
 */
public class SshPanel extends Panel {

    private static SshPanel instance;
    public static SshPanel getInstance(){
        instance = new SshPanel();
        instance.setBackground(Color.BLACK);

        return instance;
    }
}
