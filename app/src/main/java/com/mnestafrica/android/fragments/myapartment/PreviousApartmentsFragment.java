package com.mnestafrica.android.fragments.myapartment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnestafrica.android.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PreviousApartmentsFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    @Override
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
        root = inflater.inflate(R.layout.fragment_previous_apartments, container, false);
        unbinder = ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}