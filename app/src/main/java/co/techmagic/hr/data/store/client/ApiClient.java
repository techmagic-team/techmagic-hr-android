package co.techmagic.hr.data.store.client;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import co.techmagic.hr.data.manager.IAuthenticationManager;
import co.techmagic.hr.data.manager.impl.AuthenticationManagerImpl;
import co.techmagic.hr.data.store.IUserApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by techmagic on 8/29/16.
 */
public class ApiClient {

    public static final String HOST = "http://techmagic-hr-api-dev.eu-central-1.elasticbeanstalk.com";

    private static ApiClient apiClient;
    private Retrofit retrofit;

    private IAuthenticationManager authenticationManager;
    private Context context;


    public static void initApiClient(Context context) {
        if (apiClient == null) {
            apiClient = new ApiClient(context);
        }
    }


    private ApiClient(Context context) {
        this.context = context;
        authenticationManager = new AuthenticationManagerImpl();
        OkHttpClient client = buildClient();
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static ApiClient getApiClient() {
        return apiClient;
    }


    public OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            String accessToken = authenticationManager.readAccessToken(context);
            Request.Builder request = chain.request().newBuilder().addHeader("Accept", "application/json");
            if (accessToken != null) {
                // TODO: 3/24/17 add needed headers
            }
            return chain.proceed(request.build());
        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return  builder.build();
    }


    public IUserApi getUserApiClient() {
        return retrofit.create(IUserApi.class);
    }
}
