package com.mnestafrica.android.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mnestafrica.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    @BindView(R.id.browseApartmentLayout)
    CardView find_apartment;

    @BindView(R.id.my_apartmentLayout)
    CardView my_apartment;

    @BindView(R.id.card_wallet_balance)
    CardView cardView_wallet;

    @BindView(R.id.btn_hide_balance_home)
    ImageButton hide_balance;

    @BindView(R.id.btn_show_balance_home)
    ImageButton show_balance;

    @BindView(R.id.wallet_balance)
    TextView tv_wallet_balance;

    @BindView(R.id.wallet_hide)
    View view_hide_balance;

    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        find_apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Find Apartment coming soon!", Toast.LENGTH_SHORT).show();


            }
        });

        my_apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_my_apartment);

            }
        });

        cardView_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_menu_wallet);

            }
        });

        hide_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view_hide_balance.setVisibility(View.VISIBLE);
                show_balance.setVisibility(View.VISIBLE);
                hide_balance.setVisibility(View.GONE);
                tv_wallet_balance.setVisibility(View.GONE);

            }
        });

        show_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view_hide_balance.setVisibility(View.GONE);
                show_balance.setVisibility(View.GONE);
                hide_balance.setVisibility(View.VISIBLE);
                tv_wallet_balance.setVisibility(View.VISIBLE);

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}