package com.mnestafrica.android.fragments.wallet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mnestafrica.android.R;
import com.mnestafrica.android.adapters.WalletTransactionAdapter;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.models.WalletTransaction;
import com.mnestafrica.android.models.auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mnestafrica.android.dependancies.AppController.TAG;


public class WalletFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private TextInputEditText amount;
    private TextInputEditText phone;

    private WalletTransactionAdapter mAdapter;
    private ArrayList<WalletTransaction> walletTransactionArrayList;

    @BindView(R.id.card_pay_rent)
    CardView card_pay_rent;

    @BindView(R.id.card_pay_invoice)
    CardView card_pay_invoice;

    @BindView(R.id.card_send)
    CardView card_send;

    @BindView(R.id.card_pay_top_up)
    CardView card_pay_top_up;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_transactions_lyt)
    LinearLayout no_transactions_lyt;

    @BindView(R.id.error_lyt)
    LinearLayout error_lyt;

    @BindView(R.id.btn_hide_balance)
    ImageButton hide_balance;

    @BindView(R.id.btn_show_balance)
    ImageButton show_balance;

    @BindView(R.id.btn_refresh_balance)
    ImageButton refresh_balance;

    @BindView(R.id.btn_refresh_transaction)
    ImageButton refresh_transactions;

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

        initialise();

        walletTransactionArrayList = new ArrayList<>();
        mAdapter = new WalletTransactionAdapter(context, walletTransactionArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);

        loadWalletDetails();
        loadWalletTransactions();


        return root;
    }

    private void initialise(){

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

        card_pay_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(WalletFragment.this).navigate(R.id.nav_pay_rent);

            }
        });

        card_pay_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(WalletFragment.this).navigate(R.id.nav_pay_invoice);

            }
        });

        card_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               withdrawDialog();

            }
        });

        card_pay_top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topUpDialog();

            }
        });

        refresh_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadWalletDetails();

            }
        });

        refresh_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadWalletTransactions();

            }
        });


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

                        walletTransactionArrayList.clear();

                        if (recyclerView!=null)
                            recyclerView.setVisibility(View.VISIBLE);

                        if (shimmer_my_container!=null){
                            shimmer_my_container.stopShimmerAnimation();
                            shimmer_my_container.setVisibility(View.GONE);
                        }


                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String message = response.has("message") ? response.getString("message") : "";

                            if (status){

                                JSONArray myArray = response.getJSONArray("trans");

                                if (myArray.length() > 0){


                                    for (int i = 0; i < myArray.length(); i++) {

                                        JSONObject item = (JSONObject) myArray.get(i);

                                        int id = item.has("id") ? item.getInt("id") : 0;
                                        int wallet_id = item.has("wallet_id") ? item.getInt("wallet_id") : 0;
                                        String amount = item.has("amount") ? item.getString("amount") : "";
                                        String previous_balance = item.has("previous_balance") ? item.getString("previous_balance") : "";
                                        String transaction_type = item.has("transaction_type") ? item.getString("transaction_type") : "";
                                        String source = item.has("source") ? item.getString("source") : "";
                                        String trx_id = item.has("trx_id") ? item.getString("trx_id") : "";
                                        String narration = item.has("narration") ? item.getString("narration") : "";
                                        String created_at = item.has("created_at") ? item.getString("created_at") : "";
                                        String updated_at = item.has("updated_at") ? item.getString("updated_at") : "";

                                        WalletTransaction newWalletTransaction = new WalletTransaction(id,wallet_id,amount,previous_balance,transaction_type,source,trx_id,narration,created_at,updated_at);

                                        walletTransactionArrayList.add(newWalletTransaction);
                                        mAdapter.notifyDataSetChanged();

                                    }

                                }else {
                                    //not data found
                                    no_transactions_lyt.setVisibility(View.VISIBLE);


                                }

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
                        if (recyclerView!=null)
                            recyclerView.setVisibility(View.VISIBLE);

                        if (shimmer_my_container!=null){
                            shimmer_my_container.stopShimmerAnimation();
                            shimmer_my_container.setVisibility(View.GONE);
                        }

//                        Log.e(TAG, String.valueOf(error.getErrorCode()));

                        if (error.getErrorCode() == 0){

                            no_transactions_lyt.setVisibility(View.VISIBLE);
                        }
                        else {

                            error_lyt.setVisibility(View.VISIBLE);
                            Snackbar.make(root.findViewById(R.id.frag_wallet), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                        }

                    }
                });

    }

    private void topUpDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_top_up);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        amount = (TextInputEditText) dialog.findViewById(R.id.etxt_amount);
        phone = (TextInputEditText) dialog.findViewById(R.id.etxt_phone);



        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((MaterialButton) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((MaterialButton) dialog.findViewById(R.id.btn_top_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("amount", amount.getText().toString());
                    jsonObject.put("mobile", phone.getText().toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String auth_token = loggedInUser.getAuth_token();

                AndroidNetworking.post(Constants.ENDPOINT+Constants.WALLET_TOP_UP)
                        .addHeaders("Authorization","Token "+ auth_token)
                        .addHeaders("Accept", "*/*")
                        .addHeaders("Accept", "gzip, deflate, br")
                        .addHeaders("Connection","keep-alive")
                        .setContentType("application.json")
                        .addJSONObjectBody(jsonObject) // posting json
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e(TAG, response.toString());

                                try {

                                    boolean  status = response.has("success") && response.getBoolean("success");
                                    String error = response.has("error") ? response.getString("error") : "";
                                    String message = response.has("message") ? response.getString("message") : "";


                                    if (status){

                                        dialog.dismiss();
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        loadWalletDetails();
                                        loadWalletTransactions();

                                    }
                                    else if(!status) {

                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    }
                                    else {

                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.e(TAG, error.getErrorBody());
                                Toast.makeText(context, "Error: "+error.getErrorBody(), Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void withdrawDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_send_mpesa);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        amount = (TextInputEditText) dialog.findViewById(R.id.etxt_withdraw_amount);
        phone = (TextInputEditText) dialog.findViewById(R.id.etxt_withdraw_phone);



        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((MaterialButton) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((MaterialButton) dialog.findViewById(R.id.btn_withdraw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("amount", amount.getText().toString());
                    jsonObject.put("mobile", phone.getText().toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String auth_token = loggedInUser.getAuth_token();

                AndroidNetworking.post(Constants.ENDPOINT+Constants.WALLET_WITHDRAW)
                        .addHeaders("Authorization","Token "+ auth_token)
                        .addHeaders("Accept", "*/*")
                        .addHeaders("Accept", "gzip, deflate, br")
                        .addHeaders("Connection","keep-alive")
                        .setContentType("application.json")
                        .addJSONObjectBody(jsonObject) // posting json
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener(){
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e(TAG, response.toString());

                                try {

                                    boolean  status = response.has("success") && response.getBoolean("success");
                                    String error = response.has("error") ? response.getString("error") : "";
                                    String message = response.has("message") ? response.getString("message") : "";


                                    if (status){

                                        dialog.dismiss();
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        loadWalletDetails();
                                        loadWalletTransactions();

                                    }
                                    else if(!status) {

                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    }
                                    else {

                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.e(TAG, error.getErrorBody());
                                Toast.makeText(context, "Error: "+error.getErrorBody(), Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_my_container.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_my_container.stopShimmerAnimation();
        super.onPause();
    }

}