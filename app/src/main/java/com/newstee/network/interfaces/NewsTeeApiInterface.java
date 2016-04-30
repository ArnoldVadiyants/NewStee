package com.newstee.network.interfaces;

import com.newstee.model.data.DataAuthor;
import com.newstee.model.data.DataNews;
import com.newstee.model.data.DataPost;
import com.newstee.model.data.DataTag;
import com.newstee.model.data.DataUserAuthentication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Arnold on 05.04.2016.
 */
public interface NewsTeeApiInterface {
    String BASE_URL = "http://213.231.4.68/music-web/app/php/";
   //* @GET("infoOutputJson.php?json=authors")
  //  Call<List<AuthorLab>> getAuthors();
   @GET("dispatcher.php?command=get_tags")
   Call<DataTag> getTags();
    @GET("dispatcher.php?command=get_authors")
    Call<DataAuthor> getAuthors();
    @GET("dispatcher.php?command=get_news")
    Call<DataNews> getNews();
    @FormUrlEncoded
    @POST("dispatcher.php?command=get_news")
    Call<DataNews> getNewsByIds(@Field("ids") String ids);
    @FormUrlEncoded
    @POST("dispatcher.php?command=get_news")
    Call<DataNews> getNewsByAuthor(@Field("id_author") String id);
//   @GET("dispatcher.php?command=get_audio")
 //   Call<DataAudio> getAudio();
@FormUrlEncoded
@POST("dispatcher.php?command=adding_tags_to_user")
Call<DataPost> addTags(@Field("idtag")String idNews);
    @FormUrlEncoded
    @POST("dispatcher.php?command=like_news")
    Call<DataPost> likeNews(@Field("idnews")String idNews);
 @FormUrlEncoded
     @POST("dispatcher.php?command=add_to_playlist")
 Call<DataPost> addNews(@Field("idnews")String idNews);
    @FormUrlEncoded
    @POST("dispatcher.php?command=user_registration")
    Call<DataPost> createUser(@Field("username") String username, @Field("password") String password,  @Field("repassword") String reenter_password,  @Field("email") String email,@Field("language") String language );
    @FormUrlEncoded
    @POST("dispatcher.php?command=authentication")
    Call<DataUserAuthentication> signIn(@Field("user_email") String email, @Field("user_password") String password, @Field("language") String language);
    @GET("dispatcher.php?command=logout")
    Call<DataPost> logOut();


  //  @GET("your_endpoint") Call<YOUR_POJO> getWeather(@Query("from") String from);


}
