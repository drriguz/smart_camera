#import "SmartCameraPlugin.h"
#if __has_include(<smart_camera/smart_camera-Swift.h>)
#import <smart_camera/smart_camera-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "smart_camera-Swift.h"
#endif

@implementation SmartCameraPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSmartCameraPlugin registerWithRegistrar:registrar];
}
@end
