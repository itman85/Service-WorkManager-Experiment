package phannguyen.sample.serviceexperimental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import phannguyen.sample.serviceexperimental.helpers.WorkManagerHelper;
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
