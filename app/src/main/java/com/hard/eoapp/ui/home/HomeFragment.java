package com.hard.eoapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hard.eoapp.R;
import com.hard.eoapp.paket.PaketActivity;

public class HomeFragment extends Fragment {
    private ImageView imgWedding, imgBirthday;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(view);
    }

    private void findView(View view) {
        imgBirthday = view.findViewById(R.id.img_birthday);
        imgWedding = view.findViewById(R.id.img_wedding);

        imgBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PaketActivity.class);
                i.putExtra("tipe", "1");
                startActivity(i);
            }
        });

        imgWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PaketActivity.class);
                i.putExtra("tipe", "2");
                startActivity(i);
            }
        });
    }
}