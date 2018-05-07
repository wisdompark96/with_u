package lecture.mobile.final_project.ma01_20150995;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by user on 2017-12-18.
 */

public class BackPressClose {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressClose(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

