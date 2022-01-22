package com.example.bravenewcoin.ui.dashboard.asset;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bravenewcoin.R;
import com.example.bravenewcoin.data.Configs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class AssetDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_asset);

        final TextView assetId = findViewById(R.id.assetId);
        final TextView assetName = findViewById(R.id.assetName);
        final TextView assetSymbol = findViewById(R.id.assetSymbol);
        final TextView assetSlugName = findViewById(R.id.assetSlugName);
        final TextView assetStatus = findViewById(R.id.assetStatus);
        final TextView assetType = findViewById(R.id.assetType);
        final TextView assetUrl = findViewById(R.id.assetUrl);

        final ProgressBar loadingProgressBar = findViewById(R.id.loadingAssetDetail);
        loadingProgressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configs.assetDetailUrl + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingProgressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonMarket = new JSONObject(response);
                            assetId.setText("ID: " + jsonMarket.optString("id"));
                            assetName.setText("Name: " + jsonMarket.optString("name"));
                            assetSymbol.setText("Symbol: " + jsonMarket.optString("symbol"));
                            assetSlugName.setText("Slug Name: " + jsonMarket.optString("slugName"));
                            assetStatus.setText("Status: " + jsonMarket.optString("status"));
                            assetType.setText("Type: " + jsonMarket.optString("type"));
                            assetUrl.setText("Url: " + jsonMarket.optString("url"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.GONE);
                assetId.setText("That didn't work!");
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-rapidapi-host", Configs.rapidApiHostHeader);
                headers.put("x-rapidapi-key", Configs.rapidApiKeyHeader);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
