package org.jitu.filmstack;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jitesh on 6/14/2017.
 */
public class WebJ extends Activity {
    WebView wv1 ;
    TextView tv ;
    String mlink ;
    String title="";
    List<String> validUrls = new LinkedList<>();

    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);


        validUrls.add("https://www\\.fmovies\\.is/*");
        validUrls.add("https://www\\.bitport\\.io/*");
        validUrls.add("https://www\\.123movies\\.co/*");
        validUrls.add("https://www\\.seedr\\.cc/*");
        progressBar = ProgressDialog.show(WebJ.this, "", "Loading...");



        Bundle extras = getIntent().getExtras() ;
        mlink = extras.getString("mlink") ;

        wv1=(WebView)findViewById(R.id.webView);

        wv1.setWebChromeClient(new WebChromeClient()) ;
        wv1.setWebViewClient(new WebViewClient() {
            int count=1 ;
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(isValidUrl(url)){
                    return true ;}
                return false ;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.cancel();


            }
        });
        WebSettings settings = wv1.getSettings();
        settings.setSupportMultipleWindows(false);
        wv1.getSettings().setAllowFileAccess(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setDomStorageEnabled(true);
        wv1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        try {
            wv1.loadUrl(extras.getString("watch"));

            //wv1.loadUrl("javascript:document.getElementById('frm-signInForm-email').value = '"+username+"';");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private boolean isValidUrl(String url) {
        for (String validUrl : validUrls) {
            Pattern pattern = Pattern.compile(validUrl, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }



}
