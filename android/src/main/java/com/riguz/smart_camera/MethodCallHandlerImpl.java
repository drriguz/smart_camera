package com.riguz.smart_camera;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.riguz.smart_camera.types.AbstractResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
    private final Activity activity;
    private final BinaryMessenger messenger;
    private final MethodChannel methodChannel;
    private final TextureRegistry textureRegistry;
    private final CameraPermissions cameraPermissions;
    private final CameraPermissions.PermissionsRegistry permissionsRegistry;

    private @Nullable
    Camera camera;

    public MethodCallHandlerImpl(
            Activity activity,
            BinaryMessenger messenger,
            TextureRegistry textureRegistry,
            CameraPermissions.PermissionsRegistry permissionsRegistry) {
        this.activity = activity;
        this.messenger = messenger;
        this.methodChannel = new MethodChannel(messenger, "com.riguz/camera");
        this.textureRegistry = textureRegistry;
        this.cameraPermissions = new CameraPermissions();
        this.permissionsRegistry = permissionsRegistry;

        this.methodChannel.setMethodCallHandler(this);
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "availableCameras":
                try {
                    result.success(convertResult(CameraUtils.getAvailableCameras(activity)));
                } catch (Exception ex) {
                    handleException(ex, result);
                }
                break;
            case "create": {
                createCamera(call, result);
                break;
            }
            case "initialize": {
                openCamera(call, result);
                break;
            }
            default:
                result.notImplemented();
                break;
        }
    }

    private void createCamera(MethodCall call, MethodChannel.Result result) {
        if (camera != null)
            camera.close();
        cameraPermissions.requestPermissions(
                activity,
                permissionsRegistry,
                (String errCode, String errDesc) -> {
                    if (errCode == null) {
                        String cameraName = call.argument("name");
                        String resolutionPreset = call.argument("resolutionPreset");
                        try {
                            createCamera(cameraName, resolutionPreset);
                        } catch (Exception e) {
                            handleException(e, result);
                        }
                    } else {
                        result.error(errCode, errDesc, null);
                    }
                });
    }

    private void openCamera(MethodCall call, MethodChannel.Result result) {
        if (camera != null) {
            camera.open();
            result.success(null);
        } else {
            result.error("CameraNotFound",
                    "Camera not created, call `create` first",
                    null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Map<String, Object>> convertResult(List<? extends AbstractResult> results) {
        return results.stream()
                .map(AbstractResult::getResult)
                .collect(Collectors.toList());
    }

    private Camera createCamera(String cameraName, String resolutionPreset) {
        TextureRegistry.SurfaceTextureEntry texture = textureRegistry.createSurfaceTexture();

        return new Camera(flutterTexture, cameraManager, cameraName);
    }

    private void handleException(Exception ex, MethodChannel.Result result) {
        if (ex instanceof CameraAccessException) {
            result.error("CameraAccess", ex.getMessage(), null);
        } else {
            throw (RuntimeException) ex;
        }
    }
}
