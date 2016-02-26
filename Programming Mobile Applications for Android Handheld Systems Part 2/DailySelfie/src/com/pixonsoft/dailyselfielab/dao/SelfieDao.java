package com.pixonsoft.dailyselfielab.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pixonsoft.dailyselfielab.util.Util;
import com.pixonsoft.dailyselfielab.valueObjects.SelfieVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

public class SelfieDao {
	
	private static final String TAG_FOLDER = "CacheImagens";
	private File cacheDir;
	private Context context;
	private Util util = new Util();
	
	public SelfieDao(Context context){
		
		this.context = context;
		
		// Creates the folder "CacheImagens" within the sdcard to save disk images
 		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
 			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), TAG_FOLDER);
 		} else {
 			cacheDir = context.getCacheDir();
 		}
 		if (!cacheDir.exists()) {
 			// If the folder does not exist in the sdcard, creates
 			cacheDir.mkdirs();
 		}
	}
	
	// get a List<SelfieVO>
	public List<SelfieVO> findAll(){
		
		List<SelfieVO> selfies = new ArrayList<SelfieVO>();
		
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			File[] imageFiles = cacheDir.listFiles();
			
			for (File imageFile : imageFiles){
				
				SelfieVO selfie = new SelfieVO();
				selfie.setName_image(imageFile.getName());
				selfie.setImage(util.getBitmap(imageFile));
				selfie.setPath_image(imageFile.getAbsolutePath());
				selfies.add(selfie);
		    }
		}
		return selfies;
	}
	
	// return a SelfieVO with reference name.
	public SelfieVO findByName(String name){
		
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			File[] imageFiles = cacheDir.listFiles();
			
			for (File imageFile : imageFiles){
				
				if(imageFile.getName().equals(name)){
					SelfieVO selfie = new SelfieVO();
					selfie.setName_image(imageFile.getName());
					selfie.setImage(util.getBitmap(imageFile));
					selfie.setPath_image(imageFile.getAbsolutePath());
					return selfie;
				}
		    }
		}
		return null;
	}
	
	// Save a File in SDCard
	public SelfieVO save(Bitmap photo){
		
		String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
	    String photoFileName = time + "_";
		SelfieVO selfie = new SelfieVO();
	    
	    try {
			
	    	File file = File.createTempFile( photoFileName, ".jpg", cacheDir);
	    	
	    	FileOutputStream outStream = new FileOutputStream(file);  
	    	
	    	photo.compress(Bitmap.CompressFormat.PNG, 100, outStream);  
            outStream.close();
            
            selfie.setImage(photo);
            selfie.setName_image(photoFileName);
            selfie.setPath_image(file.getAbsolutePath());
            return selfie;
            
		} catch (IOException e) {
			Toast.makeText(this.context, "Unable to create a file in External Storage", Toast.LENGTH_SHORT).show();
			return selfie;
		}
	}
	
}
