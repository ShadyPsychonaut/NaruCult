package com.fandom.NarutoCult.main_tabs.region_tab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fandom.NarutoCult.R;

import java.util.List;

public class RegionRecyclerAdapter extends RecyclerView.Adapter<RegionRecyclerAdapter.MyAdapter> {

    private Context context;
    private List<RegionList> regionList;

    public RegionRecyclerAdapter(Context ctx, List<RegionList> regionList) {
        context = ctx;
        this.regionList = regionList;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regions_card, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        holder.regionName.setText(regionList.get(position).getRegionName());
        holder.regionCountry.setText(regionList.get(position).getRegionCountry());
        Glide.with(context)
                .load(regionList.get(position).getImgLink())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .centerCrop()
                .into(holder.regionImg);

        Glide.with(context)
                .load(regionList.get(position).getSymLink())
                .fitCenter()
                .into(holder.regionIcon);

    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        private ImageView regionImg;
        private TextView regionName;
        private TextView regionCountry;
        private ImageView regionIcon;
        private ProgressBar progressBar;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            regionImg = itemView.findViewById(R.id.image_region);
            regionName = itemView.findViewById(R.id.textView_region_name);
            regionCountry = itemView.findViewById(R.id.textView_region_county);
            regionIcon = itemView.findViewById(R.id.region_icon);
            progressBar = itemView.findViewById(R.id.progress_region_img);

        }
    }
}
