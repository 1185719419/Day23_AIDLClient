package org.mobiletrain.day23_aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mobiletrain.day23_aidlserver.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private EditText plus01EditText, plus02EditText;
    private TextView resultTextView;

    // 服务端Service的action
    private final String SERVER_ACTION = "com.aidl.server";

    // 代表来自服务端的接口对象
    private IMyAidlInterface callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plus01EditText = (EditText) findViewById(R.id.plus01_et);
        plus02EditText = (EditText) findViewById(R.id.plus02_et);
        resultTextView = (TextView) findViewById(R.id.result_tv);


        bindService();
    }

    // 绑定服务端的Service
    private void bindService() {
        Intent intent = new Intent(SERVER_ACTION);
        // 设置目标app的包名
        intent.setPackage("org.mobiletrain.day23_aidlserver");
        // 第三步，客户端向服务端绑定Service
        boolean isBindSuccessful = bindService(intent, this, Context.BIND_AUTO_CREATE);
        Toast.makeText(MainActivity.this, "是否绑定成功：" + isBindSuccessful, Toast.LENGTH_SHORT).show();
    }

    // 点击按钮，绑定服务端的Service，执行计算操作
    public void calculate(View view) {
        String plus01String = plus01EditText.getText().toString();
        String plus02String = plus02EditText.getText().toString();

        if (!"".equals(plus01String) && !"".equals(plus02String)) {
            int plus01 = Integer.parseInt(plus01String);
            int plus02 = Integer.parseInt(plus02String);

            // 调用AIDL接口，在服务端执行逻辑运算，将结果返回给客户端
            try {
                int result = callback.plus(plus01, plus02);
                resultTextView.setText("" + result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    //第四步，绑定成功，获取Service表面的胶水对象，间接获得AIDL接口对象
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        // 接口来自胶水，胶水来自Service表面
        callback = IMyAidlInterface.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
