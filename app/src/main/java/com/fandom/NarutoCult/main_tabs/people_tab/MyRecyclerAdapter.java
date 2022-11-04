package com.fandom.NarutoCult.main_tabs.people_tab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fandom.NarutoCult.R;
import com.fandom.NarutoCult.main_tabs.people_tab.people_activity.SecondActivity;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements Filterable {

    private List<PeopleItem> mBrowseList;
    private final List<PeopleItem> mBrowseListFull;
    private Context context;

    public MyRecyclerAdapter(Context ctx, List<PeopleItem> browseList) {
        context = ctx;
        mBrowseList = browseList;
        mBrowseListFull = new ArrayList<>(browseList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_lay, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PeopleItem currentItem = mBrowseList.get(position);

        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText(currentItem.getText2());
        Glide.with(context)
                .load(currentItem.getImageResource())
                .centerCrop()
                .into(holder.imageView);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("data1", currentItem.getText1());
                intent.putExtra("data2", currentItem.getText2());
                intent.putExtra("image_key", currentItem.getImageResource());
                intent.putExtra("you_key", currentItem.getYouLink());
                intent.putExtra("quo1", currentItem.getQuo1());
                intent.putExtra("quo2", currentItem.getQuo2());
                intent.putExtra("quo3", currentItem.getQuo3());
                intent.putExtra("quo4", currentItem.getQuo4());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBrowseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1, textView2;
        private ImageView imageView;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);

            constraintLayout = itemView.findViewById(R.id.myLayout);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PeopleItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mBrowseListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PeopleItem item : mBrowseListFull) {
                    if (item.getText1().toLowerCase().contains(filterPattern) || item.getText2().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBrowseList.clear();
            mBrowseList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
