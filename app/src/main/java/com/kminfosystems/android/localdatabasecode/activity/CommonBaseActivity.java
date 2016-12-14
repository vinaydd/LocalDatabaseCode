package com.kminfosystems.android.localdatabasecode.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;

import com.kminfosystems.android.localdatabasecode.R;
import com.kminfosystems.android.localdatabasecode.responce.BaseResponse;
import com.kminfosystems.android.localdatabasecode.utils.AppPreference;
import com.kminfosystems.android.localdatabasecode.utils.ConnectionDetector;


/**
 * Created by root on 10/17/15.
 */
public class CommonBaseActivity extends ActionBarActivity {
    public AppPreference prefs;
    public boolean isErrorResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new AppPreference(getApplicationContext());
        isErrorResponse = false;
    }

    public <T> void processResponse(T result) {
        isErrorResponse = false;
        if (result == null) {
            isErrorResponse = true;
           // Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result instanceof BaseResponse) {
            BaseResponse rsp = (BaseResponse) result;
            if (rsp.isError()) {
                if (rsp.getMessage().equalsIgnoreCase("Access Denied. Invalid Api key")) {
                    showSimpleAlertWithLogout(this, "You have been logged out from this device, since you have logged in from some other device.");
                }
                return;
            }
        }
    }

    public void logout() {
        AppPreference prefs = new AppPreference(getApplicationContext());
        prefs.clearPreferences();
       /* Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/

    }


    public void showSimpleAlertWithMessage(Context context, String msg) {
        new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    public void showSimpleAlertWithLogout(Context context, String msg) {
        new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                })
                .show();
    }

    public boolean isInternetAvailable(Context _context) {
        ConnectionDetector cd = new ConnectionDetector(_context);
        if (!cd.isConnectingToInternet()) {
            ((CommonBaseActivity) _context).showSimpleAlertWithMessage(_context, _context.getResources().getString(R.string.internetnotavaliable));
            return false;
        }
        return true;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

}
