package com.seu.magicfilter.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.SurfaceView;

import com.seu.magicfilter.camera.utils.CameraUtils;

import java.io.IOException;
import java.util.List;

public class CameraEngine {
    private static Camera camera = null;
    private static int cameraID = 0;
    private static SurfaceTexture surfaceTexture;
    private static SurfaceView surfaceView;
    private static Size defaultSize;

    public static Camera getCamera(){
        return camera;
    }

    public static boolean openCamera(){
        if(camera == null){
            try{
                camera = Camera.open(cameraID);
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static boolean openCamera(int id){
        if(camera == null){
            try{
                camera = Camera.open(id);
                cameraID = id;
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static void releaseCamera(){//释放相机
        if(camera != null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void resumeCamera(){
        openCamera();
    }

    public void setParameters(Parameters parameters){
        camera.setParameters(parameters);
    }

    public Parameters getParameters(){
        if(camera != null)
            camera.getParameters();
        return null;
    }

    public static void switchCamera(){//切换前后摄像头：
        releaseCamera();
        cameraID = cameraID == 0 ? 1 : 0;
        openCamera(cameraID);
        startPreview(surfaceTexture);
    }
//设置camera默认参数
    private static void setDefaultParameters(){
        Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        List<Size> sizeList = parameters.getSupportedPreviewSizes();//获取camera支持的预览尺寸  //获取预览的各种分辨率
        int width = 0;
        for (Size s : sizeList) {
            if (s.width > width && s.width <= 700 && s.width * 3 == s.height * 4) {
                width = s.width;
                defaultSize = s;
            }
        }
        Size previewSize = CameraUtils.getLargePreviewSize(camera);
//        parameters.setPreviewSize(previewSize.width, previewSize.height);
        parameters.setPreviewSize(defaultSize.width, defaultSize.height);
        Size pictureSize = CameraUtils.getLargePictureSize(camera);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setRotation(90);
        camera.setParameters(parameters);
    }

//    private static void setDefaultParameters(){
//        Parameters parameters = camera.getParameters();
//        if (parameters.getSupportedFocusModes().contains(
//                Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        }
//        Size previewSize = CameraUtils.getLargePreviewSize(camera);
//        parameters.setPreviewSize(previewSize.width, previewSize.height);
//        Size pictureSize = CameraUtils.getLargePictureSize(camera);
//        parameters.setPictureSize(pictureSize.width, pictureSize.height);
//        parameters.setRotation(90);
//        camera.setParameters(parameters);
//    }

    //获取预览屏幕参数
    private static Size getPreviewSize(){
        return camera.getParameters().getPreviewSize();
    }

    //获取相机参数
    private static Size getPictureSize(){
        return camera.getParameters().getPictureSize();
    }

    public static void startPreview(SurfaceTexture surfaceTexture){//开始预览：
        if(camera != null)
            try {
                camera.setPreviewTexture(surfaceTexture);
                CameraEngine.surfaceTexture = surfaceTexture;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void startPreview(){//预览
        if(camera != null)
            camera.startPreview();
    }

    public static void stopPreview(){//停止预览
        camera.stopPreview();
    }

    public static void setRotation(int rotation){
        Parameters params = camera.getParameters();
        params.setRotation(rotation);
        camera.setParameters(params);
    }

    /**
     * 保存相片：
     * @param shutterCallback
     * @param rawCallback
     * @param jpegCallback
     */
    public static void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback jpegCallback){
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static com.seu.magicfilter.camera.utils.CameraInfo getCameraInfo(){//获取当前相机相关参数：
        com.seu.magicfilter.camera.utils.CameraInfo info = new com.seu.magicfilter.camera.utils.CameraInfo();
        Size size = getPreviewSize();
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraID, cameraInfo);
        info.previewWidth = size.width;
        info.previewHeight = size.height;
        info.orientation = cameraInfo.orientation;
        info.isFront = cameraID == 1 ? true : false;
        size = getPictureSize();
        info.pictureWidth = size.width;
        info.pictureHeight = size.height;
        return info;
    }
}