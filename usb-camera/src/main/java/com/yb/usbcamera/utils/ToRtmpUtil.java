package com.yb.usbcamera.utils;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import sun.font.FontDesignMetrics;

/**
 * <p>
 * 将本地摄像头的视频转换为rtmp的工具类
 * <p>
 *
 * @author: yb
 * @date: 2022/1/12 17:55
 */
public class ToRtmpUtil {

    /**
     * 将本地摄像头的视频转换为rtmp
     *
     * @param:
     * @return: void
     */
    public static void toRtmp() {
        String localIp = null;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress().replace(".", "");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String outputAdd = "rtmp://192.168.1.106/myapp/" + localIp;
//        String outputAdd = "rtmp://10.10.13.4:1936/live/" + localIp;
        System.out.println(outputAdd);
        recordCamera1(outputAdd, 30);
        //recordCamera("D://yu//output.mp4", 26);
    }

//    /**
//     * 将本地摄像头的视频转换为rtmp
//     *
//     * @param:
//     * @return: void
//     */
//    public static void toRtmp() {
//        String localIp = null;
//        try {
//            localIp = InetAddress.getLocalHost().getHostAddress().replace(".", "");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        String outputAdd = PropertyUtil.getValue("appCnof.outputAdd") + localIp;
//        String frameRate = PropertyUtil.getValue("appCnof.frameRate");
//        System.out.println(outputAdd);
//        recordCamera(outputAdd, Integer.parseInt(frameRate));
//        //recordCamera("D://yu//output.mp4", 26);
//    }

    /**
     * 按帧录制本机摄像头视频（边预览边录制，停止预览即停止录制）
     *
     * @param: outputAdd 录制的文件路径，也可以是rtsp或者rtmp等流媒体服务器发布地址
     * @param: frameRate 视频帧率
     * @return: void
     */
    public static void recordCamera(String outputFile, int frameRate) {
        try {
            Loader.load(opencv_objdetect.class);
            // 本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
            FrameGrabber grabber = FrameGrabber.createDefault(0);
            // 开启抓取器
            grabber.start();
            // 抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
            Java2DFrameConverter conver = new Java2DFrameConverter();
            BufferedImage bufferedImage = conver.getBufferedImage(grabber.grab());
            // 封装视频
            FrameRecorder recorder = FrameRecorder.createDefault(outputFile, bufferedImage.getWidth(), bufferedImage.getHeight());
            // avcodec.AV_CODEC_ID_H264，编码
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            // 封装格式，如果是推送到rtmp就必须是flv封装格式
            recorder.setFormat("flv");
            recorder.setFrameRate(frameRate);
            // 开启录制器
            recorder.start();
            // 开始推流
            Frame rotatedFrame;
            long startTime = 0;
            long videoTS;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Font font = new Font("微软雅黑", Font.BOLD, 24);
            while ((bufferedImage = conver.getBufferedImage(grabber.grab())) != null) {
                rotatedFrame = conver.getFrame(mark(bufferedImage, dateFormat, font));
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                videoTS = 1000 * (System.currentTimeMillis() - startTime);
                recorder.setTimestamp(videoTS);
                recorder.record(rotatedFrame);
                Thread.sleep(10);
            }
            recorder.stop();
            recorder.release();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按帧录制本机摄像头视频（边预览边录制，停止预览即停止录制）
     *
     * @param: outputFile 录制的文件路径，也可以是rtsp或者rtmp等流媒体服务器发布地址
     * @param: frameRate 视频帧率
     * @return: void
     */
    public static void recordCamera1(String outputFile, int frameRate) {
        try {
            Loader.load(opencv_objdetect.class);
            // 本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
            FrameGrabber grabber = FrameGrabber.createDefault(0);
            // 窗口显示
            CanvasFrame frame = new CanvasFrame("USB摄像头", CanvasFrame.getDefaultGamma() / grabber.getGamma());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            // 开启抓取器
            grabber.start();
            // 抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
            Java2DFrameConverter conver = new Java2DFrameConverter();
            BufferedImage bufferedImage = conver.getBufferedImage(grabber.grab());
            // 封装视频
            FrameRecorder recorder = FrameRecorder.createDefault(outputFile, bufferedImage.getWidth(), bufferedImage.getHeight());
            // avcodec.AV_CODEC_ID_H264，编码
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            // 封装格式，如果是推送到rtmp就必须是flv封装格式
            recorder.setFormat("flv");
            recorder.setFrameRate(frameRate);
            // 开启录制器
            recorder.start();
            // 开始推流
            Frame rotatedFrame;
            long startTime = 0;
            long videoTS;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Font font = new Font("微软雅黑", Font.BOLD, 24);
            while (frame.isVisible() && (bufferedImage = conver.getBufferedImage(grabber.grab())) != null) {
                rotatedFrame = conver.getFrame(mark(bufferedImage, dateFormat, font));
                frame.showImage(rotatedFrame);
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                videoTS = 1000 * (System.currentTimeMillis() - startTime);
                recorder.setTimestamp(videoTS);
                recorder.record(rotatedFrame);
                Thread.sleep(10);
            }
            frame.dispose();
            recorder.stop();
            recorder.release();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加日期水印
     *
     * @param: bufImg 源图片
     * @param: dateFormat 日期格式
     * @param: font 字体
     * @return: java.awt.image.BufferedImage
     */
    public static BufferedImage mark(BufferedImage bufImg, SimpleDateFormat dateFormat, Font font) {
        Graphics2D graphics = bufImg.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        // 设置图片背景
        graphics.drawImage(bufImg, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        // 设置左上方时间显示
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        graphics.setColor(Color.white);
        graphics.setFont(font);
        graphics.drawString(dateFormat.format(new Date()), 8, metrics.getAscent());
        return bufImg;
    }

}