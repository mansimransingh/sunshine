package co.msingh.android.sunshine;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import co.msingh.android.sunshine.AlwaysOnTop;

public class MyApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private static MyApplication singleton;

    public MyApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        startService(new Intent(this, AlwaysOnTop.class));

    }

}