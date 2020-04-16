package com.xxl.push.core.schemas;

import com.xxl.push.core.schemas.bdparser.CoreBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * xxl push NamespaceHandler
 * @author xuxueli 2016-10-08 20:46:37
 */
public class XxlPushNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {  
        registerBeanDefinitionParser(CoreBeanDefinitionParser.ELEMENT, new CoreBeanDefinitionParser());
    }

}  