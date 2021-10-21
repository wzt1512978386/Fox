package com.example.fox;

import android.widget.SeekBar;

/**
 * @author: wzt
 * @date: 2020/12/4
 */
public class SendManager {
    private final int RATE=1;//情绪比例
    private final int HEART=2;//心电图
    public SendManager(){

    }
    public void sendRate(SeekBar []bars){

        StringBuilder msg= new StringBuilder(RATE + "");
        for(int i=0;i<4;i++){
            float tempRate=(float)(bars[i].getProgress()*100f/bars[i].getMax());
            msg.append("--").append(tempRate);
        }
        MyApp.btServer.send(msg.toString());
    }
    public void sendHeartType(int type){

        String msg=HEART+"--"+type;
        MyApp.btServer.send(msg);
    }
}
