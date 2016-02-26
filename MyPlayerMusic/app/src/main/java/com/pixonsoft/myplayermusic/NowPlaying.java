package com.pixonsoft.myplayermusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.pixonsoft.myplayermusic.dao.MusicDao;
import com.pixonsoft.myplayermusic.fragments.LibraryFragment;
import com.pixonsoft.myplayermusic.service.MusicService;

import java.util.ArrayList;

/**
 * Created by mobile6 on 1/24/16.
 */
public class NowPlaying extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private Toolbar toolbar;
    private final Handler handler = new Handler();
    private Intent intent;
    private MediaPlayer player;
    private Resources res;
    private PagerSlidingTabStrip tabs;
    private String palbumkey="";
    boolean donotrefresh=false;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ImageView imgvStop;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    int playing=2;
    public ArrayList<String> songList;
    private ListView songView;
    String[] sarkilarlistesi;

    //service
    public MusicService musicSrv;
    private Intent playIntent;
    private Bitmap albumart;
    ImageView albumimage;

    //binding
    private boolean musicBound=false;
    private SeekBar musicSeekBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    public static boolean isShuffle = false;
    public static boolean isRepeat = false;
    ImageView shuffle;
    ImageView repeat;

    //activity and playback
    private boolean paused=false, playbackPaused=false;
    Handler updateViewHandler = new Handler();

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();

            musicBound = true;
            updateViewFromService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_playing);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent servicestart = new Intent(this, MusicService.class);
        startService(servicestart);

        albumimage = (ImageView) findViewById(R.id.albumArt);
        imgvStop=(ImageView)findViewById(R.id.stop);
        repeat= (ImageView) findViewById(R.id.repeat);
        shuffle = (ImageView) findViewById(R.id.shuffle);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        imgvStop=(ImageView)findViewById(R.id.stop);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

        musicSeekBar = (SeekBar) findViewById(R.id.seekBar);
        musicSeekBar.setOnSeekBarChangeListener(this);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
    }

    @Override
    public void onStart() {
        super.onStart();
        res=getResources();
        if(playIntent==null){
            playIntent = new Intent(getApplicationContext(), MusicService.class);
            getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getApplicationContext().startService(playIntent);

        }

    }

    public void recycleAlbumArt(){
        try{((BitmapDrawable)albumimage.getDrawable()).getBitmap().recycle();
            albumimage.setImageDrawable(null);
            albumart.recycle();
        }

        catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        donotrefresh=true;
        recycleAlbumArt();
        albumart= BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.pixel);
        albumimage.setImageBitmap(albumart);

    }

    @Override
    public void onResume(){
        super.onResume();
        palbumkey="";
        donotrefresh=false;
        if(musicBound) {
            updateViewFromService();
        }
        if(isShuffle)
            shuffle.setImageResource(R.drawable.btn_playback_shuffle_all);
        if(isRepeat)
            repeat.setImageResource(R.drawable.btn_playback_repeat_all);
        recycleAlbumArt();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        donotrefresh=true;
        recycleAlbumArt();

    }

    @Override
    public void onStop(){
        super.onStop();
        donotrefresh=true;
        recycleAlbumArt();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
    Runnable run = new Runnable() {

        @Override
        public void run() {

            updateViewFromService();
            // update();
        }
    };

    public void updateViewFromService(){
        String title=musicSrv.getTitle();

        String info, album,artist;
        if(title.isEmpty()||title.equals(""))
        {
            title=res.getString(R.string.app_name);
            album="";
            artist="";
            info="";
        }
        else
        {
            album=musicSrv.getAlbum();
            artist=musicSrv.getArtist();
            if(title.length()>40)
                title=title.substring(0,40)+"...";
            if(album.length()>20)
                album=album.substring(0,20)+"...";
            if(artist.length()>20)
                artist=artist.substring(0,20)+"...";
            info=album+" "+res.getString(R.string.by)+" "+artist;
        }
        ((TextView)findViewById(R.id.infoSongTitle)).setText(title);
        ((TextView)findViewById(R.id.infoAlbumTitle)).setText(info);
        int s=160;
        if(musicSrv.getAlbumKey()!=null&&!palbumkey.equals(musicSrv.getAlbumKey())) {

            try {

                String art=MusicDao.getAlbumArt(getApplicationContext(),musicSrv.getAlbumKey());
                if(art!=null&&!art.equals("")) {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(art, options);
                    options.inSampleSize = MusicDao.calculateInSampleSize(options, s, s);
                    options.inJustDecodeBounds = false;
                    try{recycleAlbumArt();}catch (NullPointerException e){}
                    albumart = BitmapFactory.decodeFile(art, options);

                    albumimage.setImageBitmap(albumart);



                }
                else{
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.cover_logo,options);
                    options.inSampleSize = MusicDao.calculateInSampleSize(options, s, s);
                    options.inJustDecodeBounds = false;
                    albumart=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.cover_logo,options);


                    albumimage.setImageBitmap(albumart);

                }

            } catch (Exception ex) {

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.cover_logo,options);
                options.inSampleSize = MusicDao.calculateInSampleSize(options, s, s);
                options.inJustDecodeBounds = false;
                albumart=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.cover_logo,options);
                albumimage.setImageBitmap(albumart);
            }
            palbumkey=musicSrv.getAlbumKey();
        }
        if(musicSrv.isPlaying()) {
            if(playing!=1)
                imgvStop.setImageResource(R.drawable.stop);
            long totalDuration = musicSrv.getDuration();
            long currentDuration = musicSrv.currentPosition();

            // Tempo Duração Total da musica.
            songTotalDurationLabel.setText(""+current((totalDuration)));

            // Tempo ja concluido
            songCurrentDurationLabel.setText(""+(current(currentDuration)));

            // Atualiza o progress
            int progress = (int)(getProgressPercentage(currentDuration, totalDuration));
            musicSeekBar.setProgress(progress);
        }
        else{
            if(playing!=0)
                imgvStop.setImageResource(R.drawable.play);

        }

        if(!donotrefresh) {
            updateViewHandler.postDelayed(run, 1000);
        }
    }

    public void onControlClicked(View v) {

        String command = v.getTag().toString();
        if(command.equals("stop")){
            if(musicSrv.isPlaying()) {
                musicSrv.pause();
                imgvStop.setImageResource(R.drawable.play);
                playing=0;
            }
            else{
                musicSrv.start();
                if(musicSrv.isPlaying()) {
                    imgvStop.setImageResource(R.drawable.stop);
                    playing = 1;
                }
            }
        }

        else if(command.equals("next")){
            if(musicSrv.isPrepared()) {
                musicSrv.playNext();
            }
        }
        else if(command.equals("prev")){
            if(musicSrv.isPrepared()) {
                musicSrv.playPrev();
            }
        }
        else if (command.equals("shuffle")) {
            if (isShuffle) {
                isShuffle = false;


                shuffle.setImageResource(R.drawable.btn_playback_shuffle);
            } else {
                // marca o isShuffle como true
                isShuffle = true;

                // marca o isRepeat como false
                isRepeat = false;
                shuffle.setImageResource(R.drawable.btn_playback_shuffle_all);
                repeat.setImageResource(R.drawable.btn_playback_repeat);

            }
        }
        else if (command.equals("repeat")) {
            if (isRepeat){
                isRepeat=false;

                repeat.setImageResource(R.drawable.btn_playback_repeat);
            }
            else{
                isRepeat=true;
                isShuffle=false;
                repeat.setImageResource(R.drawable.btn_playback_repeat_all);
                shuffle.setImageResource(R.drawable.btn_playback_shuffle);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            }
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            musicSrv.player.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(run);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(run);
        int totalDuration = musicSrv.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // frente ou para trás a certos segundos
        musicSrv.player.seekTo(currentPosition);

        // atualizar o progress novamente
        updateProgressBar();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        Resources res = getResources();
        private final String[] TITLES = { res.getString(R.string.artists), res.getString(R.string.albums), res.getString(R.string.songs) };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return LibraryFragment.newInstance(position);
        }

    }

    public String current(long currentDuration) {
        String finalTimerString = "";
        String secondsString = "";
        Long time = currentDuration;
        long seconds = time / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = minutes + ":" + secondsString;

        // returna o tempo em string
        return finalTimerString;
    }

    /**
     * Função para obter percentual Progresso
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // Cálculo percentual
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentual
        return percentage.intValue();
    }

    /**
     * Função para mudar o progress
     * @param progress -
     * @param totalDuration
     * retorna duração atual em milissegundos
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // retorna duração atual em milissegundos
        return currentDuration * 1000;
    }
    public void updateProgressBar() {
        handler.postDelayed(run, 100);
    }
}
