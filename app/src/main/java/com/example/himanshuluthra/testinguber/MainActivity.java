package com.example.himanshuluthra.testinguber;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.core.auth.AccessTokenManager;
import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.core.auth.LoginCallback;
import com.uber.sdk.android.core.auth.LoginManager;
import com.uber.sdk.core.auth.AccessToken;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.Session;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.error.ApiError;
import com.uber.sdk.rides.client.error.ErrorParser;
import com.uber.sdk.rides.client.model.Product;
import com.uber.sdk.rides.client.model.ProductsResponse;
import com.uber.sdk.rides.client.model.RideEstimate;
import com.uber.sdk.rides.client.model.RideEstimate.Price;
import com.uber.sdk.rides.client.model.RideRequestParameters;
import com.uber.sdk.rides.client.model.UserProfile;
import com.uber.sdk.rides.client.services.RidesService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationCallback, Callback<RideEstimate> {

    String TAG = "MainActivity";
    LoginManager loginManager;
    RidesService service;
    AccessToken uberAccessToken;
    Button uberLoginButton;
    Button uberRequestButton;
    List<RideEstimate> mRideEstimate;
    List<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        loginManager = null;
        uberAccessToken = null;
        service = null;
        mRideEstimate = new ArrayList<>();
        uberLoginButton = (Button)findViewById(R.id.LoginButton);
        uberLoginButton.setOnClickListener(this);

        uberRequestButton = (Button)findViewById(R.id.RequestButton);
        uberRequestButton.setOnClickListener(this);

        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("XBEKt2PF4uwZnJgVGqwUl-57Lc_GUNds")
                // required for enhanced button features
//                .setServerToken("KWlDOTvtjv9XQ5_eCxYbPSZLEGmh_PxbsXhe_9Tn")
                // required for implicit grant authentication
                .setRedirectUri("https://www.google.com")
                // required scope for Ride Request Widget features
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.REQUEST))
                // optional: set Sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        UberSdk.initialize(config);

        final AccessTokenManager accessTokenManager = new AccessTokenManager(getApplicationContext());
        loginManager = new LoginManager(accessTokenManager, new LoginCallback() {
            @Override
            public void onLoginCancel() {
                Log.d(TAG, "onLoginCancel");
            }

            @Override
            public void onLoginError(@NonNull AuthenticationError error) {
                Log.d(TAG, "onLoginError");
            }

            @Override
            public void onLoginSuccess(@NonNull AccessToken accessToken) {
                Log.d(TAG, "onLoginSuccess");
                uberAccessToken = accessToken;
            }

            @Override
            public void onAuthorizationCodeReceived(@NonNull String authorizationCode) {
                Log.d(TAG, "onAuthorizationCodeReceived");
            }
        });
        Session session = loginManager.getSession();
        service = UberRidesApi.with(session).build().createService();
        LocationRetriver.getInstance(this).getLocation(this);
    }

    private void getUserProfile() {
        service.getUserProfile().enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "User Profile Retreived");
                    UserProfile userProfile = response.body();
                } else {
                    Log.d(TAG, "User Profile Retreived Error");
                    ApiError error = ErrorParser.parseError(response);
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LocationRetriver.getInstance(this).onActivityResult(requestCode, resultCode, data);
        if (loginManager != null) {
            loginManager.onActivityResult(this, requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LoginButton: {
                loginManager.login(this);
                loginManager.setRedirectForAuthorizationCode(true);
                break;
            }

            case R.id.RequestButton: {

                break;
            }
        }
    }

    private void requestCab() {

    }

    private void retreiveFares(Location pickUpLocation, List<Product> products) {
        for(Product product : mProducts) {
            RideRequestParameters rideRequestParameters = new RideRequestParameters.Builder().setPickupCoordinates(Float.parseFloat(String.valueOf(pickUpLocation.getLatitude())), Float.parseFloat(String.valueOf(pickUpLocation.getLongitude())))
                    .setProductId(products.get(0).getProductId())
                    .setDropoffCoordinates(28.38f, 77.12f)
                    .build();
            service.estimateRide(rideRequestParameters).enqueue(this);
        }
    }

    private void retreiveProducts(final Location location) {
        service.getProducts(Float.parseFloat(String.valueOf(location.getLatitude())), Float.parseFloat(String.valueOf(location.getLongitude()))).enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if(response.isSuccessful()) {
                    mProducts = response.body().getProducts();
                    retreiveFares(location, mProducts);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Retreiving Products", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Retreiving Products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call<RideEstimate> call, Response<RideEstimate> response) {
        if(response.isSuccessful()) {
            Price price = response.body().getPrice();
            String fareId = price.getFareId();
            Float surgeMultiplier = price.getSurgeMultiplier();
            Integer lowEstimate = price.getLowEstimate();
            Integer highEstimate = price.getHighEstimate();
            mRideEstimate.add(response.body());
            if(mProducts.size() == mRideEstimate.size()) {
                int x = 9;
            }
        }
    }

    @Override
    public void onFailure(Call<RideEstimate> call, Throwable t) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        for(int index = 0; index < permissions.length; index++) {
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                if(permissions[index].equals(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    LocationRetriver.getInstance(this).permissionRequestResult(true);
                }
            } else {
                if(permissions[index].equals(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    LocationRetriver.getInstance(this).permissionRequestResult(false);
                }
            }
        }
    }

    @Override
    public void locationResult(boolean result, Location location) {
        if(result) {
            retreiveProducts(location);
        } else {
            //TODO
        }
    }


}
