package com.kminfosystems.android.localdatabasecode.web;

import android.content.Context;

import com.kminfosystems.android.localdatabasecode.R;
import com.kminfosystems.android.localdatabasecode.activity.CommonBaseActivity;
import com.kminfosystems.android.localdatabasecode.responce.VenderFilterResponse;
import com.kminfosystems.android.localdatabasecode.utils.ConnectionDetector;
import com.kminfosystems.android.localdatabasecode.utils.Constants;

import java.util.HashMap;

/**
 * Created by ios4_dev on 8/29/15.
 */
public class WebManager {
    private static final String IS_FREE_APP = "isFree";
    private static WebManager singleInstance;

    protected WebManager() {
    }

    public static WebManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new WebManager();
        }
        return singleInstance;
    }


    public void vendorFilteringFra(Context context,String what, String who ) {
        HttpTask<VenderFilterResponse> events = new HttpTask<>(context, Constants.HOST_URL, VenderFilterResponse.class, Constants.VENDER_FILTERING_URL);
        HashMap<String, Object> parms = new HashMap<String, Object>();
        parms.put("what", what);
        parms.put("whom", who);
        events.setPost(true);
        events.setIsApiKeyRequired(false);
        if (isInternetAvailable(context)) {
            events.execute(parms);
        }
    }

    private boolean isInternetAvailable(Context _context) {
        ConnectionDetector cd = new ConnectionDetector(_context);
        if (!cd.isConnectingToInternet()) {
          //  ((SlidingMenuActvity) _context).showSimpleAlertWithMessage(_context, _context.getResources().getString(R.string.no_net));
            return false;
        }
        return true;
    }

    private boolean isInternetAvailableFromCommonBase(Context _context) {
        ConnectionDetector cd = new ConnectionDetector(_context);
        if (!cd.isConnectingToInternet()) {
            ((CommonBaseActivity) _context).showSimpleAlertWithMessage(_context, _context.getResources().getString(R.string.no_net));
            return false;
        }
        return true;
    }

}
