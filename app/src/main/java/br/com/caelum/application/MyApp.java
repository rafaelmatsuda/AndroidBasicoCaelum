//package br.com.caelum.application;
//
//import android.app.Application;
//import android.content.IntentFilter;
//
//import br.com.caelum.broadcast.EsperaSms;
//
//public class MyApp extends Application {
//
//    @Override
//    public void onCreate()
//    {
//        super.onCreate();
//        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(new EsperaSms(), intentFilter);
//    }
//
//    @Override
//    public void onTerminate(){
//        super.onTerminate();
//        unregisterReceiver(new EsperaSms());
//    }
//}
