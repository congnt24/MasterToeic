package com.example.cong.audiocong;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;


/**
 * Created by Cong on 2/1/2015.
 */
public class AudioCong implements View.OnTouchListener {

    public static final int AUDIO_UPDATE_TIME=100;
    public static final String TAG=AudioCong.class.getSimpleName();
    private static AudioCong audioCong;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handleUpdateSeekBar;
//    private Uri uri;
    private View btnPlay, btnPause;
    private boolean autoScroll=true;
    private long totalTime=0;
    public Context mcontext;
    MediaMetadataRetriever metadataRetriever; //get audio info
    /*
    Indicate the current run-time of the audio player
     */
    private TextView tvTotaltime, tvTitle, tvLyrics;
    private ScrollView svLyrics;
    private ImageView imgView;

    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (seekBar==null)return;
            if (handleUpdateSeekBar!=null && mediaPlayer!=null){
                int currentTime=mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentTime);
                if (autoScroll && svLyrics !=null)svLyrics.smoothScrollTo(0, (int) ((tvLyrics.getHeight()-svLyrics.getHeight())*currentTime/totalTime));
                updateRunTime(currentTime);
                handleUpdateSeekBar.postDelayed(this, AUDIO_UPDATE_TIME);
            }
        }
    };

    public static AudioCong getInstance(){
        if (audioCong==null){
            audioCong=new AudioCong();
        }
        return audioCong;
    }
    //--------------------Get MP3 TAg Info-------------------



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.MediaColumns.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            Log.d("path", cursor.getString(column_index));
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

