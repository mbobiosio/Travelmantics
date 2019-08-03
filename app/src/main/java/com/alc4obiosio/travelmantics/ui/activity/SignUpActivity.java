package com.alc4obiosio.travelmantics.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alc4obiosio.travelmantics.R;
import com.alc4obiosio.travelmantics.model.PostUser;
import com.alc4obiosio.travelmantics.ui.base.BaseActivity;
import com.alc4obiosio.travelmantics.util.CommonUtils;
import com.alc4obiosio.travelmantics.util.Constants;
import com.alc4obiosio.travelmantics.util.NavigationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import timber.log.Timber;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.user_email)
    TextInputEditText mUserEmail;
    @BindView(R.id.user_full_name)
    TextInputEditText mUserName;
    @BindView(R.id.user_password)
    TextInputEditText mPassword;
    @BindView(R.id.signup)
    Button mSignUp;
    @BindView(R.id.login_here)
    TextView mLogin;

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_TABLE);

        mAuth = FirebaseAuth.getInstance();

        mSignUp.setOnClickListener(v -> setSignUp());

        mLogin.setOnClickListener(v -> NavigationUtils.navigateLogin(this));
    }

    private void setSignUp() {
        if (isNetworkConnected()) {
            if (!mUserEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
                register(mUserEmail.getText().toString(), mPassword.getText().toString());
            } else {
                showToast("Error");
            }

        } else {
            showToast(getString(R.string.no_internet));
        }
    }

    private void register(String email, String password) {
        if (!validate()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        showToast("Error "+ task.getException().getMessage());
                    } else {
                        onAuthSuccess(task.getResult().getUser());
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        createUser(user.getUid(), mUserName.getText().toString(), mUserEmail.getText().toString());
        NavigationUtils.navigateMainFromLogin(this, mUserName.getText().toString());
    }

    private void createUser(String user, String displayName, String email) {
        /*PostUser post = new PostUser();
        post.setEmail(email);
        post.setDisplayName(name);
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        mDatabaseRef.child(user).setValue(post);*/

        PostUser postUser = new PostUser(displayName, "", CommonUtils.getDate(), email, "");
        Map<String, Object> postValues = postUser.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(user, postValues);
        mDatabaseRef.updateChildren(childUpdates);
        Timber.d(displayName);
    }

    private boolean validate() {
        boolean valid = true;
        String message = getString(R.string.required);
        if (TextUtils.isEmpty(mUserName.getText().toString())) {
            mUserName.setError(message);
            valid = false;
        } else {
            mUserName.setError(null);
        }
        if (mUserEmail.getText().toString().isEmpty()) {
            mUserEmail.setError(message);
            valid = false;
        } else if (!CommonUtils.isEmailValid(mUserEmail.getText().toString())) {
            showToast("Invalid email");
        } else {
            mUserEmail.setError(null);
        }
        return valid;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
