package com.xuxueli.demo.face;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

/**
 * Face Recognizer Util
 *
 * @author xuxueli 2018-05-11 17:57:22
 */
public class FaceRecognizerUtil {
    private static Logger logger = LoggerFactory.getLogger(FaceRecognizerUtil.class);

    private static String FACE_DATA_PATH = "/Users/xuxueli/Downloads/tmp/face2/";               // 仓库根目录
    private static final String FR_BINARY_DATA = FACE_DATA_PATH + "training_binary.dat";        // 数据：模型状态二进制数据
    private static final String FR_IMAGES = FACE_DATA_PATH + "training-images/";                // 数据：人脸剪裁图


    private opencv_face.FaceRecognizer faceRecognizer;          // 分类器
    private int highConfidenceLevel = 70;

    public static FaceRecognizerUtil getInstance(){
        return new FaceRecognizerUtil();
    }

    public FaceRecognizerUtil(){

        // init
        faceRecognizer = opencv_face.LBPHFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = opencv_face.FisherFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = opencv_face.LBPHFaceRecognizer.create();
        logger.info("FaceRecognizer init success.");

        // load
        File binaryDataFile = new File(FR_BINARY_DATA);
        if (binaryDataFile.exists()) {
            faceRecognizer.read(FR_BINARY_DATA);
        }
        logger.info("FaceRecognizer Loading Binary Data success.");

    }

    /**
     * predict face (by bin file)
     */
    public int predictFace(opencv_core.Mat mat){
        // valid mat
        if (mat == null) {
            throw new RuntimeException("图片非法");
        }

        int[] ids = new int[1];
        double[] distance = new double[1];
        faceRecognizer.predict(mat, ids, distance);

        if (ids[0] > 0 && distance[0] < highConfidenceLevel) {
            int faceId = ids[0];
            return faceId;
        }
        return -1;
    }

    private static int counter = 0;

    /**
     * train face  (write bin file)
     */
    public void trainFace(int faceId, List<String> imgList){
        // valid
        if (faceId <1) {
            throw new RuntimeException("FaceId 非法");
        }
        if (imgList==null || imgList.size()<4 || imgList.size()>50 ) {
            throw new RuntimeException("图片素材数量非法，限制为4~50张");
        }

        // images
        opencv_core.MatVector images = new opencv_core.MatVector(imgList.size());
        List<opencv_core.Mat> matList = new ArrayList<>();

        for (int i = 0; i < imgList.size(); i++) {
            opencv_core.Mat mat = imread(imgList.get(i), CV_LOAD_IMAGE_GRAYSCALE);
            matList.add(mat);
        }


        // labels
        opencv_core.Mat labels = new opencv_core.Mat(matList.size(), 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        for (int i = 0; i < matList.size(); i++) {
            // img + lable(faceid)
            opencv_core.Mat img = matList.get(i);
            int label = faceId;

            // bing image-lable, by counter
            images.put(i, img);
            labelsBuf.put(i, label);

            counter++;
        }

        // train
        faceRecognizer.train(images, labels);
        logger.info("trainFace success, train faceId:{}", faceId);

        // write to b-file
        faceRecognizer.save(FR_BINARY_DATA);
        //faceRecognizer.write(FR_BINARY_DATA);
        logger.info("trainFace success, save faceId:{}", faceId);
    }

    /**
     * sync bin file, by face img when save
     */
    public void syncBinFile(){

    }

}
