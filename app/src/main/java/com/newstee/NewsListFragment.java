package com.newstee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newstee.model.data.News;
import com.newstee.model.data.NewsLab;
import com.newstee.network.interfaces.NewsTeeApiInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NewsListFragment extends ListFragment  {

    static ImageLoader imageLoader= ImageLoader.getInstance();
    static DisplayImageOptions loaderOptions =  new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.loading_animation)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
    private final static String TAG = "NewsListFragment";
    private  final List<Item> items = new ArrayList<Item>();
    ItemAdapter adapter;
     Thread threadA;
    ProgressDialog mProgressDialog;
    private  String BASE_URL = "http://213.231.4.68/audiotest/";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build();
    private NewsTeeApiInterface newsTeeApiInterface = retrofit.create(NewsTeeApiInterface.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     Bitmap canalIcon  = BitmapFactoCatalogFragmentry.decodeResource(getActivity().getResources(),
   //             R.drawable.test_canal_icon);
                //getRoundedShape();
   //     Bitmap newPicture  = BitmapFactory.decodeResource(getActivity().getResources(),
      //          R.drawable.test_picture);
        final Bitmap canalIcon = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        final Bitmap newPicture = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
      //  mProgressDialog.show();
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
     /* threadA  = new Thread(new Runnable() {
            @Override
            public void run() {
                Call<NewsLab> call = newsTeeApiInterface.getNews();
                try {
                    Response<NewsLab> response = call.execute();
                    List<News> news = response.body().getNews();
                   for(int i =0; i<50; i++)
                    {
                        for(News n: news)
                        {
                            final Bitmap[] newsAvatar = new Bitmap[1];

                            items.add(new Item(canalIcon,n.getAvatar() , "Vazgen.com", false, 777, 100747, n.getTitle(), Constants.STATUS_NOT_ADDED));

                            System.out.println("*************");
                            System.out.println("Id " + n.getId() +"avatar "+ n.getAvatar());
                        }
                  *//*      items.add(new Item(canalIcon, newPicture, "Vazgen.com", false, 777, 100747, "In the Democratic contest, Hillary Clinton beat Vermont Senator Bernie Sanders in a tight race in Nevada.", Constants.STATUS_NOT_ADDED));
                        items.add(new Item(canalIcon, newPicture, "UkraineNews", true, 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", Constants.STATUS_NOT_ADDED));
                        items.add(new Item(canalIcon, newPicture, "NightAmerica",false, 100, 351237, "Donald Trump has won the South Carolina primary in the Republican race for president.", Constants.STATUS_WAS_ADDED));
                        items.add(new Item(canalIcon, newPicture, "UkraineNews", true, 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", Constants.STATUS_NOT_ADDED));
                *//*    }
              //      mProgressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        threadA.start();*/




    }
    class MyAsyncTask extends AsyncTask<String,Integer, NewsLab>

    {
        ProgressDialog pDialog;


        @Override
        protected NewsLab doInBackground(String... params) {
            final Bitmap canalIcon = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Call<NewsLab> call = newsTeeApiInterface.getNews();
            NewsLab newsLab = new NewsLab();
            try {
                Response<NewsLab> response = call.execute();
                newsLab = response.body();
                List<News> news = newsLab.getNews();
                for(int i =0; i<50; i++)
                {
                    for(News n: news)
                    {
                        final Bitmap[] newsAvatar = new Bitmap[1];
                        int likeCount = 0;
                        try
                        {
                            likeCount = Integer.parseInt((n.getLikeCount().trim()));
                        }
                        catch (NumberFormatException nfe)
                        {
                        }

                        items.add(new Item(canalIcon,n.getAvatar() , "Vazgen.com", false, likeCount, 100747, n.getTitle(), Constants.STATUS_NOT_ADDED, n.getAudioIds()));

                        System.out.println("*************");
                        System.out.println("Id " + n.getId() +"avatar "+ n.getAvatar());
                    }
                  /*      items.add(new Item(canalIcon, newPicture, "Vazgen.com", false, 777, 100747, "In the Democratic contest, Hillary Clinton beat Vermont Senator Bernie Sanders in a tight race in Nevada.", Constants.STATUS_NOT_ADDED));
                        items.add(new Item(canalIcon, newPicture, "UkraineNews", true, 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", Constants.STATUS_NOT_ADDED));
                        items.add(new Item(canalIcon, newPicture, "NightAmerica",false, 100, 351237, "Donald Trump has won the South Carolina primary in the Republican race for president.", Constants.STATUS_WAS_ADDED));
                        items.add(new Item(canalIcon, newPicture, "UkraineNews", true, 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", Constants.STATUS_NOT_ADDED));
                */    }
                //      mProgressDialog.dismiss();

            } catch (IOException e) {
                e.printStackTrace();
            }




            return newsLab;
        }

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();        }

        @Override
        protected void onPostExecute(NewsLab newsLab) {
            super.onPostExecute(newsLab);
            pDialog.dismiss();
            if(adapter!=null)
            {
                adapter.notifyDataSetChanged();
            }

        }
    }

    private  class Item {
        public final Bitmap canalImage;
        public final String newsImage;
        public final String canalTitle;
        public final boolean isLiked;
        public final int likeCount;
        public final long millisecondsDuring;
        public final String description;
        public final int status;
        public final String audioId;


        public Item(Bitmap canalImage, String newsImage, String canalTitle,boolean isLiked, int likeCount, long millisecondsDuring, String description, int status, String audioId) {
            this.canalImage = canalImage;
            this.newsImage = newsImage;
            this.canalTitle = canalTitle;
            this.isLiked = isLiked;
            this.likeCount = likeCount;
            this.millisecondsDuring = millisecondsDuring;
            this.description = description;
            this.status = status;
            this.audioId = audioId;
        }
    }

    private  class ViewHolder {
        public final RelativeLayout newsFeed;
        public final ImageView canalImage;
        public final ImageView newsImage;
        public final TextView canalTitle;
        public final ImageView likeView;
        public final TextView likeCount;
        public final TextView time;
        public final TextView description;
        public final ImageButton statusButton;


        public ViewHolder(ImageView canalImage, ImageView newsImage, TextView canalTitle, ImageView likeView, TextView likeCount, TextView time, TextView description, ImageButton statusButton, RelativeLayout newsFeed) {
            this.canalImage = canalImage;
            this.newsImage = newsImage;
            this.canalTitle = canalTitle;
            this.likeView = likeView;
            this.likeCount = likeCount;
            this.time = time;
            this.description = description;
            this.statusButton = statusButton;
            this.newsFeed = newsFeed;
        }
    }

    private class ItemAdapter extends ArrayAdapter<Item> {
        RelativeLayout newsFeed;
        ImageView canalImage ;
        ImageView newsImage;
        TextView canalTitle ;
        ImageView likeView;
        TextView likeCount ;
        TextView time ;
        TextView description ;
        ImageButton statusButton;

        public ItemAdapter(Context context) {
            super(context, R.layout.news_list_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
                 newsFeed = (RelativeLayout)view.findViewById(R.id.news_feed);
                canalImage = (ImageView) view.findViewById(R.id.canal_icon_ImageVew);
                 newsImage = (ImageView) view.findViewById(R.id.news_picture_imageView);
                canalTitle = (TextView) view.findViewById(R.id.canal_title_TextView);
                likeView = (ImageView)view.findViewById(R.id.like_ImageView);
               likeCount = (TextView) view.findViewById(R.id.like_count_TextView);
                time = (TextView) view.findViewById(R.id.news_date_TextView);
                 description = (TextView) view.findViewById(R.id.news_description_textView);
                 statusButton = (ImageButton)view.findViewById(R.id.news_status_ImageButton);
                view.setTag(new ViewHolder(canalImage, newsImage, canalTitle,likeView, likeCount, time, description, statusButton, newsFeed));
            }
            if (holder == null && view != null) {
                Object tag = view.getTag();
                if (tag instanceof ViewHolder) {
                    holder = (ViewHolder) tag;
                }
            }
            final Item item = getItem(position);
            if (item != null && holder != null) {
                imageLoader.displayImage(item.newsImage,holder.newsImage, loaderOptions);
           //     Picasso.with(getActivity())
             //           .load(item.newsImage)
               //         .into( holder.newsImage);
        //       holder.canalImage.setImageBitmap(item.canalImage);
          //      holder.newsImage.setImageBitmap(item.newsImage);
                int textColor = getTextColor();
                holder.canalTitle.setText(item.canalTitle);
                holder.likeCount.setText("" + item.likeCount);
                holder.canalTitle.setTextColor(getTextColor());
             //   holder.time.setText(""+item.millisecondsDuring);
                holder.time.setTextColor(textColor);
                holder.description.setText(item.description);
                holder.description.setTextColor(textColor);
                holder.newsFeed.setTag(position);
                holder.newsFeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new  Intent(getContext(), MediaPlayerFragmentActivity.class);
                        i.putExtra(MediaPlayerFragmentActivity.ARG_AUDIO_ID, item.audioId);
                        startActivity(i);

                        Log.d(TAG, "List_item onCLick" + " " + (v.getTag()));
                   }
                });
                holder.statusButton.setTag(position);
                setLikeView(holder.likeCount, holder.likeView, item.isLiked);
               setStatusImageButton(holder.statusButton, item.status);
              //  getTime(item.millisecondsDuring)
            }
            return view;
        }
        private View.OnClickListener mClickListenerItem = new View.OnClickListener() {

            public void onClick(View v) {
                if(v.getId() ==  statusButton.getId())
                {

                }
                else
                {

                }

            }
        };

    }


    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

        int targetHeight= scaleBitmapImage.getHeight();
        int targetWidth =  scaleBitmapImage.getWidth();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;


        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }


    public String getTime(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

  abstract void setStatusImageButton(ImageButton statusImageButton,final int newsStatus);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_listview, container, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        adapter = new ItemAdapter(getActivity());
        setListAdapter(adapter);

        TextView tv = (TextView)getListView().getEmptyView();
        //    TextView tv = (TextView)view.findViewById(R.id.empty);
        tv.setText(getEmpty());
    }
    abstract String getEmpty();
    abstract int  getTextColor();
    abstract void setLikeView(TextView likeTextView, ImageView likeImageView, boolean isLiked);
}
