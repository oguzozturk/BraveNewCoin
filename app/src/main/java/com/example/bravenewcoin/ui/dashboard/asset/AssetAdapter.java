package com.example.bravenewcoin.ui.dashboard.asset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bravenewcoin.R;
import com.example.bravenewcoin.ui.dashboard.market.Market;

import java.util.List;

public class AssetAdapter extends ArrayAdapter<Asset> {

    private Context context;
    private List<Asset> assets = null;

    public AssetAdapter(Context context,  List<Asset> assets) {
        super(context, 0, assets);
        this.assets = assets;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Asset asset = assets.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_asset, null);

        final TextView assetId = view.findViewById(R.id.assetId);
        assetId.setText("ID: " + asset.getId());

        final TextView assetName = view.findViewById(R.id.assetName);
        assetName.setText("Name: " + asset.getName());

        final TextView assetSymbol = view.findViewById(R.id.assetSymbol);
        assetSymbol.setText("Symbol: " + asset.getSymbol());

        final TextView assetSlugName = view.findViewById(R.id.assetSlugName);
        assetSlugName.setText("Slug Name: " + asset.getSlugName());

        final TextView assetStatus = view.findViewById(R.id.assetStatus);
        assetStatus.setText("Status: " + asset.getStatus());

        final TextView assetType = view.findViewById(R.id.assetType);
        assetType.setText("Type: " + asset.getType());

        final TextView assetUrl = view.findViewById(R.id.assetUrl);
        assetUrl.setText("Url: " + asset.getUrl());

        return view;
    }
}