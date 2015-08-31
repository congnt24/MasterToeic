package com.ptpmcn.cong.jsonconfig;

/**
 * Created by cong on 8/29/2015.
 */
public class JsonModel {
    public static AppConfig AppConfig ;
    public JsonModel() {
        AppConfig=new AppConfig();
    }
    public static class AppConfig{
        public String AppName;
        public String Version;
        public String ReleaseDate;
        public String Developers;

        public void setValue(String appName, String version, String releaseDate, String developers){
            AppName = appName;
            Version = version;
            ReleaseDate = releaseDate;
            Developers = developers;
        }
    }
}
