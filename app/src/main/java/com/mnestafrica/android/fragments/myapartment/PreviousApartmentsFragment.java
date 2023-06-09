package com.mnestafrica.android.fragments.myapartment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
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


public class PreviousApartmentsFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private WalletTransactionAdapter mAdapter;
    private ArrayList<WalletTransaction> walletTransactionArrayList;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_previous_apartment_lyt)
    LinearLayout no_previous_apartment_lyt;

    @BindView(R.id.error_lyt)
    LinearLayout error_lyt;

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

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        walletTransactionArrayList = new ArrayList<>();
        mAdapter = new WalletTransactionAdapter(context, walletTransactionArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);

        loadPreviousApartments();

        return root;
    }

    private void loadPreviousApartments(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.PREVIOUS_UNITS)
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

                                JSONArray myArray = response.getJSONArray("data");

                                if (myArray.length() > 0){


                                    for (int i = 0; i < myArray.length(); i++) {

                                        /*JSONObject item = (JSONObject) myArray.get(i);

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
                                        mAdapter.notifyDataSetChanged();*/

                                    }

                                }else {
                                    //not data found
                                    no_previous_apartment_lyt.setVisibility(View.VISIBLE);


                                }

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_previous_apartments), message, Snackbar.LENGTH_LONG).show();

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

                            no_previous_apartment_lyt.setVisibility(View.VISIBLE);
                        }
                        else {

                            error_lyt.setVisibility(View.VISIBLE);
                            Snackbar.make(root.findViewById(R.id.frag_previous_apartments), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                        }

                    }
                });

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