package com.developwear.vwcardash.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developwear.vwcardash.MainActivity;
import com.developwear.vwcardash.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Aykut on 07/05/16.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {
    String TAG="MenuFragment";
    FancyButton openSpotify;
    FancyButton openGMaps;
    FancyButton openYandexNavi;
    FancyButton openYoutube;
    FancyButton openChrome;
    FancyButton openfmRadio;
    FancyButton openFacebook;
    FancyButton openTwitter;
    FancyButton openHaberler;
    FancyButton openSettings;
    FancyButton btnOnOff;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_menu,container,false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init()
    {
        openSpotify=(FancyButton) v.findViewById(R.id.openSpotify);
        openGMaps=(FancyButton) v.findViewById(R.id.openGMaps);
        openYandexNavi=(FancyButton) v.findViewById(R.id.openYandexNavi);
        openYoutube=(FancyButton) v.findViewById(R.id.openYoutube);
        openChrome=(FancyButton) v.findViewById(R.id.openChrome);
        openfmRadio=(FancyButton) v.findViewById(R.id.openfmRadio);
        openFacebook=(FancyButton) v.findViewById(R.id.openFacebook);
        openTwitter=(FancyButton) v.findViewById(R.id.openTwitter);
        openHaberler=(FancyButton) v.findViewById(R.id.openHaberler);
        openSettings=(FancyButton) v.findViewById(R.id.openSettings);
        btnOnOff=(FancyButton) v.findViewById(R.id.btn_on_off);

        btnOnOff.setOnClickListener(this);
        openSpotify.setOnClickListener(this);
        openGMaps.setOnClickListener(this);
        openYandexNavi.setOnClickListener(this);
        openYoutube.setOnClickListener(this);
        openChrome.setOnClickListener(this);
        openfmRadio.setOnClickListener(this);
        openFacebook.setOnClickListener(this);
        openTwitter.setOnClickListener(this);
        openHaberler.setOnClickListener(this);
        openSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent launchIntent;
        switch (v.getId())
        {
            case R.id.openSpotify:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.spotify.music");
                startActivity(launchIntent);
                break;
            case R.id.openGMaps:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
                startActivity(launchIntent);
                break;
            case R.id.openYandexNavi:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("ru.yandex.yandexnavi");
                startActivity(launchIntent);
                break;
            case R.id.openYoutube:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                startActivity(launchIntent);
                break;
            case R.id.openChrome:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.android.chrome");
                startActivity(launchIntent);
                break;
            case R.id.openfmRadio:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("tunein.player");
                startActivity(launchIntent);
                break;
            case R.id.openFacebook:
//                Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                synchronized (this) {
//                    i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_WAKEUP));
//                    MainActivity.getInstance().sendOrderedBroadcast(i, null);
//
//                    i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_WAKEUP));
//                    MainActivity.getInstance().sendOrderedBroadcast(i, null);
//                }
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                startActivity(launchIntent);
                break;
            case R.id.openTwitter:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.twitter.android");
                startActivity(launchIntent);
                break;
            case R.id.openHaberler:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.developwear.gazetelervehaberler");
                startActivity(launchIntent);
                break;
            case R.id.openSettings:
                launchIntent = MainActivity.getInstance().getPackageManager().getLaunchIntentForPackage("com.android.settings");
                startActivity(launchIntent);
                break;
            case R.id.btn_on_off:
                if(MainActivity.getInstance().isRelayOn) {
                    MainActivity.getInstance().sendDataToArduino("turnoff");
                    MainActivity.getInstance().isRelayOn=false;
                }
                else
                {
                    MainActivity.getInstance().sendDataToArduino("turnon");
                    MainActivity.getInstance().isRelayOn=true;
                }
                break;
        }
    }
}
