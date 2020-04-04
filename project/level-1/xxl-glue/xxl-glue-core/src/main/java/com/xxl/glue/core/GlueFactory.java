package com.xxl.glue.core;

import com.xxl.glue.core.broadcast.GlueMessage;
import com.xxl.glue.core.broadcast.XxlGlueBroadcaster;
import com.xxl.glue.core.handler.GlueHandler;
import com.xxl.glue.core.loader.GlueLoader;
import com.xxl.glue.core.loader.impl.FileGlueLoader;
import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * glue factory, product class/object by name
 * @author xuxueli 2016-1-2 20:02:27
 */
public class GlueFactory implements ApplicationContextAware {
	private static Logger logger = LoggerFactory.getLogger(GlueFactory.class);

	// ----------------------------- base init -----------------------------

	private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();	// groovy class loader
	private long cacheTimeout = 5000;										// glue cache timeout / second
	private String appName;	// appName, used to warn-up glue data
	private GlueLoader glueLoader = new FileGlueLoader();					// code source loader

	public void setCacheTimeout(long cacheTimeout) {
		this.cacheTimeout = cacheTimeout;
		if (cacheTimeout<-1) {
			cacheTimeout = -1;	// never cache timeout, as -1, until receive message
		}
	}
	public void setAppName(String appName) {
		this.appName = appName;
		if (appName==null || appName.trim().length()==0) {
			this.appName = "default";
		}
	}
	public void setGlueLoader(GlueLoader glueLoader) {
		this.glueLoader = glueLoader;
	}
	
