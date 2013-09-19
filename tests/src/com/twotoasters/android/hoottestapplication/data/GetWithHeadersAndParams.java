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

package com.twotoasters.android.hoottestapplication.data;

import org.json.JSONException;
import org.json.JSONObject;

public class GetWithHeadersAndParams {
    public String headers;
    public GetWithParams params;

    public static GetWithHeadersAndParams fromJson(JSONObject json) {
        GetWithHeadersAndParams data = new GetWithHeadersAndParams();
        data.headers = json.optString("headers");
        try {
            data.params = GetWithParams.fromJson(json.getJSONObject("params"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
