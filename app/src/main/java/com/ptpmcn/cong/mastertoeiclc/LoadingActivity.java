package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;

import apv.congnt24.data.CreateFileFromAssets;
import apv.congnt24.data.Decompress;
import apv.congnt24.network.Downloader;

/**
 * Created by cong on 8/29/2015.
 */
public class LoadingActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        context = getApplicationContext();
        new AsyncTask<String, Boolean, Boolean>(){
            @Override
            protected Boolean doInBackground(String... params) {
                File dataFile = new File(context.getFilesDir()+"/data0.zip");
                    if (internetAvailable()) {
                        if(!dataFile.exists() ){
                            CreateFileFromAssets.getInstance().initialize(getApplicationContext()).CreateOneFile("data0.zip");
//                            Downloader.download(context
//                                    , "https://www.dropbox.com/s/fhb584qhofibl94/data0.zip?dl=1");
                            dataFile = new File(context.getFilesDir() + "/data0.zip");
                            if (dataFile.exists())
                                try {
                                    Decompress.unzip(dataFile, context.getFilesDir());
                                    dataFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }else{
                        return false;
                    }
                return true;
            }
            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (!aVoid){
                    Toast.makeText(LoadingActivity.this, "Internet is not available", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(context, MainActivity.class));
            }
        }.execute("");
        initialize();
    }

    private void initialize() {
        //Init TessTwoApi
        CreateFileFromAssets.getInstance().initialize(this).CreateFileFromPath("data");
        CreateFileFromAssets.getInstance().initialize(this).CreateFileFromPath("tessdata");
        File f = new File(getFilesDir().getPath()+"/tessdata/eng.traineddata");
        Log.d("TAg,", getFilesDir().getPath() + "/tessdata/eng.traineddata");
    }


    boolean internetAvailable(){
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
           // NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
            //return netInfo != null && netInfo.isConnectedOrConnecting();
           NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    SharedPreferences prefs;
    public SharedPreferences getPrefsDownloaded(){
        if (prefs == null){
            prefs = context.getSharedPreferences("datadownloaded", MODE_PRIVATE);
        }
        return prefs;
    }

    public void setPrefsDataDownloaded(int count){
        getPrefsDownloaded().edit().putBoolean("data" + count, true).commit();
    }
}
