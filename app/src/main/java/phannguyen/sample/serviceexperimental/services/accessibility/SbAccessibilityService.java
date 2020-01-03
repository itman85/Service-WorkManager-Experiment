package phannguyen.sample.serviceexperimental.services.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

import phannguyen.sample.serviceexperimental.utils.Constant;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;
import phannguyen.sample.serviceexperimental.utils.SharePref;

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
        info.notificationTimeout = 1000;// todo this will limit bg delay?
        info.packageNames = null;
        setServiceInfo(info);
    }

    private String eventTypeToString(int eventType){
        switch (eventType){
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                return "TYPE WINDOWS CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE WINDOW CONTENT CHANGED";
            case AccessibilityEvent.WINDOWS_CHANGE_ACTIVE:
                return "WINDOWS CHANGE ACTIVE";
            case AccessibilityEvent.WINDOWS_CHANGE_BOUNDS:
                return "WINDOWS CHANGE BOUNDS";
            case AccessibilityEvent.WINDOWS_CHANGE_ADDED:
                return "WINDOWS CHANGE ADDED";
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                return "TYPE VIEW SCROLLED";

                default:
                    return "Other Type";
        }
    }

    private void checkMainService(int eventType){
        // measure time read data from share prefs, this probably happen every often
        if( eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED ) {
            long lasttimeCheck = SharePref.getLastTimeCheckMainService(this);
            if (System.currentTimeMillis() - lasttimeCheck > Constant.INTERVAL_PROCESS_DATA*2) {
                // todo check last sync is on schedule or not, if not start service right away
                // update last time checking main service
                SharePref.setLastTimeCheckMainService(this, System.currentTimeMillis());
            }
        }
    }

    // todo : 1.AccessibilityService run in bg thread, so test wil delay time to see how event receive sync or not
    // todo: 2.Check event suitable to check main service
}
