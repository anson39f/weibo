package com.xds.weibo.api.remote;


import com.xds.weibo.api.ResData;
import com.xds.weibo.bean.Attention;
import com.xds.weibo.bean.Category;
import com.xds.weibo.bean.Comment;
import com.xds.weibo.bean.Message;
import com.xds.weibo.bean.Status;
import com.xds.weibo.bean.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 接口
 *
 * @author
 * @email
 * @date
 */
public interface BaseService {

    @Multipart
    @POST("weibo/user/register")
    Observable<ResData<User>> registerUser(@Query("username") String account, @Query("password") String password,
                                           @Query("nickName") String nickName,
                                           @Query("email") String email, @Part MultipartBody.Part file);

    @Multipart
    @POST("weibo/user/updateAvatar")
    Observable<ResData<User>> updateAvatar(@Query("username") String account, @Part MultipartBody.Part file);


    @POST("weibo/user/updateNickname")
    Observable<ResData<User>> updateNickname(@Query("username") String account, @Query("nickName") String nickName);


    @POST("weibo/user/login")
    Observable<ResData<User>> loginUser(@Query("username") String account,
                                        @Query("password") String password);

    @POST("weibo/user/get")
    Observable<ResData<User>> getUser(@Query("username") String account);

    @POST("weibo/weibo/publish")
    Observable<ResData<Void>> publish(@Query("username") String username,
                                      @Query("content") String content,
                                      @Query("categoryId") String categoryId);

    @GET("weibo/weibo/search")
    Observable<ResData<List<Status>>> search(@Query("keys") String keys);

    @GET("weibo/weibo/followedUserNewWeibo")
    Observable<ResData<List<Status>>> followedUserNewWeibo(@Query("username") String username);

    @GET("weibo/user/search")
    Observable<ResData<List<User>>> searchUser(@Query("nickname") String keys);

    @GET("weibo/user/getAttention")
    Observable<ResData<List<Attention>>> getAttention(@Query("username") String keys);

    @GET("weibo/user/getFan")
    Observable<ResData<List<Attention>>> getFan(@Query("username") String keys);

    @POST("weibo/weibo/getCategory")
    Observable<ResData<List<Category>>> getCategory();


    @POST("weibo/weibo/comment")
    Observable<ResData<Void>> comment(@Query("username") String username,
                                      @Query("content") String content,
                                      @Query("weiboId") String weiboId);

    @POST("weibo/weibo/findCommentByWeibo")
    Observable<ResData<List<Comment>>> findCommentByWeibo(@Query("weiboId") String weiboId);

    @POST("weibo/weibo/like")
    Observable<ResData<Void>> like(@Query("username") String username,
                                   @Query("weiboId") String weiboId);

    @POST("weibo/weibo/fraction")
    Observable<ResData<Void>> fraction(@Query("username") String username,
                                       @Query("fraction") String fraction,
                                       @Query("weiboId") String weiboId);

    @GET("weibo/weibo/findByUser")
    Observable<ResData<List<Status>>> findByUserWeibo(@Query("username") String username);

    @GET("weibo/weibo/categoryNewWeibo")
    Observable<ResData<List<Status>>> categoryNewWeibo(@Query("categoryId") String categoryId);

    @GET("weibo/user/message")
    Observable<ResData<List<Message>>> message(@Query("username") String username);

    @GET("weibo/user/attention")
    Observable<ResData<Void>> attention(@Query("username") String username, @Query(
            "followedUsername") String followedUsername);

    @GET("weibo/user/unAttention")
    Observable<ResData<Void>> unAttention(@Query("username") String username, @Query(
            "followedUsername") String followedUsername);
}
