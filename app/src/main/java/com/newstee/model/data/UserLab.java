package com.newstee.model.data;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class UserLab {
    public List<News> getAddedNews() {
        return mAddedNews;
    }

    public void setAddedNews(List<News> mAddedNews) {
        this.mAddedNews = mAddedNews;
    }

    public List<News> getLikedNews() {
        return mLikedNews;
    }

    public void setLikedNews(List<News> mLikedNews) {
        this.mLikedNews = mLikedNews;
    }

    public List<Tag> getAddedTags() {
        return mAddedTags;
    }

    public void setAddedTags(List<Tag> mAddedTags) {
        this.mAddedTags = mAddedTags;
    }

    private List<News> mAddedNews = new ArrayList<News>();
    private List<News> mLikedNews = new ArrayList<News>();
    private List<Tag> mAddedTags = new ArrayList<Tag>();
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

        if(stringValue == null)
        {
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

    public void addNews(News news)
    {
        String id = news.getId().trim();

        News n = null;
        for (News n2 : mAddedNews) {
            if (n2.getId().trim().equals(id)) {
                n=n2;
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
    public void likeNews(News news)
    {
        String id = news.getId().trim();

        News n = null;
        for (News n2 : mLikedNews) {
            if (n2.getId().trim().equals(id)) {
              n=n2;
                break;
            }
        }
        if (n == null) {

            mLikedNews.add(news);
        } else {
            mAddedTags.remove(n);
        }
    }

  public boolean isAddedNews(String newsId) {
        String s = newsId.trim();
        for(News news: mAddedNews)
        {

            if((news.getId().trim()).equals(s))
            {
                return true;
            }
        }
       return false;
    }

    public boolean isAddedTag(String tagId) {
        String s = tagId.trim();
        for(Tag tag: mAddedTags)
        {

            if((tag.getId().trim()).equals(s))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isLikedNews(String   newsId) {
        String s = newsId.trim();
        for(News news: mLikedNews)
        {

            if((news.getId().trim()).equals(s))
            {
                return true;
            }
        }
        return false;
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


}