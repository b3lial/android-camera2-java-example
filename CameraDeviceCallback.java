package your.app.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.Vector;

public class CameraDeviceCallback extends CameraDevice.StateCallback {
    private final String TAG = this.getClass().getSimpleName();
    private SurfaceView cameraPreview;
    private Handler handler;

    public CameraDeviceCallback(SurfaceView cameraPreview, Handler handler) {
        this.cameraPreview = cameraPreview;
        this.handler = handler;
    }

    @Override
    public void onOpened(@NonNull final CameraDevice cameraDevice) {
        Log.i(TAG, "CameraDevice.StateCallback onOpened()");
        final CameraCaptureSession.StateCallback csc = new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                Log.i(TAG, "CameraCaptureSession.StateCallback onConfigured()");

                try {
                    CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    builder.addTarget(cameraPreview.getHolder().getSurface());
                    cameraCaptureSession.setRepeatingRequest(builder.build(), null, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    return;
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                Log.e(TAG, "CameraCaptureSession.StateCallback onConfigureFailed()");
            }
        };

        Vector<Surface> v = new Vector<>();
        v.add(cameraPreview.getHolder().getSurface());
        try {
            cameraDevice.createCaptureSession(v, csc, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
        Log.i(TAG, "CameraDevice.StateCallback onDisconnected()");
    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int i) {
        Log.i(TAG, "CameraDevice.StateCallback onError()");
    }
}
