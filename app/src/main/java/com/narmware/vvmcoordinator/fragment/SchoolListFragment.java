package com.narmware.vvmcoordinator.fragment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import com.narmware.vvmcoordinator.MainActivity;
import com.narmware.vvmcoordinator.MyApplication;
import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.activity.FilterActivity;
import com.narmware.vvmcoordinator.adapter.SchoolAdapter;
import com.narmware.vvmcoordinator.db.RealmController;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.pojo.NotificationItems;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;
import com.narmware.vvmcoordinator.pojo.SchoolResponse;
import com.narmware.vvmcoordinator.realm_classes.RealmSchoolAdapter;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.EndPoints;
import com.narmware.vvmcoordinator.support.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchoolListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SchoolListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchoolListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RequestQueue mVolleyRequest;
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    public static RelativeLayout mRootView;
    @BindView(R.id.fab_filter) FloatingActionButton mFabFilter;

    @BindView(R.id.simpleSearchView) SearchView searchView;
    public static TextView mTxtTotalCount, mTxtTotalCountStudent;
    public static RelativeLayout mEmptyLinear;
    public static SchoolAdapter schoolAdapter;
    Realm realm;
    ArrayList<SchoolDetails> schoolDetails;
    public static Context mContext;

    boolean isPaid=false;
    boolean isUnpaid=false;
    boolean isAll=false;
    boolean isContacted=false;
    boolean isNotContacted=false;

    public SchoolListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchoolListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SchoolListFragment newInstance(String param1, String param2) {
        SchoolListFragment fragment = new SchoolListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_school_list, container, false);
        mContext=getContext();
        init(view);
        return view;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mVolleyRequest = Volley.newRequestQueue(getContext());
        //getSchools();
        mEmptyLinear=view.findViewById(R.id.lin_empty);
        mTxtTotalCount=view.findViewById(R.id.txt_total_count);
        mTxtTotalCountStudent=view.findViewById(R.id.txt_total_count_students);
        mRootView=view.findViewById(R.id.rootview);

        MyApplication.config_realm(getContext());

        schoolAdapter=new SchoolAdapter(getContext(),getActivity().getSupportFragmentManager());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(schoolAdapter);
        schoolDetails = new ArrayList<>();

        realm = Realm.getInstance(getContext());
        RealmResults<Login> login=realm.where(Login.class).findAll();
        if(!login.isEmpty())
        {
            Log.e("Login name",login.get(0).getName()+"   "+login.get(0).getMobile()+"  "+login.get(0).getState());
        }

       // if(RealmController.with(this).getSchoolDetailss().isEmpty()) {


        RealmController.with(this).refresh();
        setRealmAdapter(RealmController.with(this).getSchoolDetailss());
        checkAndSetFilters();
        //setRealmAdapter(RealmController.with(this).getUnpaidSchool());

        getSchools(login.get(0).getState_id());

        mFabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FilterActivity.class);
                startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setRealmAdapter(RealmController.with(getActivity()).queryedSchoolDetailss(newText));
                return true;
            }
        });
    }

    private void checkAndSetFilters() {

        isPaid=SharedPreferencesHelper.getIsPaid(getContext());
        isUnpaid=SharedPreferencesHelper.getIsUnpaid(getContext());
        isAll=SharedPreferencesHelper.getIsAll(getContext());
        isContacted=SharedPreferencesHelper.getIsContacted(getContext());
        isNotContacted=SharedPreferencesHelper.getIsNotContacted(getContext());

        if(SharedPreferencesHelper.getIsPaid(getActivity())==true)
        {
            SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getPaidSchool());

            if(SharedPreferencesHelper.getIsContacted(getActivity())==true && SharedPreferencesHelper.getIsNotContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getPaidSchool());
            }
            if(SharedPreferencesHelper.getIsContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getPaidSchool(true));
            }
            if(SharedPreferencesHelper.getIsNotContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getPaidSchool(false));
            }

        }

        if(SharedPreferencesHelper.getIsUnpaid(getActivity())==true)
        {
            SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getUnpaidSchool());

            if(SharedPreferencesHelper.getIsContacted(getActivity())==true && SharedPreferencesHelper.getIsNotContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getUnpaidSchool());
            }
            if(SharedPreferencesHelper.getIsContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getUnpaidSchool(true));
            }
            if(SharedPreferencesHelper.getIsNotContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getUnpaidSchool(false));
            }
        }

        if(SharedPreferencesHelper.getIsAll(getActivity())==true)
        {
            SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getSchoolDetailss());

            if(SharedPreferencesHelper.getIsContacted(getActivity())==true && SharedPreferencesHelper.getIsNotContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getSchoolDetailss());
            }
            if(SharedPreferencesHelper.getIsContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getSchoolDetailss(true));
            }
            if(SharedPreferencesHelper.getIsNotContacted(getActivity())==true)
            {
                SchoolListFragment.setRealmAdapter(RealmController.with(getActivity()).getSchoolDetailss(false));
            }
        }

    }

    public static void setRealmAdapter(RealmResults<SchoolDetails> books) {

        RealmSchoolAdapter realmAdapter = new RealmSchoolAdapter(mContext, books, true);
        // Set the data and tell the RecyclerView to draw
        schoolAdapter.setRealmAdapter(realmAdapter);
        schoolAdapter.notifyDataSetChanged();
    }

    private void getSchools(final String state_id) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle(Constants.PLEASE_WAIT);
        dialog.setMessage(Constants.GETTING_SCHOOLS);
        dialog.setCancelable(false);
        dialog.show();

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
                            SchoolDetails single_item=realm.where(SchoolDetails.class).equalTo("inst_id",item.getInst_id()).findFirst();
                            if(single_item==null)
                            {
                                realm.beginTransaction();
                                realm.copyToRealm(item);
                                realm.commitTransaction();
                            }
                            else{

                                //inst_name,inst_mobile,inst_id,nameofprincipal,exam_coordinator,inst_email,payment_status,city
                                realm.beginTransaction();
                                single_item.setInst_id(item.getInst_id());
                                single_item.setInst_name(item.getInst_name());
                                single_item.setInst_mobile(item.getInst_mobile());
                                single_item.setNameofprincipal(item.getNameofprincipal());
                                single_item.setExam_coordinator(item.getExam_coordinator());
                                single_item.setPayment_status(item.getPayment_status());
                                single_item.setCity(item.getCity());
                                realm.commitTransaction();
                        }

                        }
                        schoolAdapter.notifyDataSetChanged();
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
                params.put("key", Constants.API_KEY);
                params.put("param",Constants.SCHOOL);
                params.put(Constants.STATE_ID,state_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showNoConnectionDialog() {
        Snackbar.make(mRootView, Constants.NO_INTERNET, Snackbar.LENGTH_SHORT)
               /* .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),10);
                    }
                })*/
                //.setActionTextColor(Color.RED)
                .show();
    }
}
