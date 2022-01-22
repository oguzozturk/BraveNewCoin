package com.example.bravenewcoin.ui.dashboard.market;

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

public class MarketDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_market);

        final TextView marketId = findViewById(R.id.marketId);
        final TextView baseAssetId = findViewById(R.id.baseAssetId);
        final TextView quoteAssetId = findViewById(R.id.quoteAssetId);

        final ProgressBar loadingProgressBar = findViewById(R.id.loadingMarketDetail);
        loadingProgressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configs.marketDetailUrl + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loadingProgressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonMarket = new JSONObject(response);
                            marketId.setText("ID: " + jsonMarket.optString("id"));
                            baseAssetId.setText("Base Asset ID: " + jsonMarket.optString("baseAssetId"));
                            quoteAssetId.setText("Quote Asset ID: " + jsonMarket.optString("quoteAssetId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.GONE);
                marketId.setText("That didn't work!");
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
