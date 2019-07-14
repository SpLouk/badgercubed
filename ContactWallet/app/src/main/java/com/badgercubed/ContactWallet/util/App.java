package com.badgercubed.ContactWallet.util;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context m_context;

    @Override
    public void onCreate() {
        super.onCreate();
        m_context = this;
    }

    public static Context getContext(){
        return m_context;
    }
}
