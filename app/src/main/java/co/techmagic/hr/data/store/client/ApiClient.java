package co.techmagic.hr.data.store.client;

import java.util.concurrent.TimeUnit;

import co.techmagic.hr.data.store.IUserApi;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
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


    public static void initApiClient() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
    }


    private ApiClient() {
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
            String accessToken = SharedPreferencesUtil.getAccessToken();
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
