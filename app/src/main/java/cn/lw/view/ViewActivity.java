package cn.lw.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.lw.view.view.CircleView;

/**
 * 作者: Created by luow on 2017/10/9.
 * 注释：
 */

public class ViewActivity extends AppCompatActivity {
    private CircleView mCircleView;
    private boolean flag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mCircleView = (CircleView) findViewById(R.id.circleview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(100);

                        int progress = mCircleView.getProgress();

                        if (progress < 100) {
                            mCircleView.setProgress(++progress);
                        } else {
                            flag = false;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}
