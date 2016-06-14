package com.newstee.model.data;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.newstee.Constants;
import com.newstee.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class UserLab {

    private final static String LIKED_FILENAME = "liked.json";
    private final static String JSON_LIKED = "liked_news";
    private boolean isUpdated = false;
    private final static String TAG = "UserLab";
    public static boolean isLogin = false;
    public static boolean isFirstRun = true;
    private List<News> mAddedNews = new ArrayList<News>();
    private List<News> mLikedNews = new ArrayList<News>();
    private List<Tag> mAddedTags = new ArrayList<>();
    public boolean isUpdated() {
        return isUpdated;
    }
    //private Context mAppContext;
    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
    public List<News> getAddedNews() {
        return mAddedNews;
    }
    public News getAddedNewsItem(String idNews) {
        for (News n : mAddedNews) {
            if (n.getId().equals(idNews)) {
                return n;
            }
        }
        return null;
    }
    public void setAddedNews(List<News> addedNews) {

        mAddedNews.clear();
        mAddedNews.addAll(addedNews);
        deleteNullNews(mAddedNews);
    }

    public List<News> getLikedNews() {
        return mLikedNews;
    }

    public void setLikedNews(List<News> likedNews) {
        mLikedNews.clear();
        mLikedNews.addAll(likedNews);
        deleteNullNews(mLikedNews);

    }

    public List<Tag> getAddedTags() {
        return mAddedTags;
    }

    public void setAddedTags(List<Tag> addedTags) {
        mAddedTags.clear();
        mAddedTags.addAll(addedTags);

    }


   /* private List<Long> mAddedNewsIds = new ArrayList<Long>();
    private List<Long> mLikedNewsIds = new ArrayList<Long>();
    private List<Long> mAddedTagsIds = new ArrayList<Long>();*/

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
        //  initializeData();
    }

    /*private void initializeData() {
        parseData(mAddedNewsIds, mUser.getNewsAddedIds());
        parseData(mLikedNewsIds, mUser.getNewsLikedIds());
        parseData(mAddedTagsIds, mUser.getTagsIds());
    }
*/
    private void parseData(List<Long> values, @Nullable String stringValue) {

        if (stringValue == null) {
            return;
        }
        String addedNewsString = stringValue;
        String mas[] = addedNewsString.split(",");
        for (int i = 0; i < mas.length; i++) {
            mas[i].trim();
            try {
                long value = Long.parseLong(mas[i]);
                values.add(Long.valueOf(value));
            } catch (NumberFormatException n) {

            }
        }
    }

    private User mUser = new User();
    private static UserLab sUserLab;

    private UserLab( ) {


    }

    public static UserLab getInstance() {
        if (sUserLab == null) {
            sUserLab = new UserLab();
        }
        return sUserLab;
    }

    public void addNews(News news) {
        String id = news.getId().trim();

        News n = null;
        for (News n2 : mAddedNews) {
            if (n2.getId().trim().equals(id)) {
                n = n2;
                break;
            }
        }
        if (n == null) {

            mAddedNews.add(news);
        } else {
            mAddedNews.remove(n);
        }
    }

    public void addTag(Tag tag) {
        String id = tag.getId().trim();

        Tag t = null;
        for (Tag t2 : mAddedTags) {
            if (t2.getId().trim().equals(id)) {
                t = t2;
                break;
            }
        }
        if (t == null) {

            mAddedTags.add(tag);
        } else {
            mAddedTags.remove(t);
        }

    }

    public boolean likeNews(News news, Context context) {
        String id = news.getId().trim();
        if (new SessionManager(context).isLoggedIn()) {
            News n = null;
            for (News n2 : mLikedNews) {
                if (n2.getId().trim().equals(id)) {
                    n = n2;
                    break;
                }
            }
            if (n == null) {

                mLikedNews.add(news);
            } else {
                mLikedNews.remove(n);
            }
        } else {
            if (likeNewsToDevice(id, context))
            {
                mLikedNews.add(news);
            }
            else
            {
                return false;
            }
        }
return true;
    }

    private boolean likeNewsToDevice(String id,Context context) {
        boolean isContent = false;
        String likedIds = new SessionManager(context).getLikedIds();
        String likedIdsArray[] = likedIds.split(",");
        for (int i = 0; i < likedIdsArray.length; i++) {
            if (likedIdsArray[i].equals(id)) {
                isContent = true;
            }
        }
        if (!isContent) {
            if (likedIdsArray.length == 0) {
                likedIds = id;
            } else {
                likedIds = likedIds.concat("," + id);
            }
            saveLikedNewsToDevice(likedIds, context);
            return true;
        }
return false;

    }


    public List<News> getAddedNewsAndArticles() {
        List<News> news = new ArrayList<>();
        for (News n : mAddedNews) {
            if (!n.getCategory().equals(Constants.CATEGORY_STORY)) {
                news.add(n);
            }
        }
        return news;
    }


    public boolean isAddedNews(String newsId) {
        String s = newsId.trim();
        int i = 0;
        for (News news : mAddedNews) {
            i++;
            if (news == null) {
                Log.d(TAG, "@@@@@@@@@@@@@ news = null " + i);
                continue;
            }
            if ((news.getId().trim()).equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAddedTag(String tagId) {
        String s = tagId.trim();
        for (Tag tag : mAddedTags) {

            if ((tag.getId().trim()).equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLikedNews(String newsId) {
        String s = newsId.trim();
        for (News news : mLikedNews) {

            if ((news.getId().trim()).equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void deleteData() {
        mUser = new User();
        mAddedNews.clear();
        mLikedNews.clear();
        mAddedTags.clear();
        isUpdated = false;
    }

    /* public void signIn(Context c)
      {

      }
      public void signOff(Context c)
      {

      }
      public void logIn(Context c)
      {

      }*/
    public void deleteNullNews(List<News> news) {
        List<News> nullNews = new ArrayList<>();
        for (News n : news) {
            if (n == null) {
                nullNews.add(n);
            }
        }
        news.removeAll(nullNews);
    }
    public String getLikedNewsFromDevice(Context context) {
        return new SessionManager(context).getLikedIds();
    }
      /*  NewsIntentJSONSerializer serializer = new NewsIntentJSONSerializer(mAppContext, LIKED_FILENAME);
        String likedIdList = "";
       // LinkedHashSet<String>likedIdList = new LinkedHashSet<>();
        try {
            JSONArray array = serializer.loadLkedNewsId();
            for (int i = 0; i < array.length(); i++) {
                if(i == 0)
                {

                }
                likedIdList = likedIdList.concat(","+(array.getJSONObject(i)).getString(JSON_LIKED));
                likedIdList.add();
            }
            Log.d(TAG, "loading reminders: ");
        } catch (Exception e) {
            Log.e(TAG, "Error loading reminders: ", e);
        }
        String s[];

        String s = s.
        return likedIdList;

    }*/
    private void saveLikedNewsToDevice(String likedIds, Context context)
    {
        new SessionManager(context).setLkedIds(likedIds);
    }
        /*NewsIntentJSONSerializer serializer = new NewsIntentJSONSerializer(mAppContext, LIKED_FILENAME);
        JSONArray array = new JSONArray();
        Iterator<String> iterator = lkedIdList.iterator();
        try
        {
            while(!lkedIdList.isEmpty())
            {
                JSONObject json = new JSONObject();
                json.put(JSON_LIKED, iterator.next().toString());
                array.put(json);
                iterator.remove();
            }
        }catch(JSONException e)
        {
        }

        try {
            serializer.saveLkedNewsId(array);
            Log.d(TAG, "snoozedIdList saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving snoozedIdList: ", e);
            return false;
        }
    }*/

}