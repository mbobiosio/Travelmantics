package com.alc4obiosio.travelmantics.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alc4obiosio.travelmantics.R;
import com.alc4obiosio.travelmantics.ui.base.BaseActivity;
import com.alc4obiosio.travelmantics.util.NavigationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.create_account)
    TextView mCreateAccount;
    @BindView(R.id.user_email)
    TextInputEditText mEmail;
    @BindView(R.id.user_password)
    TextInputEditText mPassword;
    @BindView(R.id.login_account)
    Button mLogin;

    private FirebaseAuth mAuth;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mCreateAccount.setOnClickListener(v -> NavigationUtils.navigateSignUp(this));

        mLogin.setOnClickListener(v -> login(mEmail.getText().toString(), mPassword.getText().toString()));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Login failed! ", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
