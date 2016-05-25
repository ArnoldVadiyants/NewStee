package com.newstee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newstee.helper.InternetHelper;
import com.newstee.helper.SessionManager;
import com.newstee.model.data.Author;
import com.newstee.model.data.AuthorLab;
import com.newstee.model.data.DataNews;
import com.newstee.model.data.DataPost;
import com.newstee.model.data.News;
import com.newstee.model.data.NewsLab;
import com.newstee.model.data.UserLab;
import com.newstee.network.FactoryApi;
import com.newstee.network.interfaces.NewsTeeApiInterface;
import com.newstee.utils.DisplayImageLoaderOptions;
import com.newstee.utils.MPUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NewsListFragment extends SwipeRefreshListFragment {


    private ArrayList<String> mFilterTagIds;
    private String mCategory;
    private String mArgument;
    private String mIdStory;
    private SessionManager session;
    private boolean update = false;
    protected static final String ARG_STORY= "story";
    protected static final String ARG_CATEGORY = "category";
    protected static final String ARG_PARAMETER = "parameter";

    static ImageLoader imageLoader = ImageLoader.getInstance();
    private final static String TAG = "NewsListFragment";
    private List<News> mNews = new ArrayList<>();
    private List<News>mNewsFiltered = new ArrayList<>();
    ItemAdapter adapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Log.d("@@@@@@ " + TAG, isVisibleToUser + "" + " category " + mCategory + " argument " + mArgument);
            if(update)
            {
                update = false;
                return;
            }
            if (adapter == null) {
                Log.d("@@@@@@ " + TAG," adapter = null");
                return;
            }
            updateFragment();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
      if (adapter == null) {
            return;
        }
        updateFragment();
        update=true;
    }
    public ArrayList<String> getFilterTagIds() {
        return mFilterTagIds;
    }

    public void setFilterTagIds(ArrayList<String> filterTagIds) {
        this.mFilterTagIds = filterTagIds;
    }
    public void applyFilter()
    {

        mNewsFiltered.clear();
        if(mFilterTagIds == null ){
            mNewsFiltered.addAll(mNews);

    }
        else
        {
            for(News n: mNews)
            {
                List<String> tags = n.getIdTags();
                for(String s : tags)
                {
                    if(mFilterTagIds.contains(s))
                    {
                        mNewsFiltered.add(n);
                        break;
                    }
                }
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    public void updateFragment() {
        mNews.clear();
        List<News> news = new ArrayList<>();
        if (mArgument.equals(Constants.ARGUMENT_NONE))
        {
            news = NewsLab.getInstance().getNews();
        }
        else if(mArgument.equals(Constants.ARGUMENT_NEWS_ADDED))
        {
            news = UserLab.getInstance().getAddedNews();
        }
        else if(mArgument.equals(Constants.ARGUMENT_NEWS_LIKED))
        {
            news = UserLab.getInstance().getLikedNews();
        }
        else if(mArgument.equals(Constants.ARGUMENT_NEWS_BY_CANAL))
        {

        }
        else if(mArgument.equals(Constants.ARGUMENT_NEWS_BY_STORY))
        {
            new LoadNewsByStoryTask().execute();
            return;
        }

        if(mCategory.equals(Constants.CATEGORY_ALL))
        {int i =0;
            for (News n : news) {
                i++;
                if(n == null)
                {
                    Log.d(TAG, "@@@@@@@@@@@@@ n = null "+i);
                    continue;
                }
                mNews.add(n);
            }
        }
        else
        {
            int i =0;
            for (News n : news) {
                i++;
                if(n == null)
                {
                    Log.d(TAG, "@@@@@@@@@@@@@ n = null "+i);
                    continue;
                }
                if(n.getCategory() == null)
                {
                    Log.d(TAG, "@@@@@@@@@@@@@ category = null"+i);
                    continue;
                }
                if (n.getCategory().equals(mCategory))
                {
                    mNews.add(n);
                }
            }
        }
        applyFilter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session   = new SessionManager(getActivity());
        mCategory = getArguments().getString(ARG_CATEGORY, Constants.CATEGORY_NEWS);
        mArgument = getArguments().getString(ARG_PARAMETER, Constants.ARGUMENT_NONE);
        mIdStory = getArguments().getString(ARG_STORY);



    }



    /**
     * The fragment argument representing the section number for this
     * fragment.
     */


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */






   /* class MyAsyncTask extends AsyncTask<String,Integer, NewsLab>

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
                  *//*      items.add(new Item(canalIcon, newPicture, "Vazgen.com", false, 777, 100747, "In the Democratic contest, Hillary Clinton beat Vermont Senator Bernie Sanders in a tight race in Nevada.", Constants.STATUS_NOT_ADDED));
                        items.add(new Item(canalIcon, newPicture, "UkraineNews", true, 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", Constants.STATUS_NOT_ADDED));
                        items.add(new Item(canalIcon, newPicture, "NightAmerica",false, 100, 351237, "Donald Trump has won the South Carolina primary in the Republican race for president.", Constants.STATUS_WAS_ADDED));
                        items.add(new Item(canalIcon, newPicture, "UkraineNews", true, 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", Constants.STATUS_NOT_ADDED));
                *//*    }
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
    }*/

   /* private  class Item {
        public final String canalImage;
        public final String newsImage;
        public final String canalTitle;
        public final boolean isLiked;
        public final String likeCount;
        public final long millisecondsDuring;
        public final String description;
        public final int status;
        public final String audioId;


        public Item(String canalImage, String newsImage, String canalTitle,boolean isLiked, String likeCount, long millisecondsDuring, String description, int status, String audioId) {
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
*/
    private class ViewHolder {
        public final LinearLayout newsHeader;
        public final RelativeLayout newsFeed;
        public final ImageView canalImage;
        public final ImageView newsImage;
        public final TextView canalTitle;
        public final ImageView likeView;
        public final TextView likeCount;
        public final TextView time;
        public final TextView description;
        public final ImageButton statusButton;


        public ViewHolder(ImageView canalImage, ImageView newsImage, TextView canalTitle, ImageView likeView, TextView likeCount, TextView time, TextView description, ImageButton statusButton, RelativeLayout newsFeed, LinearLayout newsHeader) {
            this.canalImage = canalImage;
            this.newsImage = newsImage;
            this.canalTitle = canalTitle;
            this.likeView = likeView;
            this.likeCount = likeCount;
            this.time = time;
            this.description = description;
            this.statusButton = statusButton;
            this.newsFeed = newsFeed;
            this.newsHeader = newsHeader;
        }
    }

    private class ItemAdapter extends ArrayAdapter<News> {
        LinearLayout newsHeader;
        RelativeLayout newsFeed;
        ImageView canalImage;
        ImageView newsImage;
        TextView canalTitle;
        ImageView likeView;
        TextView likeCount;
        TextView time;
        TextView description;
        ImageButton statusButton;

        public ItemAdapter(Context context) {
            super(context, R.layout.news_list_item,mNewsFiltered);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
                newsHeader= (LinearLayout) view.findViewById(R.id.news_header);
                newsFeed = (RelativeLayout) view.findViewById(R.id.news_feed);
                canalImage = (ImageView) view.findViewById(R.id.canal_icon_ImageVew);
                newsImage = (ImageView) view.findViewById(R.id.news_picture_imageView);
                canalTitle = (TextView) view.findViewById(R.id.canal_title_TextView);
                likeView = (ImageView) view.findViewById(R.id.like_ImageView);
                likeCount = (TextView) view.findViewById(R.id.like_count_TextView);
                time = (TextView) view.findViewById(R.id.news_date_TextView);
                description = (TextView) view.findViewById(R.id.news_description_textView);
                statusButton = (ImageButton) view.findViewById(R.id.news_status_ImageButton);
                view.setTag(new ViewHolder(canalImage, newsImage, canalTitle, likeView, likeCount, time, description, statusButton, newsFeed, newsHeader));
            }
            if (holder == null && view != null) {
                Object tag = view.getTag();
                if (tag instanceof ViewHolder) {
                    holder = (ViewHolder) tag;
                }
            }


            final News item = getItem(position);
            if (item != null && holder != null) {
                int textColor = getTextColor();
                if (mCategory.equals(Constants.CATEGORY_STORY)) {
                    holder.newsHeader.setVisibility(View.GONE);
                    holder.description.setText(item.getTitle() + '\n' + item.getContent());
                    holder.description.setTextColor(textColor);
                    imageLoader.displayImage(item.getPictureNews(), holder.newsImage, DisplayImageLoaderOptions.getInstance());
                    holder.newsFeed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           Intent i = new Intent(getContext(), StoryNewsActivity.class);
                            i.putExtra(StoryNewsActivity.ARG_STORY_ID, item.getId());
                            i.putExtra(StoryNewsActivity.ARG_STORY_TITLE, item.getTitle());
                            startActivity(i);

                            Log.d(TAG, "List_item onCLick" + " " + (v.getTag()));
                        }
                    });
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.newsFeed.getLayoutParams();
                    params.setMargins(0, 0, 52, 0);
                    holder.newsFeed.setLayoutParams(params);
                    FrameLayout.LayoutParams paramsBtn = (FrameLayout.LayoutParams) holder.statusButton.getLayoutParams();
                    paramsBtn.setMargins(0, 0, 0, 0);
                    holder.statusButton.setLayoutParams(paramsBtn);
                } else {
                    Author author = AuthorLab.getInstance().getAuthor(item.getIdauthor());
                    if (author == null) {
                        author = new Author();
                        author.setAvatar(Constants.EMPTY_LINK_IMAGE);
                        author.setId("-1");
                        author.setName("No canal");
                    }
                    String authorAvatar = author.getAvatar();
                    String authorName = author.getName();
                    if (authorAvatar == null) {
                        authorAvatar = Constants.EMPTY_LINK_IMAGE;

                    }
                    if(authorName == null)
                    {
                        authorName = "No canal";
                    }
                    imageLoader.displayImage(item.getPictureNews(), holder.newsImage, DisplayImageLoaderOptions.getInstance());
                    imageLoader.displayImage(authorAvatar, holder.canalImage, DisplayImageLoaderOptions.getRoundedInstance());
                    //     Picasso.with(getActivity())
                    //           .load(item.newsImage)
                    //         .into( holder.newsImage);
                    //       holder.canalImage.setImageBitmap(item.canalImage);
                    //      holder.newsImage.setImageBitmap(item.newsImage);
                    holder.canalTitle.setText(authorName);
                    holder.likeCount.setText(item.getLikes());
                    holder.canalTitle.setTextColor(getTextColor());
                    holder.time.setText(new MPUtilities().getDateOrTimeFormat(item.getAdditionTime()));
                    holder.time.setTextColor(textColor);
                    holder.description.setText(item.getTitle() + '\n' + item.getContent());
                    holder.description.setTextColor(textColor);
                    holder.newsFeed.setTag(position);
                    final String newsId = (item.getId()).trim();
                    holder.newsFeed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(session.isLoggedIn())
                            {
                                if(!UserLab.getInstance().isAddedNews(newsId))
                                {
                                    NewsTeeApiInterface nApi = FactoryApi.getInstance(getActivity());
                                    Call<DataPost> call = nApi.addNews(newsId);
                                    call.enqueue(new Callback<DataPost>() {
                                        @Override
                                        public void onResponse(Call<DataPost> call, Response<DataPost> response) {
                                            if (response.body().getResult().equals(Constants.RESULT_SUCCESS)) {
                                                UserLab.getInstance().addNews(NewsLab.getInstance().getNewsItem(newsId));
                                            } else {
                                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            PlayList.getInstance().setNewsList(UserLab.getInstance().getAddedNewsAndArticles());
                                            int status = Constants.STATUS_NOT_ADDED;
                                            if (UserLab.getInstance().isAddedNews(newsId)) {
                                                status = Constants.STATUS_WAS_ADDED;
                                            }
                                            setStatusImageButton(statusButton, status);
                                            Intent i = new Intent(getActivity(), MediaPlayerFragmentActivity.class);
                                            i.putExtra(MediaPlayerFragmentActivity.ARG_AUDIO_ID, newsId);
                                            startActivity(i);

                                        }

                                        @Override
                                        public void onFailure(Call<DataPost> call, Throwable t) {
                                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                else
                                {
                                    PlayList.getInstance().setNewsList(UserLab.getInstance().getAddedNewsAndArticles());
                                    Intent i = new Intent(getActivity(), MediaPlayerFragmentActivity.class);
                                    i.putExtra(MediaPlayerFragmentActivity.ARG_AUDIO_ID, newsId);
                                    startActivity(i);
                                }
                            }
                            else
                            {
                                PlayList.getInstance().setNewsList(NewsLab.getInstance().getNewsAndArticles());
                                Intent i = new Intent(getActivity(), MediaPlayerFragmentActivity.class);
                                i.putExtra(MediaPlayerFragmentActivity.ARG_AUDIO_ID, newsId);
                                startActivity(i);
                            }


                        }
                    });
                    //holder.statusButton.setTag(position);
                    setLikeView(holder.likeCount, holder.likeView, UserLab.getInstance().isLikedNews((item.getId()).trim()));

                    //  getTime(item.millisecondsDuring)
                }
            //    holder.statusButton.setTag(position);
                final String newsId = (item.getId()).trim();
                holder.statusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (!InternetHelper.getInstance(getActivity()).isOnline()) {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.check_internet_con), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(!session.isLoggedIn())
                        {
                            Toast.makeText(getContext(),"Пожалуйста, авторизуйтесь", Toast.LENGTH_SHORT).show();
                            return;

                        }
                        UserLab.getInstance().addNews(NewsLab.getInstance().getNewsItem(newsId));
                        int status = Constants.STATUS_NOT_ADDED;
                        if (UserLab.getInstance().isAddedNews(newsId)) {
                            status = Constants.STATUS_WAS_ADDED;
                        }
                        setStatusImageButton((ImageButton) v, status);

                        NewsTeeApiInterface nApi = FactoryApi.getInstance(getActivity());
                        Call<DataPost> call = nApi.addNews(newsId);
                        call.enqueue(new Callback<DataPost>() {
                            @Override
                            public void onResponse(Call<DataPost> call, Response<DataPost> response) {
                                if (response.body().getResult().equals(Constants.RESULT_SUCCESS)) {


                                } else {
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DataPost> call, Throwable t) {
                                Toast.makeText(getContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


                int status = Constants.STATUS_NOT_ADDED;
                if (UserLab.getInstance().isAddedNews(newsId)) {
                    status = Constants.STATUS_WAS_ADDED;
                }
                setStatusImageButton(holder.statusButton, status);

            }
            return view;
        }

        private View.OnClickListener mClickListenerItem = new View.OnClickListener() {

            public void onClick(View v) {
                if (v.getId() == statusButton.getId()) {

                } else {

                }

            }
        };

    }
private List<News> loadNewsByStory()
{
    if(mIdStory == null)
    {
        Log.d(TAG,"@@@@@ idStory = "+ null);
        return new ArrayList<>();
    }
    NewsTeeApiInterface api = FactoryApi.getInstance(getActivity());
    Call<DataNews> newsC = api.getNewsByStory(mIdStory);
    Log.d(TAG,"@@@@@ idStory = "+ mIdStory);
    try {
        Response<DataNews>  newsR = newsC.execute();

    return   newsR.body().getNews();


    } catch (IOException e) {
        e.printStackTrace();
    }
    return new ArrayList<>();
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

    abstract void setStatusImageButton(ImageButton statusImageButton, final int newsStatus);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.news_listview, container, false);

            return view;
        }*/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        adapter = new ItemAdapter(getActivity());
        setListAdapter(adapter);


       /* TextView     eptyTextView = (TextView) getListView().getEmptyView();
        //    TextView tv = (TextView)view.findViewById(R.id.empty);
        eptyTextView.setText(getEmpty());
        eptyTextView.setTextColor(getTextColor());*/
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                new LoadAsyncTask(getActivity()) {
                    @Override
                    void hideContent() {

                    }

                    @Override
                    void showContent() {
                        updateFragment();

                        setRefreshing(false);
                    }
                }.execute();
            }
        });
        setColorScheme(R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    abstract String getEmpty();

    abstract int getTextColor();

    abstract void setLikeView(TextView likeTextView, ImageView likeImageView, boolean isLiked);
private class LoadNewsByStoryTask extends AsyncTask<Boolean,Integer, List<News> >
{
    ProgressDialog dialog;
    LoadNewsByStoryTask()
    {
        dialog  = new ProgressDialog(getActivity());
        dialog.setMessage("");
    }

    @Override
    protected List<News> doInBackground(Boolean... params) {

        return  loadNewsByStory();
    }



    @Override
    protected void onPostExecute(List<News> newses) {
        super.onPostExecute(newses);
        mNews.clear();
        mNews.addAll(newses);
        applyFilter();
        dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }


}

}
