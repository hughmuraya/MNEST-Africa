package com.mnestafrica.android.fragments;

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
import com.google.android.material.textview.MaterialTextView;
import com.mnestafrica.android.R;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.models.OpenUnit;
import com.mnestafrica.android.models.Property;
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


public class FindApartmentFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private int findApartmentID = 0;
    private String uuid = "";

    ArrayList<String> openUnitList;
    ArrayList<OpenUnit> openUnits;
    ArrayList<Property> properties;
    ArrayList<String> propertyList;

    @BindView(R.id.find_Spinner)
    SearchableSpinner find_Spinner;

    @BindView(R.id.tv_property_type)
    MaterialTextView tv_property_type;

    @BindView(R.id.tv_building_type)
    MaterialTextView tv_building_type;

    @BindView(R.id.tv_floor)
    MaterialTextView tv_floor;

    @BindView(R.id.tv_rent)
    MaterialTextView tv_rent;

    @BindView(R.id.tv_service_charge)
    MaterialTextView tv_service_charge;

    @BindView(R.id.tv_security_deposit)
    MaterialTextView tv_security_deposit;

    @BindView(R.id.tv_electricity)
    MaterialTextView tv_electricity;

    @BindView(R.id.tv_water)
    MaterialTextView tv_water;

    @BindView(R.id.btn_cancel)
    MaterialButton btn_cancel;

    @BindView(R.id.btn_enquire)
    MaterialButton btn_enquire;

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
        root = inflater.inflate(R.layout.fragment_find_apartment, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadVacantApartments();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(FindApartmentFragment.this).navigate(R.id.nav_menu_home);

            }
        });

        btn_enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doEnquiry();

            }
        });

        return root;
    }

    private void loadVacantApartments() {

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.get(Constants.ENDPOINT+Constants.VACANT_APARTMENTS)
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


                            openUnits = new ArrayList<OpenUnit>();
                            openUnitList = new ArrayList<String>();
                            properties = new ArrayList<Property>();
                            propertyList = new ArrayList<String>();

                            openUnits.clear();
                            openUnitList.clear();
                            properties.clear();
                            propertyList.clear();

                            if (status){

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                    int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;

                                    JSONObject property = jsonObject.getJSONObject ("property");

                                    int idp = property.has("id") ? property.getInt("id") : 0;
                                    String uuidp = property.has("uuid") ? property.getString("uuid") : "";
                                    String property_name = property.has("property_name") ? property.getString("property_name") : "";
                                    String property_type = property.has("property_type") ? property.getString("property_type") : "";
                                    String building_type = property.has("building_type") ? property.getString("building_type") : "";
                                    String electricity = property.has("electricity") ? property.getString("electricity") : "";
                                    String water = property.has("water") ? property.getString("water") : "";

                                    String uuid = jsonObject.has("uuid") ? jsonObject.getString("uuid") : "";
                                    String unit_name = jsonObject.has("unit_name") ? jsonObject.getString("unit_name") : "";
                                    String value = jsonObject.has("value") ? jsonObject.getString("value") : "";
                                    String service_charge = jsonObject.has("service_charge") ? jsonObject.getString("service_charge") : "";
                                    String floor = jsonObject.has("floor") ? jsonObject.getString("floor") : "";
                                    String size = jsonObject.has("size") ? jsonObject.getString("size") : "";
                                    String security_deposit = jsonObject.has("security_deposit") ? jsonObject.getString("security_deposit") : "";




                                    OpenUnit newOpenUnit = new OpenUnit(id,uuid,unit_name,value,service_charge,floor,size,security_deposit);

                                    Property newProperty = new Property(idp,uuidp,property_name,property_type,building_type,electricity,water);

                                    openUnits.add(newOpenUnit);
                                    openUnitList.add(newOpenUnit.getUnit_name());

                                    properties.add(newProperty);
                                    propertyList.add(newProperty.getProperty_name());
                                }


                                properties.add(new Property(0,"","","","","",""));
                                propertyList.add("Search apartment from list...");

                                int pos = properties.indexOf(new Property(0,"","","","","",""));
                                if (pos >= findApartmentID)
                                    pos=0;

                                ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        propertyList){
                                    @Override
                                    public int getCount() {
                                        return super.getCount(); // you don't display last item. It is used as hint.
                                    }
                                };

                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                if (find_Spinner != null){
                                    find_Spinner.setAdapter(aa);
                                    find_Spinner.setSelection(pos);


                                    find_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                                            tv_property_type.setText("Property Type: "+properties.get(position).getProperty_type());
                                            tv_building_type.setText("Building Type: "+properties.get(position).getBuilding_type());
                                            tv_floor.setText("Floor Number: "+openUnits.get(position).getFloor());
                                            tv_rent.setText("Rent Amount: KES "+openUnits.get(position).getValue());
                                            tv_service_charge.setText("Service Charge: KES "+openUnits.get(position).getService_charge());
                                            tv_security_deposit.setText("Security Deposit: KES "+openUnits.get(position).getSecurity_deposit());
                                            tv_electricity.setText("Electricity Payment: "+properties.get(position).getElectricity());
                                            tv_water.setText("Water Payment: "+properties.get(position).getWater());

                                            uuid = openUnits.get(position).getUuid();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {




                                        }
                                    });

                                }

                            } else{

                                Snackbar.make(root.findViewById(R.id.frag_find_apartment), errors, Snackbar.LENGTH_LONG).show();


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                            Snackbar.make(root.findViewById(R.id.frag_find_apartment), e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Log.e(TAG, String.valueOf(error.getErrorCode()));

                        Snackbar.make(root.findViewById(R.id.frag_find_apartment), error.getErrorDetail(), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    private void doEnquiry(){

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uuid", uuid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.post(Constants.ENDPOINT+Constants.ENQUIRE_UNIT)
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

                                NavHostFragment.findNavController(FindApartmentFragment.this).navigate(R.id.nav_menu_home);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            }
                            else if(!status) {

                                Snackbar.make(root.findViewById(R.id.frag_find_apartment), message, Snackbar.LENGTH_LONG).show();
                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_find_apartment), error, Snackbar.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, error.getErrorBody());
                        Snackbar.make(root.findViewById(R.id.frag_find_apartment), "Error: "+error.getErrorBody(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}