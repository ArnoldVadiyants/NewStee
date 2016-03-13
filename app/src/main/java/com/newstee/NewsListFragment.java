package com.newstee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsListFragment extends ListFragment {
    private final static String TAG = "NewsListFragment";
    private  final List<Item> items = new ArrayList<Item>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     Bitmap canalIcon  = BitmapFactoCatalogFragmentry.decodeResource(getActivity().getResources(),
   //             R.drawable.test_canal_icon);
                //getRoundedShape();
   //     Bitmap newPicture  = BitmapFactory.decodeResource(getActivity().getResources(),
      //          R.drawable.test_picture);
        Bitmap canalIcon = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        Bitmap newPicture = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
       items.add(new Item(canalIcon, newPicture, "UkraineNews", 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.",News.STATUS_IS_PLAYING));
        for(int i =0; i<50; i++)
        {
            items.add(new Item(canalIcon, newPicture, "Vazgen.com", 777, 100747, "In the Democratic contest, Hillary Clinton beat Vermont Senator Bernie Sanders in a tight race in Nevada.", News.STATUS_NOT_ADDED));
            items.add(new Item(canalIcon, newPicture, "UkraineNews", 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", News.STATUS_NOT_ADDED));
            items.add(new Item(canalIcon, newPicture, "NightAmerica", 100, 351237, "Donald Trump has won the South Carolina primary in the Republican race for president.",News.STATUS_WAS_ADDED));
            items.add(new Item(canalIcon, newPicture, "UkraineNews", 123, 13257, "Will be key ahead of the  Super Tuesday round on 1 March, when a dozen more states make their choice.", News.STATUS_NOT_ADDED));
        }

    }

    private  class Item {
        public final Bitmap canalImage;
        public final Bitmap newsImage;
        public final String canalTitle;
        public final int likeCount;
        public final long millisecondsDuring;
        public final String description;
        public final int status;


        public Item(Bitmap canalImage, Bitmap newsImage, String canalTitle, int likeCount, long millisecondsDuring, String description, int status) {
            this.canalImage = canalImage;
            this.newsImage = newsImage;
            this.canalTitle = canalTitle;
            this.likeCount = likeCount;
            this.millisecondsDuring = millisecondsDuring;
            this.description = description;
            this.status = status;
        }
    }

    private  class ViewHolder {
        public final RelativeLayout newsFeed;
        public final ImageView canalImage;
        public final ImageView newsImage;
        public final TextView canalTitle;
        public final TextView likeCount;
        public final TextView time;
        public final TextView description;
        public final ImageButton statusButton;


        public ViewHolder(ImageView canalImage, ImageView newsImage, TextView canalTitle, TextView likeCount, TextView time, TextView description, ImageButton statusButton, RelativeLayout newsFeed) {
            this.canalImage = canalImage;
            this.newsImage = newsImage;
            this.canalTitle = canalTitle;
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
               likeCount = (TextView) view.findViewById(R.id.like_count_TextView);
                time = (TextView) view.findViewById(R.id.date_TextView);
                 description = (TextView) view.findViewById(R.id.news_description_textView);
                 statusButton = (ImageButton)view.findViewById(R.id.news_status_ImageButton);
                view.setTag(new ViewHolder(canalImage, newsImage, canalTitle, likeCount, time, description, statusButton, newsFeed));
            }
            if (holder == null && view != null) {
                Object tag = view.getTag();
                if (tag instanceof ViewHolder) {
                    holder = (ViewHolder) tag;
                }
            }
            Item item = getItem(position);
            if (item != null && holder != null) {
        //       holder.canalImage.setImageBitmap(item.canalImage);
          //      holder.newsImage.setImageBitmap(item.newsImage);
                holder.canalTitle.setText(item.canalTitle);
                holder.likeCount.setText("" + item.likeCount);
             //   holder.time.setText(""+item.millisecondsDuring);
                holder.description.setText(item.description);
                holder.newsFeed.setTag(position);
               holder.newsFeed.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       startActivity(new Intent(getContext(), MediaPlayerFragmentActivity.class));

                       Log.d(TAG, "List_item onCLick" + " " + (v.getTag()));
                   }
               });
                holder.statusButton.setTag(position);
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
        int targetWidth = 50;
        int targetHeight = 50;
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

  public  abstract void setStatusImageButton(ImageButton statusImageButton,final int newsStatus);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_listview, container, false);

        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListAdapter adapter = new ItemAdapter(getActivity());
        setListAdapter(adapter);
        TextView tv = (TextView)getListView().getEmptyView();
        //    TextView tv = (TextView)view.findViewById(R.id.empty);
        tv.setText(getEmpty());
    }
    abstract String getEmpty();
}
