package com.xds.weibo.bean;

import java.io.Serializable;

/**
 * @TODO
 * @date 2020/5/31.
 * @email
 */
public class Attention implements Serializable {
    public String id;
    public User attentionUser;
    public User followedUser;
}
