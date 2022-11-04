package com.fandom.NarutoCult.main_tabs.people_tab.people_activity.your_views_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fandom.NarutoCult.R;

import java.util.List;

public class MyViewRecyclerAdapter extends RecyclerView.Adapter<MyViewRecyclerAdapter.MyAdapter> {

    private Context context;
    private List<UploadMyView> uploads;

    public MyViewRecyclerAdapter(Context ctx, List<UploadMyView> upl){
        context = ctx;
        uploads = upl;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myviewcard, parent, false);
        return new MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        UploadMyView uploadCurrent = uploads.get(position);
        holder.textView.setText(uploadCurrent.getMessage());
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public static class MyAdapter extends RecyclerView.ViewHolder{

        private TextView textView;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.myViewsText);
        }
    }
}
