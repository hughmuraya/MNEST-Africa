package com.mnestafrica.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mnestafrica.android.R;
import com.mnestafrica.android.models.OpenUnit;
import com.mnestafrica.android.models.WalletTransaction;

import java.util.ArrayList;
import java.util.List;

public class OpenUnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OpenUnit> items = new ArrayList<>();

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

                    Toast.makeText(context, "Enquired about " + obj.getUnit_name(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
