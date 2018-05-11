package com.xuxueli.demo.face;

import org.bytedeco.javacpp.opencv_core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;

/**
 * @author xuxueli 2018-05-11 19:14:40
 */
public class Test {

    public static void main(String[] args) {

        if (true) {
            opencv_core.Mat tmp = JavaCVUtil.detectFace("/Users/xuxueli/Downloads/tmp/face2/training-images/1-zjl01.jpg");

            imwrite("/Users/xuxueli/Downloads/tmp/face2/test01.jpg", tmp);
            return;
        }


        Map<Integer, List<String>> faceInfo = new HashMap<>();
        faceInfo.put(11111, Arrays.asList(
                "/Users/xuxueli/Downloads/tmp/face2/training-images/1-zjl01.jpg",
                "/Users/xuxueli/Downloads/tmp/face2/training-images/1-zjl02.jpg",
                "/Users/xuxueli/Downloads/tmp/face2/training-images/1-zjl03.jpg",
                "/Users/xuxueli/Downloads/tmp/face2/training-images/1-zjl04.jpg"
        ));

        faceInfo.put(22222, Arrays.asList(
                "/Users/xuxueli/Downloads/tmp/face2/training-images/2-xs01.jpg",
                "/Users/xuxueli/Downloads/tmp/face2/training-images/2-xs02.jpg",
                "/Users/xuxueli/Downloads/tmp/face2/training-images/2-xs03.jpg",
                "/Users/xuxueli/Downloads/tmp/face2/training-images/2-xs04.jpg"
        ));

        for (Map.Entry<Integer, List<String>> item: faceInfo.entrySet()) {
            FaceRecognizerUtil.getInstance().trainFace(item.getKey(), item.getValue());
        }

        opencv_core.Mat testImg01 = imread("/Users/xuxueli/Downloads/tmp/face2/training-images/1-zjl04.jpg", CV_LOAD_IMAGE_GRAYSCALE);
        opencv_core.Mat testImg02 = imread("/Users/xuxueli/Downloads/tmp/face2/training-images/2-xs02.jpg", CV_LOAD_IMAGE_GRAYSCALE);

        int faceId01 = FaceRecognizerUtil.getInstance().predictFace(testImg01);
        System.out.println(faceId01);
        int faceId02 = FaceRecognizerUtil.getInstance().predictFace(testImg02);
        System.out.println(faceId02);

    }

}
