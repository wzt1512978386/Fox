package com.example.fox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: wzt
 * @date: 2020/12/4
 */
public class BTServer {
    static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = BTServer.class.getSimpleName();
    //蓝牙工具
    private BluetoothServerSocket btsSocket;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private DataOutputStream mOut;//输出流
    //线程

    //状态
    private boolean isSend=false;
    public boolean isConnected=false;

    public BTServer(){
        MyApp.btServer=this;
        btAdapter=null;
        btsSocket=null;
        btSocket=null;
    }
    public void listen(){
        btAdapter=BluetoothAdapter.getDefaultAdapter();
        try {
            btsSocket =btAdapter.listenUsingInsecureRfcommWithServiceRecord(TAG,SPP_UUID);
            Log.i("IN101","服务端socket创建成功");
        } catch (IOException e) {
            Log.i("IN101","服务端socket创建失败");
        }
        MyApp.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                while (!isConnected){
                    waitForAccept();
                }
            }
        });
    }
    private void waitForAccept(){
        try {
            //close();
            btSocket= btsSocket.accept();//从监听获取socket
            btsSocket.close();//关闭监听，即只连接一个设备
            Log.i("IN101","获取socket成功");

        } catch (IOException e) {
            Log.i("IN101","获取socket失败");
            close();
            return;
        }

        try {
            if(!btSocket.isConnected())//!!!!!!很重要
                btSocket.connect();

            Log.i("IN101","连接成功！！");
            isConnected=true;
            MyApp.handler.post(new Runnable() {
                @Override
                public void run() {
                    MyApp.mainActivity.connectState.setText("已连接");
                }
            });
        } catch (IOException e) {
            Log.i("IN101","连接失败！！！");
            close();
            return;
        }

        try {
            mOut = new DataOutputStream(btSocket.getOutputStream());
            Log.i("IN101","输出流创建成功");
        } catch (IOException e) {
            close();
            Log.i("IN101","输出流创建失败！！！");
        }
    }
    public void send(String msg){
        if(!isConnected){
            //Toast.makeText(MyApp.mainActivity,"")
            return;
        }

        if(isSend){
            Log.i("IN101","正在发送数据");
            return;
        }
        isSend=true;
        try {
            mOut.writeUTF(msg);
            mOut.flush();
        } catch (IOException e) {
            Log.i("IN101","该条数据读出失败");
            close();
            try {
                mOut.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        isSend=false;


    }
    public void close(){
        try {
            if(btSocket!=null)
                btSocket.close();
            if(btsSocket!=null)
                btsSocket.close();
            MyApp.mainActivity.connectState.setText("未连接");
            isConnected=false;
            listen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
