package simpyo.simpyo.Setting;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

public class GetPermission {
    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_EXTRENAL = 101;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkLocationPermission(Activity activity) {
        String[] permissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission);

                if (result == PermissionChecker.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
                }
            }
        }

    }

    public static int locationPermission(Activity activity) {
        String permission = android.Manifest.permission.ACCESS_FINE_LOCATION;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission);
        }

        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkCameraPermission(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission);

                if (result == PermissionChecker.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_EXTRENAL);
                }
            }
        }

    }

    public static int cameraPermission(Activity activity) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission);
        }

        return 1;
    }


}
