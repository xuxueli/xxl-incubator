package com.xuxueli.poi.excel.annotation;

import org.apache.poi.hssf.util.HSSFColor;

import java.lang.annotation.*;

/**
 * 表信息
 *
 * @author xuxueli 2017-09-08 20:51:26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelSheet {

    /**
     * 表名称
     *
     * @return
     */
    String name() default "";

    /**
     * 表头/首行的颜色
     *
     * @see org.apache.poi.ss.usermodel.Font#setColor(short)
     *
     * @return
     */
    short headColor() default HSSFColor.LIGHT_GREEN.index;

}

