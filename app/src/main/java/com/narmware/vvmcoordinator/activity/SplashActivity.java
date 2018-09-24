package com.narmware.vvmcoordinator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmcoordinator.MyApplication;
import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.pojo.LoginResponse;
import com.narmware.vvmcoordinator.pojo.TimeLogResponse;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;
import com.narmware.vvmcoordinator.support.SharedPreferencesHelper;

import org.w3c.dom.ProcessingInstruction;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class SplashActivity extends AppCompatActivity {

    static int TIMEOUT=2000;
    Realm realm;
    RealmResults<Login> login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        realm = Realm.getInstance(this);
        login=realm.where(Login.class).findAll();

        if(!login.isEmpty())
        {
            Log.e("Login name",login.get(0).getName());
            MyApplication.setLogTime(SplashActivity.this,Constants.LOGIN_EVENT);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(SharedPreferencesHelper.getIsLogin(SplashActivity.this)==false)
                {
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        },TIMEOUT);
    }

}
