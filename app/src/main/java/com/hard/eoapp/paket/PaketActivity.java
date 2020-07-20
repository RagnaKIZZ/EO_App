package com.hard.eoapp.paket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hard.eoapp.R;
import com.hard.eoapp.paket.detailpaket.DetailPaketActivity;
import com.hard.eoapp.paket.paketmodel.DataItem;
import com.hard.eoapp.paket.paketmodel.ItemPaketItem;
import com.hard.eoapp.paket.paketmodel.PaketModel;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.SessionPrefs;
import com.hard.eoapp.utils.UrlServer;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import okhttp3.Response;

public class PaketActivity extends AppCompatActivity {
    private static final String TAG = "orderActivity";
    private RecyclerView rv_order;
    private LinearLayout include_lay;
    private ImageView imgMsg;
    private PaketAdapter adapter;
    private ArrayList<DataItem> list = new ArrayList<>();
    private Button btnRefresh;
    private FloatingActionButton fabBack;
    private TextView txtPaket;
    private TextView txtMsg;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paket);

        findView();
    }

    private void findView() {
        rv_order = findViewById(R.id.rv_paket);
        progressBar = findViewById(R.id.progress_bar);
        include_lay = findViewById(R.id.include_lay);
        imgMsg = findViewById(R.id.img_message);
        txtMsg = findViewById(R.id.txt_msg);
        txtPaket = findViewById(R.id.txt_paket);
        fabBack = findViewById(R.id.fab_back);
        btnRefresh = findViewById(R.id.btn_refresh);
        swipeRefreshLayout = findViewById(R.id.swipe_up_layout_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new PaketAdapter(this, list);
        rv_order.setLayoutManager(linearLayoutManager);
        rv_order.setHasFixedSize(true);
        rv_order.setAdapter(adapter);

        Intent i = getIntent();
        final String tipe = i.getStringExtra("tipe");
        include_lay.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()) {
                    if (include_lay.getVisibility() == View.VISIBLE) {
                        include_lay.setVisibility(View.GONE);
                    }
                    getListPaket(tipe);
                }
            }
        });

        if (tipe.equals("1")) {
            txtPaket.setText("Paket Ulang Tahun");
        } else {
            txtPaket.setText("Paket Pernikahan");
        }

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (include_lay.getVisibility() == View.VISIBLE) {
                    include_lay.setVisibility(View.GONE);
                }
                getListPaket(tipe);
            }
        });

        getListPaket(tipe);
    }

    private void getListPaket(String tipe) {
        if (swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.post(UrlServer.URL_PAKET)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("tipe", tipe)
                .build()
                .getAsOkHttpResponseAndObject(PaketModel.class, new OkHttpResponseAndParsedRequestListener<PaketModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, final PaketModel response) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                include_lay.setVisibility(View.GONE);
                                list.clear();
                                adapter.updateList(list);
                                for (int i = 0; i < response.getData().size(); i++) {
                                    final DataItem item = new DataItem();
                                    item.setFoto(response.getData().get(i).getFoto());
                                    item.setHarga(response.getData().get(i).getHarga());
                                    item.setNamaPake(response.getData().get(i).getNamaPake());
                                    item.setPaketId(response.getData().get(i).getPaketId());
                                    item.setTipePaket(response.getData().get(i).getTipePaket());
                                    item.setStatusPaket(response.getData().get(i).getStatusPaket());
//                                    for (int j = 0; j < response.getData().get(i).getItemPaket().size(); j++) {
//                                        final ItemPaketItem itemPaket = new ItemPaketItem();
//                                        itemPaket.setNamaItem(response.getData().get(i).getItemPaket().get(j).getNamaItem());
//                                    }
                                    list.add(item);
                                }
                                adapter.updateList(list);
                                adapter.setOnItemClickListener(new PaketAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, final int position, final DataItem model) {
                                        Intent i = new Intent(PaketActivity.this, DetailPaketActivity.class);
                                        ArrayList<ItemPaketItem> paketItems = new ArrayList<>();
                                        for (int j = 0; j < response.getData().get(position).getItemPaket().size(); j++) {
                                            ItemPaketItem item = new ItemPaketItem();
                                            item.setNamaItem(response.getData().get(position).getItemPaket().get(j).getNamaItem());
                                            paketItems.add(item);
                                        }
//                                        for (int j = 0; j < paketItems.size(); j++) {
//                                            Log.d(TAG, "onItemClick: list" + paketItems.get(j).getNamaItem());
//                                        }
                                        i.putExtra("data_item", model);
                                        i.putExtra("item_paket", paketItems);
                                        startActivity(i);
                                    }
                                });
                            } else {
                                if (!list.isEmpty()) {
                                    Toast.makeText(PaketActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                } else {
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.box, txtMsg, getString(R.string.no_cars));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()) {
                                Toast.makeText(PaketActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                            } else {
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.failure, txtMsg, getString(R.string.servererrorr));
                            }
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()) {
                                Toast.makeText(PaketActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                HelperClass.responseError(include_lay, imgMsg, R.drawable.no_internet, txtMsg, getString(R.string.nointernett));
                            }
                        }
                    }
                });

    }
}