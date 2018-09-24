package com.narmware.vvmcoordinator.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmcoordinator.MyApplication;
import com.narmware.vvmcoordinator.R;
import com.narmware.vvmcoordinator.fragment.NotificationFragment;
import com.narmware.vvmcoordinator.fragment.ProfileFragment;
import com.narmware.vvmcoordinator.fragment.SchoolListFragment;
import com.narmware.vvmcoordinator.pojo.Login;
import com.narmware.vvmcoordinator.support.Constants;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements SchoolListFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener,
ProfileFragment.OnFragmentInteractionListener{

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(new SchoolListFragment());
                    return true;
                case R.id.navigation_profile:
                    setFragment(new ProfileFragment());
                    return true;
                case R.id.navigation_notifications:
                    setFragment(new NotificationFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(new SchoolListFragment());
    }

    public void setFragment(Fragment fragment)
    {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        //MyApplication.setLogTime(HomeActivity.this, Constants.LOGOUT_EVENT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // MyApplication.setLogTime(HomeActivity.this, Constants.LOGOUT_EVENT);
    }
}
