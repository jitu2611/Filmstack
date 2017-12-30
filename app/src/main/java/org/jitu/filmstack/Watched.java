package org.jitu.filmstack;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jitesh on 6/23/2017.
 */
public class Watched extends Fragment {

    ListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.listsaved, container, false);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        //setContentView(R.layout.listsaved);
        try {
            getActivity().getActionBar().setTitle("FilmStack");
            lv = (ListView) getView().findViewById(R.id.savedpage);
            registerForContextMenu(lv);
            final Cursor cursor = getActivity().getContentResolver().query(WatchedDB.Contenturi, null, null, null,
                    null);
            if (cursor.getCount() == 0) {
                Toast.makeText(getActivity(), "No Info Available", Toast.LENGTH_SHORT).show();
            }
            cursor.moveToFirst();


            lv.setAdapter(new BookmarkAdapte(getActivity(), cursor));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Cursor cursor1 = getActivity().getContentResolver().query(WatchedDB.Contenturi, null, null, null, null);
                    cursor1.moveToPosition(position);
                    Bundle b = new Bundle();
                    b.putString("url", cursor1.getString(cursor1.getColumnIndex(WatchedDB.watched.ID)));
                    b.putString("title", cursor1.getString(cursor1.getColumnIndex(WatchedDB.watched.TITL)));
                    Display f = new Display();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public class BookmarkAdapte extends BaseAdapter {

        private Activity act;
        private Cursor c ;
        private  LayoutInflater inflater = null;
        public BookmarkAdapte(Activity a , Cursor cursor ) {
            Log.d("cursor init","f") ;
            act = a ;
            c = cursor ;
            Log.d("cursor init","f") ;
            //	Toast.makeText(act, c.getString(c.getColumnIndex(Database.bookmark.TITLE)), Toast.LENGTH_SHORT).show() ;
            try {
                inflater = (LayoutInflater) act
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            // TODO Auto-generated constructor stub
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return c.getCount() ;
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
            // TODO Auto-generated method stub
            View vi = convertView ;

            if(convertView==null)
            {  try {
                vi = inflater.inflate(R.layout.list_row, null);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            }

            c.moveToPosition(position) ;
            //  Toast.makeText(act, c.getString(c.getColumnIndex(Database.bookmark.TITLE)), Toast.LENGTH_SHORT).show() ;

            TextView Title = (TextView)vi.findViewById(R.id.title) ;

            ImageView iv  = (ImageView)vi.findViewById(R.id.list_image) ;

            Title.setText(c.getString(c.getColumnIndex(WatchedDB.watched.TITL))) ;
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .build();


            imageLoader.displayImage(MainActivity.baseurl + c.getString(c.getColumnIndex(WatchedDB.watched.IURL)), iv, options) ;
            return vi;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().getActionBar().setTitle("FilmStack");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        int q = info.position;
        Cursor c = getActivity().managedQuery(WatchedDB.Contenturi, null, null, null, null);
        c.moveToPosition(q);
        int qw = c.getInt(c.getColumnIndex(WatchedDB.watched.ID));

        switch (item.getItemId()) {

            case R.id.del:
                // String[] whereArgs = new String[] {Long.toString(l)};
                @SuppressWarnings("unused")
                int d = getActivity().getContentResolver().delete(WatchedDB.Contenturi,
                        "id = " + qw, null);

                try {
                    Cursor ci = getActivity().managedQuery(WatchedDB.Contenturi, null, null, null,
                            null);
                    ci.moveToFirst();
                    lv.setAdapter(new BookmarkAdapte(getActivity(), ci));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity().getBaseContext(), "Deleted Successfully ",
                        Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);

    }




}
