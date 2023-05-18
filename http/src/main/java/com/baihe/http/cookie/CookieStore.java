package com.baihe.http.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：cookie存储
 */
public class CookieStore {
    /**
     * cookie存储的首选项名
     */
    private static final String COOKIE_PREFS = "_cookie_prefs";
    /**
     * 存储分隔符
     */
    private static final String COOKIE_SEPARATOR = ",";
    /**
     * 配置cookie是否本地持久化
     */
    private boolean isPersistent;
    private final ConcurrentHashMap<String, List<Cookie>> cookies;
    private final SharedPreferences cookiePrefs;

    public CookieStore(Context context, boolean isPersistent) {
        this.isPersistent = isPersistent;
        this.cookies = new ConcurrentHashMap();
        this.cookiePrefs = context.getSharedPreferences(context.getPackageName() + COOKIE_PREFS, Context.MODE_PRIVATE);
        Map<String, ?> map = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String urlHost = entry.getKey();
            String encodeCookiesStr = (String) entry.getValue();
            if (encodeCookiesStr != null && !"".equals(encodeCookiesStr)) {
                List<Cookie> cookieList = new ArrayList<>();
                String[] encodeCookieListStr = TextUtils.split((String) entry.getValue(), COOKIE_SEPARATOR);
                for (String encodeCookieStr : encodeCookieListStr) {
                    Cookie decodedCookie = decodeCookie(encodeCookieStr);
                    if (decodedCookie != null) {
                        cookieList.add(decodedCookie);
                    }
                }
                cookies.put(urlHost, cookieList);
            }
        }
    }

    /**
     * 保存response返回url对应的所有cookie(保存前清空该url的所有cookie)
     * @param url
     * @param cookies
     */
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        saveCookies(url, cookies);
    }

    /**
     * request请求时加载url对应的所有cookie(不会返回过期的cookie)
     * @param url
     * @return
     */
    public List<Cookie> loadForRequest(HttpUrl url) {
        String urlHost = url.host();
        if (cookies.containsKey(urlHost)) {
            List<Cookie> list = new ArrayList<>();
            List<String> encodeCookies = new ArrayList<>();
            list.addAll(cookies.get(urlHost));
            //剔除过期的cookie
            for (Cookie cookie : list) {
                if (isExpired(cookie)) {
                    cookies.get(urlHost).remove(cookie);
                } else {
                    encodeCookies.add(encodeCookie(new SerializableCookie(cookie)));
                }
            }
            //如果配置本地持久化并且有过期的cookie被移除,更新本地存储的cookie
            if (isPersistent && cookies.get(urlHost).size() < list.size()) {
                SharedPreferences.Editor editor = cookiePrefs.edit();
                editor.putString(url.host(), TextUtils.join(COOKIE_SEPARATOR, encodeCookies));
                editor.apply();
            }
            return cookies.get(urlHost);
        }
        return null;
    }

    /**
     * 保存url对应的所有cookie(保存前清空该url的所有cookie)
     * @param url
     * @param cookies
     */
    public void saveCookies(HttpUrl url, List<Cookie> cookies) {
        String urlHost = url.host();
        if (this.cookies.containsKey(urlHost)) {
            //清空cookie
            this.cookies.get(urlHost).clear();
        } else {
            //创建新的cookie引用,防止外部引用不小心被清空
            this.cookies.put(urlHost, new ArrayList<Cookie>());
        }
        for (Cookie cookie : cookies) {
            //cookie是否具有可持久性(一次性cookie不保存)
            if (cookie.persistent()) {
                this.cookies.get(urlHost).add(cookie);
            } else {
                continue;
            }
        }
        //cookie本地持久化
        if (isPersistent) {
            List<String> encodeCookies = new ArrayList<>();
            for (Cookie cookie : this.cookies.get(urlHost)) {
                encodeCookies.add(encodeCookie(new SerializableCookie(cookie)));
            }
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.putString(url.host(), TextUtils.join(COOKIE_SEPARATOR, encodeCookies));
            editor.apply();
        }
    }

    /**
     * 获取所有的cookie(包含过期的cookie)
     * @return
     */
    public List<Cookie> getAllCookies() {
        List<Cookie> allCookies = new ArrayList<>();
        for (ConcurrentHashMap.Entry<String, List<Cookie>> entry : cookies.entrySet()) {
            List<Cookie> cookies = entry.getValue();
            allCookies.addAll(cookies);
        }
        return allCookies;
    }

    /**
     * 获取url对应的所有的cookie(包含过期的cookie)
     * @param url
     * @return
     */
    public List<Cookie> getCookies(HttpUrl url) {
        return cookies.get(url.host());
    }

    /**
     * url添加cookie集合(存在相同cookie则更新,不存在则保存)
     * @param url
     * @param cookies
     */
    public void addCookies(HttpUrl url, List<Cookie> cookies) {
        String urlHost = url.host();
        if (!this.cookies.containsKey(urlHost)) {
            this.cookies.put(urlHost, new ArrayList<Cookie>());
        }
        List<Cookie> removeCookies = new ArrayList<>();
        List<Cookie> addCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            //cookie是否具有可持久性
            if (cookie.persistent()) {
                String token = getCookieToken(cookie);
                for (Cookie mcookie : this.cookies.get(urlHost)) {
                    //存在相同的cookie,把之前的cookie删除
                    if (token.equals(getCookieToken(mcookie))) {
                        removeCookies.add(mcookie);
                        break;
                    }
                }
                addCookies.add(cookie);
            } else {
                continue;
            }
        }
        this.cookies.get(urlHost).removeAll(removeCookies);
        this.cookies.get(urlHost).addAll(addCookies);
        //cookie本地持久化
        if (isPersistent) {
            List<String> encodeCookies = new ArrayList<>();
            for (Cookie cookie : this.cookies.get(urlHost)) {
                encodeCookies.add(encodeCookie(new SerializableCookie(cookie)));
            }
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.putString(url.host(), TextUtils.join(COOKIE_SEPARATOR, encodeCookies));
            editor.apply();
        }
    }

    /**
     * url添加单个cookie(存在相同cookie则更新,不存在则保存)
     * @param url
     * @param cookie
     */
    public void addCookie(HttpUrl url, Cookie cookie) {
        String urlHost = url.host();
        if (!cookies.containsKey(urlHost)) {
            cookies.put(urlHost, new ArrayList<Cookie>());
        }
        //cookie是否具有可持久性
        if (cookie.persistent()) {
            String token = getCookieToken(cookie);
            Cookie removeCookie = null;
            for (Cookie mcookie : cookies.get(urlHost)) {
                //存在相同的cookie,把之前的cookie删除
                if (token.equals(getCookieToken(mcookie))) {
                    removeCookie = mcookie;
                    break;
                }
            }
            if (removeCookie != null)
                cookies.get(urlHost).remove(removeCookie);
            cookies.get(urlHost).add(cookie);
        }
        //cookie本地持久化
        if (isPersistent) {
            List<String> encodeCookies = new ArrayList<>();
            for (Cookie tCookie : cookies.get(urlHost)) {
                encodeCookies.add(encodeCookie(new SerializableCookie(tCookie)));
            }
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.putString(urlHost, TextUtils.join(COOKIE_SEPARATOR, encodeCookies));
            editor.apply();
        }
    }

    /**
     * 移除url对应的cookie
     * @param url
     * @param cookie
     */
    public void removeCookie(HttpUrl url, Cookie cookie) {
        String urlHost = url.host();
        if (cookies.containsKey(urlHost)) {
            cookies.get(urlHost).remove(cookie);
            if (cookies.get(urlHost).size() == 0)
                cookies.remove(urlHost);
        }
        if (isPersistent) {
            List<String> encodeCookies = new ArrayList<>();
            for (Cookie tCookie : cookies.get(urlHost)) {
                encodeCookies.add(encodeCookie(new SerializableCookie(tCookie)));
            }
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.putString(url.host(), TextUtils.join(COOKIE_SEPARATOR, encodeCookies));
            editor.apply();
        }
    }

    /**
     * 移除url对应的所有cookie
     * @param url
     */
    public void removeCookies(HttpUrl url) {
        String urlHost = url.host();
        cookies.remove(urlHost);
        //如果配置本地持久化
        if (isPersistent) {
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.remove(urlHost);
            editor.apply();
        }
    }

    /**
     * 清空所有cookie
     */
    public void clearAllCookies() {
        cookies.clear();
        SharedPreferences.Editor editor = cookiePrefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * cookie是否本地持久化(如果本地持久化,cookie将会存储在sharedPreferences上)
     * @return
     */
    public boolean isPersistent() {
        return isPersistent;
    }

    /**
     * 判断cookie是否过期
     * @param cookie 判断的cookie
     * @return true过期, false没过期
     */
    private boolean isExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    /**
     * 获取cookie标识
     * @param cookie
     * @return
     */
    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * cookie序列化
     * @param cookie
     * @return
     */
    private String encodeCookie(SerializableCookie cookie) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (Exception e) {
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * cookie反序列化
     * @param cookieStr
     * @return
     */
    private Cookie decodeCookie(String cookieStr) {
        byte[] bytes = hexStringToByteArray(cookieStr);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            cookie = ((SerializableCookie) ois.readObject()).getCookie();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     * @param b
     * @return
     */
    private String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte element : b) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转二进制数组
     * @param s
     * @return
     */
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
