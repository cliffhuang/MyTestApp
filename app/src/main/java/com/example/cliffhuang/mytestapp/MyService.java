package com.example.cliffhuang.mytestapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

    public final static String ACTION = "com.example.cliffhuang.mytestapp.MyService";

    private boolean running = false;
    private String str = "默认线程启动";
    private int i;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Bind();
    }

    public class Bind extends Binder {
        public void setStr(String tmp) {
            str = tmp;
        }

        public MyService getMyService(){
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        new Thread() {

            @Override
            public void run() {
                super.run();

                i = 0;
                while (running) {
                    i++;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String tmp =str + ":" + i;
                    System.out.println(tmp);
                    if(callBack!=null)
                        callBack.onDataChange(tmp);
                }
            }
        }.start();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        str = intent.getStringExtra("data");

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
//        System.out.println("结束一个多线程在运行");
    }


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }


    private  CallBack callBack= null;

    public static interface CallBack{
        void onDataChange(String data);
    }


}

