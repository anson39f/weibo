package com.xds.weibo.bean;

import java.io.Serializable;

/**
 * @TODO
 * @date 2020/5/31.
 * @email
 */
public class Message implements Serializable {
    public User user;
    public String content;
    public String createTime;
    public String id;
}
