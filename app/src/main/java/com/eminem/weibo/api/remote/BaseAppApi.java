package com.eminem.weibo.api.remote;

import com.eminem.weibo.base.net.HttpListener;
import com.eminem.weibo.base.net.RetrofitService;
import com.eminem.weibo.base.ui.IBaseView;
import com.eminem.weibo.base.utils.MD5Util;
import com.eminem.weibo.bean.User;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author 访问接口
 * @email
 * @date
 */
public class BaseAppApi {

    public static BaseService getBaseService() {
        return RetrofitService.getService(BaseService.class);
    }

    /**
     * 登陆
     *
     * @param name
     * @param password
     */
    public static void login(String name, String password,
                             HttpListener<User>
                                     listener) {
        RetrofitService.request(getBaseService().loginUser(name, MD5Util.MD5(password)), listener);
    }

    public static void getUser(String name,
                               HttpListener<User>
                                       listener) {
        RetrofitService.request(getBaseService().getUser(name), listener);
    }

    //注册
    public static void register(IBaseView view, String url, String account, String passwd,
                                String name,
                                String email,
                                HttpListener<User>
                                        listener) {
        File file = new File(url);
        MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
        RetrofitService.request(getBaseService().registerUser(account, MD5Util.MD5(passwd), name,
                email, part), listener);
    }

    //更新头像
    public static void updateAvatar(String url, String account,
                                    HttpListener<User>
                                            listener) {
        File file = new File(url);
        MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
        RetrofitService.request(getBaseService().updateAvatar(account, part), listener);
    }

    //更新昵称
    public static void updateNickname(String account, String nickName,
                                      HttpListener<User>
                                              listener) {
        RetrofitService.request(getBaseService().updateNickname(account, nickName), listener);
    }


    //发微博
    public static void publish(String accountId,
                               String content,
                               String categoryId,
                               HttpListener<Void>
                                       listener) {
        RetrofitService.request(getBaseService().publish(accountId, content, categoryId), listener);
    }

    //发评论
    public static void comment(String accountId,
                               String content,
                               String weiboId,
                               HttpListener<Void>
                                       listener) {
        RetrofitService.request(getBaseService().comment(accountId, content, weiboId), listener);
    }

    //点赞
    public static void like(String accountId,
                            String weiboId,
                            HttpListener<Void>
                                    listener) {
        RetrofitService.request(getBaseService().like(accountId, weiboId), listener);
    }

    //发评分
    public static void fraction(String accountId,
                                String weiboId,
                                String fraction,
                                HttpListener<Void>
                                        listener) {
        RetrofitService.request(getBaseService().fraction(accountId, fraction, weiboId), listener);
    }

    //关注
    public static void attention(String accountId,
                                 String followedUsername,
                                 HttpListener<Void>
                                         listener) {
        RetrofitService.request(getBaseService().attention(accountId, followedUsername), listener);
    }

    //取消关注
    public static void unAttention(String accountId,
                                 String followedUsername,
                                 HttpListener<Void>
                                         listener) {
        RetrofitService.request(getBaseService().unAttention(accountId, followedUsername), listener);
    }

}
