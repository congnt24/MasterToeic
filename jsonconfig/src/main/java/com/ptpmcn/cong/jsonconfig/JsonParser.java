package com.ptpmcn.cong.jsonconfig;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cong on 8/29/2015.
 */
public class JsonParser {

    public JsonModel JsonModel = new JsonModel();



    private String jsonData;
    //private Context context;
    private static JsonParser ourInstance = new JsonParser();

    public static JsonParser getInstance() {
        return ourInstance;
    }

    private JsonParser() {
    }

    public JsonParser ReadJsonFile(Context context, String fname){
        try {
            InputStream is = context.getAssets().open(fname);

            StringBuilder sb= new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String tmp="";
                while ((tmp = br.readLine()) != null){
                    sb.append(tmp);
                }
                jsonData = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
    public JsonParser ParseData(){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject appConfig = jsonObject.getJSONObject("AppConfig");
            JsonModel.AppConfig.setValue(appConfig.getString("AppName"), appConfig.getString("Version")
                    , appConfig.getString("ReleaseDate"), appConfig.getString("Developers"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }
    public JsonParser LoadJson(Context context, String fname){
        ReadJsonFile(context, fname).ParseData();
        return this;
    }

}
