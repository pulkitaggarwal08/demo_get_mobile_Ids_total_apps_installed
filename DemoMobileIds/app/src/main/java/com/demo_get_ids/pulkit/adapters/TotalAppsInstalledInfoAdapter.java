package com.demo_get_ids.pulkit.adapters;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo_get_ids.pulkit.R;
import com.demo_get_ids.pulkit.models.AppsName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkit on 24/11/17.
 */

public class TotalAppsInstalledInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AppsName> addNewItemList;
    private onClickListener onClickListener;
    private Context context;

    public interface onClickListener {
        void onClickButton(int position, int view, AppsName appsName);
    }

    public TotalAppsInstalledInfoAdapter(Context context, List<AppsName> addNewItemList, onClickListener onClickListener) {
        this.context = context;
        this.addNewItemList = addNewItemList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TotalAppsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_total_apps_installed, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TotalAppsViewHolder appsViewHolder = (TotalAppsViewHolder) holder;
        Drawable imgUrl;

        if (!addNewItemList.equals(null)) {

            if (addNewItemList.get(position).getName() != null) {
                appsViewHolder.tv_name.setText(addNewItemList.get(position).getName().toString());
                appsViewHolder.tv_apk_size.setText(String.valueOf(addNewItemList.get(position).getApkSize()));
                if (addNewItemList.get(position).getIcon() != null) {

                    imgUrl = addNewItemList.get(position).getIcon();
                    appsViewHolder.iv_apps_image.setImageDrawable(imgUrl);

//                    Glide.with(context)
//                            .load(imgUrl)
//                            .thumbnail(1.0f)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(appsViewHolder.iv_apps_image);

                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return addNewItemList.size();
    }

    private class TotalAppsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_name;
        private ImageView iv_apps_image;
        private TextView tv_apk_size;

        public TotalAppsViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            iv_apps_image = itemView.findViewById(R.id.iv_apps_image);
            tv_apk_size = itemView.findViewById(R.id.tv_apk_size);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), addNewItemList.get(getLayoutPosition()));
        }
    }

}
