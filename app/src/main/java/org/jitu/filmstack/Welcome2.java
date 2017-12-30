package org.jitu.filmstack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jitu on 12/7/2017.
 */
public class Welcome2 extends Fragment {
    static ListView glv ;
    SparseBooleanArray spj ;
    String[] osArray = {"Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","History","Horror","Mystery","Romance","Science Fiction","Thriller"};
    final String[] arr ={"28","12","16","35","80","99","18","10751","14","36","27","9648","10749","878","53"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            View v = inflater.inflate(R.layout.welcome_2, container, false);

            return v ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        glv = (ListView)getView().findViewById(R.id.listview) ;
        glv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        glv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, osArray));

        glv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //spj = glv.getCheckedItemPositions();


                //Toast.makeText(getActivity(), ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
