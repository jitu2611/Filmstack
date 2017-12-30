package org.jitu.filmstack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class Genre extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.genre,container,false);
    }

    @Override
    public void onStart(){
        super.onStart();
        //getActivity().getActionBar().setTitle("FilmStack");
        final String[] arr ={"28","12","16","35","80","99","18","10751","14","36","27","9648","10749","878","53"};
        listView =(ListView)getView().findViewById(R.id.genrelist) ;
        String[] osArray = {"Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","History","Horror","Mystery","Romance","Science Fiction","Thriller"};
       ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, osArray);
        try {
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = "http://api.themoviedb.org/3/discover/movie?api_key=1b104f628c51adebff0570a1a27ae2af&with_genres=" + arr[position] + "&page=1";

                    Search f = new Search();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle b = new Bundle();


                    b.putString("query", url);

                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    getActivity().getActionBar().setTitle("FilmStack");
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void onPause(){
        super.onPause();
        getActivity().getActionBar().setTitle("FilmStack");
    }

}
