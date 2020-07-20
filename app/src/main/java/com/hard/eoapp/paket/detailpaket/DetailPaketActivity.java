package com.hard.eoapp.paket.detailpaket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hard.eoapp.MainActivity;
import com.hard.eoapp.R;
import com.hard.eoapp.paket.DetailAdapter;
import com.hard.eoapp.paket.paketmodel.DataItem;
import com.hard.eoapp.paket.paketmodel.ItemPaketItem;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.SessionPrefs;
import com.hard.eoapp.utils.UniversalModel;
import com.hard.eoapp.utils.UrlServer;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Response;

public class DetailPaketActivity extends AppCompatActivity {
    private static final String TAG = "DetailPaketActivity";
    private String TOTAL_DAY = "total";
    private TextView txtPaket, txtHarga, txtByr, txtTf;
    private ProgressBar progressBar;
    private ImageView imgPaket;
    private FloatingActionButton fabBack;
    private DetailAdapter adapter;
    private RecyclerView rv_detail;
    private EditText edtTgl;
    private CardView cvCash, cvTrans;
    private Button btnMakeOrder;
    int payParam = 0;
    long start, end;
    int th ;
    int bl ;
    int hr;
    String jam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paket);


        findView();
    }

    private void findView() {
        txtPaket = findViewById(R.id.txt_paket);
        edtTgl = findViewById(R.id.edt_tanggal);
        cvCash = findViewById(R.id.cv_bayar_ditempat);
        cvTrans = findViewById(R.id.cv_bayar_transfer);
        imgPaket = findViewById(R.id.img_paket_detail);
        progressBar = findViewById(R.id.progress_bar);
        rv_detail = findViewById(R.id.rc_detail);
        fabBack = findViewById(R.id.fab_back);
        txtByr = findViewById(R.id.txt_bayar_ditempat);
        txtTf = findViewById(R.id.txttransfer);
        txtHarga = findViewById(R.id.txt_detail_harga);
        btnMakeOrder = findViewById(R.id.btn_make_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Intent i = getIntent();
        DataItem item = i.getParcelableExtra("data_item");
        ArrayList<ItemPaketItem> itemPaketItems = i.getParcelableArrayListExtra("item_paket");
        Log.d(TAG, "onCreate: list" + itemPaketItems.size());
        String foto = item.getFoto();
        String paket = item.getNamaPake();
        final String id_paket = item.getPaketId();
        String harga = item.getHarga();
        adapter = new DetailAdapter(this, itemPaketItems);
        rv_detail.setLayoutManager(linearLayoutManager);
        rv_detail.setAdapter(adapter);
        rv_detail.setHasFixedSize(true);

        th = Calendar.getInstance().get(Calendar.YEAR);
        bl = Calendar.getInstance().get(Calendar.MONTH);
        getTomorrow();

        HelperClass.convertHarga(txtHarga, harga);

        txtPaket.setText(paket);
        if (!foto.isEmpty()) {
            HelperClass.loadGambar(this, UrlServer.URL_FOTO_PAKET + foto, progressBar, imgPaket);
        }

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cvCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(DetailPaketActivity.this, R.animator.reduce_size);
                reducer.setTarget(v);
                reducer.start();

                if (payParam == 2) {
                    AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(DetailPaketActivity.this, R.animator.regain_size);
                    regainer.setTarget(cvTrans);
                    regainer.start();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtTf.setBackgroundColor(getColor(R.color.colorPrimary));
                    } else {
                        //noinspection deprecation
                        txtTf.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                payParam = 1;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtByr.setBackgroundColor(getColor(R.color.colorAccent));
                } else {
                    //noinspection deprecation
                    txtByr.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

            }
        });

        cvTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(DetailPaketActivity.this, R.animator.reduce_size);
                reducer.setTarget(v);
                reducer.start();

                if (payParam == 1) {
                    AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(DetailPaketActivity.this, R.animator.regain_size);
                    regainer.setTarget(cvCash);
                    regainer.start();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtByr.setBackgroundColor(getColor(R.color.colorPrimary));
                    } else {
                        //noinspection deprecation
                        txtByr.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                payParam = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtTf.setBackgroundColor(getColor(R.color.colorAccent));
                } else {
                    //noinspection deprecation
                    txtTf.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        edtTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(edtTgl);
            }
        });

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtTgl.getText().toString().isEmpty() && payParam != 0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailPaketActivity.this);
                    alert.setTitle("Pesan");
                    alert.setMessage("Apakah Anda yakin ingin pesan?")
                            .setCancelable(true)
                            .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (DetailPaketActivity.this != null) {
                                        makeOrder(id_paket, edtTgl.getText().toString(), String.valueOf(payParam));
                                    }
                                }
                            })
                            .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert.show();
                }else{
                    Toast.makeText(DetailPaketActivity.this, R.string.lengkapi_data, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void makeOrder(String paket_id, String start_date, String metode) {
        final ProgressDialog dialog = new ProgressDialog(this);
        HelperClass.loading(dialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_ORDER)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("paket_id", paket_id)
                .addBodyParameter("start_date", start_date)
                .addBodyParameter("metode", metode)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        dialog.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toast.makeText(DetailPaketActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(DetailPaketActivity.this, MainActivity.class);
                                i.putExtra("trans", "trans");
                                startActivity(i);
                                finishAffinity();
                            } else {
                                Toast.makeText(DetailPaketActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailPaketActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(DetailPaketActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDateDialog(final EditText edtEndDate) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d(TAG, "onDateSet: " + String.valueOf(th));
                Log.d(TAG, "onDateSet: " + String.valueOf(hr));
                /**
                 * Keterangan :
                 * to make condition that not allowed end date
                 * AMD
                 **/
                if (year < th) {
                    Toast.makeText(DetailPaketActivity.this, R.string.cant_startt, Toast.LENGTH_SHORT).show();
                } else if (year == th) {
                    if (month < bl) {
                        Toast.makeText(DetailPaketActivity.this, R.string.cant_startt, Toast.LENGTH_SHORT).show();
                    } else if (month == bl) {
                        if (dayOfMonth <= hr) {
                            Toast.makeText(DetailPaketActivity.this, R.string.cant_startt, Toast.LENGTH_SHORT).show();
                        } else {
                           showHourDialog(edtEndDate, year, month, dayOfMonth);
                        }
                    } else if (month > bl) {
                        showHourDialog(edtEndDate, year, month, dayOfMonth);
                    }
                } else if (year > th) {
                    showHourDialog(edtEndDate, year, month, dayOfMonth);
                }
                /**
                 * Keterangan :
                 * to calculate total days
                 * AMD
                 **/
                long total = end - start;
                long difTotal = (total / (24 * 60 * 60 * 1000));
                TOTAL_DAY = String.valueOf(difTotal);
                Log.d(TAG, "onDateSet: " + TOTAL_DAY);
                Log.d(TAG, "onDateSet: "+calendar.get(Calendar.DAY_OF_MONTH));
            }
        };
        Log.d(TAG, "findView: "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        new DatePickerDialog(DetailPaketActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void showHourDialog(final EditText edtStartHour, final int year, final int month, final int day) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (timeOfDay >= 0 && timeOfDay < 9) {
                    Toast.makeText(DetailPaketActivity.this, R.string.jam_startt, Toast.LENGTH_SHORT).show();
                } else if (timeOfDay >= 15 && timeOfDay < 24) {
                    Toast.makeText(DetailPaketActivity.this, R.string.jam_startt, Toast.LENGTH_SHORT).show();
                } else {
                    edtStartHour.setText(simpleDateFormat.format(calendar.getTime()));
                    jam = simpleDateFormat.format(calendar.getTime());
                }
            }
        };
        Log.d(TAG, "findView: "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        new TimePickerDialog(DetailPaketActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 5);
        hr = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "getTomorrow: " + hr);
    }


}