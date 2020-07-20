package com.hard.eoapp.paket;

import android.content.Context;
import android.content.Intent;
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
import com.hard.eoapp.paket.detailpaket.DetailPaketActivity;
import com.hard.eoapp.paket.paketmodel.DataItem;
import com.hard.eoapp.paket.paketmodel.ItemPaketItem;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.UrlServer;

import java.util.ArrayList;

public class PaketAdapter extends RecyclerView.Adapter<PaketAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public PaketAdapter(Context context, ArrayList<DataItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<DataItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNamaPake());
            HelperClass.convertHarga(viewHolder.txtHarga, item.getHarga());
            viewHolder.progressBar.setVisibility(View.GONE);
            HelperClass.loadGambar(context, UrlServer.URL_FOTO_PAKET+item.getFoto(), viewHolder.progressBar, viewHolder.imgPaket);

            viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, position, list.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private DataItem getItem(int position) {
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, DataItem model);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga;
        private ImageView imgPaket;
        private ProgressBar progressBar;
        private Button btnDetail;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = itemView.findViewById(R.id.txt_nama_paket);
            this.txtHarga = itemView.findViewById(R.id.txt_harga_paket);
            this.imgPaket = itemView.findViewById(R.id.img_item_paket);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
            this.btnDetail = itemView.findViewById(R.id.btn_lihat_detail);
        }

    }
}
