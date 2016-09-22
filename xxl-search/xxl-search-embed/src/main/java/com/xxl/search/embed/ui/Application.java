package com.xxl.search.embed.ui;


import javax.swing.*;

/**
 * Created by xuxueli on 16/8/18.
 */
public class Application {

    public static void main(String[] args) {
        try {
            IndexUI indexUI = IndexUI.getInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), null,JOptionPane.PLAIN_MESSAGE);
        }
    }

}