	// ----------------------------- spring support -----------------------------
	private static ApplicationContext applicationContext;
	private static GlueFactory glueFactory;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		GlueFactory.applicationContext = applicationContext;
		GlueFactory.glueFactory = (GlueFactory) applicationContext.getBean("glueFactory");
	}
	
	/**
	 * inject service of spring
	 *
	 * @param instance
	 * @throws Exception
	 */
	public void injectService(Object instance) throws Exception{
		if (instance==null) {
			return;
		}
	    
		Field[] fields = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			
			Object fieldBean = null;
			// with bean-id, bean could be found by both @Resource and @Autowired, or bean could only be found by @Autowired
			if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
				try {
					Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
					if (resource.name()!=null && resource.name().length()>0){
						fieldBean = applicationContext.getBean(resource.name());
					} else {
						fieldBean = applicationContext.getBean(field.getName());
					}
				} catch (Exception e) {
				}
				if (fieldBean==null ) {
					fieldBean = applicationContext.getBean(field.getType());
				}
			} else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
				Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
				if (qualifier!=null && qualifier.value()!=null && qualifier.value().length()>0) {
					fieldBean = applicationContext.getBean(qualifier.value());
				} else {
					fieldBean = applicationContext.getBean(field.getType());
				}
			}
			
			if (fieldBean!=null) {
				field.setAccessible(true);
				field.set(instance, fieldBean);
			}
		}
	}
	
	// ----------------------------- load instance -----------------------------
	
	// load new instance, prototype
	public GlueHandler loadNewInstance(String name) throws Exception{
		if (name==null || name.trim().length()==0) {
			return null;
		}
		String codeSource = glueLoader.load(name);
		if (codeSource!=null && codeSource.trim().length()>0) {
			Class<?> clazz = groovyClassLoader.parseClass(codeSource);
			if (clazz!=null) {
				Object instance = clazz.newInstance();
				if (instance!=null) {
					if (instance instanceof GlueHandler) {
						this.injectService(instance);
						logger.info(">>>>>>>>>>>> xxl-glue, loadNewInstance success, name:{}", name);

                        // watch topic on zk
						XxlGlueBroadcaster.getInstance().watchMsg(name);

						return (GlueHandler) instance;
					} else {
						throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, "
								+ "cannot convert from instance["+ instance.getClass() +"] to GlueHandler");
					}
				}
			}

		}
		throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadNewInstance error, instance is null");
	}
	
	// load instance, singleton
	private static final ConcurrentHashMap<String, GlueHandler> glueInstanceMap = new ConcurrentHashMap<String, GlueHandler>();	// cache instance
	private static final ConcurrentHashMap<String, Long> glueTimeoutMap = new ConcurrentHashMap<String, Long>();					// cache timeout tim

	public GlueHandler loadInstance(String name) throws Exception{
		if (name==null || name.trim().length()==0) {
			return null;
		}
		GlueHandler instance = glueInstanceMap.get(name);
		if (instance == null) {
			instance = loadNewInstance(name);
			if (instance == null) {
				throw new IllegalArgumentException(">>>>>>>>>>> xxl-glue, loadInstance error, instance is null");
			}
			glueInstanceMap.put(name, instance);
			glueTimeoutMap.put(name, cacheTimeout==-1?-1:(System.currentTimeMillis() + cacheTimeout));
		} else {
			Long instanceTim = glueTimeoutMap.get(name);
			boolean ifValid = true;
			if (instanceTim == null) {
				ifValid = false;
			} else {
				if (instanceTim.intValue() == -1) {
					ifValid = true;	
				} else if (System.currentTimeMillis() > instanceTim) {
					ifValid = false;
				}
			}
			if (!ifValid) {
				GlueMessage glueMessage = new GlueMessage();
				glueMessage.setGlueName(name);
				glueRefreshQuene.add(glueMessage);

				glueTimeoutMap.put(name, Long.valueOf(-1));	// 缓存时间临时设置为-1，永久生效，避免并发情况下多次推送异步刷新队列；
			}
		}

		return instance;
	}

	// ----------------------------- async glue refresh -----------------------------

	private static LinkedBlockingQueue<GlueMessage> glueRefreshQuene = new LinkedBlockingQueue<GlueMessage>();	// 异步刷新 + version校验：避免缓存雪崩
	private static ConcurrentHashMap<String, Long> glueVersionMap = new ConcurrentHashMap<String, Long>();
	private Thread refreshThread = null;
	private boolean glueRefreshToStop = false;
	private void init(){
		refreshThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!glueRefreshToStop){
					GlueMessage glueMessage = null;
					try {
						glueMessage = glueRefreshQuene.take();	// take glue need refresh
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						continue;
					}

					// refresh
					if (glueMessage!=null && glueMessage.getGlueName()!=null && glueInstanceMap.get(glueMessage.getGlueName())!=null) {

						// instance version
						Long existVersion = glueVersionMap.get(glueVersionMap);
						if (existVersion!=null && glueMessage.getVersion()>0 && existVersion.longValue()==glueMessage.getVersion()) {
							continue;
						}

						// refresh new instance
						GlueHandler newInstance = null;
						try {
							newInstance = GlueFactory.glueFactory.loadNewInstance(glueMessage.getGlueName());
						} catch (Exception e) {
							logger.error("", e);
						}

						if (newInstance!=null) {
							glueInstanceMap.put(glueMessage.getGlueName(), newInstance);
							glueTimeoutMap.put(glueMessage.getGlueName(), cacheTimeout==-1?-1:(System.currentTimeMillis() + cacheTimeout));

							logger.warn(">>>>>>>>>>>> xxl-glue, async glue fresh success, name:{}", glueMessage.getGlueName());
						} else {
							glueInstanceMap.remove(glueMessage);
							glueTimeoutMap.remove(glueMessage);

							logger.warn(">>>>>>>>>>>> xxl-glue, async glue fresh fail, old instance removed, name:{}", glueMessage.getGlueName());
						}
					}
				}
			}
		});
		refreshThread.setDaemon(true);
		refreshThread.start();
	}
	private void destory(){
		glueRefreshToStop = true;
	}

	public static void glueRefresh(GlueMessage glueMessage){
		// check if match appName
		boolean isMatchAppName = true;
		if (glueMessage.getAppnames()!=null && glueMessage.getAppnames().size()>0) {
			if (glueMessage.getAppnames().contains(GlueFactory.glueFactory.appName)) {
				isMatchAppName = true;
			} else {
				isMatchAppName = false;
			}
		} else {
			isMatchAppName = true;
		}
		if (!isMatchAppName) {
			return;
		}

		// fresh instance
		GlueFactory.glueRefreshQuene.add(glueMessage);
	}
	
	// ----------------------------- util -----------------------------
	public static Object glue(String name, Map<String, Object> params) throws Exception{
		return GlueFactory.glueFactory.loadInstance(name).handle(params);
	}

	
}
