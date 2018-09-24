package com.narmware.vvmcoordinator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.narmware.vvmcoordinator.MyApplication;
import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.db.RealmController;
import com.narmware.vvmcoordinator.fragment.SchoolListFragment;
import com.narmware.vvmcoordinator.support.Constants;
import com.narmware.vvmcoordinator.support.SharedPreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.pay_rad_grp) RadioGroup mRadGrpPay;
    @BindView(R.id.rad_paid) RadioButton mRadBtnPaid;
    @BindView(R.id.rad_unpaid) RadioButton mRadBtnUnpaid;
    @BindView(R.id.rad_all) RadioButton mRadBtnAll;
    @BindView(R.id.chk_contact) CheckBox mChkContacted;
    @BindView(R.id.chk_not_contact) CheckBox mChkNotContacted;
    @BindView(R.id.btn_apply_filter) Button mBtnApply;

    boolean isPaid=false;
    boolean isUnpaid=false;
    boolean isAll=false;
    boolean isContacted=false;
    boolean isNotContacted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        init();
        if(SharedPreferencesHelper.getIsPaid(FilterActivity.this)==true)
        {
            mRadBtnPaid.setChecked(true);
            isPaid=true;
        }
        if(SharedPreferencesHelper.getIsUnpaid(FilterActivity.this)==true)
        {
            mRadBtnUnpaid.setChecked(true);
            isUnpaid=true;
        }
        if(SharedPreferencesHelper.getIsAll(FilterActivity.this)==true)
        {
            mRadBtnAll.setChecked(true);
            isAll=true;
        }

        if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true)
        {
            mChkContacted.setChecked(true);
            isContacted=true;
        }
        if(SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
        {
            mChkNotContacted .setChecked(true);
            isNotContacted=true;
        }
    }

    private void init() {

        mRadGrpPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if(checkedId==R.id.rad_paid)
                {
                    //Toast.makeText(FilterActivity.this, "Paid", Toast.LENGTH_SHORT).show();
                    isPaid=true;
                    SharedPreferencesHelper.setIsUnpaid(false,FilterActivity.this);
                    SharedPreferencesHelper.setIsAll(false,FilterActivity.this);
                    SharedPreferencesHelper.setIsPaid(true,FilterActivity.this);
                }
                if(checkedId==R.id.rad_unpaid)
                {
                    //Toast.makeText(FilterActivity.this, "UnPaid", Toast.LENGTH_SHORT).show();
                    isUnpaid=true;
                    SharedPreferencesHelper.setIsPaid(false,FilterActivity.this);
                    SharedPreferencesHelper.setIsAll(false,FilterActivity.this);
                    SharedPreferencesHelper.setIsUnpaid(true,FilterActivity.this);
                }
                if(checkedId==R.id.rad_all)
                {
                    //Toast.makeText(FilterActivity.this, "All", Toast.LENGTH_SHORT).show();
                    isAll=true;
                    SharedPreferencesHelper.setIsUnpaid(false,FilterActivity.this);
                    SharedPreferencesHelper.setIsPaid(false,FilterActivity.this);
                    SharedPreferencesHelper.setIsAll(true,FilterActivity.this);
                }
            }
        });

        mChkContacted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(FilterActivity.this, ""+isChecked, Toast.LENGTH_SHORT).show();

                if(isChecked==true)
                {
                    isContacted=true;
                    SharedPreferencesHelper.setIsContacted(true,FilterActivity.this);
                }
                else{
                    isContacted=false;
                    SharedPreferencesHelper.setIsContacted(false,FilterActivity.this);
                }
            }
        });

        mChkNotContacted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(FilterActivity.this, ""+isChecked, Toast.LENGTH_SHORT).show();

                if(isChecked==true)
                {
                    isNotContacted=true;
                    SharedPreferencesHelper.setIsNotContacted(true,FilterActivity.this);
                }
                else{
                    isNotContacted=false;
                    SharedPreferencesHelper.setIsNotContacted(false,FilterActivity.this);
                }
            }
        });

        mBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedPreferencesHelper.getIsPaid(FilterActivity.this)==true)
                {
                    SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getPaidSchool());

                    if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true && SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getPaidSchool());
                    }
                    if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getPaidSchool(true));
                    }
                    if(SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getPaidSchool(false));
                    }

                }

                if(SharedPreferencesHelper.getIsUnpaid(FilterActivity.this)==true)
                {
                    SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getUnpaidSchool());

                    if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true && SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getUnpaidSchool());
                    }
                    if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getUnpaidSchool(true));
                    }
                    if(SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getUnpaidSchool(false));
                    }
                }

                if(SharedPreferencesHelper.getIsAll(FilterActivity.this)==true)
                {
                    SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getSchoolDetailss());

                    if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true && SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getSchoolDetailss());
                    }
                    if(SharedPreferencesHelper.getIsContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getSchoolDetailss(true));
                    }
                    if(SharedPreferencesHelper.getIsNotContacted(FilterActivity.this)==true)
                    {
                        SchoolListFragment.setRealmAdapter(RealmController.with(FilterActivity.this).getSchoolDetailss(false));
                    }
                }


                finish();
            }
        });
    }


 /*   @Override
    protected void onPause() {
        super.onPause();
        MyApplication.setLogTime(FilterActivity.this, Constants.LOGOUT_EVENT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setLogTime(FilterActivity.this, Constants.LOGOUT_EVENT);
    }*/
}
