package com.example.himanshuluthra.testinguber;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by himanshuluthra on 07/03/17.
 */


public class AvailableCabsDetailsAdapter extends RecyclerView.Adapter<AvailableCabsDetailsAdapter.CabsItemHolder> {

    List<CabItem> mCabList;
    Context mContext;

    public AvailableCabsDetailsAdapter() {
        mCabList = new ArrayList<>();
    }

    public AvailableCabsDetailsAdapter(Context context) {
        this();
        mContext = context;
    }

    public AvailableCabsDetailsAdapter(List<CabItem> cabItems) {
        mCabList = cabItems;
    }

    @Override
    public CabsItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cab_item_layout, viewGroup, false);
        return new CabsItemHolder(view);
    }

    @Override
    public void onBindViewHolder(CabsItemHolder holder, int position) {
        CabItem cabItem = mCabList.get(position);
        holder.category.setText(cabItem.getCategory());
        holder.price.setText(String.valueOf(cabItem.getLowEstimate() + "-" + cabItem.getHighEstimate()));
        holder.pickupTime.setText(String.valueOf(cabItem.getPickupTime()));
    }

    public void setCabList(List<CabItem> cabItems) {
        mCabList = cabItems;
    }

    public void addCabItem(CabItem cabItem) {
        mCabList.add(cabItem);
    }

    @Override
    public int getItemCount() {
        return mCabList.size();
    }

    public class CabsItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView category;
        TextView price;
        TextView pickupTime;

        CabsItemHolder(View cabItem) {
            super(cabItem);
            category = (TextView)cabItem.findViewById(R.id.category);
            price = (TextView)cabItem.findViewById(R.id.price);
            pickupTime = (TextView)cabItem.findViewById(R.id.pickupTime);
            cabItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "onClick " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
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
