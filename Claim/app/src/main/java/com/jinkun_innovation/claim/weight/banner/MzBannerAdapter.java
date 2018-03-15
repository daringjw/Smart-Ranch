package com.jinkun_innovation.claim.weight.banner;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jinkun_innovation.claim.R;

import java.util.List;

/**
 * Created by test on 2017/11/22.
 */


public class MzBannerAdapter extends RecyclerView.Adapter<MzBannerAdapter.MzViewHolder> {

    private Context context;
    private List<String> urlList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public MzBannerAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public MzBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(MzBannerAdapter.MzViewHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        final int P = position % urlList.size();
        String url = urlList.get(P);
        ImageView img = (ImageView) holder.imageView;
        CardView cardView = (CardView) holder.mCardView;
//        int screenWidth = ScreenUtils.getScreenWidth();
//        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
//        layoutParams.width = screenWidth - SizeUtils.dp2px(8);
//        layoutParams.height = (int) ((screenWidth - SizeUtils.dp2px(8)) * 0.58);
//        cardView.setLayoutParams(layoutParams);
        Glide.with(context).load(url).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(P);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView mCardView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_banner);
        }
    }

}
