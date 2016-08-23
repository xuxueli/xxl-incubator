package com.xxl.app.ui.ssh;

import com.xxl.app.util.TiminalUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by xuxueli on 16/8/18.
 */
public class SshPanel extends Panel implements ActionListener {

    private static SshPanel instance;
    public static SshPanel getInstance(){
        if (instance == null) {
            // 主界面
            instance = new SshPanel();
            instance.setBackground(Color.BLACK);

            // go
            goBtn = new JButton("GO");
            goBtn.setBackground(Color.BLACK);
            goBtn.addActionListener(instance);
            goBtn.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // super.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        goBtn.doClick();
                    }
                }
            });

            // exit
            exitBtn = new JButton("EXIT");
            exitBtn.setBackground(Color.GRAY);
            exitBtn.addActionListener(instance);
            exitBtn.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // super.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        exitBtn.doClick();
                    }
                }
            });

            // 布局
            instance.setLayout(new FlowLayout());
            instance.add(goBtn);
            instance.add(exitBtn);

        }
        return instance;
    }

    private static JButton goBtn;
    private static JButton exitBtn;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBtn) {
            Toolkit.getDefaultToolkit().beep();
            try {
                TiminalUtil.sshLogin();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(instance, "调用SSH失败:"+e1.getMessage(), null,JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }
}
