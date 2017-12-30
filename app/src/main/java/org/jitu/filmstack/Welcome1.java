package org.jitu.filmstack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by Jitu on 12/7/2017.
 */
public class Welcome1 extends Fragment {

    TextView tv ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            View v = inflater.inflate(R.layout.welcome_1, container, false);
            tv = (TextView)v.findViewById(R.id.t1) ;

            return v ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
