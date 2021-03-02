package com.riguz.smart_camera;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.view.TextureRegistry;

public class SmartCameraPlugin implements FlutterPlugin, ActivityAware {
    private FlutterPluginBinding flutterPluginBinding;
    private MethodCallHandler methodCallHandler;

    public SmartCameraPlugin() {
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        this.flutterPluginBinding = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        maybeStartListening(binding.getActivity(),
                flutterPluginBinding.getBinaryMessenger(),
                flutterPluginBinding.getTextureRegistry(),
                binding::addRequestPermissionsResultListener);
    }

    @Override
    public void onDetachedFromActivity() {
        methodCallHandler = null;
    }


    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    private void maybeStartListening(
            Activity activity,
            BinaryMessenger messenger,
            TextureRegistry textureRegistry,
            CameraPermissions.PermissionsRegistry permissionsRegistry) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If the sdk is less than 21 (min sdk for Camera2) we don't register the plugin.
            return;
        }

        methodCallHandler = new MethodCallHandlerImpl(
                activity,
                messenger,
                textureRegistry,
                permissionsRegistry);
    }
}
