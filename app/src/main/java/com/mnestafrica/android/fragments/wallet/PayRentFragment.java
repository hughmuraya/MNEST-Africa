package com.mnestafrica.android.fragments.wallet;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.mnestafrica.android.fragments.myapartment.CurrentApartmentFragment;
import com.mnestafrica.android.models.RentInvoice;
import com.mnestafrica.android.models.auth;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mnestafrica.android.dependancies.AppController.TAG;


public class PayRentFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private int rentID = 0;
    private String uuid = "";

    ArrayList<String> rentInvoiceList;
    ArrayList<RentInvoice> rentInvoices;

    @BindView(R.id.rent_Invoice_Spinner)
    SearchableSpinner rent_Invoice_Spinner;

    @BindView(R.id.tv_Rent_due)
    MaterialTextView tv_Rent_due;

    @BindView(R.id.tv_Rent_paid)
    MaterialTextView tv_Rent_paid;

    @BindView(R.id.etxt_rent_pay)
    TextInputEditText etxt_rent_pay;

    @BindView(R.id.btn_pay_rent)
    MaterialButton btn_pay_rent;

    @BindView(R.id.btn_cancel)
    MaterialButton btn_cancel;

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
        root = inflater.inflate(R.layout.fragment_pay_rent, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadOpenRentInvoice();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(PayRentFragment.this).navigate(R.id.nav_menu_wallet);

            }
        });

        btn_pay_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doPayRentInvoice();

            }
        });

        return root;
    }

    private void loadOpenRentInvoice() {

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
                        Log.e(TAG, response.toString());

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String  message = response.has("message") ? response.getString("message") : "" ;
                            String  errors = response.has("errors") ? response.getString("errors") : "" ;


                            rentInvoices = new ArrayList<RentInvoice>();
                            rentInvoiceList = new ArrayList<String>();

                            rentInvoices.clear();
                            rentInvoiceList.clear();

                            if (status){

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                    String invoice_no = jsonObject.has("invoice_no") ? jsonObject.getString("invoice_no") : "";
                                    String property_name = jsonObject.has("property_name") ? jsonObject.getString("property_name") : "";
                                    int amount = jsonObject.has("amount") ? jsonObject.getInt("amount") : 0;
                                    String uuid = jsonObject.has("uuid") ? jsonObject.getString("uuid") : "";
                                    String unit_name = jsonObject.has("unit_name") ? jsonObject.getString("unit_name") : "";
                                    String username = jsonObject.has("username") ? jsonObject.getString("username") : "";
                                    String created_at = jsonObject.has("created_at") ? jsonObject.getString("created_at") : "";
                                    int paid = jsonObject.has("paid") ? jsonObject.getInt("paid") : 0;
                                    String Status = jsonObject.has("status") ? jsonObject.getString("status") : "";
                                    int penalties = jsonObject.has("penalties") ? jsonObject.getInt("penalties") : 0;


                                    RentInvoice newRentInvoice = new RentInvoice(invoice_no,property_name,amount,uuid,unit_name,username,created_at,paid,Status,penalties);

                                    rentInvoices.add(newRentInvoice);
                                    rentInvoiceList.add(newRentInvoice.getInvoice_no());
                                }


                                rentInvoices.add(new RentInvoice("","",0,"","","","",0,"",0));
                                rentInvoiceList.add("Select a rent invoice here...");

                                int pos =rentInvoices.indexOf(new RentInvoice("","",0,"","","","",0,"",0));
                                if (pos >= rentID)
                                    pos=0;

                                ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        rentInvoiceList){
                                    @Override
                                    public int getCount() {
                                        return super.getCount(); // you don't display last item. It is used as hint.
                                    }
                                };

                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                if (rent_Invoice_Spinner != null){
                                    rent_Invoice_Spinner.setAdapter(aa);
                                    rent_Invoice_Spinner.setSelection(pos);

//                                    organizationID = organizations.get(aa.getCount()-1).getId();

                                    rent_Invoice_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                                           tv_Rent_due.setText("Rent to pay: KES "+String.valueOf(rentInvoices.get(position).getAmount()));
                                           tv_Rent_paid.setText("Rent Paid: KES "+String.valueOf(rentInvoices.get(position).getPaid()));
                                           uuid = rentInvoices.get(position).getUuid();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                }

                            } else{

                                Snackbar.make(root.findViewById(R.id.frag_pay_rent), errors, Snackbar.LENGTH_LONG).show();


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                            Snackbar.make(root.findViewById(R.id.frag_pay_rent), e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Log.e(TAG, String.valueOf(error.getErrorCode()));

                        Snackbar.make(root.findViewById(R.id.frag_pay_rent), error.getErrorDetail(), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    private void doPayRentInvoice(){

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("amount", etxt_rent_pay.getText().toString());
            jsonObject.put("inv_uuid", uuid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.post(Constants.ENDPOINT+Constants.PAY_RENT)
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

                                NavHostFragment.findNavController(PayRentFragment.this).navigate(R.id.nav_menu_wallet);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            }
                            else if(!status) {

                                Snackbar.make(root.findViewById(R.id.frag_pay_rent), message, Snackbar.LENGTH_LONG).show();
                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_pay_rent), error, Snackbar.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, error.getErrorBody());
                        Snackbar.make(root.findViewById(R.id.frag_pay_rent), "Error: "+error.getErrorBody(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}