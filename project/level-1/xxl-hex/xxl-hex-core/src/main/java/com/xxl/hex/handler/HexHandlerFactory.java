package com.xxl.hex.handler;

import com.xxl.hex.handler.annotation.HexHandlerMapping;
import com.xxl.hex.handler.request.HexRequest;
import com.xxl.hex.handler.response.HexResponse;
import com.xxl.hex.remote.client.HexClient;
import com.xxl.hex.serialise.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuxueli on 16/9/14.
 */
public class HexHandlerFactory implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(HexHandlerFactory.class);

    // handler repository
    private static ConcurrentHashMap<String, HexHandler> handlerRepository = new ConcurrentHashMap<String, HexHandler>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(HexHandlerMapping.class);
        if (serviceMap!=null && serviceMap.size()>0) {
            for (Object serviceBean : serviceMap.values()) {
                if (serviceBean instanceof HexHandler) {
                    // valid annotation
                    HexHandlerMapping annotation = serviceBean.getClass().getAnnotation(HexHandlerMapping.class);
                    if (annotation!=null && annotation.value()!=null && annotation.value().trim().length()>0 ) {
                        handlerRepository.put(annotation.value(), (HexHandler) serviceBean);
                        logger.info(">>>>>>>>>>> xxl-hex, bind hex handler success : {}", annotation.value());
                    }
                }

            }
        }
    }

    public static String dispatchHandler(HttpServletRequest request, HttpServletResponse response, String mapping, String request_hex){
        // valid param
        if (mapping==null || mapping.trim().length()==0) {
            StringBuffer sb = new StringBuffer();
            sb.append("在线HexHandler列表 (" + handlerRepository.size() + ") :<hr>");
            if (handlerRepository !=null && handlerRepository.size()>0) {
                for (Map.Entry<String, HexHandler> item: handlerRepository.entrySet()) {
                    Type[] requestClassTypps = ((ParameterizedType)item.getValue().getClass().getGenericSuperclass()).getActualTypeArguments();
                    Class requestClass = (Class) requestClassTypps[0];
                    sb.append(item.getKey());
                    sb.append(" : ");
                    sb.append(item.getValue().getClass());
                    sb.append("<br>");
                }
            }

            return sb.toString();
        }
        if (request_hex==null || request_hex.trim().length()==0) {
            return HexClient.formatObj2Json2Byte2Hex(new HexResponse.SimpleHexResponse("必要参数缺失[request_hex]"));
        }

        try {
            // handler
            HexHandler handler = handlerRepository.get(mapping);
            if (handler == null) {
                return HexClient.formatObj2Json2Byte2Hex(new HexResponse.SimpleHexResponse("handler不存在"));
            }

            // ex requeset
            Type[] requestClassTypps = ((ParameterizedType)handler.getClass().getGenericSuperclass()).getActualTypeArguments();
            Class requestClass = (Class) requestClassTypps[0];      // ((Class) requestClassTypps[0]).isAssignableFrom(HexRequest.class)
            HexRequest hexrequest = (HexRequest) HexClient.parseHex2Byte2Json2Obj(request_hex, requestClass);

            if (hexrequest==null) {
                return HexClient.formatObj2Json2Byte2Hex(new HexResponse.SimpleHexResponse("params parse fail."));
            }

            // init base request
            hexrequest.setRequest(request);
            hexrequest.setResponse(response);

            // do validate
            HexResponse validateResponse = handler.validate(hexrequest);
            if (validateResponse!=null) {
                String response_hex = HexClient.formatObj2Json2Byte2Hex(validateResponse);
                return response_hex;
            }

            // do invoke
            HexResponse hexResponse = handler.handle(hexrequest);
            String response_hex = HexClient.formatObj2Json2Byte2Hex(hexResponse);
            return response_hex;
        } catch (Exception e) {
            logger.error("", e);
            return HexClient.formatObj2Json2Byte2Hex(new HexResponse.SimpleHexResponse(e.getMessage()));
        }

    }

    public static String dispatchHandlerPlain(HttpServletRequest request, HttpServletResponse response, String mapping) {
        // valid param
        if (mapping==null || mapping.trim().length()==0) {
            StringBuffer sb = new StringBuffer();
            sb.append("在线HexHandler列表 (" + handlerRepository.size() + ") :<hr>");
            if (handlerRepository !=null && handlerRepository.size()>0) {
                for (Map.Entry<String, HexHandler> item: handlerRepository.entrySet()) {
                    Type[] requestClassTypps = ((ParameterizedType)item.getValue().getClass().getGenericSuperclass()).getActualTypeArguments();
                    Class requestClass = (Class) requestClassTypps[0];
                    sb.append(item.getKey());
                    sb.append(" : ");
                    sb.append(item.getValue().getClass());
                    sb.append("<br>");
                }
            }

            return sb.toString();
        }

        try {
            // handler
            HexHandler handler = handlerRepository.get(mapping);
            if (handler == null) {
                return JacksonUtil.writeValueAsString(new HexResponse.SimpleHexResponse("handler不存在"));
            }

            // ex requeset 1/2
            Type[] requestClassTypps = ((ParameterizedType)handler.getClass().getGenericSuperclass()).getActualTypeArguments();
            Class requestClass = (Class) requestClassTypps[0];      // ((Class) requestClassTypps[0]).isAssignableFrom(HexRequest.class)

            Field[] fields = requestClass.getDeclaredFields();
            Set<String> fielsNameSet = new HashSet<String>();
            if (fields!=null) {
                for (Field field: fields) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    fielsNameSet.add(field.getName());
                }
            }

            // request json
            Map<String, String> originParams = new HashMap<String, String>();
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1 && fielsNameSet.contains(paramName)) {
                    originParams.put(paramName, paramValues[0]);
                }
            }
            String request_json = JacksonUtil.writeValueAsString(originParams);

            // ex requeset 2/2
            HexRequest hexrequest = (HexRequest) JacksonUtil.readValue(request_json, requestClass);

            // init base request
            hexrequest.setRequest(request);
            hexrequest.setResponse(response);

            // do validate
            HexResponse validateResponse = handler.validate(hexrequest);
            if (validateResponse!=null) {
                String response_json = JacksonUtil.writeValueAsString(validateResponse);
                return response_json;
            }

            // do invoke
            HexResponse hexResponse = handler.handle(hexrequest);
            String response_hex = JacksonUtil.writeValueAsString(hexResponse);
            return response_hex;
        } catch (Exception e) {
            logger.error("", e);
            return JacksonUtil.writeValueAsString(new HexResponse.SimpleHexResponse(e.getMessage()));
        }
    }

}
