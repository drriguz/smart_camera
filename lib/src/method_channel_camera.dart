import 'dart:async';

import 'package:flutter/services.dart';
import 'package:smart_camera/src/types/camera_event.dart';
import 'package:smart_camera/src/types/resolution_preset.dart';

import 'types/camera_description.dart';
import 'types/camera_exception.dart';
import 'package:stream_transform/stream_transform.dart';

const MethodChannel _channel = MethodChannel('com.riguz/camera');

class MethodChannelCamera {
  final Map<int, MethodChannel> _cameraChannels = {};
  final StreamController<CameraEvent> _cameraEventStreamController =
      StreamController<CameraEvent>.broadcast();

  static MethodChannelCamera _instance = new MethodChannelCamera();

  static MethodChannelCamera get instance => _instance;

  static Future<List<CameraDescription>> availableCameras() async {
    try {
      final List<Map<dynamic, dynamic>> cameras = await _channel
          .invokeListMethod<Map<dynamic, dynamic>>('availableCameras');
      print('cameras: $cameras');
      return cameras.map((Map<dynamic, dynamic> camera) {
        return CameraDescription(
          name: camera['name'],
          lensDirection: parseCameraLensDirection(camera['lensFacing']),
          sensorOrientation: camera['sensorOrientation'],
        );
      }).toList();
    } on PlatformException catch (e) {
      throw CameraException(e.code, e.message);
    }
  }

  Future<int> createCamera(
    CameraDescription cameraDescription,
    ResolutionPreset resolutionPreset,
  ) async {
    try {
      final int textureId = await _channel.invokeMethod(
        "create",
        <String, dynamic>{
          "name": cameraDescription.name,
          "resolutionPreset": resolutionPreset.name
        },
      );
      return textureId;
    } on PlatformException catch (e) {
      throw CameraException(e.code, e.message);
    }
  }

  Future<void> initializeCamera(int cameraId) {
    _cameraChannels.putIfAbsent(cameraId, () {
      final channel = new MethodChannel('com.riguz/cameraDevice/$cameraId');
      channel.setMethodCallHandler((call) => handleCameraMethodCall(
            call,
            cameraId,
          ));
      return channel;
    });

    Completer completer = new Completer();

    onCameraInitialized(cameraId).first.then((value) => completer.complete());

    _channel.invokeMapMethod(
      'initialize',
      <String, dynamic>{'cameraId': cameraId},
    );

    return completer.future;
  }

  Future<void> dispose(int cameraId) async {
    await _channel.invokeMethod(
      'dispose',
      <String, dynamic>{'cameraId': cameraId},
    );
    if (_cameraChannels.containsKey(cameraId)) {
      _cameraChannels[cameraId].setMethodCallHandler(null);
      _cameraChannels.remove(cameraId);
    }
  }

  Future<dynamic> handleCameraMethodCall(MethodCall call, int cameraId) {
    switch (call.method) {
      case 'initialized':
        _cameraEventStreamController.add(
          new CameraInitializedEvent(
            cameraId,
            call.arguments['previewWidth'],
            call.arguments['previewHeight'],
          ),
        );
        break;
    }
  }

  Stream<CameraEvent> _cameraEvents(int cameraId) =>
      _cameraEventStreamController.stream
          .where((event) => event.cameraId == cameraId);

  Stream<CameraInitializedEvent> onCameraInitialized(int cameraId) {
    return _cameraEvents(cameraId).whereType<CameraInitializedEvent>();
  }
}
