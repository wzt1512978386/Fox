package com.example.fox;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class UtilSys {
    //申请权限
    public static void requestPermisson(Activity activity, String permission,int requestCode){
        if(ContextCompat.checkSelfPermission(activity,permission)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
        }
    }

}
