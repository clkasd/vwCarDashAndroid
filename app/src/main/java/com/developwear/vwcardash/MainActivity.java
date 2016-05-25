package com.developwear.vwcardash;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.developwear.vwcardash.Fragments.MenuFragment;
import com.developwear.vwcardash.Fragments.RadioFragment;

public class MainActivity extends BluetoothBaseActivity implements BluetoothBaseActivity.DataArrivaListener{
    static MainActivity activity;
    Fragment activeFragment;
    AudioManager audioManager ;
    public DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    public ComponentName compName;

    public boolean isRelayOn=false;

    public static MainActivity getInstance()
    {
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        MenuFragment fragment=new MenuFragment();
        isRelayOn=false;
        startFragment(fragment);
        setDataArrivaListener(this);
        audioManager= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        deviceManger = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this,DeviceAdminSampleReceiver.class);
    }

    public void startFragment(Fragment fragment)
    {
        activeFragment=fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("asdas").replace(R.id.content, fragment).commit();
    }
    public void sendDataToArduino(String data)
    {
        sendData(data);
        Log.d("data",data);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1)
            getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDataArrived(String data) {
        if(data.equals("voldwn"))
        {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

        }
        else if(data.equals("volup"))
        {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }
        else if(data.equals("mute"))
        {
            for(int i=0;i<15;i++) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        }
        else if(data.equals("ok"))
        {
            KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
            if( myKM.inKeyguardRestrictedInputMode()) {
                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();
            } else {
                deviceManger.lockNow();
            }
        }
        else if(data.equals("back"))
        {

        }
        else if(data.equals("forwrd"))
        {

        }
        else if(data.contains("freq"))
        {

        }
        else if(data.equals("shutdwn"))
        {
            deviceManger.lockNow();
        }
        else if(data.equals("start"))
        {
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
        }
    }
}

