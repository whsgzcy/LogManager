package com.iwant.mlog;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.iwant.mlog.file.FileUtil;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClearLogService extends Service {


    public class LogBinder extends Binder {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LogBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        mScheduledThreadPoolExecutor.scheduleAtFixedRate(mFileThread, 0, 2, TimeUnit.HOURS);
    }

    Thread mFileThread = new Thread() {

        @Override
        public void run() {
            super.run();

            List<String> lists = FileUtil.getFilesAllName("/sdcard/migu");
            if (lists == null) return;
            FileUtil.sortFilesNameList(lists);

            FileUtil.deleteFiles(lists);

            Log.d("migu", "***************************");
        }
    };
}
