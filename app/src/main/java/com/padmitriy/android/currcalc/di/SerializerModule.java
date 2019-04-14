package com.padmitriy.android.currcalc.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;

@Module
public class SerializerModule {

    @Provides
    public Gson provideApiGson() {
        return new GsonBuilder()
                .create();
    }
}
