package com.riguz.smart_camera.types;

import android.media.CamcorderProfile;

public enum ResolutionPreset {
    low(CamcorderProfile.QUALITY_QVGA),
    medium(CamcorderProfile.QUALITY_480P),
    high(CamcorderProfile.QUALITY_720P),
    veryHigh(CamcorderProfile.QUALITY_1080P),
    ultraHigh(CamcorderProfile.QUALITY_2160P),
    max(CamcorderProfile.QUALITY_HIGH);

    private final int quality;

    ResolutionPreset(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }
}
