package com.newstee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newstee.helper.SQLiteHandler;
import com.newstee.helper.SessionManager;
import com.newstee.model.data.User;
import com.newstee.model.data.UserLab;
import com.newstee.utils.DisplayImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by Arnold on 17.02.2016.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private Button editButton;
    private SessionManager session;
    private SQLiteHandler db;
    private boolean update = false;

ImageLoader imageLoader = ImageLoader.getInstance();
    LinearLayout profileInfo;
    ImageView backgroundImgView;
    ImageView avatarImgView;
    TextView usernameTitle;
    TextView name;
    TextView likes;
    TextView subscribes;

  private ProfilePagerAdapter mProfilePagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new SessionManager(getActivity());
        db = new SQLiteHandler(getActivity());
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    // private ViewPager mViewPager;
public void update()
{
    if(session.isLoggedIn())
    {profileInfo.setVisibility(View.VISIBLE);
        //    name.setText(UserLab.getInstance().getUser().getUserLogin());
        User u = UserLab.getInstance().getUser();
        String name = u.getUserLogin();
        if(name == null){
            HashMap<String, String> userData = db.getUserDetails();
            String username = userData.get(SQLiteHandler.KEY_NAME);
            if(username !=null)
            {
                name = username;
            }
            else
            {
                name = getString(R.string.tab_profile);
            }
        }
        usernameTitle.setText(name);
        likes.setText(""+UserLab.getInstance().getLikedNews().size());
        subscribes.setText(""+UserLab.getInstance().getAddedTags().size());
        String avatar = UserLab.getInstance().getUser().getAvatar();
        if(avatar != null)
        {
            imageLoader.displayImage(avatar, avatarImgView, DisplayImageLoaderOptions.getRoundedInstance());
            imageLoader.displayImage(avatar,backgroundImgView, DisplayImageLoaderOptions.getInstance());
        }
        Log.d(TAG, "@@@@@@ avatar "+ avatar);
    }
    else
    {
        usernameTitle.setText(R.string.tab_profile);
        name.setVisibility(View.GONE);
        profileInfo.setVisibility(View.GONE);
    }
}


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            update();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        profileInfo = (LinearLayout)rootView.findViewById(R.id.profile_info_layout);
        likes = (TextView)rootView.findViewById(R.id.profile_like_count);
        subscribes = (TextView)rootView.findViewById(R.id.profile_subscribes_count);
        usernameTitle = (TextView)rootView.findViewById(R.id.profile_username_title);
        name = (TextView)rootView.findViewById(R.id.profile_name_TextView);
        backgroundImgView= (ImageView)rootView.findViewById(R.id.profile_background_imageView);
        avatarImgView = (ImageView)rootView.findViewById(R.id.profile_avatar_imageView);

       /* BitmapFactory.Options o=new BitmapFactory.Options();
                o.inSampleSize = 4;
                o.inDither=false;                     //Disable Dithering mode
                o.inPurgeable=true;
                avatarImgView.setImageBitmap(new CircleTransform().transform(BitmapFactory.decodeResource(getResources(), R.drawable.avatar_test, o)));
               */ editButton = (Button)rootView.findViewById(R.id.profile_edit_btn);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().startActivity(new Intent(getContext(), EditProfileActivity.class));
                        Toast.makeText(getActivity(), "edit profile clicked", Toast.LENGTH_SHORT).show();
                    }
                });
       update();
         mProfilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.profile_container);
        mViewPager.setAdapter(mProfilePagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(mViewPager);
       /* View article_view =  rootView.findViewById(R.id.theme_article_item);
            article_icon = (ImageView) article_view.findViewById(R.id.item_icon);
            article_titleTextView = (TextView) article_view.findViewById(R.id.item_title);
        article_titleTextView.setText(R.string.theme_article);
            article_relative = (RelativeLayout) article_view.findViewById(R.id.item_action);

        View publish_view =  rootView.findViewById(R.id.publish);
            publish_icon = (ImageView) publish_view.findViewById(R.id.item_icon);
            publish_titleTextView = (TextView) publish_view.findViewById(R.id.item_title);
        publish_titleTextView.setText(R.string.publish);
            publish_relative = (RelativeLayout) publish_view.findViewById(R.id.item_action);
        View mixes_view =  rootView.findViewById(R.id.new_mixes);
            mixes_icon = (ImageView) mixes_view.findViewById(R.id.item_icon);
            mixes_titleTextView = (TextView)mixes_view.findViewById(R.id.item_title);
        mixes_titleTextView.setText(R.string.publish);
            mixes_relative = (RelativeLayout)mixes_view.findViewById(R.id.item_action);
            filter_button = (ImageButton)mixes_view.findViewById(R.id.item_filter_button);

*/
        return rootView;
    }
    public Bitmap getRoundedShape(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

     int   x = (source.getWidth() - size) / 2;
      int  y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap.Config config = source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(size, size, config);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size/2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }


    /*
      int targetHeight= source.getHeight();
        int targetWidth =  source.getWidth();
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
        Bitmap sourceBitmap = source;


        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }*/
public class ProfilePagerAdapter extends FragmentPagerAdapter {

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ProfileListFragment();
            case 1:
                return MyPlaylistListFragment.newInstance(Constants.ARGUMENT_NEWS_LIKED,Constants.CATEGORY_ALL);
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
            /*Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_image_thread);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;*/
           /* switch (position) {
                case 0:
                    return (getResources().getString(R.string.lenta)).toUpperCase();
                case 1:
                    return (getResources().getString(R.string.catalog)).toUpperCase();
                case 2:
                    return (getResources().getString(R.string.play_list)).toUpperCase();
            }
            return null;*/

        switch (position) {
            case 0:
                return getActivity().getResources().getString(R.string.my_subscriptions);
            case 1:
                return getActivity().getResources().getString(R.string.my_likes);

        }

        return null;
    }
}
public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
}
