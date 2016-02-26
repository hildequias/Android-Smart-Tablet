package com.pixonsoft.dailyselfielab.valueObjects;

import android.graphics.Bitmap;

public class SelfieVO{
	
	private String name_image;
	private Bitmap image;
	private String path_image;
	
	public String getName_image() {
		return name_image;
	}
	public void setName_image(String name_image) {
		this.name_image = name_image;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getPath_image() {
		return path_image;
	}
	public void setPath_image(String path_image) {
		this.path_image = path_image;
	}
}
