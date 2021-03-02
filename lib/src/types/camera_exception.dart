class CameraException implements Exception {
  final String code;
  final String description;

  CameraException(this.code, this.description);

  @override
  String toString() {
    return 'Camera exception: $code, $description';
  }
}
