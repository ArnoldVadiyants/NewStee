package com.newstee.model.data;

import android.support.annotation.Nullable;
import android.util.Log;

import com.newstee.Constants;

import java.util.ArrayList;
import java.util.List;


public class UserLab {
    private boolean isUpdated = false;
    private final static String TAG = "UserLab";
    public static boolean isLogin = false;
    private List<News> mAddedNews = new ArrayList<News>();
    private List<News> mLikedNews = new ArrayList<News>();
    private List<Tag> mAddedTags = new ArrayList<>();
    public boolean isUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
    public List<News> getAddedNews() {
        return mAddedNews;
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

    private UserLab() {

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

    public void likeNews(News news) {
        String id = news.getId().trim();

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


}