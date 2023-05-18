package com.baihe.http.cookie;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：okhttp的CookieJar具体实现
 */
public class MyCookieJar implements CookieJar {
    private CookieStore cookieStore;

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public MyCookieJar(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.saveFromResponse(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.loadForRequest(url) == null ? new ArrayList<Cookie>() : cookieStore.loadForRequest(url);
    }

}
