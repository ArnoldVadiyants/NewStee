package com.newstee.model.data;
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


public class AuthorLab {


    private List<Author> mAuthors = new ArrayList<>();
    private static AuthorLab sAuthorLab;
    private Context mAppContext;
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(NewsTeeApiInterface.BASE_URL)
            .build();
    private NewsTeeApiInterface newsTeeApiInterface = retrofit.create(NewsTeeApiInterface.class);
    private AuthorLab(Context appContext) {
        mAppContext = appContext;
        loadAuthors();
    }

    public static AuthorLab getInstance(Context context){
        if (sAuthorLab == null) {
            sAuthorLab = new AuthorLab(context.getApplicationContext());
        }
        return sAuthorLab;
    }
    public void loadAuthors()
    {
       AuthorAsyncTask task = new AuthorAsyncTask();
        task.execute();
    }
    public Author getAuthor(String id) {
        for (Author a : mAuthors) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }
    public List<Author> getAuthors() {
        return mAuthors;
    }
    public void setAuthors(List<Author> authors) {
        this.mAuthors = authors;
    }


    private  class AuthorAsyncTask extends AsyncTask<String,Integer, DataAuthor>

    {
        ProgressDialog pDialog;


        @Override
        protected DataAuthor doInBackground(String... params) {

            Call<DataAuthor> call = newsTeeApiInterface.getAuthors();
            DataAuthor dataAuthor = new DataAuthor();
            try {
                Response<DataAuthor> response = call.execute();
                dataAuthor = response.body();
                mAuthors = dataAuthor.getData();
               
            /*    if(songId.equals("3") )
                {
                    songId = "2";
                }
                for(Author a : Author)
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

            return dataAuthor;
        }

        @Override
        protected void onPreExecute() {
            //     player.reset();
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(mAppContext);
            pDialog.setMessage(mAppContext.getString(R.string.loadingAuthor));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();       }

        @Override
        protected void onPostExecute( DataAuthor dataAuthor) {
            super.onPostExecute(dataAuthor);
            Toast toast = Toast.makeText(mAppContext, dataAuthor.getResult(), Toast.LENGTH_SHORT);
            //      pDialog.dismiss();
            //  Uri trackUri =Uri.parse(songUrl);
       /*         ContentUris.withAppendedId(
                android.provider.MediaStore.Author.Media.EXTERNAL_CONTENT_URI,
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