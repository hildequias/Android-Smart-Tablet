package com.pixonsoft.myplayermusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.pixonsoft.myplayermusic.adapter.AlbumAdapter;
import com.pixonsoft.myplayermusic.adapter.SongAdapter;
import com.pixonsoft.myplayermusic.dao.MusicDao;
import com.pixonsoft.myplayermusic.fragments.LibraryFragment;
import com.pixonsoft.myplayermusic.model.Album;
import com.pixonsoft.myplayermusic.model.Song;
import com.pixonsoft.myplayermusic.service.MusicService;

import java.util.ArrayList;

/**
 * Created by mobile6 on 1/24/16.
 */
public class SongListActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private final Handler handler = new Handler();
    private Intent intent;
    private Resources res;
    private PagerSlidingTabStrip tabs;
    private RelativeLayout rlControlsBg;
    private String palbumkey="";
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

    //binding
    private boolean musicBound=false;
    boolean donotrefresh=false;
    private Bitmap albumart;
    private ImageView albumimage;

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
        setContentView(R.layout.activity_song_list);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlControlsBg=(RelativeLayout)findViewById(R.id.controlsbg);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        albumimage = (ImageView) findViewById(R.id.albumArt);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

        imgvStop=(ImageView)findViewById(R.id.stop);

        Intent servicestart = new Intent(this, MusicService.class);
        startService(servicestart);
        imgvStop=(ImageView)findViewById(R.id.stop);
        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");

        if(type.equals("songsofalbum")) {
            //Lista de musicas do Album
            String albumkey = extras.getString("key");
            getSupportActionBar().setTitle("Songs");
            final ArrayList<Song> songlist = new ArrayList<Song>();
            final ArrayList<Song> arrLAlbums = MusicDao.getSongList(getApplicationContext());
            for (Song item : arrLAlbums) {
                if (albumkey.equals(item.albumkey)) {
                    songlist.add(item);
                }
            }
            SongAdapter adapter = new SongAdapter(this, songlist);
            ListView listemiz = (ListView) findViewById(R.id.list1);
            listemiz.setAdapter(adapter);
            listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView selectedview = (TextView) view.findViewById(R.id.tvSongId);
                    String selectedsongid = selectedview.getText().toString();

                    if (musicBound) {
                        musicSrv.getSongListAndSong(songlist, position);

                    }

                }
            });
        }
        else if(type.equals("albumsofartist")){
            String artistkey = extras.getString("key");
            getSupportActionBar().setTitle("Albums");
            final ArrayList<Album> albumlist = new ArrayList<Album>();
            final ArrayList<Album> arrLAlbums = MusicDao.getAlbumListFromSongs(getApplicationContext());
            for (Album item : arrLAlbums) {
                if (artistkey.equals(item.albumartist)) {

                    albumlist.add(item);
                }

            }
            AlbumAdapter adapter = new AlbumAdapter(this, albumlist);
            ListView listemiz = (ListView) findViewById(R.id.list1);
            listemiz.setAdapter(adapter);
            intent=new Intent(this, SongListActivity.class);
            listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView selectedview = (TextView) view.findViewById(R.id.tvSongId);
                    String selectedsongid = selectedview.getText().toString();


                    intent.putExtra("type", "songsofalbum");
                    intent.putExtra("key", selectedsongid);

                    startActivity(intent);
                }
            });
        }
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
    public void recycleAlbumArt(){
        try{

            albumart= BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.pixel);
            albumimage.setImageBitmap(albumart);
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
        donotrefresh=true;
        recycleAlbumArt();


    }

    @Override
    public void onStop(){
        super.onStop();
        donotrefresh=true;
        recycleAlbumArt();

    }

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
        outState.putInt("currentColor", currentColor);
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
