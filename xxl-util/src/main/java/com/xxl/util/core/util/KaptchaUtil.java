package com.xxl.util.core.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * Kaptcha验证码工具类
 * @author xuxueli2016-7-5 19:13:27
 */
public class KaptchaUtil {
	private static Logger logger = LoggerFactory.getLogger(KaptchaUtil.class);
	
	public static void generateImage(HttpServletRequest request, HttpServletResponse response) {
		Producer captchaProducer = (Producer) SpringContentUtil.getBeanByName("captchaProducer");

		// Kaptcha生成的随机数，存储在session中
		String capText = captchaProducer.createText();
		HttpSessionUtil.set(request.getSession(), Constants.KAPTCHA_SESSION_KEY, capText);
		
		// create image
		BufferedImage bufferedImage = captchaProducer.createImage(capText);
		
		// write to response
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",	"no-store, no-cache, must-revalidate");	// Set standard HTTP/1.1 no-cache headers.
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");	// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.setHeader("Pragma", "no-cache");	// Set standard HTTP/1.0 no-cache header.
		response.setContentType("image/jpeg");	// return a jpeg

		try {
			// write the data out
			ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			logger.error("kaptcha error.", e);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				logger.error("kaptcha error.", e);
			}
		}
	}

}
