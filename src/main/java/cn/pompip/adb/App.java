package cn.pompip.adb;

import cn.pompip.utils.Log;
import com.android.ddmlib.Client;

import java.net.URL;

public class App {
    public String url = "/baidulite.apk";
    public String packageName ="com.baidu.searchbox.lite";
    public String startActivityName = "";
    public Client client;

    public URL getURL(){
        URL u =  this.getClass().getResource(url);
        Log.i(this,u.getPath());
        return u;
    }

    public Client getClient( ){
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
