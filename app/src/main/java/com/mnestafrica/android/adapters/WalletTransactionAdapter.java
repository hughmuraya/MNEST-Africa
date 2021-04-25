package com.mnestafrica.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mnestafrica.android.R;
import com.mnestafrica.android.models.WalletTransaction;

import java.util.ArrayList;
import java.util.List;

public class WalletTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WalletTransaction> items = new ArrayList<>();

    private Context context;
    private WalletTransactionAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(WalletTransactionAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public WalletTransactionAdapter(Context context, List<WalletTransaction> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView transactionType;
        public TextView transactionId;
        public TextView narration;
        public TextView createdBy;
        public TextView amount;


        public OriginalViewHolder(View v) {
            super(v);
            transactionType = (TextView) v.findViewById(R.id.tv_transaction_type);
            transactionId = (TextView) v.findViewById(R.id.tv_transaction_id);
            narration = (TextView) v.findViewById(R.id.tv_narration);
            createdBy = (TextView) v.findViewById(R.id.tv_date_time);
            amount = (TextView) v.findViewById(R.id.tv_amount);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_transaction, parent, false);
        vh = new WalletTransactionAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        WalletTransaction obj = items.get(position);
        if (holder instanceof WalletTransactionAdapter.OriginalViewHolder) {
            WalletTransactionAdapter.OriginalViewHolder view = (WalletTransactionAdapter.OriginalViewHolder) holder;

            view.transactionId.setText("Transaction ID: "+obj.getTrx_id());
            view.narration.setText("Narration: "+obj.getNarration());
            view.createdBy.setText(obj.getCreated_at());
            view.amount.setText("KES "+obj.getAmount());

            if (obj.getTransaction_type().contains("CR")){
                view.transactionType.setText("Received");
            }
            else{
                view.transactionType.setText("Sent");
            }

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
