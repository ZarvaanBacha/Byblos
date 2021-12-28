package com.example.byblos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class MainActivity extends AppCompatActivity {

    // The Idling Resource which will be null in production.
    @Nullable
    private CountingIdlingResource countingIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link CountingIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (countingIdlingResource == null) {
            String _ACTIVITY_NAME = "MAIN_ACTIVITY";
            countingIdlingResource = new CountingIdlingResource(_ACTIVITY_NAME);
        }
        return countingIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countingIdlingResource = (CountingIdlingResource) getIdlingResource();

        // Button assignments to id
        final Button LOGIN_BUTTON = findViewById(R.id.loginButton_main);
        final Button REGISTER_BUTTON = findViewById(R.id.registerButton_main);

        // goto log-in
        LOGIN_BUTTON.setOnClickListener(view -> {
            countingIdlingResource.increment();  // TODO DEBUG
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            countingIdlingResource.decrement();  // TODO DEBUG
        });
        // goto sign-up
        REGISTER_BUTTON.setOnClickListener(view -> {
//            countingIdlingResource.increment();  // TODO DEBUG
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
//            countingIdlingResource.decrement();  // TODO DEBUG
        });
    }

}