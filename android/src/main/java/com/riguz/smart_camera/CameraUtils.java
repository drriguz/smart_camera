package com.riguz.smart_camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.CamcorderProfile;
import android.util.Size;

import com.riguz.smart_camera.types.CameraDescription;
import com.riguz.smart_camera.types.ResolutionPreset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static android.hardware.camera2.CameraCharacteristics.*;

public final class CameraUtils {
    private CameraUtils() {
    }

    public static List<CameraDescription> getAvailableCameras(Activity activity)
            throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIds = cameraManager.getCameraIdList();

        List<CameraDescription> cameras = new ArrayList<>();
        for (String cameraId : cameraIds) {
            CameraDescription cameraInfo = getCameraInfo(cameraManager, cameraId);
            cameras.add(cameraInfo);
        }

        return cameras;
    }

    public static Size computeBestPreviewSize(
            String cameraName,
            ResolutionPreset preset) {
        // use high as maximum quality
        if (preset.ordinal() > ResolutionPreset.high.ordinal())
            preset = ResolutionPreset.high;

        CamcorderProfile bestProfile = getBestAvailableCamcorderProfile(cameraName, preset);
        return new Size(bestProfile.videoFrameWidth, bestProfile.videoFrameHeight);
    }

    public static CamcorderProfile getBestAvailableCamcorderProfile(
            String cameraName,
            ResolutionPreset resolutionPreset) {
        final int cameraId = Integer.parseInt(cameraName);
        final int presetQuality = resolutionPreset.getQuality();
        if (CamcorderProfile.hasProfile(cameraId, presetQuality))
            return CamcorderProfile.get(cameraId, presetQuality);
        else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW))
            return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
        else
            throw new RuntimeException("Unable to get any CamcorderProfile");
    }

    private static CameraDescription getCameraInfo(
            CameraManager cameraManager,
            String cameraId) throws CameraAccessException {
        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
        return new CameraDescription(cameraId,
                characteristics.get(SENSOR_ORIENTATION),
                characteristics.get(LENS_FACING));
    }
}
