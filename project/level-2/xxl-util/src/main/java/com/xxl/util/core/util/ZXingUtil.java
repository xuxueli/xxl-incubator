package com.xxl.util.core.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成
 * @author xuxueli 2016-7-5 22:31:14
 */
public class ZXingUtil {
	private static Logger logger = LoggerFactory.getLogger(ZXingUtil.class);
	
	/**
	 * 生成二维码 (黑白颜色，长200宽200) (其他用法，如修改颜色、尺寸、内嵌logo等可自行google资料)
	 * @param response
	 * @param qrCodeContent
	 */
	public static void qrCode(HttpServletResponse response, String qrCodeContent){
		try {
			if (qrCodeContent == null || qrCodeContent.equals("")) {
				response.setContentType("text/plain;charset=UTF-8");
				response.getOutputStream().write("二维码内容不能为空!".getBytes("utf-8"));
				response.getOutputStream().flush();
			} else {
				// hints
				Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
				hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
				
				// BitMatrix
				BitMatrix byteMatrix = new MultiFormatWriter().
						encode(new String(qrCodeContent.getBytes("UTF-8"), "ISO-8859-1"), 
								BarcodeFormat.QR_CODE, 200, 200, hints);	// width=200,height=200;
				
				// BufferedImage
				BufferedImage image = new BufferedImage(byteMatrix.getWidth(), byteMatrix.getHeight(), BufferedImage.TYPE_INT_ARGB);
				for (int x = 0; x < byteMatrix.getWidth(); x++) {
					for (int y = 0; y < byteMatrix.getHeight(); y++) {
						image.setRGB(x, y, byteMatrix.get(x, y) ? -16777216 : -1);	// black=-16777216;white=-1;
					}
				}

				response.setContentType("image/png");
				ImageIO.write(image, "png", response.getOutputStream());
				response.getOutputStream().flush();
			}
		} catch (Exception e) {
			logger.info("ZXingUtil errir.", e);
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				logger.info("ZXingUtil errir.", e);
			}
		}
	}
	
}
