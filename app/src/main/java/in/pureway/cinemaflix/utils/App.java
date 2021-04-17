package in.pureway.cinemaflix.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import in.pureway.cinemaflix.javaClasses.commentsInterface;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.util.List;
import java.util.Locale;

public class App extends Application {

    private static App instance;
    private static Sharedpref sharedpref;
    private static Singleton singleton;
    Context context;
    commentsInterface commentsInterface;
    public static SimpleCache simpleCache;
    public static ExoDatabaseProvider exoDatabaseProvider;
    public static Long exoPlayerCacheSize = Long.valueOf(1 * 1024 * 1024);
    public static LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        sharedpref = new Sharedpref(getApplicationContext());
        singleton = new Singleton();
        if(leastRecentlyUsedCacheEvictor == null){
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);

        }
        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(context);
        }
        if (simpleCache == null) {
            simpleCache = new SimpleCache(context.getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
        }
    }
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }
    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(20)
                .build();
    }
    public static void changeLanguage(Activity activity, String lag) {
        Resources res = activity.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();

        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lag)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    public static Sharedpref getSharedpref() {
        return sharedpref;
    }

    public static Singleton getSingleton() {
        return singleton;
    }

    public static App getAppContext() {
        return instance;
    }

    public commentsInterface getCommentsInterface() {
        return commentsInterface;
    }

    public void setCommentsInterface(commentsInterface commentsInterface) {
        this.commentsInterface = commentsInterface;
    }

    public static boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
// Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }
}

