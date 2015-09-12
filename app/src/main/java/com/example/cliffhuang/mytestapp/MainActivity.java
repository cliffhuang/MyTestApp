package com.example.cliffhuang.mytestapp;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private EditText editText;
    private TextView textView;
    private MyService.Bind bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textViewOut);

        findViewById(R.id.startActivity).setOnClickListener(this);
        findViewById(R.id.startservice).setOnClickListener(this);
        findViewById(R.id.stopservice).setOnClickListener(this);
        findViewById(R.id.bindservice).setOnClickListener(this);
        findViewById(R.id.unbindservice).setOnClickListener(this);
        findViewById(R.id.syncService).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startActivity:
                //Toast.makeText(this, "button1", Toast.LENGTH_SHORT).show();
                Intent intentAty = new Intent(this, SecActivity.class);
                intentAty.putExtra("data", "this is a test");
                startActivity(intentAty);
                break;
            case R.id.startservice:
                Intent startService = new Intent(MainActivity.this, MyService.class);
                //   Intent startService = new Intent(MyService.ACTION);
                startService.putExtra("data", editText.getText().toString());
                startService(startService);
                break;
            case R.id.stopservice:
                Intent stopService = new Intent(MainActivity.this, MyService.class);
                stopService(stopService);
                break;
            case R.id.bindservice:
                bindService(new Intent(this, MyService.class), this, BIND_AUTO_CREATE);
                System.out.println("zz");
                break;
            case R.id.unbindservice:
                unbindService(this);
                break;
            case R.id.syncService:
                if (bind != null)
                    bind.setStr(editText.getText().toString());
                break;
        }
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        System.out.println("这是个测试");
        bind = (MyService.Bind) iBinder;
        ((MyService.Bind) iBinder).getMyService().setCallBack(new MyService.CallBack() {
            @Override
            public void onDataChange(String data) {
                Message msg =new Message();
                Bundle b = new Bundle();
                b.putString("data",data);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Handler handler= new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText(msg.getData().getString("data"));
        }
    };


}
