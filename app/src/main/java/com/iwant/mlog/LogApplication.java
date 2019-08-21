package com.iwant.mlog;

import android.app.Application;

public class LogApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogRecorder logRecorderError
                = new LogRecorder.Builder(this)
                .setLogFolderName("asdasdsa")
                .setLogFolderPath("/sdcard/migu")
                .setLogFileNameSuffix("asdasdsad")
                .setLogFileSizeLimitation(10 * 1024)
                .setLogLevel(2)
                .build();
        logRecorderError.start();
    }
}
