package com.example.bravenewcoin.ui.dashboard.market;

public class Market {

    private String marketId;

    private String baseAssetId;

    private String quoteAssetId;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getBaseAssetId() {
        return baseAssetId;
    }

    public void setBaseAssetId(String baseAssetId) {
        this.baseAssetId = baseAssetId;
    }

    public String getQuoteAssetId() {
        return quoteAssetId;
    }

    public void setQuoteAssetId(String quoteAssetId) {
        this.quoteAssetId = quoteAssetId;
    }
}
