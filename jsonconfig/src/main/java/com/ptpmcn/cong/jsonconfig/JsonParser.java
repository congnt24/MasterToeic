package com.ptpmcn.cong.jsonconfig;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    JSONObject jsonObject;
    public JsonParser ParseData(){
        try {
            jsonObject  = new JSONObject(jsonData);
            Iterator<String> keys = jsonObject.keys();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void ParseJsonObject(String objName) {
        Object appConfig = null;
        try {
            appConfig = jsonObject.get(objName);
            List<Object> listValues = new ArrayList<>();
            if(appConfig instanceof JSONObject){
                Iterator<String> keys = ((JSONObject)appConfig).keys();
                while (keys.hasNext()){
                    listValues.add(((JSONObject)appConfig).get(keys.next()));
                }
            }
            JsonModel.setValue(objName, listValues);
        } catch (JSONException e) {
            //If
        }
    }
    public JsonParser LoadJson(Context context, String fname){
        ReadJsonFile(context, fname).ParseData();
        return this;
    }

}
