package com.narmware.vvmcoordinator.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.narmware.vvmcoordinator.db.RealmController;
import com.narmware.vvmcoordinator.fragment.NotificationFragment;
import com.narmware.vvmcoordinator.fragment.SchoolListFragment;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;
import com.narmware.vvmcoordinator.pojo.TimeLogResponse;
import com.narmware.vvmcoordinator.realm_classes.RealmRecyclerViewAdapter;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rohitsavant on 20/09/18.
 */

public class SchoolAdapter extends RealmRecyclerViewAdapter<SchoolDetails> {

    private static Context context;
    private static Realm realm;
    public RequestQueue mVolleyRequest;

    public SchoolAdapter(Context context) {
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_school, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder1, final int position) {
        realm = RealmController.getInstance().getRealm();
        //final RealmResults<SchoolDetails> results = realm.where(SchoolDetails.class).findAll();

        final SchoolDetails schoolDetails = getItem(position);
        MyViewHolder holder= (MyViewHolder) viewholder1;

        holder.mTxtId.setText(schoolDetails.getInst_id());
        holder.mTxtName.setText(schoolDetails.getInst_name());
        if(schoolDetails.getExam_coordinator().equals(""))
        {
            holder.mTxtCoName.setText(Constants.NOT_PROVIDED);
        }else{
            holder.mTxtCoName.setText(schoolDetails.getExam_coordinator());
        }
        holder.mTxtContact.setText(schoolDetails.getInst_mobile());
        if(schoolDetails.getCity().equals(""))
        {
            holder.mTxtCity.setText(Constants.NOT_PROVIDED);
        }else{
            holder.mTxtCity.setText(schoolDetails.getCity());
        }
        holder.mTxtUsername.setText(schoolDetails.getEmail());
        holder.mTxtPass.setText(schoolDetails.getPass());

        holder.mTxtPayStatus.setText(schoolDetails.getPayment_status());
        holder.mTxtCallCount.setText("("+schoolDetails.getCall_count()+")");
        holder.mTxtLastCall.setText(schoolDetails.getLast_call());

        String cnt = schoolDetails.getTotal_count()+ " / " + schoolDetails.getPaid_count() + " / " + schoolDetails.getUnpaid_count();
        holder.mCountTextView.setText(cnt);

        if(schoolDetails.getNameofprincipal().equals("") || schoolDetails.getNameofprincipal()==null)
        {
            holder.mTxtProfileStatus.setText(Constants.INCOMPLETE);
            holder.mTxtProfileStatus.setTextColor(context.getResources().getColor(R.color.red_500));
        }
        else{
            holder.mTxtProfileStatus.setText(Constants.COMPLETE);
            holder.mTxtProfileStatus.setTextColor(context.getResources().getColor(R.color.blue_700));
        }

        if(schoolDetails.getCall_count().equals(""))
        {
            holder.mTxtCallCount.setText("(0)");
        }

        if(schoolDetails.getPayment_status().equals(Constants.PAID))
        {
            holder.mTxtPayStatus.setTextColor(context.getResources().getColor(R.color.green_500));
        }
        if(schoolDetails.getPayment_status().equals(Constants.UNPAID))
        {
            holder.mTxtPayStatus.setTextColor(context.getResources().getColor(R.color.red_500));
        }
        holder.mItem=schoolDetails;

        holder.mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int call_count=0;
                realm.beginTransaction();
                if(!schoolDetails.getCall_count().equals(""))
                {
                    call_count= Integer.parseInt(schoolDetails.getCall_count());
                }
                schoolDetails.setCall_count(String.valueOf(call_count+1));
                schoolDetails.setCalled(true);

                Calendar calander = Calendar.getInstance();
                long cDay = calander.get(Calendar.DAY_OF_MONTH);
                long cMonth = calander.get(Calendar.MONTH) + 1;
                long cYear = calander.get(Calendar.YEAR);
                long cHour = calander.get(Calendar.HOUR);
                long cMinute = calander.get(Calendar.MINUTE);
                long cSecond = calander.get(Calendar.AM_PM);
                String am_pm=null;
                if(cSecond==1)
                {
                    am_pm="pm";
                }if(cSecond==0)
                {
                    am_pm="am";
                }
                String current_time="last call: "+cDay+"-"+cMonth+"-"+cYear+" at "+cHour+":"+cMinute+" "+am_pm;
                schoolDetails.setLast_call(current_time);
                realm.commitTransaction();
                notifyDataSetChanged();

                //Log.e("Current date",cDay+"-"+cMonth+"-"+cYear+"on "+cHour+":"+cMinute+" "+cSecond);

                String phone = schoolDetails.getInst_mobile();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                context.startActivity(intent);
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
            TextView mTxtId,mTxtName,mTxtCoName,mTxtContact,mTxtCity,mTxtPayStatus,mTxtCallCount,
                    mTxtLastCall,mTxtProfileStatus, mCountTextView,mTxtUsername,mTxtPass;
            SchoolDetails mItem;
            LinearLayout mBtnCall;
            ImageButton mBtnReport;
            View view;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTxtId=itemView.findViewById(R.id.txt_sch_id);
            mTxtName=itemView.findViewById(R.id.txt_sch_name);
            mTxtCoName=itemView.findViewById(R.id.txt_sch_co_name);
            mTxtContact=itemView.findViewById(R.id.txt_sch_contact);
            mTxtUsername=itemView.findViewById(R.id.txt_sch_username);
            mTxtPass=itemView.findViewById(R.id.txt_sch_password);
            mTxtCity=itemView.findViewById(R.id.txt_sch_city);
            mTxtPayStatus=itemView.findViewById(R.id.txt_pay_status);
            mTxtCallCount=itemView.findViewById(R.id.txt_call_count);
            mTxtLastCall=itemView.findViewById(R.id.txt_last_call);
            mTxtProfileStatus=itemView.findViewById(R.id.txt_profile_status);
            mCountTextView = itemView.findViewById(R.id.txt_profile_paidcnt);
            mBtnCall=itemView.findViewById(R.id.btn_call);
            mBtnReport=itemView.findViewById(R.id.btn_report);

            mBtnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RealmResults<Login> login=realm.where(Login.class).findAll();
                    sendReport(login.get(0).getCo_ordinator_id(),mItem.getInst_id(),mItem.getInst_name(),mItem.getCity(),mItem.getInst_mobile());
                }
            });

            view=itemView;
        }
    }
    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {

            if(getRealmAdapter().getCount()==0)
            {
                SchoolListFragment.mEmptyLinear.setVisibility(View.VISIBLE);
                SchoolListFragment.mTxtTotalCount.setText("0");
            }
            else{
                SchoolListFragment.mEmptyLinear.setVisibility(View.INVISIBLE);
                SchoolListFragment.mTxtTotalCount.setText("Total schools : "+getRealmAdapter().getCount());
            }
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public void sendReport(final String coordid, final String sch_id, final String sch_name, final String sch_city, final String sch_contact) {
        mVolleyRequest = Volley.newRequestQueue(context);

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

                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    public void showNoConnectionDialog(){
        Snackbar.make(SchoolListFragment.mRootView, "No internet connection", Snackbar.LENGTH_SHORT)
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
