package com.narmware.vvmcoordinator.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmcoordinator.MainActivity;
import com.narmware.vvmcoordinator.MyApplication;
import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.pojo.LoginResponse;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;
import com.narmware.vvmcoordinator.support.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_username) EditText mEdtUsername;
    @BindView(R.id.edt_password) EditText mEdtPassword;
    @BindView(R.id.btn_forgot) Button mBtnForgot;
    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.rootview) RelativeLayout mRootView;
    String username,password;
    int validData=0;

    public RequestQueue mVolleyRequest;
    Realm realm;
    RealmResults<Login> login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);

        mBtnForgot.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        MyApplication.config_realm(LoginActivity.this);

        realm = Realm.getInstance(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_forgot:
                break;

            case R.id.btn_login:
                validData=0;

                username=mEdtUsername.getText().toString().trim();
                password=mEdtPassword.getText().toString().trim();

                if(username==null || username.isEmpty())
                {
                    validData=1;
                    mEdtUsername.setError("Enter Username");
                }
                if(password==null || password.isEmpty())
                {
                    validData=1;
                    mEdtPassword.setError("Enter Password");
                }

                if(validData==0)
                {
                    LoginUser();
                }
                break;

        }
    }

    private void LoginUser() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle(Constants.PLEASE_WAIT);
        dialog.setMessage(Constants.LOGIN_DIALOG_TITLE);
        dialog.setCancelable(false);
        dialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("LOGIN RESPONSE",response);

                        try {
                            Gson gson = new Gson();
                            LoginResponse dataResponse = gson.fromJson(response, LoginResponse.class);
                            Login data = dataResponse.getResult();
                            if (dataResponse.getStatus_code().equals(Constants.ERROR)) {
                                Toast.makeText(LoginActivity.this, dataResponse.getError_message(), Toast.LENGTH_SHORT).show();
                            }
                            if (dataResponse.getStatus_code().equals(Constants.SUCCESS)) {
                                SharedPreferencesHelper.setIsLogin(true, LoginActivity.this);
                                realm.beginTransaction();
                                realm.copyToRealm(data);
                                realm.commitTransaction();

                                login=realm.where(Login.class).findAll();

                                if(!login.isEmpty())
                                {
                                    Log.e("Login name",login.get(0).getName());
                                }

                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }catch (Exception e)
                        {e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
                dialog.dismiss();
                showNoConnectionDialog();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.LOGIN);
                params.put(Constants.USERNAME,username);
                params.put(Constants.PASSWORD,password);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    private void ForgetPassword() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.LOGIN);
                params.put(Constants.USERNAME,username);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }


        public void showNoConnectionDialog(){
            Snackbar.make(mRootView, "No internet connection", Snackbar.LENGTH_SHORT)
                /*.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),10);
                    }
                })*/
                    //.setActionTextColor(Color.RED)
                    .show();
        }

}
