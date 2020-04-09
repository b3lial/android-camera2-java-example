# android-camera2-java-example
I needed a Camera2 based Android API example which can be easily integrated in my projects. Therefore, I wrote this 
CameraController Class for Java which lets you easily start a Preview in a SurfaceView.

## Usage
This example is written for a Fragment. We create a CameraController instance after we got the corresponding permissions:

```java
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    //...
    String[] perm = {
            Manifest.permission.CAMERA
    };
    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(perm, REQUEST_CAMERA);
    } else {
        Log.i(TAG, "CAMERA permissions available, creating SurfaceView");
        this.mSurfaceView = binding.cameraPreview;
        this.mSurfaceHolder = this.mSurfaceView.getHolder();
        this.mSurfaceHolder.addCallback(this);
        
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        cameraController = new CameraController(cameraManager, mSurfaceView);
    }                         
}

@Override
public void onRequestPermissionsResult(int requestCode,
                                       String[] permissions,
                                       int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && requestCode == REQUEST_CAMERA) {
        Log.i(TAG, "CAMERA permissions granted, recreating Activity to generate SurfaceView");
        getActivity().recreate();
    } else {
        Log.e(TAG, "CAMERA permissions denied");
        NavHostFragment.findNavController(this).navigateUp();
    }
}
```

The SurfaceView object is received via the new binding API but you could also use Butterknife or something comparable. When
the CameraController has been instanciated, start the preview after surface creation:

```java
@Override
public void surfaceCreated(SurfaceHolder holder) {
    Log.i(TAG, "surfaceCreated()");
    if(cameraController != null) {
        cameraController.startCameraPreview();
    }
    else{
        Log.e(TAG, "CameraController not available, can not start Camera Preview");
    }
}
```

Enjoy your preview :)
