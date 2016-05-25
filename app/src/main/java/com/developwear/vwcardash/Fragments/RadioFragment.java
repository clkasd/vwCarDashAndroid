package com.developwear.vwcardash.Fragments;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developwear.vwcardash.MainActivity;
import com.developwear.vwcardash.R;
import com.developwear.vwcardash.SharedPrefsHelper;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.w3c.dom.Text;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Aykut on 06/05/16.
 */
public class RadioFragment extends Fragment implements View.OnClickListener,View.OnLongClickListener{
    String TAG="RadioFragment";
    DiscreteSeekBar freqSeeker;
    TextView currentFreq;
    FancyButton seekUp;
    FancyButton seekDown;
    FancyButton firstchannel;
    FancyButton secondchannel;
    FancyButton thirdchannel;
    FancyButton fourthchannel;
    FancyButton fifthchannel;
    FancyButton sixthchannel;
    FancyButton seventhchannel;
    FancyButton eightchannel;
    FancyButton ninechannel;
    FancyButton tenthchannel;

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_radio,container,false);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("dest","destroy");
        if(recorder!=null)
            recorder.stahp();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("backstack","pause");
        if(recorder!=null)
            recorder.stahp();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        Log.d("radio","radio init");
    }
    AudioPipeStream recorder;
    private void init()
    {
        firstchannel=(FancyButton)v.findViewById(R.id.firstchannel);
        secondchannel=(FancyButton)v.findViewById(R.id.secondchannel);
        thirdchannel=(FancyButton)v.findViewById(R.id.thirdchannel);
        fourthchannel=(FancyButton)v.findViewById(R.id.fourthchannel);
        fifthchannel=(FancyButton)v.findViewById(R.id.fifthchannel);
        sixthchannel=(FancyButton)v.findViewById(R.id.sixthchannel);
        seventhchannel=(FancyButton)v.findViewById(R.id.seventhchannel);
        eightchannel=(FancyButton)v.findViewById(R.id.eightchannel);
        ninechannel=(FancyButton)v.findViewById(R.id.ninethchannel);
        tenthchannel=(FancyButton)v.findViewById(R.id.tenthchannel);

        firstchannel.setOnClickListener(this);
        secondchannel.setOnClickListener(this);
        thirdchannel.setOnClickListener(this);
        fourthchannel.setOnClickListener(this);
        fifthchannel.setOnClickListener(this);
        sixthchannel.setOnClickListener(this);
        seventhchannel.setOnClickListener(this);
        eightchannel.setOnClickListener(this);
        ninechannel.setOnClickListener(this);
        tenthchannel.setOnClickListener(this);

        firstchannel.setOnLongClickListener(this);
        secondchannel.setOnLongClickListener(this);
        thirdchannel.setOnLongClickListener(this);
        fourthchannel.setOnLongClickListener(this);
        fifthchannel.setOnLongClickListener(this);
        sixthchannel.setOnLongClickListener(this);
        seventhchannel.setOnLongClickListener(this);
        eightchannel.setOnLongClickListener(this);
        ninechannel.setOnLongClickListener(this);
        tenthchannel.setOnLongClickListener(this);


        seekUp=(FancyButton)v.findViewById(R.id.seekup);
        seekUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().sendDataToArduino("seekup");
            }
        });
        seekDown=(FancyButton)v.findViewById(R.id.seekdown);
        seekDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().sendDataToArduino("seekdn");
            }
        });


        freqSeeker=(DiscreteSeekBar)v.findViewById(R.id.radioSeek);
        currentFreq=(TextView)v.findViewById(R.id.currentFreq);

        freqSeeker.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                double frq=freqSeeker.getProgress()/100.0;
                currentFreq.setText(String.format("%.1f",frq));
                freqSeeker.setIndicatorFormatter(String.format("%.1f",frq));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                Log.d(TAG,currentFreq.getText().toString());//send data
                MainActivity.getInstance().sendDataToArduino("setfr;"+currentFreq.getText().toString());
            }
        });
        this.recorder = this.new AudioPipeStream();
    }

    @Override
    public void onClick(View v) {
        String freq=SharedPrefsHelper.getString(MainActivity.getInstance(),String.valueOf(v.getId()));
        MainActivity.getInstance().sendDataToArduino("setfr;"+freq);
    }

    @Override
    public boolean onLongClick(View v) {
        SharedPrefsHelper.addString(MainActivity.getInstance(),String.valueOf(v.getId()),currentFreq.getText().toString());
        Toast.makeText(MainActivity.getInstance(),"Kaydedildi",Toast.LENGTH_SHORT).show();
        return false;
    }

    private class AudioPipeStream extends Thread
    {
        private boolean running = false;

        public final int[] rates = new int[] {44100, 22050, 16000, 11025, 8000};

        private AudioPipeStream()
        {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            start();
        }

        @Override
        public void run()
        {
            running = true;

            int hz = 0;
            int N = 0;
            short[] buf;

            for(int rate : rates)
            {
                N = AudioRecord.getMinBufferSize(rate,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
                if(N > 0)
                {
                    hz = rate;
                    break;
                }
            }

            buf = new short[N];

            N *= 5;

            if(hz == 0)
            {
                throw new UnsupportedOperationException("No supported sample rate found");
            }
            else
            {
                Log.i("SNDREC", "Sample rate is "+ hz);
            }

            AudioRecord rec = null;
            AudioTrack trk = null;
            //short[][] bufz  = new short[256][160];
            //int ix = 0;

            try
            {
                rec = new AudioRecord(MediaRecorder.AudioSource.MIC, hz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, N);
                trk = new AudioTrack(AudioManager.STREAM_MUSIC, hz, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, N, AudioTrack.MODE_STREAM);
                rec.startRecording();
                trk.play();

                Log.i("SNDREC", "Start'd");

                while(running)
                {
                    //short[] buf = bufz[ix];
                    N = rec.read(buf,0,buf.length);
                    trk.write(buf, 0, buf.length);
                    //ix = (ix+1) % bufz.length;
                }
                Log.i("SNDREC", "Stahp'd");
            }
            catch(Throwable t)
            {
                t.printStackTrace();
                Log.e("SNDREC", "RETVAL #" + N);
            }
            finally
            {
                rec.stop();
                rec.release();
                trk.stop();
                trk.release();
                Log.i("SNDREC", "Disposed");
            }
        }

        public void stahp()
        {
            running = false;
        }

    }
    public void setCurrentFreq(String freq)
    {

        currentFreq.setText(freq);
        freq=freq.replace(",",".");
        freqSeeker.setProgress((int)(Double.parseDouble(freq)*100));
    }
}
