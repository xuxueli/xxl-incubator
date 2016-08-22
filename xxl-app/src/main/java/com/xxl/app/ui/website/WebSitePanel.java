package com.xxl.app.ui.website;


import com.xxl.app.util.BrowseUtil;
import com.xxl.app.util.ConfigUtil;

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
            instance.setBackground(Color.LIGHT_GRAY);

            // Key
            keyWordSelect = new JComboBox();
            keyWordSelect.setEditable(true);
            //keyWordSelect.setFont(new Font("Arial",Font.PLAIN,16));
            keyWordSelect.setMaximumSize(new Dimension(250, 25));
            keyWordSelect.setMinimumSize(new Dimension(250, 25));
            keyWordSelect.setPreferredSize(new Dimension(250, 25));
            for (String appName : ConfigUtil.KeyWords) {
                keyWordSelect.addItem(appName);
            }

            // URL
            siteTemplateSelect = new JComboBox();
            siteTemplateSelect.setMaximumSize(new Dimension(250, 25));
            siteTemplateSelect.setMinimumSize(new Dimension(250, 25));
            siteTemplateSelect.setPreferredSize(new Dimension(250, 25));
            for (Map.Entry<String, String> site : ConfigUtil.SiteTemplates.entrySet()) {
                siteTemplateSelect.addItem(site.getKey());
            }

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
            //instance.setLayout(new FlowLayout());
            instance.setLayout(new BoxLayout(instance, BoxLayout.LINE_AXIS));
            instance.add(new JLabel("关键字:"));
            instance.add(keyWordSelect);
            instance.add(new JLabel("URL模板:"));
            instance.add(siteTemplateSelect);
            instance.add(goBtn);
            instance.add(exitBtn);
        }
        return instance;
    }

    private static JComboBox keyWordSelect;
    private static JComboBox siteTemplateSelect;
    private static JButton goBtn;
    private static JButton exitBtn;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBtn) {
            Toolkit.getDefaultToolkit().beep();

            // appname
            String appName = keyWordSelect.getSelectedItem().toString();
            if (appName==null || appName.trim().length()==0) {
                JOptionPane.showMessageDialog(instance, "请选择或数据关键字", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // site template
            String siteKey = siteTemplateSelect.getSelectedItem().toString();
            String siteTemplate = ConfigUtil.SiteTemplates.get(siteKey);

            try {
                String url = siteTemplate.replaceAll("\\{0\\}", URLEncoder.encode(appName, "utf-8"));
                BrowseUtil.browse(url);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(instance, "调用浏览器失败:"+e1.getMessage(), null,JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }


}
