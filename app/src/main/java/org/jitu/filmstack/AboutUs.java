package org.jitu.filmstack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class AboutUs extends Fragment{
ImageView mailj,mailb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.about,container,false);
    }
    @Override
    public void onStart(){
        super.onStart();
        try {
            try {
                getActivity().getActionBar().hide() ;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
           /* mailj = (ImageView) getView().findViewById(R.id.mail2j);
            mailb = (ImageView) getView().findViewById(R.id.mail2b);
            mailj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getActivity(),"igdgf",Toast.LENGTH_LONG).show();
                    Intent mj = new Intent(Intent.ACTION_SEND);
                    mj.setData(Uri.parse("mailto:"));
                    mj.setType("text/plain");
                    mj.putExtra(Intent.EXTRA_EMAIL, new String[]{"jk26111996@gmail.com"});
                    startActivity(mj);
                }
            });
            mailb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mj = new Intent(Intent.ACTION_SEND);
                    mj.setData(Uri.parse("mailto:"));
                    mj.setType("text/plain");
                    mj.putExtra(Intent.EXTRA_EMAIL, new String[]{"bibhu.u1@gmail.com"});
                    startActivity(mj);
                }
            }); */
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void onPause(){
        super.onPause();
        try {
            getActivity().getActionBar().show();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
