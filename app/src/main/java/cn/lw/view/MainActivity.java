package cn.lw.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import cn.lw.view.view.CircleView;

public class MainActivity extends AppCompatActivity {

    private CircleView mCircleView;
    private boolean flag = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int progress = mCircleView.getProgress();
            if (progress < 100) {
                mCircleView.setProgress(++progress);
            } else
                flag = false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleView = (CircleView) findViewById(R.id.circleview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(100);
                        mHandler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
