package com.imadji.punten;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_PERMISSION = 999;

    private boolean isFirstTimeAskingPermission = true;
    private TextView textPermissionInfo;
    private Button buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textPermissionInfo = findViewById(R.id.text_permission_info);
        buttonSettings = findViewById(R.id.btn_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAppPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        isFirstTimeAskingPermission = false;
        if (!PermissionUtil.isAllPermissionsVerified(grantResults)) {
            Log.d(TAG, "Permission hasn't been granted and must be requested");
            return;
        }

        Log.d(TAG, "All permissions has been granted");
        showContent();

    }

    private void checkAppPermissions() {
        Log.d(TAG, "Check if the all permissions has been granted");
        if (!PermissionUtil.isAllPermissionsGranted(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!isFirstTimeAskingPermission && PermissionUtil.shouldShowRequestPermissionRationale(this)) {
                    Log.d(TAG, "Show UI with explanation for why user need this permission");
                    textPermissionInfo.setText(getResources().getString(R.string.main_permission_explanation));
                    buttonSettings.setVisibility(View.VISIBLE);
                    return;
                }

                ActivityCompat.requestPermissions(this, PermissionUtil.PERMISSIONS, REQUEST_CODE_PERMISSION);
                return;
            }
        }

        Log.d(TAG, "All permissions has been granted");
        showContent();

    }

    private void showContent() {
        textPermissionInfo.setText(getResources().getString(R.string.main_permission_granted));
        buttonSettings.setVisibility(View.GONE);
    }

    private void goToSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}
