package com.example.nguyentrungcong.createfilefromassets;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Debug;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nguyen Trung Cong on 9/11/2015.
 */
public class CreateFileFromAssets {

    Context context;
    private  static CreateFileFromAssets instance;

    public static CreateFileFromAssets getInstance(){
        if (instance == null){
            instance = new CreateFileFromAssets();
        }
        return instance;
    }

    public CreateFileFromAssets initialize(Context context){
        this.context = context;
        return this;
    }
    public CreateFileFromAssets CreateFileFromPath(String path){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            String[] listFile = assetManager.list(path);


            for (int i = 0; i < listFile.length; i++) {
                inputStream = assetManager.open(path+"/"+listFile[i]);
                File file = new File(context.getFilesDir().getPath()+"/"+path+"/"+listFile[i]);
                if (!file.exists()){
                    file.createNewFile();
                }
                int read = 0;
                outputStream = new FileOutputStream(file);
                byte[] offer = new byte[1024];
                while ( (read = inputStream.read(offer)) != -1){
                    outputStream.write(offer, 0, read);
                }
                Log.d("Write File:", "Success write file "+listFile[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return this;
    }
}
