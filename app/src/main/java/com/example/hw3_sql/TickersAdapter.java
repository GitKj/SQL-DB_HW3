package com.example.hw3_sql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TickersAdapter extends BaseAdapter {

    private ArrayList<Tickers> tickersList;
    private Context context;

    public TickersAdapter(ArrayList<Tickers> list, Context context)
    {
        this.tickersList = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return this.tickersList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tickersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);

            holder = new ViewHolder();
            holder.ticker = (TextView) convertView.findViewById(R.id.textview_ticker);
            holder.company = (TextView) convertView.findViewById(R.id.textview_company);
            holder.pps = (TextView) convertView.findViewById(R.id.textview_pps);
            holder.transAmount = (TextView) convertView.findViewById(R.id.textview_transAmount);
            holder.transType = (TextView) convertView.findViewById(R.id.textview_transType);
            holder.orderType = (TextView) convertView.findViewById(R.id.textview_orderType);
            holder.confCode = (TextView) convertView.findViewById(R.id.textview_confCode);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Tickers ticker = tickersList.get(position);
        holder.ticker.setText("Ticker: " + ticker.getTickerName());
        holder.company.setText("Company: " + ticker.getCompanyName());
        holder.pps.setText("Price per share: " + ticker.getPPS());
        holder.transAmount.setText("Transaction Amount: " + ticker.getTransactionAmount());
        holder.transType.setText("Transaction Type: " + ticker.getTransType());
        holder.orderType.setText("Type of Order: " + ticker.getOrderType());
        holder.confCode.setText("Confirmation Code: " + ticker.getConfirmationCode());
        return convertView;
    }

    private static class ViewHolder
    {
        TextView ticker;
        TextView company;
        TextView pps;
        TextView transAmount;
        TextView transType;
        TextView orderType;
        TextView confCode;
    }
}
