package com.narmware.vvmcoordinator.realm_classes;

import android.content.Context;

import com.narmware.vvmcoordinator.pojo.SchoolDetails;

import io.realm.RealmResults;

public class RealmSchoolAdapter extends RealmModelAdapter<SchoolDetails> {
 
    public RealmSchoolAdapter(Context context, RealmResults<SchoolDetails> realmResults, boolean automaticUpdate) {
 
        super(context, realmResults, automaticUpdate);
    }
}