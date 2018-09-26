package com.narmware.vvmcoordinator.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.fragment.SchoolListFragment;
import com.narmware.vvmcoordinator.pojo.TimeLogResponse;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PropDialogFragment extends DialogFragment {

    Button mBtnCancel,mBtnSend;
    public RequestQueue mVolleyRequest;
    String coordid,sch_id,sch_name,sch_city,sch_contact;
    @SuppressLint("ValidFragment")
    private PropDialogFragment() { /*empty*/ }

    /** creates a new instance of PropDialogFragment */
    public static PropDialogFragment newInstance() {
        return new PropDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getting proper access to LayoutInflater is the trick. getLayoutInflater is a                   //Function
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_report, null);
        mBtnCancel=view.findViewById(R.id.btn_cancel);
        mBtnSend=view.findViewById(R.id.btn_ok);

        coordid = getArguments().getString(Constants.COORD_ID);
        sch_id = getArguments().getString(Constants.SCH_ID);
        sch_name = getArguments().getString(Constants.SCH_NAME);
        sch_city = getArguments().getString(Constants.SCH_CITY);
        sch_contact = getArguments().getString(Constants.SCH_CONTACT);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
    public void sendReport() {
        mVolleyRequest = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("Report RESPONSE",response);

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
                showNoConnectionDialog();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.SCHOOL_DELETE);
                params.put(Constants.COORD_ID,coordid);
                params.put(Constants.SCH_ID,sch_id);
                params.put(Constants.SCH_NAME,sch_name);
                params.put(Constants.SCH_CITY,sch_city);
                params.put(Constants.SCH_CONTACT,sch_contact);
                params.put(Constants.REMARK,"Fake School");

                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    public void showNoConnectionDialog(){
        Snackbar.make(SchoolListFragment.mRootView, Constants.NO_INTERNET, Snackbar.LENGTH_SHORT)
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