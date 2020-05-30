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
     * @param view
     * @param name
     * @param pasword
     * @param listener
     */
    public static void login(IBaseView view, String name, String pasword, String type,
                             HttpListener<User>
                                     listener) {
        RetrofitService.request(getBaseService().loginUser(name, MD5Util.MD5(pasword)), listener);
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

    public static void modify(IBaseView view, String type, String account,
                              String name,
                              String phoneNumber, String address,
                              String dateOfBirth,
                              HttpListener<User>
                                      listener) {
        if ("2".equals(type)) {
            RetrofitService.request(getBaseService().modifyUser(account, name, phoneNumber, address,
                    dateOfBirth), listener);
        } else {
            RetrofitService.request(getBaseService().modifyWaiter(account, name,
                    phoneNumber, address,
                    dateOfBirth), view.bindToLife(),
                    listener);
        }
    }

    /**
     * 更新密码
     *
     * @param view
     * @param password
     * @param listener
     */
    public static void updatePassword(IBaseView view, String type, String name, String password,
                                      HttpListener<Void> listener) {
        if ("2".equals(type)) {
            RetrofitService.request(getBaseService().updateUserPassword(name, MD5Util.MD5(password)),
                    listener);
        } else {
            RetrofitService.request(getBaseService().updateWaiterPassword(name, MD5Util.MD5(password)), view.bindToLife(),
                    listener);
        }
    }


    //呼叫物业
    public static void addWuye(IBaseView view, String accountId,
                               String content,
                               HttpListener<Void>
                                       listener) {
        if (view == null) {
            RetrofitService.request(getBaseService().addWuye(accountId, content), listener);
        } else {
            RetrofitService.request(getBaseService().addWuye(accountId, content), view.bindToLife(),
                    listener);
        }
    }

    //呼叫医疗服务
    public static void addDoctor(IBaseView view, String accountId,
                                 String content,
                                 HttpListener<Void>
                                         listener) {
        if (view == null) {
            RetrofitService.request(getBaseService().addDoctor(accountId, content), listener);
        } else {
            RetrofitService.request(getBaseService().addDoctor(accountId, content), view.bindToLife(),
                    listener);
        }
    }

    //紧急呼叫
    public static void addSos(IBaseView view, String accountId,
                              String content,
                              HttpListener<Void>
                                      listener) {
        if (view == null) {
            RetrofitService.request(getBaseService().addSos(accountId, content), listener);
        } else {
            RetrofitService.request(getBaseService().addSos(accountId, content), view.bindToLife(),
                    listener);
        }
    }


    //打卡
    public static void addCheck(IBaseView view, String type, String accountId,
                                HttpListener<Void>
                                        listener) {
        RetrofitService.request(getBaseService().addUserCheck(accountId), view.bindToLife(), listener);
    }


    //今日考勤
    public static void addTodaySos(IBaseView view,
                                   HttpListener<Void>
                                           listener) {
        RetrofitService.request(getBaseService().addWaiterCheck(), view.bindToLife(),
                listener);
    }


}
