package phannguyen.sample.serviceexperimental.utils;

import android.os.Build;

public class Constant {
    public static final String TASK_DATA_INTERVAL_MAIN_WORKER_TAG = "TASK_DATA_INTERVAL_MAIN_WORKER_TAG";
    public static final String TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME = "TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME";
    public static final String TASK_DATA_ONETIME_WORKER_TAG = "TASK_DATA_ONETIME_WORKER_TAG";
    public static final String BROADCAST_CUSTOM_ACTION = "phannguyen.com.test.START";

    public static final int INTERVAL_PROCESS_DATA_IN_SECOND = 30*60; // 30 mins
    public static final int TIME_PERIOD_TO_CHECK_SERVICE_PROCESS_DATA_IN_SECOND = 2 * INTERVAL_PROCESS_DATA_IN_SECOND; //

    public static final int DELAY_PROCESS_DATA = 3*60; // 3 mins
    public static final int SERVICE_RUN_TIME = 5*60; // 5 mins

    public static final int QUICK_DELAY_PROCESS_DATA_IN_SECOND = 5; // 10 second

    public static final String APP_TAG = "App";

    public static final long SLEEP_TIME_IN_MS = 3*60*1000; // 3 mins
    public static final long SLEEP_TIME_LOOP_IN_MS = 5*1000; // 5 second

    // will use alarm manager to schedule job for all android sdk versions from 5 -> 10
    public static boolean TEST_USE_ALARM_MANAGER_FOR_ALL_SDK_VERSION = false;

    //from this version of sdk will use work manager instead of alarm manager
    public static int SDK_USE_WORK_MANAGER = Build.VERSION_CODES.O;

    public static int PENDING_INTENT_MAIN_BROADCAST_RECEIVER_REQUEST_CODE = 1;

    public enum SchedulePolicy{
        Keep,
        Replace
    }
}
