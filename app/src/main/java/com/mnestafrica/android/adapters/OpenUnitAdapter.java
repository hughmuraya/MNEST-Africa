package com.mnestafrica.android.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.mnestafrica.android.R;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.fragments.FindApartmentFragment;
import com.mnestafrica.android.models.OpenUnit;
import com.mnestafrica.android.models.WalletTransaction;
import com.mnestafrica.android.models.auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mnestafrica.android.dependancies.AppController.TAG;

public class OpenUnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OpenUnit> items = new ArrayList<>();

    private auth loggedInUser;
    private View root;

    private Context context;
    private OpenUnitAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OpenUnitAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OpenUnitAdapter(Context context, List<OpenUnit> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView unitName;
        public TextView rent;
        public TextView unitSize;
        public TextView floor;
        public MaterialButton enquire;



        public OriginalViewHolder(View v) {
            super(v);
            unitName = (TextView) v.findViewById(R.id.tv_unit_name);
            rent = (TextView) v.findViewById(R.id.tv_rent);
            unitSize = (TextView) v.findViewById(R.id.tv_unit_size);
            floor = (TextView) v.findViewById(R.id.tv_floor);
            enquire = (MaterialButton) v.findViewById(R.id.btn_enquire);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_open_unit, parent, false);
        vh = new OpenUnitAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OpenUnit obj = items.get(position);
        if (holder instanceof OpenUnitAdapter.OriginalViewHolder) {
            OpenUnitAdapter.OriginalViewHolder view = (OpenUnitAdapter.OriginalViewHolder) holder;

            view.unitName.setText("Unit Name: "+obj.getUnit_name());
            view.rent.setText("Rent Amount: KES "+obj.getValue());
            view.unitSize.setText("Unit Size: "+obj.getSize());
            view.floor.setText("Floor Number:  "+obj.getFloor());

            view.enquire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("uuid", obj.getUuid());

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

                                            AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                            builder.setTitle("Success");
                                            builder.setIcon(R.drawable.ic_notified);
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Navigation.findNavController(v).navigate(R.id.nav_view_open_unit);
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.setMessage(message);
                                            AlertDialog alert = builder.create();
                                            alert.show();


                                        }
                                        else if(!status) {

                                            Snackbar.make(root.findViewById(R.id.frag_open_units), message, Snackbar.LENGTH_LONG).show();



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
                                    Log.e(TAG, error.getErrorBody());
                                    Snackbar.make(root.findViewById(R.id.frag_open_units), "Error: "+error.getErrorBody(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
