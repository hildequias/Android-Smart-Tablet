package com.pixonsoft.dailyselfielab;

import java.util.List;

import com.pixonsoft.dailyselfie.R;
import com.pixonsoft.dailyselfielab.adapter.SelfieAdapter;
import com.pixonsoft.dailyselfielab.dao.SelfieDao;
import com.pixonsoft.dailyselfielab.util.Util;
import com.pixonsoft.dailyselfielab.valueObjects.SelfieVO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity{
	
	static final String TAG = "MainActivity";
	static final int CAMERA_REQUEST = 1;
	
	private ListView listView;
	private SelfieDao dao;
	private List<SelfieVO> selfies;
	private Util util = new Util();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				
				SelfieVO selfie = (SelfieVO) parent.getAdapter().getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
				intent.putExtra(TAG, selfie.getName_image());
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		dao = new SelfieDao(this);
		updateListView();
		
		util.calcelNotification(this); // cancel the notification for app.
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		
		util.turnNotification(this); // turn on the notification for app.
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// inflater Menu
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		int id = item.getItemId();
		if (id == R.id.menu_camera){
			takeSelfie(); 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void takeSelfie(){
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{	
	    if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
	    	Bitmap photo = (Bitmap)data.getExtras().get("data");
	    	selfies.add(dao.save(photo)); //create a file photo in SDCARD and add in List selfies.
	    }
	}
	
	// update ListView
	protected void updateListView(){
		
		selfies = dao.findAll();
		listView.setAdapter(new SelfieAdapter(this, selfies));
	}
	
}
