package com.example.bravenewcoin.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bravenewcoin.R;
import com.example.bravenewcoin.data.Configs;
import com.example.bravenewcoin.ui.dashboard.DashboardActivity;
import com.example.bravenewcoin.ui.dashboard.asset.Asset;
import com.example.bravenewcoin.ui.dashboard.asset.AssetAdapter;
import com.example.bravenewcoin.ui.dashboard.asset.AssetDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {

        //String welcome = getString(R.string.welcome) + model.getAccessToken();

        try {
            // Make service calls using singleton design pattern

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            // Request a string response from the provided URL.
            JSONObject requestBody = new JSONObject();
            requestBody.put("audience", Configs.audience);
            requestBody.put("client_id", Configs.clientID);
            requestBody.put("grant_type", Configs.grantType);


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Configs.getTokenUrl, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonResponse) {

                            loadingProgressBar.setVisibility(View.GONE);
                            try {
                                String accessToken = jsonResponse.getString("access_token");
                                // Set up token manager mechanism to refresh access token
                                // Produce new access token when time expires
                                long expiresIn = jsonResponse.getLong("expires_in");
                                String scope = jsonResponse.getString("scope");
                                String tokenType = jsonResponse.getString("token_type");

                                // Save access token to call services
                                SharedPreferences.Editor editor = getSharedPreferences(Configs.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("accessToken", accessToken);
                                editor.apply();

                                Intent newIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(newIntent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_LONG).show();
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
                    headers.put("content-type", "application/json");
                    headers.put("x-rapidapi-host", Configs.rapidApiHostHeader);
                    headers.put("x-rapidapi-key", Configs.rapidApiKeyHeader);
                    return headers;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);


        } catch (Exception ex) {
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_LONG).show();
        }

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}