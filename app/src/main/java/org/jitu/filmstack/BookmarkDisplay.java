package org.jitu.filmstack;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class BookmarkDisplay extends Fragment {

	TextView title, release, genre, desc;
	ImageView poster, icon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.bookmarkdisplay, container, false);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// setContentView(R.layout.bookmarkdisplay);
		try {
			poster = (ImageView) getView().findViewById(R.id.poster);

			icon = (ImageView) getView().findViewById(R.id.icon);

			title = (TextView) getView().findViewById(R.id.title);
			release = (TextView) getView().findViewById(R.id.release);
			genre = (TextView) getView().findViewById(R.id.genre);
			desc = (TextView) getView().findViewById(R.id.desc);
			Bundle b = getArguments();
			int pos = b.getInt("pos");
			Cursor c = getActivity().managedQuery(Database.Contenturi, null, null,
					null, null);
			c.moveToPosition(pos);
			title.setText(c.getString(c.getColumnIndex(Database.bookmark.TITLE)));
			release.setText(c.getString(c.getColumnIndex(Database.bookmark.RELEASE)));
			genre.setText(c.getString(c.getColumnIndex(Database.bookmark.GENRE)));
			desc.setText(c.getString(c.getColumnIndex(Database.bookmark.DESC)));
			byte[] byteArray = c.getBlob(c.getColumnIndex(Database.bookmark.ICON));

			Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);
			icon.setImageBitmap(bm);
			byteArray = c.getBlob(c.getColumnIndex(Database.bookmark.POSTER));

			bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			poster.setImageBitmap(bm);
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
