package com.kminfosystems.android.localdatabasecode.responce;


import com.kminfosystems.android.localdatabasecode.model.VenderFilterDataModelnew;

/**
 * Created by root on 7/4/16.
 */
public class VenderFilterResponse extends BaseResponse {

    private VenderFilterDataModelnew[] data;


    public VenderFilterDataModelnew[] getData() {
        return data;
    }

    public void setData(VenderFilterDataModelnew[] data) {
        this.data = data;
    }


}
