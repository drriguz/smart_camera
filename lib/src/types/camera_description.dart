enum CameraLensDirection {
  front,
  back,
  external,
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

class CameraDescription {
  final String name;
  final CameraLensDirection lensDirection;
  final int sensorOrientation;

  CameraDescription({
    this.name,
    this.lensDirection,
    this.sensorOrientation,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is CameraDescription &&
          runtimeType == other.runtimeType &&
          name == other.name;

  @override
  int get hashCode => name.hashCode;

  @override
  String toString() {
    return 'CameraDescription{name: $name, lensDirection: $lensDirection, sensorOrientation: $sensorOrientation}';
  }
}
