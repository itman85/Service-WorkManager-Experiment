package phannguyen.sample.serviceexperimental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import phannguyen.sample.serviceexperimental.helpers.WorkManagerHelper;
import phannguyen.sample.serviceexperimental.services.accessibility.SbAccessibilityService;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.QUICK_DELAY_PROCESS_DATA;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.startBtn);
        Button enableService = findViewById(R.id.enableServiceBtn);

        start.setOnClickListener(view -> {
            FileLogs.writeLogNoThread(this,APP_TAG,"I","App Click Button Start Active");
            //NOTE From android 8+ must use explicit intent for MainBroadcastReceiver class
           /* Intent intent = new Intent(this, MainBroadcastReceiver.class);
            intent.setAction(Constant.BROADCAST_CUSTOM_ACTION);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //
            Bundle extras = new Bundle();
            extras.putString("send_data", "test");
            intent.putExtras(extras);
            //
            sendBroadcast(intent);*/
            // todo consider choose stable way to start running service at press active button,
            // this way not sure work manager will fire worker to doWork after delay exactly
            WorkManagerHelper.startWorkingInDelayTime(this,QUICK_DELAY_PROCESS_DATA);
            //
            forceGetToken();
        });

        enableService.setOnClickListener(view -> {
            if(!isAccessServiceEnabled(MainActivity.this, SbAccessibilityService.class)){
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Make sure Accessibility Service is enabled in Device Settings (Accessibility section).")
                        .setPositiveButton("Ok", (dialog, which) -> {
                            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivityForResult(intent, 0);
                        }).setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(
                            MainActivity.this,
                            "In order to use this app you must have Standalone ACCESSIBILITY SERVICE turn on, please reopen app and turn it on.",
                            Toast.LENGTH_LONG).show();
                }).show();

            }else{
                Toast.makeText(MainActivity.this,"Already enable this accessibility service",Toast.LENGTH_LONG).show();
            }

        });

        //check write external storage and ask permission if needed
        isStoragePermissionGranted();

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                SbLog.v(TAG,"Permission is granted");
                return true;
            } else {

                SbLog.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            SbLog.v(TAG,"Permission is granted");
            return true;
        }
    }

    // todo test on other each version of android 5,6,7,8,9,10
    public boolean isAccessServiceEnabled(Context context, Class accessibilityServiceClass)
    {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return prefString!= null && prefString.contains(context.getPackageName() + "/" + accessibilityServiceClass.getName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            SbLog.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void forceGetToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, instanceIdResult -> {
            String refreshedToken = instanceIdResult.getToken();
            if (refreshedToken != null && !refreshedToken.equals("")) {
                SbLog.i(TAG,"fcmToken: " +refreshedToken);
                FileLogs.writeLogInThread(this,"fcmToken","I","fcmToken "+ refreshedToken);
            }

        });
    }
}
