package com.kminfosystems.android.localdatabasecode.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kminfosystems.android.localdatabasecode.R;
import com.kminfosystems.android.localdatabasecode.responce.BaseResponse;
import com.kminfosystems.android.localdatabasecode.utils.AppPreference;

/**
 * Created by ios4_dev on 11/19/15.
 */
public class CommonBaseFragment extends Fragment {

    public AppPreference prefs;
    public boolean isErrorResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new AppPreference(getActivity().getApplicationContext());
        isErrorResponse = false;
    }


    public void nestedReplaceFragment(int container, Fragment newFragment, boolean backStackTag, String fragmentTag) {
        try {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                transaction.add(container, newFragment, fragmentTag);
            } else {
                transaction.replace(container, newFragment, fragmentTag);
            }
            if (backStackTag) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } catch (Exception e) {
            Log.e("exception ", " on adding fragment");
            // TODO: handle exception
        }
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    public void clearStack() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    public <T> void processFragmentResponse(T result) {
        isErrorResponse = false;
        if (result == null) {
            isErrorResponse = true;
            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result instanceof BaseResponse) {
            BaseResponse rsp = (BaseResponse) result;
            if (rsp.isError()) {
                if (rsp.getMessage().equalsIgnoreCase("Access Denied. Invalid Api key")) {
                    showSimpleAlertWithMessage(getActivity(), "You have been logged out from this device, since you have logged in from some other device.");
                }

                return;
            }
           /* if (rsp.isError()) {
                isErrorResponse = true;
                Toast.makeText(getActivity(), rsp.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }*/

        }
    }


    public void showSimpleAlertWithMessage(Context context, String msg) {
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


    private void logout() {
        prefs.clearPreferences();
     /*   Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
*/

    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }
}