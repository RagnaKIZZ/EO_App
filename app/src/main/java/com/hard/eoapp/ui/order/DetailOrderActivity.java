package com.hard.eoapp.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hard.eoapp.R;
import com.hard.eoapp.paket.DetailAdapter;
import com.hard.eoapp.paket.detailpaket.DetailPaketActivity;
import com.hard.eoapp.paket.paketmodel.ItemPaketItem;
import com.hard.eoapp.ui.order.ordermodel.DataItem;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.SessionPrefs;
import com.hard.eoapp.utils.UniversalModel;
import com.hard.eoapp.utils.UrlServer;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import okhttp3.Response;

public class DetailOrderActivity extends AppCompatActivity {
    private static final String TAG = "DetailPaketActivity";
    private String TOTAL_DAY = "total";
    private TextView txtPaket, txtHarga, txtByr, txtTf, txtStatus, txtTglAcara, txtTglOrder;
    private ProgressBar progressBar;
    private ImageView imgPaket;
    private FloatingActionButton fabBack;
    private DetailAdapter adapter;
    private RecyclerView rv_detail;
    private CardView cvCash, cvTrans;
    private Button btnCancel, btnWa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        findView();
    }

    private void findView() {
        btnCancel = findViewById(R.id.btn_cancel);
        btnWa = findViewById(R.id.btn_kirim_bukti);
        txtPaket = findViewById(R.id.txt_paket);
        cvCash = findViewById(R.id.cv_bayar_ditempat);
        cvTrans = findViewById(R.id.cv_bayar_transfer);
        imgPaket = findViewById(R.id.img_paket_detail);
        progressBar = findViewById(R.id.progress_bar);
        rv_detail = findViewById(R.id.rc_detail);
        fabBack = findViewById(R.id.fab_back);
        txtByr = findViewById(R.id.txt_bayar_ditempat);
        txtTf = findViewById(R.id.txttransfer);
        txtStatus = findViewById(R.id.txt_status_order);
        txtTglAcara = findViewById(R.id.txt_tanggal_acara);
        txtTglOrder = findViewById(R.id.txt_tanggal_order);
        txtHarga = findViewById(R.id.txt_detail_harga);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Intent i = getIntent();
        DataItem item = i.getParcelableExtra("data_item");
        ArrayList<ItemPaketItem> itemPaketItems = i.getParcelableArrayListExtra("item_paket");
        Log.d(TAG, "onCreate: list" + itemPaketItems.size());
        String foto = item.getFoto();
        String paket = item.getNamaPake();
        String metodePembayaran = item.getMetodePembayaran();
        String id_paket = item.getPaketId();
        final String id_order = item.getOrderId();
        String harga = item.getHarga();
        String oDate = item.getWaktuOrder();
        String sDate = item.getWaktuAcara();
        String status = item.getStatusOrder();
        adapter = new DetailAdapter(this, itemPaketItems);
        rv_detail.setLayoutManager(linearLayoutManager);
        rv_detail.setAdapter(adapter);
        rv_detail.setHasFixedSize(true);

        HelperClass.parseDate(oDate, txtTglOrder);
        HelperClass.parseDate(sDate, txtTglAcara);

        HelperClass.convertHarga(txtHarga, harga);

        txtPaket.setText(paket);
        if (!foto.isEmpty()) {
            HelperClass.loadGambar(this, UrlServer.URL_FOTO_PAKET + foto, progressBar, imgPaket);
        }

        if (metodePembayaran.equals("1")) {
            cvCash.setVisibility(View.VISIBLE);
            cvTrans.setVisibility(View.GONE);
            getStatus(false, status);
        } else {
            cvCash.setVisibility(View.GONE);
            cvTrans.setVisibility(View.VISIBLE);
            getStatus(true, status);
        }

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetailOrderActivity.this);
                alert.setTitle("Batal");
                alert.setMessage("Apakah Anda yakin ingin membatalkan pesanan?")
                        .setCancelable(true)
                        .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrder(Prefs.getString(SessionPrefs.U_ID, ""), Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""), id_order);
                            }
                        })
                        .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });

        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = "+6285695234975"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(DetailOrderActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }

    private void getStatus(boolean metode, String status) {
        String stat;
        if (status.equals("0")) {
            stat = "Menunggu Pembayaran";
            if (metode) {
                btnWa.setVisibility(View.VISIBLE);
            } else {
                btnCancel.setVisibility(View.VISIBLE);
                btnWa.setVisibility(View.GONE);
            }
        } else {
            btnWa.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            if (status.equals("1")) {
                stat = "Lunas";
            } else if (status.equals("3")) {
                stat = "Cancel";
            } else {
                stat = "Selesai";
            }
        }
        txtStatus.setText(stat);
    }

    private void cancelOrder(String id, String token, String order_id) {
        final ProgressDialog dialog = new ProgressDialog(DetailOrderActivity.this);
        HelperClass.loading(dialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_CANCEL)
                .addBodyParameter("id", id)
                .addBodyParameter("token", token)
                .addBodyParameter("order_id", order_id)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        dialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toast.makeText(DetailOrderActivity.this, "Pesanan dibatalkan!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(DetailOrderActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailOrderActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());

                            Toast.makeText(DetailOrderActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}