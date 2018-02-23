package com.mikegregory.heyremember;

import android.location.Location;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mikegregory on 2/18/18.
 */

public class LocationService {

    private static final String BASE_URL="https://30d39c32.ngrok.io/";
    private static final String JSON_FORMAT=".json";
    private static final String LOCATION_URL=BASE_URL + "location" + JSON_FORMAT;

    private double latitude;
    private double longitude;
    private App app;
    private OkHttpClient client = new OkHttpClient();

    LocationService(App app, double longitude, double latitude) {
        this.app = app;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static void postLocation(App app, double longitude, double latitude) {
        LocationService locationService = new LocationService(app, longitude, latitude);
        locationService.postLocation();
    }

    private void postLocation() {
        Request request = buildRequest();

        client.newCall(request).enqueue(postCallback());
    }

    private Callback postCallback() {
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("Failed!", "Failed!");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Log.v(res, res);
            }
        };
    }

    private Request buildRequest() {
        RequestBody body = RequestBody.create(jsonMediaType(), buildJsonString());
        Request request = new Request.Builder()
                .url(LOCATION_URL)
                .post(body)
                .build();
        return request;
    }

    private String buildJsonString() {
        JSONObject postBodyJson = new JSONObject();
        try {
            postBodyJson.put("latitude", latitude);
            postBodyJson.put("longitude", longitude);
            postBodyJson.put("android_device_uuid", app.id());
        } catch (JSONException e) {
            Log.v("Building JSON Failed!", "Building JSON Failed!");
        }
        return postBodyJson.toString();
    }

    private MediaType jsonMediaType() {
        return MediaType.parse("application/json; charset=utf-8");
    }
}
