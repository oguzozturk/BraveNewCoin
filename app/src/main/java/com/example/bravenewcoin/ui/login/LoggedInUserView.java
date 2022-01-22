package com.example.bravenewcoin.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String accessToken;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String accessToken) {
        this.accessToken = accessToken;
    }

    String getAccessToken() {
        return accessToken;
    }
}