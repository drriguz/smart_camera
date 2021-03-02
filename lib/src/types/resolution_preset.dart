enum ResolutionPreset {
  low,
  medium,
  high,
  veryHigh,
  ultraHigh,
  max,
}

extension ResolutionPresetExtension on ResolutionPreset {
  String get name {
    switch (this) {
      case ResolutionPreset.low:
        return 'low';
      case ResolutionPreset.medium:
        return "medium";
      case ResolutionPreset.high:
        return "high";
      case ResolutionPreset.veryHigh:
        return "veryHigh";
      case ResolutionPreset.ultraHigh:
        return "ultraHigh";
      case ResolutionPreset.max:
        return "max";
      default:
        return null;
    }
  }
}
