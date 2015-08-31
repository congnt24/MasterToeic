package com.example.cong.audiocong;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cong on 3/11/2015.
 */
public class Downloader {

    private static Downloader downloader;
    private String link, fname;
    private Context mcontext;

    public static Downloader getInstance(){
        if (downloader==null)
            downloader=new Downloader();
        return downloader;
    }
    public Downloader init(Context context, String link){
        setLink(link);
        this.mcontext=context;
        return this;
    }
    public Downloader setFileName(String fname){
        this.fname=fname;
        return this;
    }
    public Downloader setLink(String link){
        this.link = link;
        return this;
    }
    //Download mp3 file
    public File startDownload(){
        new AsyncTask<String, Integer, String>(){

            @Override
            protected String doInBackground(String... urls) {
                String fileName= Uri.parse(urls[0]).getLastPathSegment();
                if (new File(mcontext.getFilesDir()+"/"+fileName).exists()){
                    return fileName;
                }
                InputStream inputStream=null;
                FileOutputStream outputStream=null;
                HttpURLConnection connection = null;
                try {
                    URL url=new URL(urls[0]);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                        Log.d("Downloader", "start download");
                        int fileLength=connection.getContentLength();
                        inputStream=connection.getInputStream();
                        outputStream=mcontext.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                        byte data[]=new byte[4096];
                        long total = 0;
                        int count;
                        while((count=inputStream.read(data)) > 0){
                            total+=count;
                            if (fileLength>0)
                                publishProgress((int) total*100/fileLength);
                            outputStream.write(data, 0, count);
                        }
                        Log.d("Downloader", "finish download");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (inputStream!=null)
                            inputStream.close();
                        if (outputStream!=null)
                            outputStream.close();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                    if (connection!=null)
                        connection.disconnect();
                }
                Log.d("aaaaaaaaaaaaa", "aaaassssssssssss1 " + fileName);
                setFileName(fileName);
                endDownload=true;
                return fileName;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                //Update progress bar
            }


           /* @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("aaaaaaaaaaaaa", "aaaassssssssssss " + s);
                setFileName(s);
                endDownload=true;
            }*/
        }.execute(link);
        while(!endDownload) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.d("aaaaaaaaaaaaa", "aaaaaaaaaaaa "+fname);
        return new File(mcontext.getFilesDir()+"/"+this.fname);

    }
    boolean endDownload=false;



}
