package com.mnestafrica.android.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.mnestafrica.android.R;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.models.auth;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class WalletFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    @BindView(R.id.btn_hide_balance)
    ImageButton hide_balance;

    @BindView(R.id.btn_show_balance)
    ImageButton show_balance;

    @BindView(R.id.tv_wallet)
    TextView tv_wallet_balance;

    @BindView(R.id.tv_wallet_id)
    TextView tv_wallet_id;

    @BindView(R.id.current_wallet_hide)
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
        root = inflater.inflate(R.layout.fragment_wallet, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadWalletDetails();

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

    private void loadWalletDetails(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.WALLET_DETAILS)
                .addHeaders("Authorization","Token "+ auth_token)
                .addHeaders("Content-Type", "application.json")
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
//                        Log.e(TAG, response.toString());

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String message = response.has("message") ? response.getString("message") : "";

                            if (status){

                                JSONObject walletDetails = response.has("wallet") ? response.getJSONObject("wallet") : null ;

                                int id = walletDetails.has("id") ? walletDetails.getInt("id") : 0;
                                String current_balance = walletDetails.has("current_balance") ? walletDetails.getString("current_balance") : "";
                                String previous_balance = walletDetails.has("previous_balance") ? walletDetails.getString("previous_balance") : "";
                                String currency = walletDetails.has("currency") ? walletDetails.getString("currency") : "";
                                String created_at = walletDetails.has("created_at") ? walletDetails.getString("created_at") : "";
                                String updated_at = walletDetails.has("updated_at") ? walletDetails.getString("updated_at") : "";


                                tv_wallet_balance.setText(currency+" "+current_balance);
                                tv_wallet_id.setText("Wallet ID: " + id);

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_wallet), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_wallet), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private void loadWalletTransactions(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.WALLET_TRANSACTIONS)
                .addHeaders("Authorization","Token "+ auth_token)
                .addHeaders("Content-Type", "application.json")
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
//                        Log.e(TAG, response.toString());

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String message = response.has("message") ? response.getString("message") : "";

                            if (status){

                                JSONObject walletDetails = response.has("wallet") ? response.getJSONObject("wallet") : null ;

                                int id = walletDetails.has("id") ? walletDetails.getInt("id") : 0;
                                String current_balance = walletDetails.has("current_balance") ? walletDetails.getString("current_balance") : "";
                                String previous_balance = walletDetails.has("previous_balance") ? walletDetails.getString("previous_balance") : "";
                                String currency = walletDetails.has("currency") ? walletDetails.getString("currency") : "";
                                String created_at = walletDetails.has("created_at") ? walletDetails.getString("created_at") : "";
                                String updated_at = walletDetails.has("updated_at") ? walletDetails.getString("updated_at") : "";




                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_wallet), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_wallet), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}