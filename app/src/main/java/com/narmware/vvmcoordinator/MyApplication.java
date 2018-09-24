package com.narmware.vvmcoordinator;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmcoordinator.activity.SplashActivity;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.pojo.TimeLogResponse;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyApplication extends MultiDexApplication {
    public static RequestQueue mVolleyRequest;
    static Realm realm;
    static RealmResults<Login> login;

    public static void config_realm(Context context)
    {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static String ipAddress() {
        String ipAddress = "N/A";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                }
            }

        } catch (SocketException ex) {
        }

        return ipAddress;
    }

    public static void setLogTime(Context context,final String event) {
        mVolleyRequest = Volley.newRequestQueue(context);
        realm = Realm.getInstance(context);
        login=realm.where(Login.class).findAll();
        final String coordid=login.get(0).getCo_ordinator_id();
        final String ip= MyApplication.ipAddress();
        Log.e("IP Address", ip);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("TIMELOG RESPONSE",response);

                        try {
                            Gson gson = new Gson();
                            TimeLogResponse dataResponse = gson.fromJson(response, TimeLogResponse.class);
                            if (dataResponse.getStatus_code().equals(Constants.ERROR)) {

                            }
                            if (dataResponse.getStatus_code().equals(Constants.SUCCESS)) {

                            }
                        }catch (Exception e)
                        {e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
                //showNoConnectionDialog();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.TIMELOG);
                params.put(Constants.EVENT,event);
                params.put(Constants.COORD_ID,coordid);
                params.put(Constants.IP_ADDRESS,ip);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }


}
