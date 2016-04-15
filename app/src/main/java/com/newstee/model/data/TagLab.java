package com.newstee.model.data;

/**
 * Created by Arnold on 12.04.2016.
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


public class TagLab {

    private List<Tag> mTags = new ArrayList<>();
    private static TagLab sTagLab;
    private Context mAppContext;
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(NewsTeeApiInterface.BASE_URL)
            .build();
    private NewsTeeApiInterface newsTeeApiInterface = retrofit.create(NewsTeeApiInterface.class);
    private TagLab(Context appContext) {
        mAppContext = appContext;
        loadTags();
    }

    public static TagLab getInstance(Context context){
        if (sTagLab == null) {
            sTagLab = new TagLab(context.getApplicationContext());
        }
        return sTagLab;
    }
    public void loadTags()
    {
TagAsyncTask tagAsyncTask = new TagAsyncTask();
        tagAsyncTask.execute();
    }
    public List<Tag> getTags() {
        return mTags;
    }
    public void setTags(List<Tag> tags) {
        this.mTags = tags;
    }
    public Tag getTag(String id) {
        for (Tag t : mTags) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    private  class TagAsyncTask extends AsyncTask<String,Integer, DataTag>

    {
        ProgressDialog pDialog;


        @Override
        protected DataTag doInBackground(String... params) {

            Call<DataTag> call = newsTeeApiInterface.getTags();
            DataTag dataTag = new DataTag();
            try {
                Response<DataTag> response = call.execute();
                dataTag = response.body();
                mTags = dataTag.getData();
               
            /*    if(songId.equals("3") )
                {
                    songId = "2";
                }
                for(Tag a : Tag)
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

            return dataTag;
        }

        @Override
        protected void onPreExecute() {
            //     player.reset();
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(mAppContext);
            pDialog.setMessage(mAppContext.getString(R.string.loadingTags));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();       }

        @Override
        protected void onPostExecute( DataTag dataTag) {
            super.onPostExecute(dataTag);
            Toast toast = Toast.makeText(mAppContext, dataTag.getResult(), Toast.LENGTH_SHORT);
            //      pDialog.dismiss();
            //  Uri trackUri =Uri.parse(songUrl);
       /*         ContentUris.withAppendedId(
                android.provider.MediaStore.Tag.Media.EXTERNAL_CONTENT_URI,
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

