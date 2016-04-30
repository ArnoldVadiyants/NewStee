package com.newstee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.newstee.model.data.UserLab;
import com.newstee.utils.DisplayImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Arnold on 07.04.2016.
 */
public class EditProfileActivity extends AppCompatActivity{
    ImageLoader imageLoader = ImageLoader.getInstance();
ImageView avatarImgView;
    ImageView backgroundImgView;
    EditText nameEditText;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.edit_profile);
        setContentView(R.layout.edit_profile_layout);
        avatarImgView = (ImageView)findViewById(R.id.edit_profile_avatar_imgView);
        backgroundImgView =(ImageView)findViewById(R.id.edit_profile_back_imgView);
        nameEditText = (EditText)findViewById(R.id.edit_profile_name_editText);
        nameEditText.setText( UserLab.getInstance().getUser().getUserLogin());
        String avatar = UserLab.getInstance().getUser().getAvatar();
        if(avatar != null)
        {
            imageLoader.displayImage(avatar, avatarImgView, DisplayImageLoaderOptions.getRoundedInstance());
            imageLoader.displayImage(avatar,backgroundImgView, DisplayImageLoaderOptions.getInstance());

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

}
