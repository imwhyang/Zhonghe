package com.zhonghe.shiangou.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zhonghe.lib_base.utils.UtileDevice;
import com.zhonghe.shiangou.data.baseres.BaseRes;
import com.zhonghe.shiangou.data.bean.HomeData;
import com.zhonghe.shiangou.ui.listener.ResultListener;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.zhonghe.shiangou.utile.Device;
import com.zhonghe.shiangou.utile.JSONParser;
import com.zhonghe.shiangou.utile.PrefUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map;

/**
 * Created by a on 2017/8/10.
 */

public class HttpUtil {
    public static String URL_HomeData = "http://test.shiangou.com.cn/Android/index.php";

    public static Request<?> volleyPost(Context context, String url, final Map<String, String> map, final ResultListener listener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    listener.onSuccess(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFail(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        return request;
    }

    public static Request<?> getHomeData(Context context, final ResultListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("curpage", "0");
        BaseRes<HomeData> res = new BaseRes<>();
        Request<?> request = volleyPost(context, URL_HomeData, map, listener, res);
        return request;
    }


    public static final String CHAR_SET = "utf-8";

    public static boolean isWiFiActive(Context inContext) {
        ConnectivityManager cm = (ConnectivityManager) inContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param map
     * @param listener
     * @param bean
     * @return
     */
    public static Request<?> volleyPost(final Context context, final String url, final Map<String, String> map, final ResultListener listener, final Object bean) {
//        map.put("pubversion", Device.getCurrentAppVersionName(context) + "");
//        map.put("pubdevice", Device.getDeviceId(context));
//        map.put("pubplatform", "android");

//        String memberKey = PrefUtils.getString(context, Const.MEMBER_KEY, "");
//        if (!TextUtils.isEmpty(memberKey)) {
//            map.put("pubMemberKey", memberKey);
//        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    if (s.indexOf("null{") >= 0) {
                        s = s.replace("null{", "{");
                    }
                    Log.d("resp-url", url);
                    Log.d("resp-map", map.toString());
                    Log.d("resp-str", s);
                    JSONObject json = new JSONObject(s);
                    Object obj = JSONParser.toObject(json.toString(), bean);
                    listener.onSuccess(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VolleyErrorHelper.getMessage(volleyError, context);
                Log.d("resp-url", url);
                Log.d("resp-map", map.toString());
                Log.d("resp-errorMsg", errorMsg);
                listener.onFail(errorMsg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    sb.append(key + "--" + val + ",");
                }
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, CHAR_SET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));// 设置超时时间,取消自动重试
        return request;
    }


    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param map
     * @param listener
     * @param bean
     * @return
     */
    public static Request<?> volleyPostForPay(final Context context, final String url, final Map<String, String> map, final ResultListener listener, final Object bean) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    if (s.indexOf("null{") >= 0) {
                        s = s.replace("null{", "{");
                    }
                    Log.d("resp-url", url);
                    Log.d("resp-map", map.toString());
                    Log.d("resp-str", s);
                    JSONObject json = new JSONObject(s);
                    Object obj = JSONParser.toObject(json.toString(), bean);
                    listener.onSuccess(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VolleyErrorHelper.getMessage(volleyError, context);
                Log.d("resp-url", url);
                Log.d("resp-map", map.toString());
                Log.d("resp-errorMsg", errorMsg);
                listener.onFail(errorMsg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    sb.append(key + "--" + val + ",");
                }
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, CHAR_SET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));// 设置超时时间,取消自动重试
        return request;
    }


    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param map
     * @param listener
     * @param bean
     * @return
     */
    public static Request<?> volleyPostStringForZFBLogin(final Context context, final String url, final Map<String, String> map, final ResultListener listener, final Object bean) {
        //此接口不加公共参数，提供支付快捷登录访问，加入后支付宝授权失败
//        map.put("pubversion", Device.getCurrentAppVersionName(context) + "");
//        map.put("pubdevice", Device.getDeviceId(context));
//        map.put("pubplatform", "android");
//
//        String memberKey = PrefUtils.getString(context, Const.MEMBER_KEY, "");
//        if (!TextUtils.isEmpty(memberKey)) {
//            map.put("pubMemberKey", memberKey);
//        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    Log.d("resp-url", url);
                    Log.d("resp-map", map.toString());
                    Log.d("resp-str", s);
                    listener.onSuccess(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VolleyErrorHelper.getMessage(volleyError, context);
                Log.d("resp-url", url);
                Log.d("resp-map", map.toString());
                Log.d("resp-errorMsg", errorMsg);
                listener.onFail(errorMsg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    sb.append(key + "--" + val + ",");
                }
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, CHAR_SET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));// 设置超时时间,取消自动重试
        return request;
    }

    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param map
     * @param listener
     * @param bean
     * @return
     */
    public static Request<?> volleyPostString(final Context context, final String url, final Map<String, String> map, final ResultListener listener, final Object bean) {
        map.put("pubversion", Device.getCurrentAppVersionName(context) + "");
        map.put("pubdevice", Device.getDeviceId(context));
        map.put("pubplatform", "android");

//        String memberKey = PrefUtils.getString(context, "pubMemberKey", "");
//        if (!TextUtils.isEmpty(memberKey)) {
//            map.put("pubMemberKey", memberKey);
//        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    Log.d("resp-url", url);
                    Log.d("resp-map", map.toString());
                    Log.d("resp-str", s);
                    listener.onSuccess(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VolleyErrorHelper.getMessage(volleyError, context);
                Log.d("resp-url", url);
                Log.d("resp-map", map.toString());
                Log.d("resp-errorMsg", errorMsg);
                listener.onFail(errorMsg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    sb.append(key + "--" + val + ",");
                }
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, CHAR_SET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));// 设置超时时间,取消自动重试
        return request;
    }


    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param map
     * @param listener
     * @param bean
     * @return
     */
    public static Request<?> volleyPostForVesion(final Context context, final String url, final Map<String, String> map, final ResultListener listener, final Object bean) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    if (s.indexOf("null{") >= 0) {
                        s = s.replace("null{", "{");
                    }
                    Log.d("resp-url", url);
                    Log.d("resp-map", map.toString());
                    Log.d("resp-str", s);
                    JSONObject json = new JSONObject(s);
                    Object obj = JSONParser.toObject(json.toString(), bean);
                    listener.onSuccess(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VolleyErrorHelper.getMessage(volleyError, context);
                Log.d("resp-url", url);
                Log.d("resp-map", map.toString());
                Log.d("resp-errorMsg", errorMsg);
                listener.onFail(errorMsg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    sb.append(key + "--" + val + ",");
                }
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, CHAR_SET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));// 设置超时时间,取消自动重试
        return request;
    }


    /**
     * get 请求
     *
     * @param context
     * @param url
     * @param listener
     * @param bean
     * @return
     */
    public static Request<?> volleyGet(final Context context, final String url, final ResultListener listener, final Object bean) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("resp-url", url);
                    Log.d("resp-str", s);
                    JSONObject json = new JSONObject(s);
                    Object obj = JSONParser.toObject(json.toString(), bean);
                    listener.onSuccess(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = VolleyErrorHelper.getMessage(volleyError, context);
                Log.d("resp-url", url);
                Log.d("resp-errorMsg", errorMsg);
                listener.onFail(errorMsg);
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, CHAR_SET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));// 设置超时时间,取消自动重试
        return request;
    }

}
