package com.narmware.vvmcoordinator.realm_classes;

import android.content.Context;

import com.narmware.vvmcoordinator.pojo.NotificationItems;
import com.narmware.vvmcoordinator.pojo.SchoolDetails;

import io.realm.RealmResults;

public class RealmNotificationAdapter extends RealmModelAdapter<NotificationItems> {

    public RealmNotificationAdapter(Context context, RealmResults<NotificationItems> realmResults, boolean automaticUpdate) {
 
        super(context, realmResults, automaticUpdate);
    }
}