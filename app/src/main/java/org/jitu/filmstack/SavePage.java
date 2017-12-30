package org.jitu.filmstack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class SavePage extends Fragment{

	ListView lv;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.listsaved, container,false) ;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//setContentView(R.layout.listsaved);
		getActivity().getActionBar().setTitle("FilmStack");
		lv = (ListView) getView().findViewById(R.id.savedpage);
		registerForContextMenu(lv);

		try {
			Cursor cursor = getActivity().managedQuery(Database.Contenturi, null, null, null,
					null);
			if(cursor.getCount()==0)
			{
				Toast.makeText(getActivity(),"NO BOOKMARKS AVAILABLE",Toast.LENGTH_SHORT).show();
			}
			cursor.moveToFirst();

			Log.d("cursor init", cursor.getString(cursor
					.getColumnIndex(Database.bookmark.TITLE)));

			lv.setAdapter(new BookmarkAdapter(getActivity(), cursor));
		} catch (Exception e) {
			e.printStackTrace();
		}
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				try {
					BookmarkDisplay f = new BookmarkDisplay();
					Bundle b = new Bundle();
					b.putInt("pos", pos);
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					f.setArguments(b);
					ft.hide(fm.findFragmentById(R.id.mainfragment));
					ft.add(((ViewGroup) getView().getParent()).getId(), f);
					ft.addToBackStack(null);
					ft.commit();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.savedpage) {

			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.menu_list, menu);

		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// return super.onContextItemSelected(item);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int q = info.position;
		Cursor c = getActivity().managedQuery(Database.Contenturi, null, null, null, null);
		c.moveToPosition(q);
		int qw = c.getInt(c.getColumnIndex(Database.bookmark._ID));

		switch (item.getItemId()) {

		case R.id.del:
			// String[] whereArgs = new String[] {Long.toString(l)};
			@SuppressWarnings("unused")
			int d = getActivity().getContentResolver().delete(Database.Contenturi,
					"_id = " + qw, null);

			try {
				Cursor ci = getActivity().managedQuery(Database.Contenturi, null, null, null,
						null);
				ci.moveToFirst();
				lv.setAdapter(new BookmarkAdapter(getActivity(), ci));
			} catch (Exception e) {
				e.printStackTrace();
			}

			Toast.makeText(getActivity().getBaseContext(), "Deleted Successfully ",
					Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);

	}

	public void onPause(){
		super.onPause();
		getActivity().getActionBar().setTitle("FilmStack");
	}

}
