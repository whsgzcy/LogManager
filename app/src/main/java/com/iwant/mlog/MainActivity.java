package com.iwant.mlog;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iwant.mlog.file.FileUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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


        // 开启service，进行定期的删除日志
        /**启动 NavCtrlMonitorConnection*/
        mClearLogConnection = new ClearLogServiceConnection();
        Intent navCtrlMonitorIntent = new Intent(this, ClearLogService.class);
        bindService(navCtrlMonitorIntent, mClearLogConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mClearLogConnection != null) {
            unbindService(mClearLogConnection);
        }
    }
}
