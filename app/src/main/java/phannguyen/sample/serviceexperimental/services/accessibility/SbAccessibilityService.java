package phannguyen.sample.serviceexperimental.services.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import phannguyen.sample.serviceexperimental.helpers.ServiceHelper;
import phannguyen.sample.serviceexperimental.services.main.TestLongRunningService;
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

    // this run in bg thread
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event != null && event.getPackageName() != null) {
            SbLog.i(TAG,"Event type "+ eventTypeToString(event.getEventType()));
            SbLog.i(TAG,"Package "+ event.getPackageName());
            //FileLogs.writeLogInThread(this,TAG,"I","Event type "+ eventTypeToString(event.getEventType()));
            checkToStartMainServiceProcessData(event.getEventType());
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
                    return "Other Type "+eventType;
        }
    }

    private void checkToStartMainServiceProcessData(int eventType){
        // measure time read data from share prefs, this probably happen every often -> ~ 15ms 1st time, 0 - 5 ms from 2nd time
        if( eventType == AccessibilityEvent.WINDOWS_CHANGE_ADDED ) {
            FileLogs.writeLogInThread(this,TAG,"I","Window added check last time checking service");
            //long s1 = System.currentTimeMillis();
            long lastTimeCheck = SharePref.getLastTimeCheckMainService(this);
            //long s2 = System.currentTimeMillis();
            //SbLog.i(TAG,"Time to read shareprefs "+(s2-s1));
            if (System.currentTimeMillis() - lastTimeCheck > Constant.TIME_PERIOD_TO_CHECK_SERVICE_PROCESS_DATA_IN_SECOND * 1000) {
                FileLogs.writeLogInThread(this,TAG,"I","Main service has not been started for long time, so start service now");
                // update last time checking main service
                SharePref.setLastTimeCheckMainService(this, System.currentTimeMillis());
                // for too long time it has not started service, so start service now
                ServiceHelper.startLongRunningServiceInBackground(this,new Intent(this, TestLongRunningService.class));
            }
        }
    }

    // todo : 1.AccessibilityService run in bg thread, so test wil delay time to see how event receive sync or not
    // todo: 2.Check event suitable to check main service
}
