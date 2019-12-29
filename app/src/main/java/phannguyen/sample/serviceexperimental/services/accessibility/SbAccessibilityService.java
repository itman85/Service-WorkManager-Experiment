package phannguyen.sample.serviceexperimental.services.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

public class SbAccessibilityService extends AccessibilityService {
    private static final String TAG = "SbAccessibilityService";

    @Override
    public void onCreate() {
        super.onCreate();
        SbLog.i(TAG,"Created");
        FileLogs.writeLogInThread(this,TAG,"I","Created");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event != null && event.getPackageName() != null) {
            SbLog.i(TAG,"Event type "+ eventTypeToString(event.getEventType()));
            FileLogs.writeLogInThread(this,TAG,"I","Event type "+ eventTypeToString(event.getEventType()));
        }
    }

    @Override
    public void onInterrupt() {
        FileLogs.writeLogInThread(this,TAG,"I","Interrupted");
        SbLog.i(TAG,"Interrupted");
    }

    @Override
    protected void onServiceConnected() {
        SbLog.i(TAG,"Connected");
        FileLogs.writeLogInThread(this,TAG,"I","Connected");
        //super.onServiceConnected();
        // this same config in @xml/serviceconfig
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 1000;
        info.packageNames = null;
        setServiceInfo(info);
    }

    private String eventTypeToString(int eventType){
        switch (eventType){
            case 2048:
                return "TYPE WINDOW CONTENT CHANGED";
            case 32:
                return "WINDOWS CHANGE ACTIVE";
            case 8:
                return "WINDOWS CHANGE BOUNDS";
            case 1:
                return "WINDOWS CHANGE ADDED";
            case 4096:
                return "TYPE VIEW SCROLLED";

                default:
                    return "Other Type";
        }
    }
}
