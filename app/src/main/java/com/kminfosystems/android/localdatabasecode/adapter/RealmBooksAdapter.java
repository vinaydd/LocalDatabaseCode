package com.kminfosystems.android.localdatabasecode.adapter;

import android.content.Context;

import com.kminfosystems.android.localdatabasecode.model.VenderFilterDataModel;

import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<VenderFilterDataModel> {

    public RealmBooksAdapter(Context context, RealmResults<VenderFilterDataModel> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}