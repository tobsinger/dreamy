package de.dreamy;

import android.app.Application;
import android.os.Debug;
import android.util.Log;

import de.dreamy.modules.DaggerDreamyComponent;
import de.dreamy.modules.DreamyComponent;
import de.dreamy.modules.DreamyModule;

/**
 * Created by tobe on 13.07.16.
 */
public class DreamyApplication extends Application {

    private static final String TAG = DreamyApplication.class.getSimpleName();

    private static DreamyComponent dreamyComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "starting Dreamy application and initializing injection component");
        dreamyComponent = DaggerDreamyComponent.builder().dreamyModule(new DreamyModule()).build();
    }

    public static DreamyComponent getDreamyComponent() {
        if (dreamyComponent == null) {
            Log.e(TAG, "trying to get injection component before it was initialized");
        }
        return dreamyComponent;
    }
}
