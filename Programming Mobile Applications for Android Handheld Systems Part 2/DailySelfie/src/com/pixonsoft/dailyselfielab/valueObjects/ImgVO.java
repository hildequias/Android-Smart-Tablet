package com.pixonsoft.dailyselfielab.valueObjects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImgVO extends BitmapFactory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2918340194872287676L;
	private Bitmap image;
	
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	/* When we use a serialized class that has a bitmap as an attribute we need to implement the writeObject  
	*  and readObject methods to serialize the bitmap manually else or the O.S throws an exception
	*/
	private void writeObject(ObjectOutputStream out) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
		byte bitmapBytes[] = byteStream.toByteArray();
		out.write(bitmapBytes, 0, bitmapBytes.length);
	}

	private void readObject(ObjectInputStream in) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int b;
		while((b = in.read()) != -1)
			byteStream.write(b);
		byte bitmapBytes[] = byteStream.toByteArray();
		image = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
	}
}
