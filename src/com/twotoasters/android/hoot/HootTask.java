/*
 * Copyright (C) 2012 Two Toasters, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twotoasters.android.hoot;

import android.os.AsyncTask;
import android.os.Build;

public class HootTask extends AsyncTask<HootRequest, HootRequest, HootRequest> {

    private HootTransport mTransport = null;

    @Override
    protected HootRequest doInBackground(HootRequest... params) {
        HootRequest request = params[0];

        // let the UI thread get an update so we know we've started the request
        publishProgress(params);

        // create our transport
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            mTransport = new HootTransportHttpUrlConnection();
        } else {
            mTransport = new HootTransportHttpClient();
        }

        do {
            mTransport.synchronousExecute(request);
            if (request.getResult().isSuccess()) {
                return request;
            }
        } while (request.shouldRetry() && !isCancelled());

        return request;
    }

    @Override
    protected void onPostExecute(HootRequest request) {
        mTransport = null;
        if (request != null && request.getListener() != null
                && request.getResult() != null) {
            request.getListener().onRequestCompleted(request);
            HootResult result = request.getResult();
            request.setComplete(true);
            if (result.isSuccess()) {
                request.getListener().onSuccess(request, result);
            } else {
                request.getListener().onFailure(request, result);
            }
        }
    }

    @Override
    protected void onProgressUpdate(HootRequest... values) {
        HootRequest request = values[0];

        if (request != null && request.getListener() != null) {
            request.getListener().onRequestStarted(request);
        }
    }

    void cancel() {
        this.cancel(true);
        if (mTransport != null) {
            mTransport.cancel();
        }
    }
}
