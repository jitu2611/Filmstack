package org.jitu.filmstack;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
 String baseurl= "http://image.tmdb.org/t/p/w154" ;
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);
try {
	TextView Title = (TextView) vi.findViewById(R.id.title);
	//TextView id =(TextView)vi.findViewById(R.id.id);
	ImageView iv = (ImageView) vi.findViewById(R.id.list_image);

	HashMap<String, String> list = new HashMap<String, String>();
	list = data.get(position);
	String poster = list.get(Search.IMAGE);
	poster = baseurl + poster;
	Uri u = Uri.parse(poster);

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
			.cacheOnDisk(true).resetViewBeforeLoading(true)
			.build();


	imageLoader.displayImage(poster, iv, options) ;

	Title.setText(list.get(Search.TITLE));
	//id.setText(list.get(Search.ID));
}catch(Exception e)
{e.printStackTrace();

}
	return vi;


	}

}
