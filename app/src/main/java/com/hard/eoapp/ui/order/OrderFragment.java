package com.hard.eoapp.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hard.eoapp.R;
import com.hard.eoapp.paket.PaketActivity;
import com.hard.eoapp.paket.PaketAdapter;
import com.hard.eoapp.paket.detailpaket.DetailPaketActivity;
import com.hard.eoapp.paket.paketmodel.ItemPaketItem;
import com.hard.eoapp.paket.paketmodel.PaketModel;
import com.hard.eoapp.ui.order.ordermodel.DataItem;
import com.hard.eoapp.ui.order.ordermodel.OrderModel;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.SessionPrefs;
import com.hard.eoapp.utils.UrlServer;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class OrderFragment extends Fragment {
    private static final String TAG = "orderActivity";
    private RecyclerView rv_order;
    private LinearLayout include_lay;
    private Button btnRefresh;
    private OrderAdapter adapter;
    private ArrayList<DataItem> list  = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView txtMsg;
    private ImageView imgMsg;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(view);
    }

    private void findView(View view) {
        rv_order = view.findViewById(R.id.rv_order);
        progressBar = view.findViewById(R.id.progress_bar);
        include_lay = view.findViewById(R.id.include_lay);
        imgMsg = view.findViewById(R.id.img_message);
        txtMsg = view.findViewById(R.id.txt_msg);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        swipeRefreshLayout = view.findViewById(R.id.swipe_up_layout_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new OrderAdapter(getActivity(), list);
        rv_order.setLayoutManager(linearLayoutManager);
        rv_order.setHasFixedSize(true);
        rv_order.setAdapter(adapter);

        include_lay.setVisibility(View.GONE);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()) {
                    if (include_lay.getVisibility() == View.VISIBLE) {
                        include_lay.setVisibility(View.GONE);
                    }
                    getListPaket();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (include_lay.getVisibility() == View.VISIBLE) {
                    include_lay.setVisibility(View.GONE);
                }
                getListPaket();
            }
        });

        if (isAdded()){
            getListPaket();
        }
    }

    private void getListPaket() {
        if (swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.post(UrlServer.URL_ORDERAN)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("param", "1")
                .build()
                .getAsOkHttpResponseAndObject(OrderModel.class, new OkHttpResponseAndParsedRequestListener<OrderModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, final OrderModel response) {
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
                                    item.setWaktuAcara(response.getData().get(i).getWaktuAcara());
                                    item.setWaktuOrder(response.getData().get(i).getWaktuOrder());
                                    item.setOrderId(response.getData().get(i).getOrderId());
                                    item.setStatusOrder(response.getData().get(i).getStatusOrder());
                                    item.setMetodePembayaran(response.getData().get(i).getMetodePembayaran());
                                    list.add(item);
                                }
                                adapter.updateList(list);
                                adapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, DataItem model) {
                                        Intent i = new Intent(getActivity(), DetailOrderActivity.class);
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
                                        startActivityForResult(i, 1);
                                    }
                                });
                            } else {
                                if (!list.isEmpty()) {
                                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (isAdded()){
                                        HelperClass.responseError(include_lay, imgMsg, R.drawable.box, txtMsg, getString(R.string.no_trans));
                                    }
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
                                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                            } else {
                                if (isAdded()){
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.failure, txtMsg, getString(R.string.servererrorr));
                                }
                            }
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            if (!list.isEmpty()) {
                                Toast.makeText(getActivity(), R.string.cek_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                if (isAdded()){
                                    HelperClass.responseError(include_lay, imgMsg, R.drawable.no_internet, txtMsg, getString(R.string.nointernett));
                                }
                            }
                        }
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1) {
            if (isAdded()){
                list.clear();
                adapter.updateList(list);
                getListPaket();
            }
            }
        }
    }
}