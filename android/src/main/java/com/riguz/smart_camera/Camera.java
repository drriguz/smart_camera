package com.riguz.smart_camera;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.CamcorderProfile;
import android.media.ImageReader;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.riguz.smart_camera.types.ResolutionPreset;

import io.flutter.view.TextureRegistry;

import static com.riguz.smart_camera.CameraUtils.computeBestPreviewSize;
import static com.riguz.smart_camera.CameraUtils.getBestAvailableCamcorderProfile;

public class Camera {
    private final TextureRegistry.SurfaceTextureEntry flutterTexture;
    private final CameraManager cameraManager;
    private final String cameraName;
    private final CameraCharacteristics cameraCharacteristics;
    private final CamcorderProfile profile;
    private final Size captureSize;

    private CameraDevice cameraDevice;
    private ImageReader pictureImageReader;

    public Camera(TextureRegistry.SurfaceTextureEntry flutterTexture,
                  CameraManager cameraManager,
                  ResolutionPreset resolutionPreset,
                  String cameraName)
            throws CameraAccessException {
        this.flutterTexture = flutterTexture;
        this.cameraManager = cameraManager;
        this.cameraName = cameraName;

        cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraName);
        profile = getBestAvailableCamcorderProfile(cameraName, resolutionPreset);
        captureSize = new Size(profile.videoFrameWidth, profile.videoFrameHeight);
    }

    public void open() throws CameraAccessException {
        pictureImageReader = ImageReader.newInstance(
                captureSize.getWidth(),
                captureSize.getHeight(),
                ImageFormat.JPEG,
                2);

        cameraManager.openCamera(
                cameraName,
                new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice device) {
                        cameraDevice = device;
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice cameraDevice) {

                    }

                    @Override
                    public void onError(@NonNull CameraDevice cameraDevice, int i) {

                    }
                },
                null
        );
    }

    private void startPreview() {
        if (pictureImageReader == null
                || pictureImageReader.getSurface() == null)
            return;

    }

    private void createCaptureSession(
            int templateType,
            Surface... surfaces) throws CameraAccessException {
        closeCaptureSession();

        cameraDevice.createCaptureRequest(templateType);
        SurfaceTexture surfaceTexture = flutterTexture.surfaceTexture();
        surfaceTexture.setDefaultBufferSize();

    }

    private void closeCaptureSession() {

    }

    public void close() {

    }

    public void dispose() {

    }
}
