package com.hard.eoapp.ui.order;

import android.content.Context;
import android.graphics.Color;
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
import com.hard.eoapp.ui.order.ordermodel.DataItem;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.UrlServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public OrderAdapter(Context context, ArrayList<DataItem> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.txtNama.setText(item.getNamaPake());
            viewHolder.txtJenis.setText(item.getTipePaket());
            String status = "";
            int color = 0;
            if (item.getStatusOrder().equals("0")){
                status = "Menunggu pembayaran";
                color = Color.parseColor("#ffa726");
            }else if (item.getStatusOrder().equals("1")){
                status = "Lunas";
                color = Color.GREEN;
            }else if (item.getStatusOrder().equals("2")){
                status = "Selesai";
                color = Color.GREEN;
            }else if (item.getStatusOrder().equals("3")){
                status = "Dibatalkan";
                color = Color.RED;
            }
            viewHolder.txtStatus.setText(status);
            viewHolder.txtStatus.setTextColor(color);
            HelperClass.convertHarga(viewHolder.txtHarga, item.getHarga());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date transDate = format.parse(item.getWaktuOrder());
                HelperClass.getDate(transDate, item.getWaktuOrder(), viewHolder.txtTanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
        private TextView txtNama, txtHarga, txtJenis, txtStatus, txtTanggal;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtNama = itemView.findViewById(R.id.txt_paket);
            this.txtHarga = itemView.findViewById(R.id.txt_price_order);
            this.txtJenis = itemView.findViewById(R.id.txt_type_order);
            this.txtStatus = itemView.findViewById(R.id.txt_status_order);
            this.txtTanggal = itemView.findViewById(R.id.txt_date_order);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }

    }
}
