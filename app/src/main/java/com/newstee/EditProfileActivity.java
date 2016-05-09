package com.newstee;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.newstee.helper.SQLiteHandler;
import com.newstee.model.data.DataUpdateUser;
import com.newstee.model.data.UserLab;
import com.newstee.network.FactoryApi;
import com.newstee.utils.DisplayImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arnold on 07.04.2016.
 */
public class EditProfileActivity extends AppCompatActivity{
    private static final int SELECT_PICTURE = 1;
private static final String TAG = "EditProfileActivity";
    private String selectedImagePath="";
    private String imgPath;
    private byte[]bytes;
    private SQLiteHandler db;
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
        db = new SQLiteHandler(this);
        avatarImgView = (ImageView)findViewById(R.id.edit_profile_avatar_imgView);
        avatarImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(intent, CAPTURE_IMAGE);
            }
        });


        backgroundImgView =(ImageView)findViewById(R.id.edit_profile_back_imgView);
        /*backgroundImgView.setOnClickListener(new View.OnClickListener() {

            @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    startActivityForResult(intent, CAPTURE_IMAGE);
            }
        });*/
        /*avatarImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });*/

        nameEditText = (EditText)findViewById(R.id.edit_profile_name_editText);
        nameEditText.setText( UserLab.getInstance().getUser().getUserLogin());
        String avatar = UserLab.getInstance().getUser().getAvatar();
        if(avatar != null)
        {
            imageLoader.displayImage(avatar, avatarImgView, DisplayImageLoaderOptions.getRoundedInstance());
            imageLoader.displayImage(avatar,backgroundImgView, DisplayImageLoaderOptions.getInstance());

        }
        saveBtn = (Button)findViewById(R.id.edit_profile_save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

updateUser(nameEditText.getText().toString(),null,selectedImagePath);
                // create RequestBody instance from file


            }
        });

    }
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }

public boolean updateUser(@Nullable final String name,@Nullable final String email,@Nullable String imagePath )
{
    final boolean[] result = {false};
    if(name == null && email == null && imagePath == null )
    {
        return false;
    }
    RequestBody fbody = null;
    if(!selectedImagePath.equals(""))
    {
        File file = new File(selectedImagePath);
        fbody = RequestBody.create(MediaType.parse("image/*"), file);
    }
    // fbody = RequestBody.create(MediaType.parse("image/*"),bytes );
    RequestBody username = RequestBody.create(MediaType.parse("text/plain"),name);
    Call<DataUpdateUser> call = FactoryApi.getInstance(getApplicationContext()).update_user(username,null,fbody);
    call.enqueue(new Callback<DataUpdateUser>() {
        @Override
        public void onResponse(Call<DataUpdateUser> call, Response<DataUpdateUser> response) {
            if(response.body().getResult().equals(Constants.RESULT_SUCCESS))
            {
                UserLab.getInstance().getUser().setAvatar(response.body().getData().getAvatar());
                UserLab.getInstance().getUser().setUserEmail(response.body().getData().getEmail());
                UserLab.getInstance().getUser().setUserLogin(response.body().getData().getUsername());
                db.updateUser(name, email);
                Toast.makeText(getApplicationContext(), R.string.update_data_success, Toast.LENGTH_LONG).show();
                result[0] =  true;

            }
            else
            {
                Toast.makeText(getApplicationContext(), R.string.update_data_failure, Toast.LENGTH_LONG).show();

                result[0] =  false;
                Log.d(TAG, "@@@@@@ Message " + response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<DataUpdateUser> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.update_data_failure, Toast.LENGTH_LONG).show();
            result[0] =  false;
        }
    });
    return result[0];
}


   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                Bitmap bitmap;
                Uri uri = data.getData();
              //  String strUri = getAbsolutePath(uri);
/*File f = new File(strUri);
                Uri uri = */
                //DebugDialog.DebugLog("STR URI:%s", strUri);
               /* if (strUri.contains("document") || strUri.contains("mediaKey") || strUri.contains("content://") ||
                        //if(sourcePath.startsWith("/document") || sourcePath.startsWith("/mediaKey") ||
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {*/
                    int read;
                    byte[] buffer;
                    InputStream fileInputStream;
                    ByteArrayOutputStream byteArrayOutputStream;

                    try {
                        fileInputStream = getApplicationContext().getContentResolver().openInputStream(uri);
                        byteArrayOutputStream = new ByteArrayOutputStream();

                        buffer = new byte[2048];
                        while(true)
                        {
                            read = fileInputStream.read(buffer);
                            if(read < 0)
                                break;

                            byteArrayOutputStream.write(buffer, 0, read);

                        }
                        bytes = byteArrayOutputStream.toByteArray();
                         bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        avatarImgView.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



              //  }
            }else if (requestCode == CAPTURE_IMAGE) {
                selectedImagePath = getImagePath();
                avatarImgView.setImageBitmap(decodeFile(selectedImagePath));
                backgroundImgView.setImageBitmap(decodeFile(selectedImagePath));
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
    @SuppressWarnings("deprecation")
    Cursor cursor = managedQuery(uri, projection, null, null, null);
    if (cursor != null) {
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    } else
            return null;
}
    /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (resultCode == RESULT_OK) {
             if (requestCode == SELECT_PICTURE) {
                 Uri selectedImageUri = data.getData();
                 selectedImagePath = getPath(selectedImageUri);
                 System.out.println("Image Path : " + selectedImagePath);
                 File file = new File(selectedImagePath);
                 Call<DataPost>call = FactoryApi.getInstance(this).update_user(null,null,file);
                 call.enqueue(new Callback<DataPost>() {
                     @Override
                     public void onResponse(Call<DataPost> call, Response<DataPost> response) {
                         Toast.makeText(getApplicationContext(),response.body().getResult(),Toast.LENGTH_LONG);
                     }

                     @Override
                     public void onFailure(Call<DataPost> call, Throwable t) {

                     }
                 });
             }
         }
     }*/
   /* public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/
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
