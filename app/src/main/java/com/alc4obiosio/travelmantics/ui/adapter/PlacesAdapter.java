package com.alc4obiosio.travelmantics.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alc4obiosio.travelmantics.R;
import com.alc4obiosio.travelmantics.model.TravelDeal;
import com.alc4obiosio.travelmantics.ui.activity.CreatePlaceActivity;
import com.alc4obiosio.travelmantics.ui.activity.MainActivity;
import com.alc4obiosio.travelmantics.util.CommonUtils;
import com.alc4obiosio.travelmantics.util.FirebaseUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mbuodile Obiosio on Aug 03,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.DealViewHolder> {
    private ArrayList<TravelDeal> deals;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private Context mContext;

    public PlacesAdapter(){
        FirebaseUtil.firebaseRef("traveldeals");
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;

        deals = FirebaseUtil.deals;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                assert travelDeal != null;
                travelDeal.setId(dataSnapshot.getKey());
                deals.add(travelDeal);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.deal_item, parent, false);
        mContext = itemView.getContext();
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.set(deal);
        holder.mDealCard.setOnClickListener(v -> {
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(mContext, CreatePlaceActivity.class);
            /*intent.putExtra("title", selectedDeal.getTitle());
            intent.putExtra("description", selectedDeal.getDescription());
            intent.putExtra("price", selectedDeal.getPrice());
            intent.putExtra("image", selectedDeal.getImageUrl());*/
            intent.putExtra("Deal", selectedDeal);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.description)
        TextView mDescription;
        @BindView(R.id.price)
        TextView mPrice;
        @BindView(R.id.place_image)
        ImageView mPlaceImage;
        @BindView(R.id.deal_item)
        CardView mDealCard;

        public DealViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void set(TravelDeal deal) {
            mName.setText(deal.getTitle());
            mDescription.setText(deal.getDescription());
            mPrice.setText(deal.getPrice());
            CommonUtils.loadGlideImage(mContext, mPlaceImage, deal.getImageUrl());
        }

    }
}