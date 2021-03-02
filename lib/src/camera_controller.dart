import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pedantic/pedantic.dart';
import 'package:smart_camera/src/types/camera_event.dart';
import 'package:smart_camera/src/types/camera_exception.dart';

import '../smart_camera.dart';
import 'types/camera_description.dart';
import 'types/resolution_preset.dart';

class CameraValue {
  final bool isInitialized;
  final String errorDescription;
  final Size previewSize;

  const CameraValue({
    this.isInitialized,
    this.errorDescription,
    this.previewSize,
  });

  const CameraValue.uninitialized()
      : this(
          isInitialized: false,
        );

  double get aspectRatio => previewSize.width / previewSize.height;

  bool get hasError => errorDescription != null;

  CameraValue copyWith({
    isInitialized,
    errorDescription,
    previewSize,
  }) {
    return CameraValue(
      isInitialized: isInitialized ?? this.isInitialized,
      errorDescription: errorDescription ?? this.errorDescription,
      previewSize: previewSize ?? this.previewSize,
    );
  }
}

class CameraController extends ValueNotifier<CameraValue> {
  final CameraDescription description;
  final ResolutionPreset resolutionPreset;
  final bool enableAudio;
  final _methodChannelCamera = MethodChannelCamera.instance;

  bool _isDisposed = false;
  int _cameraId;
  FutureOr<bool> _initCalled;

  CameraController(
    this.description,
    this.resolutionPreset,
    this.enableAudio,
  ) : super(CameraValue.uninitialized());

  int get cameraId => _cameraId;

  Future<void> initialize() async {
    if (_isDisposed)
      throw CameraException(
        'CameraController Disposed',
        'initialize was called on a disposed CameraController',
      );

    try {
      Completer<CameraInitializedEvent> initializeCompleter = new Completer();
      _cameraId = await _methodChannelCamera.createCamera(
        description,
        resolutionPreset,
      );

      unawaited(_methodChannelCamera
          .onCameraInitialized(_cameraId)
          .first
          .then((event) => initializeCompleter.complete(event)));

      await _methodChannelCamera.initializeCamera(_cameraId);

      value = value.copyWith(
        isInitialized: true,
        previewSize: await initializeCompleter.future
            .then((value) => new Size(value.previewWidth, value.previewHeight)),
      );
    } on PlatformException catch (e) {
      throw CameraException(e.code, e.message);
    }
  }

  @override
  Future<void> dispose() async {
    if (_isDisposed) return;
    _isDisposed = true;
    super.dispose();

    if (_initCalled != null) {
      await _initCalled;
      await _methodChannelCamera.dispose(_cameraId);
    }
  }
}

CameraLensDirection parseCameraLensDirection(String string) {
  switch (string) {
    case 'front':
      return CameraLensDirection.front;
    case 'back':
      return CameraLensDirection.back;
    case 'external':
      return CameraLensDirection.external;
  }
  throw ArgumentError('Unknown CameraLensDirection value');
}
