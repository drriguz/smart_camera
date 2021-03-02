import 'package:flutter/material.dart';
import 'package:smart_camera/smart_camera.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final cameras = await MethodChannelCamera.availableCameras();
  final camera = cameras[0];
  print('using camera: $camera');

  runApp(MyApp(camera));
}

class MyApp extends StatefulWidget {
  final CameraDescription cameraDescription;

  const MyApp(this.cameraDescription, {Key key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  CameraController _controller;

  @override
  void initState() {
    super.initState();

    _controller = new CameraController(
      widget.cameraDescription,
      ResolutionPreset.low,
      true,
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: CameraPreview(_controller),
      ),
    );
  }
}
