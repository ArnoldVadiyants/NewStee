package com.newstee.model.data;

/**
 * Created by Arnold on 09.04.2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newstee.R;
import com.newstee.network.interfaces.NewsTeeApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsLab {

    private List<News> mNews = new ArrayList<>();
    private static NewsLab sNewsLab;
    private Context mAppContext;
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(NewsTeeApiInterface.BASE_URL)
            .build();
    private NewsTeeApiInterface newsTeeApiInterface = retrofit.create(NewsTeeApiInterface.class);
    private NewsLab(Context appContext) {
        mAppContext = appContext;
        loadNews();
    }

    public static NewsLab getInstance(Context context){
        if (sNewsLab == null) {
            sNewsLab = new NewsLab(context.getApplicationContext());
        }
        return sNewsLab;
    }
    public void loadNews()
    {
        NewsAsyncTask task = new NewsAsyncTask();
       task.execute();
    }
    public List<News> getNews() {
        return mNews;
    }
    public void setNews(List<News> News) {
        this.mNews = News;
    }
    public News getNewsItem(String id) {
        for (News n : mNews) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }

    private  class NewsAsyncTask extends AsyncTask<String,Integer, DataNews>

    {
        ProgressDialog pDialog;


        @Override
        protected DataNews doInBackground(String... params) {

            Call<DataNews> call = newsTeeApiInterface.getNews();
            DataNews dataNews = new DataNews();
            try {
                Response<DataNews> response = call.execute();
                dataNews = response.body();
                mNews = dataNews.getNews();
               
            /*    if(songId.equals("3") )
                {
                    songId = "2";
                }
                for(News a : News)
                {
                    if(a.getId().equals(songId))
                    {
                        songUrl = a.getSource();
                        break;
                    }
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dataNews;
        }

        @Override
        protected void onPreExecute() {
            //     player.reset();
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(mAppContext);
            pDialog.setMessage(mAppContext.getString(R.string.loadingNews));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();       }

        @Override
        protected void onPostExecute( DataNews dataNews) {
            super.onPostExecute(dataNews);
            Toast toast = Toast.makeText(mAppContext, dataNews.getResult(), Toast.LENGTH_SHORT);
            //      pDialog.dismiss();
            //  Uri trackUri =Uri.parse(songUrl);
       /*         ContentUris.withAppendedId(
                android.provider.MediaStore.News.Media.EXTERNAL_CONTENT_URI,
                currSong);*/
            //set the data source
      /*      try{

                player.setDataSource(songUrl);
                player.prepare();
                player.start();
            }
            catch(Exception e){
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }

*/


        }
    }
}