package com.renewal_studio.pechengator;

import androidx.multidex.MultiDexApplication;

import com.yariksoffice.lingver.Lingver;
import com.yariksoffice.lingver.store.PreferenceLocaleStore;

import java.util.Locale;

import static com.renewal_studio.pechengator.utils.Constant.LANGUAGE_RUSSIAN;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceLocaleStore store = new PreferenceLocaleStore(this, new Locale(LANGUAGE_RUSSIAN));
        Lingver lingver = Lingver.init(this, store);
    }
}
