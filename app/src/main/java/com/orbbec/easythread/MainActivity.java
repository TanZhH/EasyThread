package com.orbbec.easythread;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lzh.easythread.Callback;
import com.lzh.easythread.EasyThread;

import java.util.concurrent.locks.LockSupport;


/**
 * @author tanzhuohui
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button1;
    private static String TAG = "Join";
    private EasyThread easyThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.bt_thread1);
        button1.setOnClickListener(this);
        easyThread = ThreadManager.getIO();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_thread1:
                easyThread.setName("runnable1").execute(new Task2(1, 10));
                break;
            default:
                break;
        }
    }


    class Task implements Runnable {
        private int num;
        private int s;

        public Task(int num, int s) {
            super();
            this.num = num;
            this.s = s;
        }

        @Override
        public void run() {
            Log.e(TAG, "task" + num + " 开始执行了...开始执行了...");
            for (int i = 0; i < s; i++) {
                SystemClock.sleep(1000);
                Log.e(TAG, "task" + num + "执行中........" + i + ".........");
            }
            Log.e(TAG, "task" + num + "   结束了...");
        }
    }


    class Task2 implements Runnable {
        private int num;
        private int s;

        public Task2(int num, int s) {
            super();
            this.num = num;
            this.s = s;
        }

        @Override
        public void run() {
            myrun(num, s);
        }
    }

    private void myrun(int num, int s) {
        //当前线程
        final Thread thread = Thread.currentThread();
        Log.d(TAG, "task" + num + " 开始执行了...开始执行了...");
        for (int i = 0; i < s; i++) {
            SystemClock.sleep(1000);
            Log.d(TAG, "task" + num + "执行中........" + i + ".........");
            //当在i等于3时，可以使用
            if (i == 3) {
                Task task2 = new Task(2,5);
                //开启次线程
                startThread(task2 , thread);
                //阻塞当前的线程
                ThreadManager.join(task2);
            }
        }
        Log.d(TAG, "task" + num + "   结束了...");
    }

    private void startThread(Task task2 , final Thread thread){
        easyThread.setName("runnable2").setCallback(new Callback() {
            @Override
            public void onError(String threadName, Throwable t) {
                //线程任务运行时出现异常时的通知
            }

            @Override
            public void onCompleted(String threadName) {
                //线程任务正常执行完成时的通知
                Log.e(TAG, "onCompleted: " + threadName + "结束");
                Toast.makeText(MainActivity.this, threadName + "结束", Toast.LENGTH_LONG).show();
//                            easyThread.setName("runnable3").execute(new Task(3 , 5));
                //唤醒外层线程
                ThreadManager.wakeup(thread);
            }

            @Override
            public void onStart(String threadName) {
                //线程任务启动时的通知
            }
        }).execute(task2);
    }
}
