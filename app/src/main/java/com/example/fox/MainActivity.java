package com.example.fox;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //蓝牙
    private BTServer btServer;
    //控件
    private SeekBar[]bars;
    private TextView[]rates;
    public TextView connectState,radioInfo;
    private RadioGroup radioGroup;

    //发送器
    private SendManager sendManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.mainActivity=this;
        setContentView(R.layout.activity_main);
        //权限申请
        requestPermission();
        //蓝牙服务端
        btServer=new BTServer();
        btServer.listen();
        //滑条
        initSeekBar();
        initRate();
        //radio
        initRatio();
        //初始化发射器
        sendManager=new SendManager();
        //信息显示
        connectState=findViewById(R.id.textview_connectState);//显示连接情况
    }
    private void initRate(){
        //控件
        rates=new TextView[4];
        rates[0]=(TextView)findViewById(R.id.textview_rate1);
        rates[1]=(TextView)findViewById(R.id.textview_rate2);
        rates[2]=(TextView)findViewById(R.id.textview_rate3);
        rates[3]=(TextView)findViewById(R.id.textview_rate4);
        //事件

    }
    private void initRatio(){
        //radioInfo=(TextView)findViewById(R.id.textview_radioinfo);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup_heart);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radiobutton_heart_happy:
                        //radioInfo.setText("Happy");
                        sendManager.sendHeartType(0);
                        break;
                    case R.id.radiobutton_heart_neut:
                        //radioInfo.setText("Neut");
                        sendManager.sendHeartType(1);
                        break;
                    case R.id.radiobutton_heart_anger:
                        //radioInfo.setText("Sadn");
                        sendManager.sendHeartType(3);
                        break;
                    case R.id.radiobutton_heart_nothing:
                        //radioInfo.setText("Nothing");
                        sendManager.sendHeartType(4);
                        break;
                }
            }
        });
    }
    private void initSeekBar(){
        //控件
        bars=new SeekBar[4];
        bars[0]=(SeekBar)findViewById(R.id.seekbar_face1);
        bars[1]=(SeekBar)findViewById(R.id.seekbar_face2);
        bars[2]=(SeekBar)findViewById(R.id.seekbar_face3);
        bars[3]=(SeekBar)findViewById(R.id.seekbar_face4);
        final boolean []barFlag={false,false,false,false};

        //事件
        for(int i=0;i<4;i++){
            final int finalI = i;
            bars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(barFlag[finalI]) {
                        rates[finalI].setText(String.format("%02.01f%%",progress*100f/seekBar.getMax()));
                        int D = seekBar.getMax() - progress;
                        int sum = 0;
                        for (int j = 0; j < 4; j++) {
                            if (j == finalI)
                                continue;
                            sum += bars[j].getProgress();
                        }
                        float zoom = D * 1f / sum;
                        for (int j = 0; j < 4; j++) {
                            if (j == finalI)
                                continue;
                            bars[j].setProgress((int) (bars[j].getProgress() * zoom));
                            rates[j].setText(String.format("%02.01f%%",bars[j].getProgress()*100f/seekBar.getMax()));
                        }
                        sendManager.sendRate(bars);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    barFlag[finalI]=true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    barFlag[finalI]=false;

                }
            });
        }
    }
    private void requestPermission(){
        UtilSys.requestPermisson(this, Manifest.permission.BLUETOOTH_ADMIN,0);
        UtilSys.requestPermisson(this, Manifest.permission.BLUETOOTH,0);
        UtilSys.requestPermisson(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,0);
        //Android10的蓝牙扫描要添加定位权限
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i("IN101", "sdk < 28 Q");
            UtilSys.requestPermisson(this,Manifest.permission.ACCESS_FINE_LOCATION, 1);
            UtilSys.requestPermisson(this,Manifest.permission.ACCESS_COARSE_LOCATION, 1);
        }
        else {
            UtilSys.requestPermisson(this,Manifest.permission.ACCESS_FINE_LOCATION, 2);
            UtilSys.requestPermisson(this,Manifest.permission.ACCESS_COARSE_LOCATION, 2);
            UtilSys.requestPermisson(this, "android.permission.ACCESS_BACKGROUND_LOCATION", 2);
        }
    }
}