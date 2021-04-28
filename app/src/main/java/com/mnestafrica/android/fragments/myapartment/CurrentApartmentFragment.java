package com.mnestafrica.android.fragments.myapartment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.fragments.MyProfileFragment;
import com.mnestafrica.android.models.auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mnestafrica.android.dependancies.AppController.TAG;


public class CurrentApartmentFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;
    private String SCHEDULE_DATE = "";

    private TextInputEditText reason;
    private TextInputEditText dateVacate;
    private TextInputEditText daysNotice;
    private TextInputEditText movingContact;


    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_lease_lyt)
    LinearLayout no_lease_lyt;

    @BindView(R.id.error_lyt)
    LinearLayout error_lyt;

    @BindView(R.id.tv_rent_due)
    TextView tv_rent_balance;

    @BindView(R.id.tv_invoice_due)
    TextView tv_invoice_balance;

    @BindView(R.id.btn_vacate_apartment)
    MaterialButton btn_vacate_apartment;

    @BindView(R.id.btn_view_open_units)
    MaterialButton btn_view_open_units;


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
        root = inflater.inflate(R.layout.fragment_current_apartment, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

       loadOpenRent();
       loadOpenInvoice();

        btn_vacate_apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vacateUnitDialog();
            }

        });

        btn_view_open_units.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(CurrentApartmentFragment.this).navigate(R.id.nav_view_open_unit);

            }

        });

        return root;
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

                                Snackbar.make(root.findViewById(R.id.frag_current_apartment), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_current_apartment), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

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

                                Snackbar.make(root.findViewById(R.id.frag_current_apartment), message, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_current_apartment), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private void vacateUnitDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_vacate_unit);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        reason = (TextInputEditText) dialog.findViewById(R.id.etxt_reason);
        dateVacate = (TextInputEditText) dialog.findViewById(R.id.etxt_date_vacate);
        daysNotice = (TextInputEditText) dialog.findViewById(R.id.etxt_days_notice);
        movingContact = (TextInputEditText) dialog.findViewById(R.id.etxt_phone);


        ((TextInputEditText) dialog.findViewById(R.id.etxt_date_vacate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cur_calender = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                long date_ship_millis = calendar.getTimeInMillis();
                                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

                                SCHEDULE_DATE = newFormat.format(new Date(date_ship_millis));

                                dateVacate.setText(SCHEDULE_DATE);
                            }
                        }, cur_calender.get(Calendar.YEAR),
                        cur_calender.get(Calendar.MONTH),
                        cur_calender.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

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

        ((MaterialButton) dialog.findViewById(R.id.btn_vacate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("reason", reason.getText().toString());
                    jsonObject.put("date", dateVacate.getText().toString());
                    jsonObject.put("days_notice", daysNotice.getText().toString());
                    jsonObject.put("moving_contact", movingContact.getText().toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String auth_token = loggedInUser.getAuth_token();

                AndroidNetworking.post(Constants.ENDPOINT+Constants.VACATE_UNIT)
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

                                        NavHostFragment.findNavController(CurrentApartmentFragment.this).navigate(R.id.nav_current_apartment);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    }
                                    else if(!status) {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                                        Snackbar.make(root.findViewById(R.id.frag_current_apartment), message, Snackbar.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
//                                        Snackbar.make(root.findViewById(R.id.frag_current_apartment), error, Snackbar.LENGTH_LONG).show();

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
//                                Snackbar.make(root.findViewById(R.id.frag_current_apartment), "Error: "+error.getErrorBody(), Snackbar.LENGTH_LONG).show();
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