abstract class CameraEvent {
  final int cameraId;

  CameraEvent(this.cameraId);
}

class CameraInitializedEvent extends CameraEvent {
  final double previewWidth;
  final double previewHeight;

  CameraInitializedEvent(
    int cameraId,
    this.previewWidth,
    this.previewHeight,
  ) : super(cameraId);
}
