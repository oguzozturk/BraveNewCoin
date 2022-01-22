package com.example.bravenewcoin.ui.dashboard.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bravenewcoin.R;

import java.util.List;

public class MarketAdapter extends ArrayAdapter<Market> {

    private Context context;
    private List<Market> markets = null;

    public MarketAdapter(Context context,  List<Market> markets) {
        super(context, 0, markets);
        this.markets = markets;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Market market = markets.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_market, null);

        final TextView marketId = view.findViewById(R.id.marketId);
        marketId.setText("ID: " + market.getMarketId());

        final TextView baseAssetId = view.findViewById(R.id.baseAssetId);
        baseAssetId.setText("Base Asset ID: " + market.getBaseAssetId());

        final TextView quoteAssetId = view.findViewById(R.id.quoteAssetId);
        quoteAssetId.setText("Quote Asset ID: " + market.getQuoteAssetId());

        return view;
    }
}