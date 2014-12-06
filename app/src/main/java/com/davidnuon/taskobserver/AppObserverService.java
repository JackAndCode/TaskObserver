/**
 *  AppObserverService
 *  David Nuon
 *
 *  Very hideous demonstration how a background service can
 *  be used to associate information with running processes.
 *
 *  It'll count how may times it has seen a process
 *
 *  Wanna burn out your phone? POLL. POLL LIKE NO TOMORROW.
 *
 *  I am not liable for any technical debt / suffering that results
 *  from viewing this file
 */
package com.davidnuon.taskobserver;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppObserverService extends IntentService {
    public static final String TAG = "AppObserverService";

    private Map<String, Integer> mAppLookup = new HashMap<>();

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        final ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        new Thread(new Runnable() {
            String currentApp = "";

            @Override
            public void run() {
                while(true) {
                    List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(1);

                    try {
                        Thread.sleep(314, 0); // This number was empirically derived of course.
                        String topLevelApp = list.get(0).baseActivity.getPackageName();
                        if(!currentApp.equals(topLevelApp)) {
                            if(mAppLookup.containsKey(topLevelApp)) {
                                mAppLookup.put(topLevelApp, mAppLookup.get(topLevelApp) + 1);
                            } else {
                                mAppLookup.put(topLevelApp, 1);
                            }
                            currentApp = topLevelApp;
                        }
                        Log.i(TAG, String.format("%s : %d", topLevelApp, mAppLookup.get(topLevelApp)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public AppObserverService() {
        super("AppObserverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
