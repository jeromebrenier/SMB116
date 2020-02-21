package fr.jbrenier.petfoodingcontrol.android.activities.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;

/**
 * A helper to permission management for the Main Activity.
 * @author Jérôme Brenier
 */
class MainActivityPermissionsHelper {
    /** Logging */
    private static final String TAG = "MainActivityPermissionsHelper";

    /** Request code */
    static final int PERMISSIONS_REQUEST = 3;

    private final MainActivity mainActivity;
    private final Resources resources;
    private final PetFoodingControl petFoodingControl;

    MainActivityPermissionsHelper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.resources = mainActivity.getResources();
        petFoodingControl = (PetFoodingControl) mainActivity.getApplication();
    }

    /**
     * Check if the required permissions to use the application have been accepted and are already
     * granted. If not, launch a dialog to inform the user of the permission need for the missing
     * ones.
     */
    void checkApplicationPermissions() {
        Log.i(TAG, "Checking permissions.");
        final List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        if (addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(resources.getString(R.string.permission_camera));
        if (addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add(resources.getString(R.string.permission_read_external_storage));

        if (permissionsList.size() > 0) {
            Log.i(TAG,"permissionsList.size()");
            if (permissionsNeeded.size() > 0) {
                Log.i(TAG,"permissionsNeeded.size()");
                String message = resources.getString(R.string.permission_grant_access_needed)
                        + " " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        (dialog, which) -> mainActivity.requestPermissions(permissionsList.toArray(
                                new String[permissionsList.size()]), PERMISSIONS_REQUEST),
                        (dialog, which) -> mainActivity.getPermissionProcessDone().setValue(true));
                return;
            }
            mainActivity.requestPermissions(permissionsList.toArray(
                    new String[permissionsList.size()]), PERMISSIONS_REQUEST);
            return;
        }
        Log.i(TAG,"All required permissions are already granted.");
        petFoodingControl.isCameraPermissionGranted.setValue(true);
        petFoodingControl.isReadExternalStoragePermissionGranted.setValue(true);
        mainActivity.getPermissionProcessDone().setValue(true);
    }

    /**
     * Check if the user have to be :
     * <ul>
     *     <li>informed that some permissions are needed with a message</li>
     *     <li>asked to grant the permission</li>
     * </ul>
     * @param permissionsList the list of permission
     * @param permission the permission to check
     * @return false if no info message needed, true otherwise.
     */
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (mainActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        permissionsList.add(permission);
        return !mainActivity.shouldShowRequestPermissionRationale(permission);
    }

    /**
     * Show an OK/Cancel dialog message to inform that a permission is needed and ask for a
     * reaction.
     * @param message the message to display
     * @param okListener the listener triggered by the OK button
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener,
                                     DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(mainActivity)
                .setMessage(message)
                .setPositiveButton(resources.getString(R.string.btn_OK), okListener)
                .setNegativeButton(resources.getString(R.string.btn_cancel), cancelListener)
                .create()
                .show();
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.i(TAG, "RequestPermissionResult");
        Map<String, Integer> perms = new HashMap<String, Integer>();

        perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
        }

        if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "isCameraPermissionGranted.setValue(true)");
            petFoodingControl.isCameraPermissionGranted.setValue(true);
        }
        if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "isReadExternalStoragePermissionGranted.setValue(true)");
            petFoodingControl.isReadExternalStoragePermissionGranted.setValue(true);
        }
        mainActivity.getPermissionProcessDone().setValue(true);


    }
}
