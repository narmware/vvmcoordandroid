package com.narmware.vvmcoordinator.db;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.narmware.vvmcoordinator.pojo.NotificationItems;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;
import com.narmware.vvmcoordinator.support.Constants;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
 
    private static RealmController instance;
    private final Realm realm;
 
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }
 
    public static RealmController with(Fragment fragment) {
 
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }
 
    public static RealmController with(Activity activity) {
 
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }
 
   /* public static RealmController with(Application application) {
 
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }
 */
    public static RealmController getInstance() {
 
        return instance;
    }
 
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }
 
    //clear all objects from SchoolDetails.class
    public void clearAll() {
 
        realm.beginTransaction();
        realm.clear(SchoolDetails.class);
        realm.commitTransaction();
    }
 
    //find all objects in the SchoolDetails.class
    public RealmResults<SchoolDetails> getSchoolDetailss() {
 
        return realm.where(SchoolDetails.class).findAll();
    }

    public RealmResults<SchoolDetails> getSchoolDetailss(boolean isContacted) {

        return realm.where(SchoolDetails.class)
                .equalTo("isCalled",isContacted)
                .findAll();
    }

    public RealmResults<NotificationItems> getNotificationDetails() {

       // return realm.where(NotificationItems.class).findAll();

        return realm.where(NotificationItems.class).findAll();
    }

    public RealmResults<SchoolDetails> getUnpaidSchool() {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.UNPAID)
                .findAll();
    }

    public RealmResults<SchoolDetails> getPaidSchool() {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.PAID)
                .findAll();
    }

    public RealmResults<SchoolDetails> getPaidSchool(boolean isContacted) {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.PAID)
                .equalTo("isCalled",isContacted)
                .findAll();
    }
    public RealmResults<SchoolDetails> getUnpaidSchool(boolean isCalled) {

        return realm.where(SchoolDetails.class)
                .equalTo("payment_status", Constants.UNPAID)
                .equalTo("isCalled",isCalled)
                .findAll();
    }
 
    //query a single item with the given id
    public SchoolDetails getSchoolDetails(String id) {
 
        return realm.where(SchoolDetails.class).equalTo("id", id).findFirst();
    }

    //query example
    public RealmResults<SchoolDetails> queryedSchoolDetailss() {
 
        return realm.where(SchoolDetails.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();
 
    }
}