package your.app.camera;

import android.annotation.SuppressLint;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;

public class CameraController {
    private final String TAG = this.getClass().getSimpleName();
    private CameraManager cameraManager;
    private SurfaceView cameraPreview;

    public CameraController(CameraManager cameraManager, SurfaceView cameraPreview){
        this.cameraManager = cameraManager;
        this.cameraPreview = cameraPreview;
    }

    @SuppressLint("MissingPermission")
    public void startCameraPreview(){
        Log.i(TAG, "Starting Camera Preview");
        final Handler handler = new Handler();
        CameraDeviceCallback csc = new CameraDeviceCallback(cameraPreview, handler);

        //we want to use the backfacing camera
        String backfacingId = getBackfacingCameraId(cameraManager);
        if (backfacingId == null) {
            Log.e(TAG, "Can not open Camera because no backfacing Camera was found");
            return;
        }

        //we have a backfacing camera, so we can start the preview
        try {
            cameraManager.openCamera(backfacingId, csc, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String getBackfacingCameraId(CameraManager cameraManager){
        try {
            String[] ids = cameraManager.getCameraIdList();
            for (int i = 0; i < ids.length; i++) {
                Log.i(TAG, "Found Camera ID: " + ids[i]);
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(ids[i]);
                int cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraDirection == CameraCharacteristics.LENS_FACING_BACK) {
                    Log.i(TAG, "Found back facing camera");
                    return ids[i];
                }
            }
            return null;
        }
        catch(CameraAccessException ce){
            ce.printStackTrace();
            return null;
        }
    }
}
