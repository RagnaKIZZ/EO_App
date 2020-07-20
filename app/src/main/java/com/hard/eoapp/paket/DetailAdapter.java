package com.hard.eoapp.paket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hard.eoapp.R;
import com.hard.eoapp.paket.paketmodel.ItemPaketItem;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.UrlServer;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemPaketItem> list = new ArrayList<>();

    public DetailAdapter(Context context, ArrayList<ItemPaketItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<ItemPaketItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ItemPaketItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNamaItem());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private ItemPaketItem getItem(int position) {
        return list.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = itemView.findViewById(R.id.txt_item_detail);
        }

    }
}
