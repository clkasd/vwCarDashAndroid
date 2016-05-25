package com.developwear.vwcardash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Aykut on 20/05/16.
 */
public class BluetoothBaseActivity extends AppCompatActivity {

    public interface DataArrivaListener
    {
        void onDataArrived(String data);
    }
    DataArrivaListener listener;
    public void setDataArrivaListener(DataArrivaListener lis)
    {
        listener=lis;
    }
    BluetoothSPP bt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bt = new BluetoothSPP(this);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Bağlanılıyor");
        pDialog.setCancelable(false);
        pDialog.show();

        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
             if(state==3) {
                 pDialog.dismissWithAnimation();
             }
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                if(listener!=null)
                    listener.onDataArrived(message);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.disconnect();
        bt.stopService();

    }

    public void onStart() {
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void setup() {
        bt.autoConnect("HC-06");
    }
    public void sendData(String data)
    {
        bt.send(data,false);
    }
}
