package com.xxl.search.embed.ui;


import com.xxl.search.embed.excel.ExcelUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

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

            // 索引目录
            directoryInput = new JTextField();
            directoryInput.setColumns(20);

            directoryBtn = new JButton("...");
            directoryBtn.setBackground(Color.BLACK);
            directoryBtn.addActionListener(instance);


            // templateGenerateBtn
            templateGenerateBtn = new JButton("索引模板生成");
            templateGenerateBtn.setBackground(Color.BLACK);
            templateGenerateBtn.addActionListener(instance);

            // createIndex
            createIndexBtn = new JButton("生成索引");
            createIndexBtn.setBackground(Color.BLACK);
            createIndexBtn.addActionListener(instance);

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

            instance.add(new JLabel("索引生成目录:"));
            instance.add(directoryInput);
            instance.add(directoryBtn);

            instance.add(templateGenerateBtn);
            instance.add(createIndexBtn);
            instance.add(exitBtn);
        }
        return instance;
    }

    private static JTextField directoryInput;
    private static JButton directoryBtn;        // 选择索引目录

    private static JButton templateGenerateBtn; // 索引模板下载
    private static JButton createIndexBtn;      // 生成索引库
    private static JButton exitBtn;

    private static ExcelFileFilter excelFileFilter = new ExcelFileFilter();

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == directoryBtn) {
            Toolkit.getDefaultToolkit().beep();
            JFileChooser chooser = new JFileChooser();
            chooser.setApproveButtonText("选择");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(instance);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                directoryInput.setText(path);
            }
            return;
        } else if (e.getSource() == templateGenerateBtn) {
            String directoryPath = directoryInput.getText();
            if (directoryPath==null || directoryPath.trim().length()==0) {
                JOptionPane.showMessageDialog(instance, "请选择索引生成目录, 索引模板将会在改目录生成", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }

            File directoryFile = new File(directoryPath);
            if (!directoryFile.exists()) {
                JOptionPane.showMessageDialog(instance, "索引生成目录不存在, 请重新选择, 索引模板将会在改目录生成", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // 生成索引模板
            try {
                File templateFile = new File(directoryFile, ExcelUtil.TEMPLATE_NAME);
                if (templateFile.exists()){
                    JOptionPane.showMessageDialog(instance, "索引模板以存在, 请勿重复生成", null,JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                ExcelUtil.generateTemplate(directoryFile);
                JOptionPane.showMessageDialog(instance, "索引模板生成成功, 存放在索引生成目录下", null,JOptionPane.PLAIN_MESSAGE);
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(instance, "索引模板生成失败:"+e1.getMessage(), null,JOptionPane.PLAIN_MESSAGE);
            }
        } else if (e.getSource() == createIndexBtn) {
            // directory
            String directoryPath = directoryInput.getText();
            if (directoryPath==null || directoryPath.trim().length()==0) {
                JOptionPane.showMessageDialog(instance, "请选择索引目录", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }
            File directoryFile = new File(directoryPath);
            if (!directoryFile.exists()) {
                JOptionPane.showMessageDialog(instance, "输入的索引目录不存在", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }

            // template
            File templateFile = new File(directoryFile, ExcelUtil.TEMPLATE_NAME);
            if (!templateFile.exists()) {
                JOptionPane.showMessageDialog(instance, "索引模板不存在, 请手动生成", null,JOptionPane.PLAIN_MESSAGE);
                return;
            }

            try {
                ExcelUtil.createIndexByTemplate(templateFile, directoryFile);
                JOptionPane.showMessageDialog(instance, "恭喜您, 生成索引库成功!", null,JOptionPane.PLAIN_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(instance, "生成索引库失败:"+e1.getMessage(), null,JOptionPane.PLAIN_MESSAGE);
            }

        } else if (e.getSource() == exitBtn) {
            //JOptionPane.showMessageDialog(instance, "请选择或数据关键字", null,JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
    }

    private static class ExcelFileFilter extends FileFilter {
        public String getDescription() {
            return "*.xls、*.xlsx";
        }

        public boolean accept(File file) {
            String name = file.getName();
            return name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx");
        }
    }

}
