package com.peasun.aispeech.aiopen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.sharjeck.musicdemo.R;

/**
 * Created by Shahsen on 2020/2/23.
 */
public class AIOpenService extends Service {
    private String TAG = "AIOpenService";
    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForce();
        mHandler.removeCallbacks(stopServiceTask);
        mHandler.postDelayed(stopServiceTask, 15000);

        if (intent != null) {
            Bundle data = intent.getExtras();
            String action = intent.getAction();

            System.out.print(TAG + ",action," + action);
            if (!TextUtils.isEmpty(action)) {
                switch (action) {
                    case AIOpenConstant.AI_OPEN_ACTION_MUSIC: {
                        if (data != null) {
                            //play text
                            String command = data.getString("common");
                            if (!TextUtils.isEmpty(command)) {
                                if ("search".equals(command)) {
                                    String keyword = data.getString("keyword");

                                    String singer = data.getString("singerName");
                                    String songName = data.getString("songName");

                                    Log.d(TAG, "receive :" + command + ", " + keyword + ", singer " + singer + ", song " + songName);
                                    //todo

                                } else if ("control".equals(command)) {
                                    String ctlAction = data.getString("action");
                                    String playIndex = data.getString("PlaySongIndex");

                                    Log.d(TAG, "receive :" + command + ", " + ctlAction + ", index " + playIndex);
                                    //todo

                                }
                            }

                        }
                    }
                    break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(this.getMainLooper());
    }

    private Runnable stopServiceTask = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                System.out.print(TAG + ",stop service");
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startForce() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ONE_ID = "com.sharjeck.openai";
                String CHANNEL_ONE_NAME = "AI SERVICE";
                NotificationChannel notificationChannel =
                        new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(notificationChannel);
//                startForeground(1, new NotificationCompat.Builder(this, CHANNEL_ONE_ID).build());
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ONE_ID);
                Notification notification = builder.setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(10, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE);
                } else {
                    startForeground(10, notification);
                }
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
