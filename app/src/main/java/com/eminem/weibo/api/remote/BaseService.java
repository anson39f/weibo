package com.eminem.weibo.api.remote;


import com.eminem.weibo.api.ResData;
import com.eminem.weibo.bean.User;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
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
    Observable<ResData<User>> registerUser(@Query("username") String account, @Query("password") String pasword,
                                           @Query("nickName") String nickName,
                                           @Query("email") String email, @Part MultipartBody.Part file);

    @Multipart
    @POST("weibo/user/updateAvatar")
    Observable<ResData<User>> updateAvatar(@Query("username") String account, @Part MultipartBody.Part file);


    @POST("weibo/user/updateNickname")
    Observable<ResData<User>> updateNickname(@Query("username") String account, @Query("nickName") String nickName);


    @POST("weibo/user/login")
    Observable<ResData<User>> loginUser(@Query("username") String account,
                                        @Query("password") String pasword);
    @POST("weibo/user/get")
    Observable<ResData<User>> getUser(@Query("username") String account);

    @POST("community/user/password")
    Observable<ResData<Void>> updateUserPassword(@Query("username") String account, @Query(
            "password") String pasword);

    @POST("community/waiter/password")
    Observable<ResData<Void>> updateWaiterPassword(@Query("username") String account, @Query(
            "password") String pasword);


    @POST("community/user/modify")
    Observable<ResData<User>> modifyUser(@Query("username") String account, @Query("fullName") String name,
                                         @Query("phoneNumber") String phoneNumber, @Query("homeAddress") String address,
                                         @Query("dateOfBirth") String dateOfBirth);

    @POST("community/waiter/modify")
    Observable<ResData<User>> modifyWaiter(@Query("username") String account,
                                           @Query("fullName") String name,
                                           @Query("phoneNumber") String phoneNumber, @Query("homeAddress") String address,
                                           @Query("dateOfBirth") String dateOfBirth);


    @POST("community/user/cs/add")
    Observable<ResData<Void>> addWuye(@Query("username") String username,
                                      @Query("content") String content);


    @POST("community/user/ds/add")
    Observable<ResData<Void>> addDoctor(@Query("username") String username,
                                        @Query("content") String content);

    @POST("community/user/us/add")
    Observable<ResData<Void>> addSos(@Query("username") String username,
                                     @Query("content") String content);

    @POST("community/user/attendance/check")
    Observable<ResData<Void>> addUserCheck(@Query("id") String id);

    @POST("community/attendance/day")
    Observable<ResData<Void>> addWaiterCheck();


}
