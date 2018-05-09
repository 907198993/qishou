package com.qifan.qishou;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;
import com.github.retrofitutil.NetWorkManager;
/**
 * Created by administartor on 2017/8/8.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if(true&&BuildConfig.DEBUG){                           //http://192.168.0.19:20001/        //http://121.40.186.118:5108
         //   NetWorkManager.getInstance(getApplicationContext(),"http://121.40.186.118:5108",BuildConfig.DEBUG).complete();

             NetWorkManager.getInstance(getApplicationContext(),"http://47.104.102.17:8003",BuildConfig.DEBUG).complete();
        }else{
          //  NetWorkManager.getInstance(getApplicationContext(),"http://121.40.186.118:5008",BuildConfig.DEBUG).complete();
            NetWorkManager.getInstance(getApplicationContext(),"http://47.104.102.17:8003",BuildConfig.DEBUG).complete();
        }

//        if(true&&BuildConfig.DEBUG){                           //http://192.168.0.19:20001/        //http://121.40.186.118:5108
//            NetWorkManager.getInstance(getApplicationContext(),"http://121.40.186.118:5108",BuildConfig.DEBUG).complete();
//
//           // NetWorkManager.getInstance(getApplicationContext(),"http://192.168.0.19:20001",BuildConfig.DEBUG).complete();
//        }else{
//           NetWorkManager.getInstance(getApplicationContext(),"http://121.40.186.118:5008",BuildConfig.DEBUG).complete();
////            NetWorkManager.getInstance(getApplicationContext(),"http://192.168.0.19:20001",BuildConfig.DEBUG).complete();
//        }
        initDownloader();
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }


}
