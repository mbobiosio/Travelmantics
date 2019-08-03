package com.alc4obiosio.travelmantics.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class CreatePlaceActivity extends BaseActivity {

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
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Insert Picture"), IMAGE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            final StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());

            UploadTask uploadTask = ref.putFile(imageUri);
            uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //String url = taskSnapshot.getStorage().getDownloadUrl().toString();
                    String url = taskSnapshot.getStorage().getPath();
//                    deal.setImageUrl(url);
                    mTravelDeal.setImageUrl(url);
                    //
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();
                        mTravelDeal.setImageUrl(url);
                        CommonUtils.loadGlideImage(CreatePlaceActivity.this, mPlaceImage, url);
                    }
                }
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
            Toast.makeText(this, "Save the deal first", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(mTravelDeal.getId()).removeValue();
        if (mTravelDeal.getTitle() != null && !mTravelDeal.getImageUrl().isEmpty()) {
            StorageReference imageRef = FirebaseUtil.mStorageReference.child(mTravelDeal.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CreatePlaceActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Timber.d(e.getCause()));
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
