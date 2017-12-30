package org.jitu.filmstack;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Jitesh on 6/23/2017.
 */
public class Favact extends Fragment{
    GridView gridview ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            return inflater.inflate(R.layout.favact, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        getActivity().getActionBar().setTitle("FilmStack");
        try {
            Cursor cursor = getActivity().getContentResolver().query(FavactDB.Contenturi, null, null, null,
                    null);
            if (cursor.getCount() == 0) {
                Toast.makeText(getActivity(), "No Info Available", Toast.LENGTH_SHORT).show();
            }
            gridview = (GridView) getView().findViewById(R.id.gridview);
            gridview.setAdapter(adapter);
        }catch (Exception e)
        {
            e.printStackTrace();

        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor1 = getActivity().getContentResolver().query(FavactDB.Contenturi,null,null,null,null) ;
                cursor1.moveToPosition(position) ;
                Bundle b = new Bundle();
                b.putString("pid", cursor1.getString(cursor1.getColumnIndex(FavactDB.favact.CID)));
                Person f = new Person();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                f.setArguments(b);
                ft.hide(fm.findFragmentById(R.id.mainfragment));
                ft.add(((ViewGroup) getView().getParent()).getId(), f);
                ft.addToBackStack(null) ;
                ft.commit();
            }
        });
    }

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public int getCount() {

            Cursor c = getActivity().getContentResolver().query(FavactDB.Contenturi,null,null,null,null) ;
            return  c.getCount();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.hpp, null);

            Cursor c = getActivity().getContentResolver().query(FavactDB.Contenturi, null, null, null, null) ;
            c.moveToPosition(position) ;

            ImageView imageView = (ImageView)retval.findViewById(R.id.iview) ;

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .build();


            imageLoader.displayImage( Person.baseurl + c.getString(c.getColumnIndex(FavactDB.favact.CIURL))  , imageView,options) ;




            return retval;
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getActionBar().setTitle("FilmStack");
    }
}
