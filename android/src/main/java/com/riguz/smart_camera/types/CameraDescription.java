package com.riguz.smart_camera.types;

import static android.hardware.camera2.CameraMetadata.LENS_FACING_BACK;
import static android.hardware.camera2.CameraMetadata.LENS_FACING_EXTERNAL;
import static android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT;

public class CameraDescription extends AbstractResult {
    public CameraDescription(String name, int sensorOrientation, int lensFacing) {
        set("name", name);
        set("sensorOrientation", sensorOrientation);
        set("lensFacing", getLensFacingName(lensFacing));
    }

    private static String getLensFacingName(int lensFacing) {
        switch (lensFacing) {
            case LENS_FACING_FRONT:
                return "front";
            case LENS_FACING_BACK:
                return "back";
            case LENS_FACING_EXTERNAL:
                return "external";
            default:
                throw new IllegalArgumentException("Unexpected lens facing:" + lensFacing);
        }
    }
}
