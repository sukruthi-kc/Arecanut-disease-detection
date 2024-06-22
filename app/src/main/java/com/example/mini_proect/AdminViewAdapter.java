package com.example.mini_proect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminViewAdapter extends RecyclerView.Adapter<AdminViewAdapter.ViewHolder> {

    private Context context;
    private List<AdminViewItem> adminViewItemList;

    public AdminViewAdapter(Context context, List<AdminViewItem> adminViewItemList) {
        this.context = context;
        this.adminViewItemList = adminViewItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_adminview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminViewItem item = adminViewItemList.get(position);
        holder.imageView.setImageBitmap(item.getImage());
        holder.predictedClassTextView.setText("Predicted class: " + item.getPredictedClass());
    }

    @Override
    public int getItemCount() {
        return adminViewItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView predictedClassTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
            predictedClassTextView = itemView.findViewById(R.id.itemPredictedClassTextView);
        }
    }
}
