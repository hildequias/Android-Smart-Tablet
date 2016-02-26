package com.pixonsoft.dailyselfielab;

import com.pixonsoft.dailyselfie.R;
import com.pixonsoft.dailyselfielab.dao.SelfieDao;
import com.pixonsoft.dailyselfielab.valueObjects.SelfieVO;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class DetailActivity extends Activity {
	
	ImageView imageView;
	String mCurrentPhotoPath;
	SelfieDao dao;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		String name = this.getIntent().getStringExtra(MainActivity.TAG);
		
		dao = new SelfieDao(this);
		SelfieVO selfie = dao.findByName(name);
		
		if(selfie != null){
			imageView = (ImageView) findViewById(R.id.show_image);
			imageView.setImageBitmap(selfie.getImage());
			
			mCurrentPhotoPath = "file:" + selfie.getPath_image();
			
			//setPic();
		}
	}
	
	private void setPic() {
	    // Get the dimensions of the View
	    int targetW = imageView.getWidth();
	    int targetH = imageView.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    imageView.setImageBitmap(bitmap);
	}
}
