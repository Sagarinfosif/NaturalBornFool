package in.pureway.cinemaflix.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static Retrofit retrofit = null;
    static Retrofit retrofit1 = null;
    static final String BASEURL = "https://cinemaflix.in/admin/index.php/api/NaturalBorn/";
//    static final String BASEURL = "http://13.126.136.148/app/index.php/api/user/";

    static final String COMMENTBASEURL = "http://3.131.84.133/app/";

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit commentClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder().baseUrl(COMMENTBASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
                    .build();
        }
        return retrofit1;
    }
}
