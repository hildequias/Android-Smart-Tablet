package com.pixonsoft.myplayermusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.pixonsoft.myplayermusic.dao.MusicDao;
import com.pixonsoft.myplayermusic.fragments.LibraryFragment;
import com.pixonsoft.myplayermusic.service.MusicService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private AccountHeader.Result header;
    private final Handler handler = new Handler();
    private Resources res;
    private PagerSlidingTabStrip tabs;
    private RelativeLayout rlControlsBg;
    private String palbumkey="";
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ImageView imgvStop;
    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    private int textColor = 0xffffffff;
    public ArrayList<String> songList;
    private ListView songView;
    String[] sarkilarlistesi;

    //service
    public MusicService musicSrv;
    private Intent playIntent;

    //binding
    private boolean musicBound=false;
    private Bitmap albumart;
    private ImageView albumimage;
    boolean donotrefresh=false;

    //activity and playback
    private int playing=2;
    Handler updateViewHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        rlControlsBg=(RelativeLayout)findViewById(R.id.controlsbg);
        pager.setAdapter(adapter);
        albumimage = (ImageView) findViewById(R.id.albumArt);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setTextColor(textColor);
        tabs.setIndicatorColor(textColor);
        tabs.setIndicatorHeight(5);
        tabs.setShouldExpand(true);
        tabs.setViewPager(pager);

        imgvStop=(ImageView)findViewById(R.id.stop);

        header = new AccountHeader().withActivity(this).withHeaderBackground(R.drawable.drawer).build();

        Drawer.Result result = new Drawer()
                .withActivity(this).withCloseOnClick(true)
                .withToolbar(toolbar).withDisplayBelowToolbar(false).withAccountHeader(header) //.withDrawerWidthDp(250)
                .withTranslucentStatusBar(false)
                .withSelectedItem(-1).withTranslucentActionBarCompatibility(false)

                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Now Playing").withIcon(R.drawable.ic_library).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        // fazer algo com o item clicado
                        if (drawerItem.getIdentifier() == 1) {
                            Intent intent = new Intent(MainActivity.this, NowPlaying.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4) {
                            Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                            i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, musicSrv.player.getAudioSessionId());
                            startActivityForResult(i, 11113);
                        }
                    }
                })
                .build();

        Intent servicestart = new Intent(this, MusicService.class);
        startService(servicestart);

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
        palbumkey="";

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
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onStop(){

        super.onStop();
        donotrefresh=true;
        recycleAlbumArt();
        if(!musicSrv.isPlaying()){
            musicSrv.notMgr.cancelAll();
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;

            //get service
            musicSrv = binder.getService();

            musicBound = true;
            try {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String action = extras.getString("action");
                    if (action != null && action.equals("stop")) {

                        musicSrv.stop();
                        Log.i("FM", "Parou a musica");
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            updateViewFromService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    public void recycleAlbumArt(){
        try{

            albumart=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.pixel);
            albumimage.setImageBitmap(albumart);
        }

        catch(NullPointerException ex){
            ex.printStackTrace();
        }
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


        else if(command.equals("songinfo")) {

            Intent intent=new Intent(getApplicationContext(), NowPlaying.class);

            startActivity(intent);

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
}
