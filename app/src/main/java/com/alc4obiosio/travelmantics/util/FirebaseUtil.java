package com.alc4obiosio.travelmantics.util;

import com.alc4obiosio.travelmantics.model.TravelDeal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Mbuodile Obiosio on Aug 03,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class FirebaseUtil {

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    public static StorageReference mStorageReference;
    public static FirebaseStorage mFirebaseStorage;
    public static FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> deals;

    private FirebaseUtil() {}

    public static void firebaseRef(String ref){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseStorage();
        }
        deals = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(ref);
    }

    public static void firebaseStorage(){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("deals_picture");
    }
}
