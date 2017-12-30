package org.jitu.filmstack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NoInternet extends Fragment {
    Button b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.nointernet, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
       // getActivity().getActionBar().hide();
        try {
            b = (Button) getView().findViewById(R.id.retry);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        getActivity().getFragmentManager().popBackStack();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
