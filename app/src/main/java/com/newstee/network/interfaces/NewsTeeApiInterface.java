package com.newstee.network.interfaces;

import com.newstee.model.data.DataAudio;
import com.newstee.model.data.DataAuthor;
import com.newstee.model.data.DataNews;
import com.newstee.model.data.DataTag;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Arnold on 05.04.2016.
 */
public interface NewsTeeApiInterface {
   public static String BASE_URL = "http://213.231.4.68/audiotest/";
   /* @GET("infoOutputJson.php?json=authors")
    Call<List<AuthorLab>> getAuthors();*/
   @GET("dispatcher.php?command=get_tags")
   Call<DataTag> getTags();
    @GET("dispatcher.php?command=get_authors")
    Call<DataAuthor> getAuthors();
    @GET("dispatcher.php?command=get_news")
    Call<DataNews> getNews();
   @GET("dispatcher.php?command=get_audio")
    Call<DataAudio> getAudio();
/*//@From
  //  @GET("your_endpoint") Call<YOUR_POJO> getWeather(@Query("from") String from);

    class Factory {
        private static NewsTeeApiInterface service;

        public static NewsTeeApiInterface getIstance(Context context) {
            if (service == null) {

                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                builder.readTimeout(15, TimeUnit.SECONDS);
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.writeTimeout(10, TimeUnit.SECONDS);

                //builder.certificatePinner(new CertificatePinner.Builder().add("*.androidadvance.com", "sha256/RqzElicVPA6LkKm9HblOvNOUqWmD+4zNXcRb+WjcaAE=")
                //    .add("*.xxxxxx.com", "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=")
                //    .add("*.xxxxxxx.com", "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=")
                //    .add("*.xxxxxxx.com", "sha256/VjLZe/p3W/PJnd6lL8JVNBCGQBZynFLdZSTIqcO0SJ8=")
                //    .build());

                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                    builder.addInterceptor(interceptor);
                }

                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                Cache cache = new Cache(context.getCacheDir(), cacheSize);
                builder.cache(cache);

                Retrofit retrofit = new Retrofit.Builder().client(builder.build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(NewsTeeApiInterface.class);
                return service;
            } else {
                return service;
            }
        }
    }*/
}
