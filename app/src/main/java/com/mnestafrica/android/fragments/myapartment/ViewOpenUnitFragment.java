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
import com.mnestafrica.android.adapters.OpenUnitAdapter;
import com.mnestafrica.android.adapters.WalletTransactionAdapter;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.models.OpenUnit;
import com.mnestafrica.android.models.WalletTransaction;
import com.mnestafrica.android.models.auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewOpenUnitFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private OpenUnitAdapter mAdapter;
    private ArrayList<OpenUnit> openUnitArrayList;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_open_unit_lyt)
    LinearLayout no_open_unit_lyt;

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
        root = inflater.inflate(R.layout.fragment_view_open_unit, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadOpenUnits();

        openUnitArrayList = new ArrayList<>();
        mAdapter = new OpenUnitAdapter(context, openUnitArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);


        return root;
    }

    private void loadOpenUnits(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.VACANT_UNITS)
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

                        openUnitArrayList.clear();

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

                                        JSONObject item = (JSONObject) myArray.get(i);

                                        int id = item.has("id") ? item.getInt("id") : 0;
                                        String uuid = item.has("uuid") ? item.getString("uuid") : "";
                                        String unit_name = item.has("unit_name") ? item.getString("unit_name") : "";
                                        String value = item.has("value") ? item.getString("value") : "";
                                        String service_charge = item.has("service_charge") ? item.getString("service_charge") : "";
                                        String floor = item.has("floor") ? item.getString("floor") : "";
                                        String size = item.has("size") ? item.getString("size") : "";
                                        String security_deposit = item.has("security_deposit") ? item.getString("security_deposit") : "";


                                        OpenUnit newOpenUnit = new OpenUnit(id,uuid,unit_name,value,service_charge,floor,size,security_deposit);

                                        openUnitArrayList.add(newOpenUnit);
                                        mAdapter.notifyDataSetChanged();

                                    }

                                }else {
                                    //not data found
                                    no_open_unit_lyt.setVisibility(View.VISIBLE);


                                }

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_open_units), message, Snackbar.LENGTH_LONG).show();

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

                            no_open_unit_lyt.setVisibility(View.VISIBLE);
                        }
                        else {

                            error_lyt.setVisibility(View.VISIBLE);
                            Snackbar.make(root.findViewById(R.id.frag_open_units), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

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