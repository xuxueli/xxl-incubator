package com.xuxueli.demo.face;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * javacv util
 *
 * @author xuxueli 2018-05-10 22:02:30
 */
public class JavaCVUtil {

    private static OpenCVFrameConverter.ToIplImage openCVFrameConverter = new OpenCVFrameConverter.ToIplImage();
    private static Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    private static opencv_objdetect.CvHaarClassifierCascade cascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(
            Thread.currentThread().getContextClassLoader().getResource("haarcascade_frontalface_alt.xml").getPath()
    )); // 配置：Haar分类器


    // ---------------------- detect face ----------------------
    /**
     * detect face
     *
     * @param imgFile
     * @return
     */
    public static Mat detectFace(String imgFile) {
        Mat originalMat = imread(imgFile);  //imread(imgFile, CV_LOAD_IMAGE_GRAYSCALE);
        IplImage originalImage = JavaCVUtil.mat2IplImage(originalMat);

        CvSeq faces = detectFace(originalImage);
        System.out.println("faces num----" + faces.total());
        if (faces.total() == 1) {
            CvRect r = new CvRect(cvGetSeqElem(faces, 0));
            //preprocess get Image
            IplImage trainImage = preprocessImage(originalImage, r);
            return JavaCVUtil.iplImage2Mat(trainImage);
        }

        return null;
    }

    // Detect Faces ( get CvSeq )
    public static CvSeq detectFace(IplImage originalImage) {
        Loader.load(opencv_objdetect.class);

        IplImage grayImage = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
        CvMemStorage storage = CvMemStorage.create();
        CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);

        return faces;
    }

    // Preprocess get Image
    public static IplImage preprocessImage(IplImage image, CvRect cvRect) {
        IplImage gray = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        IplImage roi = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        CvRect r1 = new CvRect(cvRect.x() - 10, cvRect.y() - 10, cvRect.width() + 10, cvRect.height() + 10);
        cvCvtColor(image, gray, CV_BGR2GRAY);
        cvSetImageROI(gray, r1);
        cvResize(gray, roi, CV_INTER_LINEAR);
        cvEqualizeHist(roi, roi);
        return roi;
    }


    // ---------------------- BufferedImage/IplImage ----------------------
    public static BufferedImage iplImage2BufferedImage(IplImage iplImage){
        Frame frame = openCVFrameConverter.convert(iplImage);
        BufferedImage bufferedImage = java2DFrameConverter.convert(frame);
        return bufferedImage;
    }

    // ---------------------- Frame/IplImage ----------------------
    public static IplImage frame2IplImage(Frame frame){
        IplImage iplImage = openCVFrameConverter.convertToIplImage(frame);
        return iplImage;
    }

    public static Frame iplImage2Frame(IplImage iplImage){
        Frame frame = openCVFrameConverter.convert(iplImage);
        return frame;
    }

    // ---------------------- Mat/IplImage ----------------------
    public static IplImage mat2IplImage(Mat mat){
        IplImage iplImage = new IplImage(mat);
        return iplImage;
    }

    public static Mat iplImage2Mat(IplImage iplImage){
        Mat mat = new Mat(iplImage);
        return mat;
    }

    // ---------------------- Mat/IplImage ----------------------
    public static Mat frame2Mat(Frame frame){
        Mat mat = openCVFrameConverter.convertToMat(frame);
        return mat;
    }

    public static Frame mat2Frame(Mat mat){
        Frame frame = openCVFrameConverter.convert(mat);
        return frame;
    }

}
