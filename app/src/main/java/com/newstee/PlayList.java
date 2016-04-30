package com.newstee;

import com.newstee.model.data.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnold on 29.04.2016.
 */
public class PlayList {

    private List<News>mNewsList = new ArrayList<>();
    private News mCurrent;

    public List<News> getNewsList() {
        return mNewsList;
    }
    public int getPosition(String url)
    {
        if(url == null)
        {
            return -1;
        }
        String s = url.trim();
        for(News n : mNewsList)
        {
            if(n.getLinksong()== null)
            {
                continue;
            }
           if(n.getLinksong().trim().equals(s))
           {
               return mNewsList.indexOf(n);
           }
        }
        return  -1;
    }

    public void setNewsList(List<News> mNewsList) {
        this.mNewsList = mNewsList;
    }
    public boolean contains(News news)
    {
        if(mNewsList.contains(news))
        {
            return true;
        }
        else
        {
            for(News n : mNewsList )
            {
                if(n.getId().equals(news.getId()))
                {
                   return true;
                }
            }
        }
        return false;
    }

    public void addToPlayList(News news)
    {
       if(!contains(news))
       {
           mNewsList.add(news);
       }
    }



    public void deleteFromPlaylist(News news)
    {
        if(!mNewsList.remove(news))
        {
            for(News n : mNewsList )
            {
                if(n.getId().equals(news.getId()))
                {
                    mNewsList.remove(n);
                }
            }
        }
    }
    private PlayList()
    {

    }

    private static PlayList sPlayList;
    public static PlayList getInstance()
    {
        if(sPlayList == null)
        {
            sPlayList = new PlayList();
        }

            return sPlayList;
    }

    public News getCurrent() {
        return mCurrent;
    }

    public void setCurrent(News current) {
        this.mCurrent = current;
    }
}
