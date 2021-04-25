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


public class HomeFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_my_apartment)
    TextView tv_my_apartment;

    @BindView(R.id.browseApartmentLayout)
    CardView find_apartment;

    @BindView(R.id.my_apartmentLayout)
    CardView my_apartment;

    @BindView(R.id.tv_rent_balance)
    TextView tv_rent_balance;

    @BindView(R.id.tv_invoice_balance)
    TextView tv_invoice_balance;

    @BindView(R.id.card_wallet_balance)
    CardView cardView_wallet;

    @BindView(R.id.btn_hide_balance_home)
    ImageButton hide_balance;

    @BindView(R.id.btn_show_balance_home)
    ImageButton show_balance;

    @BindView(R.id.tv_wallet_balance)
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

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadCurrentUser();
        loadWalletDetails();
        loadOpenRent();
        loadOpenInvoice();
        loadCurrentUnit();

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

    private void loadCurrentUser(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.CURRENT_USER)
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

                            int id = response.has("id") ? response.getInt("id") : 0;
                            String  email = response.has("email") ? response.getString("email") : "" ;
                            String  username = response.has("username") ? response.getString("username") : "" ;
                            String  last_login = response.has("last_login") ? response.getString("last_login") : "" ;
                            String  date_joined = response.has("date_joined") ? response.getString("date_joined") : "" ;

                            JSONObject myProfile = response.has("profile") ? response.getJSONObject("profile") : null ;

                            int idp = myProfile.has("id") ? myProfile.getInt("id") : 0;
                            String  uuid = response.has("uuid") ? response.getString("uuid") : "" ;
                            String first_name = myProfile.has("first_name") ? myProfile.getString("first_name") : "";
                            String last_name = myProfile.has("last_name") ? myProfile.getString("last_name") : "";
                            String id_number = myProfile.has("id_number") ? myProfile.getString("id_number") : "";
                            String phone_no = myProfile.has("msisdn") ? myProfile.getString("msisdn") : "";
                            String terms_accepted = myProfile.has("terms_accepted") ? myProfile.getString("terms_accepted") : "";
                            int hapokash = myProfile.has("hapokash") ? myProfile.getInt("hapokash") : 0;
                            int user = myProfile.has("user") ? myProfile.getInt("user") : 0;


                            tv_name.setText(first_name+" "+last_name);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_my_profile), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

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


                                tv_wallet_balance.setText(current_balance);

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_home), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_home), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private void loadOpenRent(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.OPEN_RENT)
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

                                String total_due = response.has("total_due") ? response.getString("total_due") : "";


                                tv_rent_balance.setText(total_due);

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_home), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_home), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private void loadOpenInvoice(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.OPEN_INVOICES)
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

                                String total_due = response.has("total_due") ? response.getString("total_due") : "";


                                tv_invoice_balance.setText(total_due);

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_home), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_home), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private void loadCurrentUnit(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.CURRENT_UNIT)
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

                                JSONObject data = response.has("data") ? response.getJSONObject("data") : null ;

                                int id = data.has("id") ? data.getInt("id") : 0;

                                JSONObject curr_unit = data.has("curr_unit") ? data.getJSONObject("curr_unit") : null ;

                                 int idu = curr_unit.has("id") ? curr_unit.getInt("id") : 0;
                                 String  uuid = curr_unit.has("uuid") ? curr_unit.getString("uuid") : "" ;
                                 String  unit_name = curr_unit.has("unit_name") ? curr_unit.getString("unit_name") : "" ;
                                 String  type_of_unit = curr_unit.has("type_of_unit") ? curr_unit.getString("type_of_unit") : "" ;
                                 String  property = curr_unit.has("property") ? curr_unit.getString("property") : "" ;


                                String  start_date = data.has("start_date") ? data.getString("start_date") : "" ;
                                int tenant = data.has("tenant") ? data.getInt("tenant") : 0;

                                tv_my_apartment.setText(property);

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_home), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                            Snackbar.make(root.findViewById(R.id.frag_home), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();




                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}