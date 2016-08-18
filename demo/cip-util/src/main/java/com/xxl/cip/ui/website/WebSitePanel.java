package com.xxl.cip.ui.website;

import com.xxl.cip.util.BrowseUtil;
import com.xxl.cip.util.ConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/18.
 */
public class WebSitePanel extends Panel implements ActionListener {

    private static WebSitePanel instance;
    public static WebSitePanel getInstance(){
        if (instance==null) {
            // 主界面
            instance = new WebSitePanel();
            instance.setBackground(Color.GRAY);

            // 初始化元素
            appNameSelect = new JComboBox();
            appNameSelect.setEditable(true);
            for (String appName : ConfigUtil.AppNames) {
                appNameSelect.addItem(appName);
            }

            siteSelect = new JComboBox();
            for (Map.Entry<String, String> site : ConfigUtil.SiteTemplates.entrySet()) {
                siteSelect.addItem(site.getKey());
            }

            goBtn = new JButton("GO");
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

            exitBtn = new JButton("退出");
            exitBtn.addActionListener(instance);

            // 布局
            instance.setLayout(new FlowLayout());
            instance.add(appNameSelect);
            instance.add(siteSelect);
            instance.add(goBtn);
            instance.add(exitBtn);
        }
        return instance;
    }

    private static JComboBox appNameSelect;
    private static JComboBox siteSelect;
    private static JButton goBtn;
    private static JButton exitBtn;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBtn) {
            Toolkit.getDefaultToolkit().beep();

            // appname
            String appName = appNameSelect.getSelectedItem().toString();
            if (appName==null || appName.trim().length()==0) {
                JOptionPane.showMessageDialog(instance, "请输入AppName", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // site template
            String siteKey = siteSelect.getSelectedItem().toString();
            String siteTemplate = ConfigUtil.SiteTemplates.get(siteKey);

            try {
                String url = siteTemplate.replaceAll("\\{appName\\}", URLEncoder.encode(appName, "utf-8"));
                BrowseUtil.browse(url);
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(instance, "调用浏览器失败:"+e1.getMessage(), null,JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }


}
