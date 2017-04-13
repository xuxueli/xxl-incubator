package com.xxl.util.core.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

/**
 * Created by xuxueli on 16/10/25.
 */
public class MvcUtil {

    /**
     * 校验是否JSON接口
     * @param handler
     * @return
     */
    public static boolean isJsonHandler(Object handler) {
        boolean isJson = false;
        if (handler!=null && handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod)handler;
            ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
            if (responseBody != null) {
                isJson = true;
            }
        }
        return isJson;
    }

    /**
     * 初始化页面类对象
     * @param model
     */
    public static void initStaticMethod(Model model, Class<?>[] clazzs){
        if (model==null || clazzs==null || clazzs.length==0) {
            return;
        }

        for (Class<?> clazz : clazzs) {
            String packageName = clazz.getName();
            String key = packageName.substring(packageName.lastIndexOf(".")+1, packageName.length());
            model.addAttribute(key, MvcUtil.generateStaticModel(packageName));
        }
    }

    public static void initStaticMethod(ModelMap modelMap, Class<?>[] clazzs){
        if (modelMap==null || clazzs==null || clazzs.length==0) {
            return;
        }

        for (Class<?> clazz : clazzs) {
            String packageName = clazz.getName();
            String key = packageName.substring(packageName.lastIndexOf(".")+1, packageName.length());
            modelMap.addAttribute(key, MvcUtil.generateStaticModel(packageName));
        }
    }

    /**
     * ftl中增强Java类对象 （不推荐在ftl中使用Java静态类方法，View层只处理自己职责范围内功能即可）
     * @param packageName
     * @return
     */
    public static TemplateHashModel generateStaticModel(String packageName) {
        try {
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
            return fileStatics;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
