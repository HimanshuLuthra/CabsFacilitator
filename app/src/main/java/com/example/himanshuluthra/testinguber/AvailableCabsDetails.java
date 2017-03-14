package com.example.himanshuluthra.testinguber;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by himanshuluthra on 07/03/17.
 */


public class AvailableCabsDetails extends RecyclerView.Adapter<AvailableCabsDetails.CabsItemHolder> {

    List<CabItem> mCabList;
    Context mContext;

    AvailableCabsDetails(Context context) {
        mCabList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public CabsItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cab_item_layout, viewGroup, false);
        CabsItemHolder cabsItemHolder = new CabsItemHolder(view);
        return cabsItemHolder;
    }

    @Override
    public void onBindViewHolder(CabsItemHolder holder, int position) {
        CabItem cabItem = null;
        cabItem = mCabList.get(position);
        holder.category.setText(cabItem.getCabType().getValue() + cabItem.getCabSubType());
        holder.price.setText(String.valueOf(cabItem.getPrice()));
        holder.pickupTime.setText(String.valueOf(cabItem.getPickupTime()));
    }

    @Override
    public int getItemCount() {
        return mCabList.size();
    }

    public static class CabsItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView category;
        TextView price;
        TextView pickupTime;

        CabsItemHolder(View cabItem) {
            super(cabItem);
            cardView = (CardView)cabItem.findViewById(R.id.card);
            category = (TextView)cabItem.findViewById(R.id.category);
            price = (TextView)cabItem.findViewById(R.id.price);
            pickupTime = (TextView)cabItem.findViewById(R.id.pickupTime);
            cabItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            if(null != mItemClickListener) {
//                mItemClickListener.onItemClick(view, getPosition());
//            }
        }
    }

//    public interface OnItemClickListener {
//        public void onItemClick(View view, int position);
//    }
//
//    public long getStoryId(int position) {
//        long storyId;
//        storyId = (long)storyList.get(position).getId();
//        return  storyId;
//    }
//
//    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
//        this.mItemClickListener = onItemClickListener;
//    }

}
