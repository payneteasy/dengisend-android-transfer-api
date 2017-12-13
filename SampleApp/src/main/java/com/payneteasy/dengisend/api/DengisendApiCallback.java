package com.payneteasy.dengisend.api;


import java.util.List;
import java.util.Map;

import com.payneteasy.android.ApiCallback;
import com.payneteasy.android.ApiException;

//import com.payneteasy.android.ApiCallback;
//import com.payneteasy.android.ApiException;


/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 12/10/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public abstract class DengisendApiCallback<T> implements ApiCallback<T> {
    @Override
    public abstract void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders);

    @Override
    public abstract void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders);

    @Override
    public void onUploadProgress(long bytesWritten, long contentLength, boolean done) { }

    @Override
    public void onDownloadProgress(long bytesRead, long contentLength, boolean done) { }
}
