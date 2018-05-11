package com.xuxueli.demo.face;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_objdetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Face Recognizer Util
 *
 * @author xuxueli 2018-05-11 17:57:22
 */
public class FaceRecognizerUtil2 {
    private static Logger logger = LoggerFactory.getLogger(FaceRecognizerUtil2.class);

    private static String FACE_DATA_PATH = "/Users/xuxueli/Downloads/tmp/face2/";               // 仓库根目录
    private static final String FR_BINARY_DATA = FACE_DATA_PATH + "training_binary.dat";        // 数据：模型状态二进制数据
    private static final String FR_IMAGES = FACE_DATA_PATH + "training-images/";                // 数据：人脸剪裁图

    opencv_objdetect.CvHaarClassifierCascade cascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad("haarcascade_frontalface_alt.xml")); // 配置：Haar分类器
    //private int FACE_NUM = 50;                              // 配置：每个FaceId支持最大图片数量
    private opencv_face.FaceRecognizer faceRecognizer;      // 分类器
    private int highConfidenceLevel = 70;

    public static FaceRecognizerUtil2 getInstance(){
        return new FaceRecognizerUtil2();
    }

    public FaceRecognizerUtil2(){

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
     * detect face
     *
     * @param imgFile
     * @return
     */
    public IplImage detectFace(String imgFile) {

        Mat originalMat = imread(imgFile, CV_LOAD_IMAGE_GRAYSCALE);
        IplImage originalImage = JavaCVUtil.mat2IplImage(originalMat);

        CvSeq faces = detectFace(originalImage);
        System.out.println("faces num----" + faces.total());
        if (faces.total() == 1) {
            CvRect r = new CvRect(cvGetSeqElem(faces, 0));
            //preprocess get Image
            IplImage trainImage = preprocessImage(originalImage, r);
        }

        return null;
    }

    //Detect Faces ( get CvSeq )
    public CvSeq detectFace(IplImage originalImage) {
        CvSeq faces = null;
        Loader.load(opencv_objdetect.class);
        try {
            IplImage grayImage = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
            cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
            CvMemStorage storage = CvMemStorage.create();
            faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faces;
    }


    // Preprocess get Image
    public IplImage preprocessImage(IplImage image, CvRect r) {
        IplImage gray = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        IplImage roi = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        CvRect r1 = new CvRect(r.x() - 10, r.y() - 10, r.width() + 10, r.height() + 10);
        cvCvtColor(image, gray, CV_BGR2GRAY);
        cvSetImageROI(gray, r1);
        cvResize(gray, roi, CV_INTER_LINEAR);
        cvEqualizeHist(roi, roi);
        return roi;
    }


    /**
     * predict face (by bin file)
     */
    public int predictFace(Mat mat){
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
        // valid faceid
        if (faceId <1) {
            throw new RuntimeException("FaceId 非法");
        }

        // valid images
        if (imgList==null || imgList.size()<4 || imgList.size()>50 ) {
            throw new RuntimeException("图片素材数量非法，限制为4~50张");
        }

        // images
        MatVector images = new MatVector(imgList.size());
        List<Mat> matList = new ArrayList<>();

        for (int i = 0; i < imgList.size(); i++) {

            Mat mat = imread(imgList.get(i), CV_LOAD_IMAGE_GRAYSCALE);
            matList.add(mat);

            String imageFileName = FR_IMAGES + faceId + "_" + i + ".bmp";
            File imgFile = new File(imageFileName);
            if (imgFile.exists()) {
                imgFile.delete();
            }
            //cvSaveImage(imageFileName, JavaCVUtil.mat2IplImage(mat));
        }


        // labels
        Mat labels = new Mat(matList.size(), 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        for (int i = 0; i < matList.size(); i++) {
            // img + lable(faceid)
            Mat img = matList.get(i);
            int label = faceId;

            // bing image-lable, by counter
            images.put(counter, img);
            labelsBuf.put(counter, label);

            counter++;
        }

        // train
        faceRecognizer.train(images, labels);
        logger.info("trainFace success, train faceId:{}", faceId);

        // write to b-file
        File binaryDataFile = new File(FR_BINARY_DATA);
        if (binaryDataFile.exists()) {
            binaryDataFile.delete();
        }
        //faceRecognizer.save(FR_BINARY_DATA);
        logger.info("trainFace success, save faceId:{}", faceId);
    }

    /**
     * sync bin file, by face img when save
     */
    public void syncBinFile(){

    }

}
