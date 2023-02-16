package com.yb.usbcamera;

import com.yb.usbcamera.utils.ToRtmpUtil;

public class UsbCameraApplication {

    public static void main(String[] args) {
        // 将本地摄像头的视频转换为rtmp
        ToRtmpUtil.toRtmp();
    }

}
