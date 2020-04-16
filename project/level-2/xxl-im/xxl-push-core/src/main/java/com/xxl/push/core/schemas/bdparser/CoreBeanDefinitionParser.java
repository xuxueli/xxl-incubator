package com.xxl.push.core.schemas.bdparser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;

/**
 * xxl push BeanDefinitionParser
 *
 * @author xuxueli 2016-10-08 20:46:37
 */
public class CoreBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(org.w3c.dom.Element element) {
        return Element.class;
    }

    protected void doParse(org.w3c.dom.Element element, BeanDefinitionBuilder bean) {

        String id = element.getAttribute("id");
        String address = element.getAttribute("address");
        String beat = element.getAttribute("beat");

        if (StringUtils.hasText(id)) {
            bean.addPropertyValue("id", id);
        }

        if (StringUtils.hasText(address)) {
            bean.addPropertyValue("address", address);
        }

        if (StringUtils.hasText(beat)) {
            bean.addPropertyValue("beat", Integer.valueOf(beat));
        }

    }

    public static final String ELEMENT = "core";
    public static class Element {

        private String id;          // spring bean id
        private String address;     // cluster redis address
        private Integer beat;       // refresh socket beat, second

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getBeat() {
            return beat;
        }

        public void setBeat(Integer beat) {
            this.beat = beat;
        }
    }

}