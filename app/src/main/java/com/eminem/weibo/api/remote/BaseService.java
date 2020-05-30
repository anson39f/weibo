package com.eminem.weibo.api.remote;


import com.eminem.weibo.api.ResData;
import com.eminem.weibo.bean.Category;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.User;

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

    @POST("weibo/weibo/getCategory")
    Observable<ResData<List<Category>>> getCategory();


}
