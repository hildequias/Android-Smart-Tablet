package com.pixonsoft.dailyselfielab.adapter;

import java.util.List;

import com.pixonsoft.dailyselfie.R;
import com.pixonsoft.dailyselfielab.valueObjects.SelfieVO;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
    private final List<SelfieVO> selfies;
	private final Activity context;
	
	public SelfieAdapter(Activity context, List<SelfieVO> selfies){
		
		this.selfies = selfies;
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return selfies != null ? selfies.size() : 0;
	}

	@Override
	public Object getItem(int position) {
	    return selfies != null ? selfies.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (view == null) {
			// no exist a view in cache. 
			
            holder = new ViewHolder();
            // Search the layout for each picture taken
            int layout = R.layout.list_item;
            view = inflater.inflate(layout, null);
            view.setTag(holder);
            holder.name_image = (TextView) view.findViewById(R.id.image_name);
            holder.image = (ImageView) view.findViewById(R.id.imageView);
            holder.path_image = "";
		}else {
            // it exist in cache
            holder = (ViewHolder) view.getTag();
        }
		
		// Create a SelfieVO valueObject
        SelfieVO s = selfies.get(position);

        holder.name_image.setText(s.getName_image());
        holder.image.setImageBitmap(s.getImage());
        holder.path_image = s.getPath_image();
        return view;
	}
	
	// Design Patter "ViewHolder" to Android
    static class ViewHolder {
    	TextView name_image;
    	ImageView image;
    	String path_image;
    }

}
