package com.example.bravenewcoin.ui.dashboard.market;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bravenewcoin.R;
import com.example.bravenewcoin.data.Configs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;

public class MarketFragment extends Fragment {

    public MarketFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        final TextView textView = view.findViewById(R.id.textView);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loadingMarket);
        loadingProgressBar.setVisibility(View.VISIBLE);

        // Get access token to call service
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Configs.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String accessToken = prefs.getString("accessToken", "access token not retrieved!");
        //Toast.makeText(getContext(), accessToken, Toast.LENGTH_LONG).show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configs.marketListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingProgressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("content");

                            List<Market> markets = new ArrayList<Market>();
                            for(int i = 0; i < jsonArray.length(); i++) {
                                Market market = new Market();
                                JSONObject jsonMarket = jsonArray.getJSONObject(i);
                                market.setMarketId(jsonMarket.optString("id"));
                                market.setBaseAssetId(jsonMarket.optString("baseAssetId"));
                                market.setQuoteAssetId(jsonMarket.optString("quoteAssetId"));
                                markets.add(market);
                            }

                            ListView marketList = view.findViewById(R.id.marketList);

                            MarketAdapter marketAdapter = new MarketAdapter(getActivity(), markets);
                            marketList.setAdapter(marketAdapter);

                            marketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                    String selected = markets.get(position).getMarketId();

                                    Intent intent = new Intent(getActivity(), MarketDetailActivity.class);
                                    intent.putExtra("id", selected);
                                    startActivity(intent);

                                }});

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingProgressBar.setVisibility(View.GONE);
                textView.setText("That didn't work!");
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

        return view;
    }


}


