package com.alc4obiosio.travelmantics.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.alc4obiosio.travelmantics.R;
import com.alc4obiosio.travelmantics.model.TravelDeal;
import com.alc4obiosio.travelmantics.ui.base.BaseActivity;
import com.alc4obiosio.travelmantics.util.CommonUtils;
import com.alc4obiosio.travelmantics.util.FirebaseUtil;
import com.alc4obiosio.travelmantics.util.NavigationUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import timber.log.Timber;

import static com.alc4obiosio.travelmantics.util.Constants.IMAGE_RESULT;
import static com.alc4obiosio.travelmantics.util.FirebaseUtil.databaseReference;

public class CreateDealActivity extends BaseActivity {

    @BindView(R.id.place_name)
    TextInputEditText mPlaceName;
    @BindView(R.id.place_description)
    TextInputEditText mPlaceDescription;
    @BindView(R.id.place_price)
    TextInputEditText mPlacePrice;
    @BindView(R.id.select_image)
    Button mSelectImage;
    @BindView(R.id.image_preview)
    ImageView mPlaceImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TravelDeal mTravelDeal;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_create_place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUtil.firebaseRef("traveldeals");
        mFirebaseDatabase = FirebaseUtil.firebaseDatabase;
        mDatabaseReference = databaseReference;

        mSelectImage.setOnClickListener(v -> selectImage());

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null) {
            deal = new TravelDeal();
        }
        this.mTravelDeal = deal;

        mPlaceName.setText(mTravelDeal.getImageName());
        mPlaceDescription.setText(mTravelDeal.getDescription());
        mPlacePrice.setText(mTravelDeal.getPrice());
        CommonUtils.showImage(mPlaceImage, mTravelDeal.getImageUrl());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Insert Picture"), IMAGE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK)
        {
            final Uri imageUri = data.getData();
            StorageReference reference = FirebaseUtil.mStorageReference.child("deals_image").child(imageUri.toString()+".jpg");
            reference.putFile(imageUri).addOnSuccessListener(this, taskSnapshot -> {
                StorageReference taskReference = taskSnapshot.getMetadata().getReference();
                Task<Uri> uriTask = taskReference.getDownloadUrl();
                uriTask.addOnCompleteListener(task -> {
                    String imageUrl = task.getResult().toString();
                    String pictureName = taskSnapshot.getStorage().getPath();
                    mTravelDeal.setImageUrl(imageUrl);
                    mTravelDeal.setImageName(pictureName);
                    CommonUtils.showImage(mPlaceImage, imageUrl);
                });
            });
        }
    }



    private void saveDeal() {
        mTravelDeal.setTitle(mPlaceName.getText().toString());
        mTravelDeal.setDescription(mPlaceDescription.getText().toString());
        mTravelDeal.setPrice(mPlacePrice.getText().toString());
        if (mTravelDeal.getId() == null) {
            databaseReference.push().setValue(mTravelDeal);
        } else {
            mDatabaseReference.child(mTravelDeal.getId()).setValue(mTravelDeal);
        }

    }

    private void deleteDeal() {
        if (mTravelDeal == null) {
            showToast(getString(R.string.save_deal_warning));
            return;
        }
        mDatabaseReference.child(mTravelDeal.getId()).removeValue();
        if (mTravelDeal.getTitle() != null && !mTravelDeal.getImageUrl().isEmpty()) {
            StorageReference imageRef = FirebaseUtil.mStorageReference.child(mTravelDeal.getImageUrl());
            imageRef.delete().addOnSuccessListener(aVoid -> showToast(getString(R.string.image_deleted))).addOnFailureListener(e -> Timber.d(e.getCause()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create:
                saveDeal();
                NavigationUtils.navigateMain(this);
                break;
            case R.id.delete:
                deleteDeal();
                NavigationUtils.navigateMain(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}