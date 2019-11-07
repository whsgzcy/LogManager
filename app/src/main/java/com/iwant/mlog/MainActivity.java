package com.iwant.mlog;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iwant.mlog.file.FileUtil;
import com.iwant.mlog.manager.CMTManagerService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity";

    ClearLogServiceConnection mClearLogConnection;
    ClearLogService.LogBinder mClearLogBinder;

    public class ClearLogServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("dadasdsad", "开始启动日志清除进程");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mClearLogBinder = null;
            Log.d("dadasdsad", "关闭日志清除进程");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("adadasdsa", "has started ----");

        final TextView show = (TextView) findViewById(R.id.show);

        Button read = (Button) findViewById(R.id.read);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> lists = FileUtil.getFilesAllName("/sdcard/migu");
                if (lists == null) return;
                String m = "";
                for (int i = 0; i < lists.size(); i++) {
                    m = m + lists.get(i) + "\n";
                }
                show.setText(m);
            }
        });

        Button sort = (Button) findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> lists = FileUtil.getFilesAllName("/sdcard/migu");
                if (lists == null) return;
                FileUtil.sortFilesNameList(lists);
                String m = "";
                for (int i = 0; i < lists.size(); i++) {
                    m = m + lists.get(i) + "\n";
                }
                show.setText(m);
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> lists = FileUtil.getFilesAllName("/sdcard/migu");
                if (lists == null) return;
                FileUtil.sortFilesNameList(lists);

                FileUtil.deleteFiles(lists);
                String m = "";
                for (int i = 0; i < lists.size(); i++) {
                    m = m + lists.get(i) + "\n";
                }
                show.setText(m);
            }
        });

        Button read_app_cpu = (Button) findViewById(R.id.read_app_cpu);
        read_app_cpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String totalMemory = CMTManagerService.getTotalMemory() + "";

                String availableMemory = CMTManagerService.getAvailableMemory(MainActivity.this) + "";

                String usedPercentMemoryValue = CMTManagerService.getUsedPercentValue(MainActivity.this) + "";

                String processMemoryRate1 = CMTManagerService.getProcessMemoryRate(MainActivity.this, 20646) + "";
                String processMemoryRate2 = CMTManagerService.getProcessMemoryRate(MainActivity.this, 8459) + "";

                String totalCpuRate = CMTManagerService.getTotalCpuRate() + "";

                String str = "totalMem:" + totalMemory + " " +
                        "availableMemory:" + availableMemory + " " +
                        "userdPercentMemory:" + usedPercentMemoryValue + " " +
                        "processMemoryRate:" + processMemoryRate1 + " " + processMemoryRate2 + " " +
                        "totalCpuRate:" + totalCpuRate;

                show.setText(str);
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mClearLogConnection != null) {
            unbindService(mClearLogConnection);
        }
    }
}
