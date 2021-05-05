package com.mnestafrica.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;


import com.mnestafrica.android.R;
import com.mnestafrica.android.models.LeaseAgreement;


import java.util.ArrayList;
import java.util.List;


public class LeaseAgreementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LeaseAgreement> items = new ArrayList<>();

    private View root;

    private Context context;
    private LeaseAgreementAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(LeaseAgreementAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public LeaseAgreementAdapter(Context context, List<LeaseAgreement> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView unitName;
        public LinearLayout link;



        public OriginalViewHolder(View v) {
            super(v);
            unitName = (TextView) v.findViewById(R.id.tv_unit_name);
            link = (LinearLayout) v.findViewById(R.id.lyt_parent);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lease_agreement, parent, false);
        vh = new LeaseAgreementAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LeaseAgreement obj = items.get(position);
        if (holder instanceof LeaseAgreementAdapter.OriginalViewHolder) {
            LeaseAgreementAdapter.OriginalViewHolder view = (LeaseAgreementAdapter.OriginalViewHolder) holder;

            view.unitName.setText(obj.getUnit());

            view.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    WebView myWebView = new WebView(context);
                    myWebView.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                            myWebView.loadUrl(obj.getLink());
                        }
                    });
                    myWebView.loadUrl(obj.getLink());

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
