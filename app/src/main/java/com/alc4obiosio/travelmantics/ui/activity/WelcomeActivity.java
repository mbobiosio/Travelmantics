package com.alc4obiosio.travelmantics.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.alc4obiosio.travelmantics.BuildConfig;
import com.alc4obiosio.travelmantics.R;
import com.alc4obiosio.travelmantics.ui.base.BaseActivity;
import com.alc4obiosio.travelmantics.util.NavigationUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import timber.log.Timber;

public class WelcomeActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    @BindView(R.id.google_login)
    Button mGoogle;
    @BindView(R.id.email_login)
    Button mEmailLogin;

    private FirebaseAuth mFAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFUser;
    AuthCredential mAuthCredential;

    String prov;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFAuth = FirebaseAuth.getInstance();

        mEmailLogin.setOnClickListener(v -> NavigationUtils.navigateLogin(this));

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthStateListener = fireBaseAuth -> {
            mFUser = fireBaseAuth.getCurrentUser();
            if (mFUser != null) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("prov", prov);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        };

        mGoogle.setOnClickListener(v -> {
            if (!isNetworkConnected()) {
                showToast(getString(R.string.no_internet));
            } else {
                //showProgress(getString(R.string.logging_in));
                signInGoogle();
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                firebaseAuthWithGoogle(account);
                prov = "Google";
            } else {
                Timber.d(result.getStatus().getStatusMessage());
                //errorDialog(""+result.isSuccess(), "google");
                //showToast(getString(R.string.no_internet), "google");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        //showProgress(getString(R.string.logging_in));

        mAuthCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFAuth.signInWithCredential(mAuthCredential)
                .addOnCompleteListener(this, task -> {

                    if (!task.isSuccessful()) {
                        //Timber.d(task.getException());
                        showToast(getString(R.string.auth_failed));
                        Timber.e(task.getException());
                    }
                    //hideProgress();
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast(getString(R.string.play_error));
    }

    @Override
    public void onStart() {
        super.onStart();
        mFAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
