package org.jitu.filmstack;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Jitesh on 6/22/2017.
 */
public class Algo extends Fragment {

    GridView gridview ;
    Bundle b ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            View v = inflater.inflate(R.layout.upcoming, container, false);
            gridview = (GridView) v.findViewById(R.id.gridview);
            try{
                b=getArguments() ;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return v ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        int check =0 ;
        try{
            check = b.getInt("check") ;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try {

            gridview.setAdapter(adapter);
            try{
            if(check==1)
            {   Log.i("displayid","a"+ b.getString("notificationid")) ;
                Bundle bb = new Bundle();
                bb.putString("url",b.getString("notificationid"));
                Display f = new Display();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                f.setArguments(bb);
                ft.hide(fm.findFragmentById(R.id.mainfragment));
                ft.add(R.id.mainfragment, f);
                ft.addToBackStack(null);
                ft.commit();
            }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Cursor c = getActivity().getContentResolver().query(AlgoDB.Contenturi, null, null,
                            null, "count DESC");
                    if (c != null) {
                        c.moveToPosition(position);
                        Bundle b = new Bundle();
                        b.putString("url", c.getString(c.getColumnIndex(AlgoDB.algo.ID)));
                        b.putString("title", c.getString(c.getColumnIndex(AlgoDB.algo.TITLE)));
                        c.close();

                        Display f = new Display();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        f.setArguments(b);
                        ft.hide(fm.findFragmentById(R.id.mainfragment));
                        ft.add(R.id.mainfragment, f);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            Cursor c = getActivity().getContentResolver().query(AlgoDB.Contenturi, null,null,
                    null,"count DESC");
            if(c!=null) {
                int l = c.getCount();
                c.close();
                return l;
            }
            return 0 ;
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
        public View getView(int position, View retval, ViewGroup parent) {
            if(retval==null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                retval = inflater.inflate(R.layout.hrow, parent, false);
            }
            ImageView img = (ImageView) retval.findViewById(R.id.hrow);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            img.getLayoutParams().height = (int)((width/2) * 1.2f) ;


            final ProgressBar spinner = (ProgressBar) retval.findViewById(R.id.loading);
            try {
                Cursor c = getActivity().getContentResolver().query(AlgoDB.Contenturi, null,null,
                        null,"count DESC");
                if(c!=null) {
                    c.moveToPosition(position);


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
                            .cacheOnDisk(true).resetViewBeforeLoading(true)
                            .build();


                    imageLoader.displayImage(c.getString(c.getColumnIndex(AlgoDB.algo.IURL)), img, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            spinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            spinner.setVisibility(View.GONE);
                        }
                    });
                    c.close();
                }

        }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return retval;
        }

    };
}
