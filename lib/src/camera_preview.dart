import 'package:flutter/material.dart';

import '../smart_camera.dart';

class CameraPreview extends StatelessWidget {
  final CameraController controller;

  const CameraPreview(this.controller, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return controller.value.isInitialized
        ? AspectRatio(
            aspectRatio: controller.value.aspectRatio,
            child: Stack(
              fit: StackFit.expand,
              children: [
                Texture(textureId: controller.cameraId),
              ],
            ),
          )
        : Container();
  }
}
