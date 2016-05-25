package com.newstee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.newstee.helper.SQLiteHandler;
import com.newstee.helper.SessionManager;
import com.newstee.model.data.DataPost;
import com.newstee.model.data.DataUserAuthentication;
import com.newstee.model.data.User;
import com.newstee.model.data.UserLab;
import com.newstee.network.FactoryApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arnold on 03.03.2016.
 */
public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomeActivity";
    private SQLiteHandler db;
    private SessionManager session;
    private CallbackManager callbackmanager;
    private ProgressBar mProgress;
    private LoginButton btnFacebookReg;
    private ImageButton btnTwitterReg;
    private ImageButton btnGoogleReg;
    private ImageButton btnVkReg;
    private Button btnRegistration;
    private Button btnResume;
    private FrameLayout layoutSignIn;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_layout);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if (isFirstTime()) {
            // What you do when the Application is Opened First time Goes here
        }
        if(session.isLoggedIn())
        {
            Intent intent = new Intent(WelcomeActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
            return;

        }
        mProgress = (ProgressBar)findViewById(R.id.welcome_progress);
        mProgress.setVisibility(View.GONE);
        callbackmanager = CallbackManager.Factory.create();

        show_facebook_keyhash();
        btnFacebookReg = (LoginButton) findViewById(R.id.facebook_imageButton);
        btnTwitterReg = (ImageButton) findViewById(R.id.twitter_imageButton);
        btnVkReg = (ImageButton) findViewById(R.id.vk_imageButton);
        btnGoogleReg = (ImageButton) findViewById(R.id.google_imageButton);
        btnRegistration = (Button)findViewById(R.id.registration_via_email_button);
        layoutSignIn = (FrameLayout)findViewById(R.id.sign_in_layout);
        btnResume = (Button)findViewById(R.id.resume_button);
      //  btnFacebookReg.setBackgroundResource(R.drawable.ic_facebook);
        btnFacebookReg.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));
       /* btnFacebookReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        btnFacebookReg.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                update_ui("login successfully");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            update_ui("error");

                        } else {
                            String jsonresult = String.valueOf(json);
                            Log.d(TAG,"@@@@@@@ jsonResult " +jsonresult);
                            System.out.println("@@@@@@@ JSON Result " + jsonresult);
                            try {
                               final String id =  json.getString("id");
                                Call<DataPost> callR = FactoryApi.getInstance(getApplicationContext()).createUser(id, "ru");
                                callR.enqueue(new Callback<DataPost>() {
                                    @Override
                                    public void onResponse(Call<DataPost> call, retrofit2.Response<DataPost> response) {
                                        String result = response.body().getResult();
                                        String msgRegister = response.body().getMessage();
                         /*    Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                             Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + " Result: " + result + " Data " + data + " Message " + msg);
                             Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@");*/
                                        if (result.equals(Constants.RESULT_SUCCESS)) {
                                        //    Toast.makeText(getApplicationContext(), R.string.register_msg, Toast.LENGTH_LONG).show();
                                            Call<DataUserAuthentication> auth = FactoryApi.getInstance(getApplicationContext()).signIn(id, "ru");
                                            auth.enqueue(new Callback<DataUserAuthentication>() {
                                                @Override
                                                public void onResponse(Call<DataUserAuthentication> call, Response<DataUserAuthentication> response) {
                                                    String result = response.body().getResult();

                                                    if (result.equals(Constants.RESULT_SUCCESS)) {
                                                        User data = response.body().getData().get(0);
                                                        db.addUser(data.getUserLogin(), null, null, id);
                                                        UserLab.getInstance().setUser(data);
                                                        UserLab.isLogin = true;
                                                        session.setLogin(true);
                                                        Intent intent = new Intent(
                                                                WelcomeActivity.this,
                                                                MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),response.body().getMessage() , Toast.LENGTH_LONG).show();
                                                        return;

                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<DataUserAuthentication> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "@Login: " +t.getMessage(), Toast.LENGTH_LONG).show();
                                                    Log.d(TAG,   "@@@@@@Login: " +t.getMessage());

                                                }
                                            });

                                        } else {
                                            Toast.makeText(getApplicationContext(), msgRegister, Toast.LENGTH_LONG).show();
                                        }
                                        // Launch login activity
                  /*           Intent intent = new Intent(
                                     RegisterActivity.this,
                                     LoginActivity.class);
                             startActivity(intent);
                             finish();
*/

                                    }

                                    @Override
                                    public void onFailure(Call<DataPost> call, Throwable t) {
                                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + t.getCause());
                                        t.printStackTrace();
                                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + t.getLocalizedMessage());

                                        Toast.makeText(getApplicationContext(),
                                                "@Registered: " +t.getMessage() , Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            update_ui(jsonresult);
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                update_ui("CANCELED");
            }

            @Override
            public void onError(FacebookException exception) {
                update_ui("ERROR");
            }
        });
        btnGoogleReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnVkReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnTwitterReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        RegisterActivity.class);
                startActivity(intent);

            }
        });
        layoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }


    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }





    private void update_ui(String message) {
       Log.d(TAG, "@@@@@@ LOG MESSAGE "+ message);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);

    }
private  void showContent()
{
    btnFacebookReg.setVisibility(View.GONE);
    btnTwitterReg.setVisibility(View.GONE);
    btnVkReg.setVisibility(View.GONE);
    btnGoogleReg.setVisibility(View.GONE);
    btnRegistration.setVisibility(View.GONE);
    layoutSignIn.setVisibility(View.GONE);
    btnResume.setVisibility(View.GONE);
    mProgress.setVisibility(View.VISIBLE);
}
    private  void hideContent()
    {
        btnFacebookReg.setVisibility(View.GONE);
        btnTwitterReg.setVisibility(View.GONE);
        btnVkReg.setVisibility(View.GONE);
        btnGoogleReg.setVisibility(View.GONE);
        btnRegistration.setVisibility(View.GONE);
        layoutSignIn.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void show_facebook_keyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.newstee", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Facebook KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
}
