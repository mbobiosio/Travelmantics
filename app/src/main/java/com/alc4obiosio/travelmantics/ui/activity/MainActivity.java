package com.alc4obiosio.travelmantics.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alc4obiosio.travelmantics.R;
import com.alc4obiosio.travelmantics.model.PostUser;
import com.alc4obiosio.travelmantics.ui.adapter.PlacesAdapter;
import com.alc4obiosio.travelmantics.ui.base.BaseActivity;
import com.alc4obiosio.travelmantics.util.CommonUtils;
import com.alc4obiosio.travelmantics.util.Constants;
import com.alc4obiosio.travelmantics.util.FirebaseUtil;
import com.alc4obiosio.travelmantics.util.NavigationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mFAuth;
    private FirebaseUser mFUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFDatabase;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    @BindView(R.id.places_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    LinearLayoutManager mLayoutManager;

    //ImageView mImageView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFAuth = FirebaseAuth.getInstance();
        mFDatabase = FirebaseDatabase.getInstance();

        loginCheck();
        initRecycler();

        setSupportActionBar(mToolbar);

        //mLogout.setOnClickListener(v -> logout(this, mAuthStateListener, mFUser, mFAuth));
    }

    void loginCheck() {
        mAuthStateListener = fireBaseAuth -> {
            mFUser = fireBaseAuth.getCurrentUser();
            if (mFUser != null) {
                Intent intent = getIntent();
                String name = intent.getStringExtra("name");
                String url, displayName, email;

                url = String.valueOf(mFUser.getPhotoUrl());
                displayName = mFUser.getDisplayName();
                //name = intent.getStringExtra("name");

                email = mFUser.getEmail();
                //Toast.makeText(this, email, Toast.LENGTH_LONG).show();
                //PreferenceUtils.saveUserData(displayName, url);

                DatabaseReference mDatabase = mFDatabase.getReference(Constants.USERS_TABLE);

                PostUser post = new PostUser(displayName, url, CommonUtils.getDate(), email, getIntent().getStringExtra("prov"));
                Map<String, Object> postValues = post.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(mFUser.getUid(), postValues);
                mDatabase.updateChildren(childUpdates);

                //mName.setText(displayName);

                //CommonUtils.loadGlideImage(this, mImageView, url);
            } else {
                NavigationUtils.navigateMain(this);
            }
        };
    }

    private void initRecycler() {
        FirebaseUtil.firebaseRef("traveldeal");
        PlacesAdapter mAdapter = new PlacesAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFUser != null) {
            mFAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_place) {
            NavigationUtils.navigateCreatePlace(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
