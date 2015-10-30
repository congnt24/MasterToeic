package com.ptpmcn.cong.jsonconfig;

import java.util.List;

/**
 * Created by cong on 8/29/2015.
 */
public class JsonModel {
    public static AppConfig AppConfig ;
    public JsonModel() {
        AppConfig=new AppConfig();
    }
    public void setValue(String objName, List<Object> listValues){
        switch (objName){
            case "AppConfig":
                AppConfig.setValue(listValues);
                break;
            case "RelatedApps":
                break;
        }
    }

    public static class AppConfig{
        public String AppName;
        public String Version;
        public String Publisher;
        public String ReleaseDate;
        public String Developers;
        public String StoreAndroid;
        public String StoreIOS;
        public String SupportEmail;
        public String Website;
        public int NotShow;

        public void setValue(List<Object> values){
            AppName = String.valueOf(values.get(0));
            Version = String.valueOf(values.get(1));
            Publisher = String.valueOf(values.get(2));
            ReleaseDate = String.valueOf(values.get(3));
            Developers = String.valueOf(values.get(4));
            StoreAndroid = String.valueOf(values.get(5));
            StoreIOS = String.valueOf(values.get(6));
            SupportEmail = String.valueOf(values.get(7));
            Website = String.valueOf(values.get(8));
            NotShow = (int)values.get(9);
        }
    }
    public static class RelatedApps{
        public String ImageUrl, TargetUrl, Name, Description;
    }
}
