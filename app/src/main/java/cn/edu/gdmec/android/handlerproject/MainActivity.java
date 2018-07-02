package cn.edu.gdmec.android.handlerproject;


import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //主线程
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        * UI线程、主线程，做一些轻量级的事情
        * */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);

        //创建一个Handler
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                /*
                * 接到子线程发出来的消息，处理
                * */
                //处理消息
                Log.i(TAG,"handleMessage:" + msg.what);

                if (msg.what==1002){
                    textView.setText("huang");
                    Log.d(TAG,"handleMessage:"+msg.arg1);
                }

            }
        };

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 有可能做大量耗时的操作
                * 子线程
                * */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        /*
                        * 通知UI更新
                        * */
                        handler.sendEmptyMessage(1001);
                        //textView.setText("huang");
                        Message message = Message.obtain();
                        message.what=1002;
                        message.arg1=1003;
                        message.arg2=1004;
                        message.obj=MainActivity.this;
                        handler.sendMessage(message);

                        //定时任务
                        handler.sendMessageAtTime(message,SystemClock.uptimeMillis() + 3000);
                        handler.sendMessageDelayed(message,2000);

                        //运行可执行对象 消息来做
                        /*handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int a = 1+2+3;
                            }
                        });*/
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                int a=1+2+3;
                            }
                        };
                        handler.post(runnable);
                        runnable.run();
                        //定时任务
                        handler.postDelayed(runnable,2000);

                    }
                }).start();

            }
        });
        //handler.sendEmptyMessage(1001);
    }
    //主线程
}
