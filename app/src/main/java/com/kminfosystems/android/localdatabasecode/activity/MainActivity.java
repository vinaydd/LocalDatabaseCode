package com.kminfosystems.android.localdatabasecode.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kminfosystems.android.localdatabasecode.R;
import com.kminfosystems.android.localdatabasecode.adapter.BooksAdapter;
import com.kminfosystems.android.localdatabasecode.adapter.RealmBooksAdapter;
import com.kminfosystems.android.localdatabasecode.model.VenderFilterDataModel;
import com.kminfosystems.android.localdatabasecode.model.VenderFilterDataModelnew;
import com.kminfosystems.android.localdatabasecode.realm.RealmController;
import com.kminfosystems.android.localdatabasecode.responce.VenderFilterResponse;
import com.kminfosystems.android.localdatabasecode.utils.Prefs;
import com.kminfosystems.android.localdatabasecode.web.WebManager;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends CommonBaseActivity {
    private BooksAdapter adapter;
    private Realm realm;
    private ArrayList<VenderFilterDataModelnew> venderArray;
    private RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        if (!Prefs.with(this).getPreLoad()) {
            WebManager.getInstance().vendorFilteringFra(this,"0","0");
        }
        setupRecycler();
        RealmController.with(this).refresh();
        setRealmAdapter(RealmController.with(this).getBooks());
    }
    private void setRealmDataFromList(ArrayList<VenderFilterDataModelnew> list) {
        for (VenderFilterDataModelnew b : list) {
            VenderFilterDataModel vnf = new VenderFilterDataModel();
            vnf.setVendor_id(b.getVendor_id());
            vnf.setVendor_name(b.getVendor_name());
            vnf.setVendor_image(b.getVendor_image());
            vnf.setFollowers_count(b.getFollowers_count());
            vnf.setUser_following(b.getUser_following());
            vnf.setVendor_logo(b.getVendor_logo());
            vnf.setVendor_rating(b.getVendor_rating());
            realm.beginTransaction();
            realm.copyToRealm(vnf);
            realm.commitTransaction();
        }
        Prefs.with(this).setPreLoad(true);
        setRealmAdapter(RealmController.with(this).getBooks());
    }
    public void setRealmAdapter(RealmResults<VenderFilterDataModel> books) {
        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), books, true);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }
    private void setupRecycler() {
        recycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        adapter = new BooksAdapter(this);
        recycler.setAdapter(adapter);
    }
    @Override
    public <T> void processResponse(T result) {
        super.processResponse(result);
        if (!isErrorResponse) {
            if (result instanceof VenderFilterResponse) {
                VenderFilterResponse resp = (VenderFilterResponse) result;
                VenderFilterDataModelnew[] modelss = resp.getData();
                venderArray = new ArrayList<VenderFilterDataModelnew>(Arrays.asList(modelss));
                //showSimpleAlertWithMessage(this, resp.getMessage());
                 setRealmDataFromList(venderArray);
            }
        }

    }



}