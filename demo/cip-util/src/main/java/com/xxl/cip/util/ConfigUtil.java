package com.xxl.cip.util;

import javax.swing.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by xuxueli on 16/8/18.
 */
public class ConfigUtil {
    private static final String FILE_NAME = "xxl-cip.conf";

    private enum ModelEnum{
        AppName, SiteTemplate;
        public String model(){
            return MessageFormat.format("[{0}]", this.name());
        }
    }
    public static LinkedHashSet<String> AppNames = new LinkedHashSet<String>();
    public static LinkedHashMap<String, String> SiteTemplates = new LinkedHashMap<String, String>();

    static{
        InputStream ins = null;
        BufferedReader reader = null;
        try {

            // 获取当前Class(target/classes)或Jar(abc/xxx.jar)的绝对路径
            String jarOrClassRealPath = ConfigUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();

            // 当前类parent目录(target)或者Jar所属的目录(abc)的绝对路径       (导出为Jar时,使用)
            String parentPath = (new File(jarOrClassRealPath)).getParentFile().getAbsolutePath() + "/" + FILE_NAME;

            File file = new File(parentPath);
            if (!file.exists()) {
                // 获取maven的resources目录下配置文件(Jar内部的配置文件)      (开发时, 使用)
                String path = ConfigUtil.class.getClassLoader().getResource(FILE_NAME).getPath();
                file = new File(path);
            }

            ins = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            if (reader != null) {
                ModelEnum model = null;
                String content = null;
                while ((content = reader.readLine()) != null) {
                    if (content != null && content.trim().length() > 0 && content.indexOf("#") == -1) {
                        if (ModelEnum.AppName.model().equals(content.trim())) {
                            model = ModelEnum.AppName;
                            continue;
                        } else if (ModelEnum.SiteTemplate.model().equals(content.trim())) {
                            model = ModelEnum.SiteTemplate;
                            continue;
                        }

                        if (model!=null) {
                            switch (model){
                                case AppName:{
                                    AppNames.add(content.trim());
                                    break;
                                }
                                case SiteTemplate:{
                                    int index = content.indexOf("=");
                                    if (index > -1){
                                        SiteTemplates.put(content.substring(0, index), content.substring(index+1, content.length()));
                                    }
                                    break;
                                }
                            }
                        }


                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), null,JOptionPane.PLAIN_MESSAGE);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(AppNames);
        System.out.println(SiteTemplates);
    }

}
