package com.kminfosystems.android.localdatabasecode.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kminfosystems.android.localdatabasecode.activity.CommonBaseActivity;
import com.kminfosystems.android.localdatabasecode.fragment.CommonBaseFragment;
import com.kminfosystems.android.localdatabasecode.utils.AppPreference;
import com.kminfosystems.android.localdatabasecode.utils.Constants;

import java.util.HashMap;
import java.util.Map;


public class HttpTask<T> extends AsyncTask<Map<String, Object>, Void, T> {
    private static final String TAG = HttpTask.class.getSimpleName();
    protected ProgressDialog _dialog;
    protected String message = "Loading...";
    Context _context;
    Class<T> _responseType;
    String _requestUrl;
    String methodName;
    private boolean isPost;
    private boolean isForFragment;
    private boolean isUserResponse;
    private boolean isApiRequired = true;
    private String apiKey;
    private CommonBaseFragment mFragment;
    private boolean isToShowProgress = true;


    public HttpTask(Context context, String requestUrl, Class<T> responseType, String methodName) {
        _context = context;
        _requestUrl = requestUrl;
        _responseType = responseType;
        this.methodName = methodName;
        AppPreference prefs = new AppPreference(_context.getApplicationContext());
        apiKey = prefs.getStringValueForTag(Constants.API_KEY);
    }


    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean isPost) {
        this.isPost = isPost;
    }

    public void setIsApiKeyRequired(boolean isReqoured) {
        this.isApiRequired = isReqoured;
    }

    public boolean isFragment() {
        return isForFragment;
    }

    void setFragment(CommonBaseFragment fragment) {
        mFragment = fragment;
    }

    public void setForFragment(boolean isFragment) {
        this.isForFragment = isFragment;
    }

    public void setMessage(String loadingMessage) {
        message = loadingMessage;
    }

    @Override
    public void onPreExecute() {
        _dialog = ProgressDialog.show(_context, "", message, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T doInBackground(Map<String, Object>... params) {
        T result = null;
        String response = "";
        try {
            WebService webService = WebService.getInstance(_requestUrl);

            Map<String, Object> param = null;
            if (params != null && params.length > 0) {
                param = params[0];
            } else {
                param = new HashMap<String, Object>();
            }
            if (isPost) {
                response = webService.webInvoke(methodName, param, null, isApiRequired, apiKey);
            } else {
                response = webService.webGet(methodName, param);
            }
            System.err.println(response);
            result = new Gson().fromJson(response, _responseType);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return result;

    }

    @Override
    protected void onPostExecute(T result) {
        if (_dialog.isShowing()) {
            _dialog.dismiss();
        }
        if (isFragment()) {
            mFragment.processFragmentResponse(result);
        }
        else {
            if(this._context instanceof CommonBaseActivity){
                ((CommonBaseActivity) _context).processResponse(result);
            }

        }
    }

    public void setShowProgress(boolean isToShowProgress) {
        this.isToShowProgress = isToShowProgress;
    }
}
