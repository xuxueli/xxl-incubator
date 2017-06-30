package com.xxl.util.core.skill.screencapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by xuxueli on 17/3/1.
 */
public class ScreenCaptureUtil {

    public static void captureScreen(String fileName) throws Exception {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        ImageIO.write(image, "png", new File(fileName));

    }

    public static void main(String[] args) {
        try {
            captureScreen("/Users/xuxueli/Downloads/tmp/shot_"+System.currentTimeMillis()+".png");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