//Lay thong tin bai hat
    public void initAudioInfo(Context context, Uri uri) {
        if (metadataRetriever==null){
            metadataRetriever=new MediaMetadataRetriever();
        }
        metadataRetriever.setDataSource(context, uri);
        tvTitle.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        String fpath=uri.toString().substring(0, uri.toString().length()-3)+"lrc";
        File f=new File(fpath);
        if (f.exists()){
            try {
                BufferedReader br=new BufferedReader(new FileReader(f));
                String line;
                while((line=br.readLine())!=null){
                    tvLyrics.append(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            MP3File mp3File = new MP3File(new File(getRealPathFromURI(context, uri)));
            if (!mp3File.getID3v2Tag().getSongLyric().equals(""))
                tvLyrics.setText(mp3File.getID3v2Tag().getSongLyric());
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }
    }

    //---------------------Play pause----------------------
    public void play(){
        if (mediaPlayer==null) {
            throw new IllegalStateException("MediaPlayer cannot be null");
        }
        if (mediaPlayer.isPlaying())
            return;
        if (btnPlay==null){
            throw new IllegalStateException("btnPlay cannot be null");
        }
        handleUpdateSeekBar.postDelayed(updateProgress, AUDIO_UPDATE_TIME);
        mediaPlayer.start();
        setPausable();// Play gone Pause visitble
    }

    private void setPausable() {
        Log.d(TAG, "Pausable");
        if (btnPlay != null){
            Log.d(TAG, "Play gone");
            btnPlay.setVisibility(View.GONE);
        }
        if (btnPause != null){
            Log.d(TAG,"Pause visitble");
            btnPause.setVisibility(View.VISIBLE);
        }
    }
    public void pause(){
        if (mediaPlayer==null){
            return;
        }
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            Log.d(TAG, "pause() start");
            setPlayable();
        }else{
            setPlayable();
        }
    }

    private void setPlayable() {
        if (btnPause != null){
            btnPause.setVisibility(View.GONE);
        }
        if (btnPlay != null){
            btnPlay.setVisibility(View.VISIBLE);
        }
    }

    public AudioCong init(Context context, Uri uri){
        if (uri==null){
            throw new NullPointerException("Uri cannot be null");
        }
        if (audioCong==null){
            audioCong=new AudioCong();
        }
        mcontext=context;
        autoScroll=true;
        handleUpdateSeekBar=new Handler();
        initPlayer(context, uri);
        initEvents();
        initAudioInfo(context, uri);

        return this;
    }
    //---------Playautio from File
    public AudioCong init(Context context, File file){
        Uri uri=Uri.fromFile(file);
        Log.d("xxxxxxxxx", "yyyyyyyyyyy "+uri.toString());
        if (uri==null){
            throw new NullPointerException("Uri cannot be null");
        }
        if (audioCong==null){
            audioCong=new AudioCong();
        }
        mcontext=context;
        autoScroll=true;
        handleUpdateSeekBar=new Handler();
        initPlayer(context, uri);
        initEvents();
        //init audio info
        if (metadataRetriever==null){
            metadataRetriever=new MediaMetadataRetriever();
        }

        try {
            metadataRetriever.setDataSource(context, uri);
            if (tvTitle!=null)
                tvTitle.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            if (tvLyrics !=null) {
                MP3File mp3File = new MP3File(file);
                if (!mp3File.getID3v2Tag().getSongLyric().equals(""))
                    tvLyrics.setText(mp3File.getID3v2Tag().getSongLyric());
            }
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }

        return this;
    }
    //---Play audio from URL---
    public AudioCong init(Context context, String url){
        if (url.equals(""))
            return this;
        if (audioCong==null){
            audioCong=new AudioCong();
        }
        mcontext=context;
//        new downloadFile().execute(url);
        init(context, Downloader.getInstance().init(context, url).startDownload());

        return this;
    }

    private void initEvents() {
        initMediaSeekBar();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

    }

    private void initPlayer(Context context, Uri uri) {
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(onCompletion);
    }

    private MediaPlayer.OnCompletionListener onCompletion=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            int currentTime=0;
            seekBar.setProgress(currentTime);
            updateRunTime(currentTime);
            setPlayable();

        }
    };
    //---------Set Default View-----------
    public AudioCong setDefaultUi(ViewGroup container, LayoutInflater inflater){
        if (container==null){
            throw new NullPointerException("Container or Inflater cannot be null");
        }
        if (inflater==null){
            throw new IllegalArgumentException("Container or Inflater cannot be null");
        }
        View rootView=inflater.inflate(R.layout.audio_cong, container, true);
        View playView=rootView.findViewById(R.id.play);
        View pauseView=rootView.findViewById(R.id.pause);
        SeekBar seekBar= (SeekBar) rootView.findViewById(R.id.media_seekbar);
        TextView playbackTime = (TextView) rootView.findViewById(R.id.playback_time);
        setPlayView(playView);
        setPauseView(pauseView);
        setSeekBarView(seekBar);
        setPlaybackTime(playbackTime);
        return this;
    }

    //---------View only play button----------
    public AudioCong setOnlyPlayUi(ViewGroup container, LayoutInflater inflater){
        if (container==null){
            throw new NullPointerException("Container or inflater cannot be null");
        }
        if (inflater==null){
            throw new IllegalArgumentException("Inflater cannot be null");
        }

        View rootView = inflater.inflate(R.layout.play_only, container, true);
        View playView=rootView.findViewById(R.id.play1);
        View pauseView=rootView.findViewById(R.id.pause1);
        setPauseView(pauseView);
        setPlayView(playView);
        return this;

    }
    //-------View Only Lyrics------
    public AudioCong setOnlyLyricUi(ViewGroup container, LayoutInflater inflater){
        if (container==null){
            throw new NullPointerException("Container or inflater cannot be null");
        }
        if (inflater==null){
            throw new IllegalArgumentException("Inflater cannot be null");
        }

        View rootView = inflater.inflate(R.layout.lyric_only, container, true);
        TextView lyrics = (TextView) rootView.findViewById(R.id.lyrics);
        setLyricsView(lyrics);
        return this;

    }
    //-------View Only Images------
    public AudioCong setOnlyImageUi(ViewGroup container, LayoutInflater inflater){
        if (container==null){
            throw new NullPointerException("Container or inflater cannot be null");
        }
        if (inflater==null){
            throw new IllegalArgumentException("Inflater cannot be null");
        }

        View rootView = inflater.inflate(R.layout.image_only, container, true);
        ImageView hint = (ImageView) rootView.findViewById(R.id.iv_hint);
        setImageView(hint);
        return this;

    }


    //--------View full media player-----------
    public AudioCong setFullAudioUi(ViewGroup container, LayoutInflater inflater){
        if (container==null){
            throw new NullPointerException("Container or Inflater cannot be null");
        }
        if (inflater==null){
            throw new IllegalArgumentException("Container or Inflater cannot be null");
        }
        View rootView=inflater.inflate(R.layout.audio_full_player, container, true);
        View playView=rootView.findViewById(R.id.play);
        View pauseView=rootView.findViewById(R.id.pause);
        SeekBar seekBar= (SeekBar) rootView.findViewById(R.id.media_seekbar);
        TextView playbackTime = (TextView) rootView.findViewById(R.id.playback_time);
        TextView title= (TextView) rootView.findViewById(R.id.title);
        TextView lyrics = (TextView) rootView.findViewById(R.id.lyrics);
        ScrollView scrollView= (ScrollView) rootView.findViewById(R.id.scroll_lyrics);
        ImageView imgV = (ImageView) rootView.findViewById(R.id.imageView);
        setScrollView(scrollView);
        setTitleView(title);
        setLyricsView(lyrics);
        setPlayView(playView);
        setPauseView(pauseView);
        setSeekBarView(seekBar);
        setPlaybackTime(playbackTime);
        setImageView(imgV);
        return this;
    }


    //Init Image
    public AudioCong initImage(File file) {
        if (imgView == null){
            return this;
        }
        //----Check if file exist
        //if (file.exists()){
            imgView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
       // }
        //----If file not exist
        return this;
    }

    public AudioCong toggleImageLyrics(){
        if (imgView.VISIBLE != View.VISIBLE){
            imgView.setVisibility(View.VISIBLE);
            svLyrics.setVisibility(View.GONE);
        }else{
            imgView.setVisibility(View.GONE);
            svLyrics.setVisibility(View.VISIBLE);
        }
        return this;
    }
    private AudioCong setImageView(ImageView imgV) {
        if (imgV==null){
            throw new NullPointerException("Lyrics cannot be null");
        }
        imgView=imgV;
        return this;
    }

    private AudioCong setScrollView(ScrollView scrollView) {
        if (scrollView==null){
            throw new NullPointerException("Lyrics cannot be null");
        }
        svLyrics=scrollView;
        svLyrics.setOnTouchListener(this);
        return this;
    }

    private AudioCong setLyricsView(TextView lyrics) {
        if (lyrics==null){
            throw new NullPointerException("Lyrics cannot be null");
        }
        tvLyrics=lyrics;
        return this;
    }
    //Set Lyrics from String and Save Lyrics to SD
    public AudioCong setLyrics(String data, String fname){
        tvLyrics.setText(Html.fromHtml(data));
        saveLyrics(data, fname);
        return this;
    }
    //Set Lyrics from File
    public AudioCong setLyrics(File f){
        StringBuilder sb = new StringBuilder();
        if (f.exists()){
            BufferedReader br = null;
            try {
                br=new BufferedReader(new FileReader(f));
                String line;
                while((line=br.readLine())!=null){
                    sb.append(line);
                }
                tvLyrics.setText(Html.fromHtml(sb.toString()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (br!=null)
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return this;
    }
    //Save Lyrics to SDcard
    public AudioCong saveLyrics(String str, String fname){
        //----Check if file exist
        File file=new File(mcontext.getFilesDir()+"/"+fname);
        if (file.exists())
            return this;
        //----If file not exist
        OutputStream outputStream = null;
        try {
            outputStream=mcontext.openFileOutput(fname, Context.MODE_WORLD_READABLE);
            outputStream.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream!=null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return this;
    }

    public AudioCong setTitleView(TextView title) {
        if (title==null){
            throw new NullPointerException("Title cannot be null");
        }
        tvTitle=title;
        return this;
    }

    public AudioCong setPlaybackTime(TextView playbackTime) {
        if (playbackTime==null){
            throw new NullPointerException("TextView cannot be null");
        }
        tvTotaltime=playbackTime;
        return this;
    }

    public AudioCong setPlayView(View play){
        if (play == null) {
            throw new NullPointerException("PlayView cannot be null");
        }
        btnPlay=play;
        return this;
    }
    public AudioCong setPauseView(View pause){
        if (pause == null) {
            throw new NullPointerException("PlayView cannot be null");
        }
        btnPause=pause;
        return this;
    }
    private AudioCong setSeekBarView(SeekBar seekB) {
        if (seekB==null){
            throw new NullPointerException("SeekBar cannot be null");
        }
        seekBar=seekB;
        return this;
    }

    private void initMediaSeekBar() {
        if (seekBar==null) return;
        totalTime=mediaPlayer.getDuration();
        seekBar.setMax((int) totalTime);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                updateRunTime(seekBar.getProgress());
            }
        });
    }

    private void updateRunTime(int i) {
        if (tvTotaltime==null){
            return;
        }
        if (mediaPlayer!=null){
            int min= (int) TimeUnit.MILLISECONDS.toMinutes(i);
            int min2= (int) TimeUnit.MILLISECONDS.toMinutes(totalTime);
            tvTotaltime.setText(String.format("%02d:%02d/%02d:%02d", min, TimeUnit.MILLISECONDS.toSeconds(i)-min*60,min2, TimeUnit.MILLISECONDS.toSeconds(totalTime)-60*min2));
        }
    }

    //Release mediaPlayer
    public void release(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    //--Lyrics touch listener------------ Enable/Disable auto scroll Lyrics
    private int timeCount, doubleDuration = 1000;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            int x = (int) System.currentTimeMillis() - timeCount;
            if (x <= doubleDuration){
                autoScroll = !autoScroll;
                timeCount = 0;
            }else{
                timeCount = (int) System.currentTimeMillis();
            }
        }
        return false;
    }

}
