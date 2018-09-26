package com.narmware.vvmcoordinator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmcoordinator.adapter.SchoolAdapter;
import com.narmware.vvmcoordinator.db.RealmController;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;
import com.narmware.vvmcoordinator.pojo.SchoolResponse;
import com.narmware.vvmcoordinator.realm_classes.RealmSchoolAdapter;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    RequestQueue mVolleyRequest;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.btn_unpaid) Button mBtnUnpaid;

    SchoolAdapter schoolAdapter;
    Realm realm;
    ArrayList<SchoolDetails> schoolDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVolleyRequest = Volley.newRequestQueue(MainActivity.this);
        //getSchools();

       // MyApplication.config_realm(MainActivity.this);


        init();
    }

    private void init() {

        ButterKnife.bind(this);
        schoolAdapter=new SchoolAdapter(MainActivity.this,getSupportFragmentManager());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(schoolAdapter);
        schoolDetails = new ArrayList<>();

        realm = Realm.getInstance(this);
        RealmResults<Login> login=realm.where(Login.class).findAll();
        if(!login.isEmpty())
        {
            Log.e("Login name",login.get(0).getName()+"   "+login.get(0).getMobile()+"  "+login.get(0).getState());
        }

        if(RealmController.with(this).getSchoolDetailss().isEmpty()) {

            getSchools();
        }

        mBtnUnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setRealmAdapter(RealmController.with(MainActivity.this).getUnpaidSchool());
            }
        });
        RealmController.with(this).refresh();
        setRealmAdapter(RealmController.with(this).getSchoolDetailss());

        // Obtain realm instance
        //RealmController.with(this).clearAll();

    }

    public void setRealmAdapter(RealmResults<SchoolDetails> books) {

        RealmSchoolAdapter realmAdapter = new RealmSchoolAdapter(this.getApplicationContext(), books, true);
        // Set the data and tell the RecyclerView to draw
        schoolAdapter.setRealmAdapter(realmAdapter);
        schoolAdapter.notifyDataSetChanged();
    }

    private void getSchools() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();
                        SchoolResponse dataResponse=gson.fromJson(response,SchoolResponse.class);
                        SchoolDetails[] array=dataResponse.getResult();

                        for(SchoolDetails item:array)
                        {
                            realm.beginTransaction();
                            realm.copyToRealm(item);
                            realm.commitTransaction();
                        }
                        schoolAdapter.notifyDataSetChanged();
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
                params.put("key",Constants.API_KEY);
                params.put("param",Constants.SCHOOL);
                params.put(Constants.STATE_ID, "21");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

}
