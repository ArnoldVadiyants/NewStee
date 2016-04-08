package com.newstee;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.newstee.utils.CircleTransform;

/**
 * Created by Arnold on 07.04.2016.
 */
public class EditProfileActivity extends AppCompatActivity{
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
        BitmapFactory.Options o=new BitmapFactory.Options();
        o.inSampleSize = 4;
        o.inDither=false;                     //Disable Dithering mode
        o.inPurgeable=true;
        avatarImgView.setImageBitmap(new CircleTransform().transform(BitmapFactory.decodeResource(getResources(), R.drawable.avatar_test, o)));;
        backgroundImgView =(ImageView)findViewById(R.id.edit_profile_back_imgView);
      backgroundImgView.setImageResource(R.drawable.profile_back2);

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
