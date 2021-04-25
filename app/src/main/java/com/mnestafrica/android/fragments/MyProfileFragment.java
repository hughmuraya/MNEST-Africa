package com.mnestafrica.android.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mnestafrica.android.R;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.models.auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mnestafrica.android.dependancies.AppController.TAG;


public class MyProfileFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    @BindView(R.id.card_name)
    MaterialTextView card_name;

    @BindView(R.id.card_phone)
    MaterialTextView card_phone;

    @BindView(R.id.card_wallet_id)
    MaterialTextView card_wallet_id;

    @BindView(R.id.etxt_first_name)
    TextInputEditText etxt_first_name;

    @BindView(R.id.etxt_lastname)
    TextInputEditText etxt_last_name;

    @BindView(R.id.etxt_phone_number)
    TextInputEditText etxt_phone;

    @BindView(R.id.etxt_id_number)
    TextInputEditText etxt_id_no;

    @BindView(R.id.etxt_email)
    TextInputEditText etxt_email;

    @BindView(R.id.btn_update_profile)
    MaterialButton btn_update_profile;


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
        root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadCurrentUser();

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdateUserDetails();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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


                            card_name.setText(first_name+" "+last_name);
                            card_phone.setText(phone_no);
                            card_wallet_id.setText(String.valueOf(hapokash));

                            etxt_first_name.setText(first_name);
                            etxt_last_name.setText(last_name);
                            etxt_phone.setText(phone_no);
                            etxt_id_no.setText(id_number);
                            etxt_email.setText(email);


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

    private void doUpdateUserDetails() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", etxt_email.getText().toString());
            jsonObject.put("first_name", etxt_first_name.getText().toString());
            jsonObject.put("last_name", etxt_last_name.getText().toString());
            jsonObject.put("id_number", etxt_id_no.getText().toString());
            jsonObject.put("msisdn", etxt_phone.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.patch(Constants.ENDPOINT+Constants.UPDATE_USER)
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

                            String  errors = response.has("error") ? response.getString("error") : "" ;


                            if (response.has("id")){

                                NavHostFragment.findNavController(MyProfileFragment.this).navigate(R.id.nav_menu_home);
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{

                                Toast.makeText(context, errors, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_my_profile), "Error: "+error.getErrorBody(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

}