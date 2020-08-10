package me.muhammadfaisal.mycarta.v1.home.fragment.money.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.MoneyManagerFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.bottom_sheet.DetailTransactionBottomSheet;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.model.MoneyManager;

public class MoneyManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MoneyManager> moneyManagers;
    Context context;
    MoneyManagerFragment moneyManagerFragment;


    private static final int THIS_DATE = 0;
    private static final int THIS_DATA = 1;


    public MoneyManagerAdapter(ArrayList<MoneyManager> moneyManagers, Context context, MoneyManagerFragment moneyManagerFragment) {
        this.moneyManagers = moneyManagers;
        this.context = context;
        this.moneyManagerFragment = moneyManagerFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case THIS_DATE:
                return new DateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_manager_date, parent, false));
            case THIS_DATA:
                return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_manager, parent, false));
        }
        return null;
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final HashMap<String, Object> hashMap = moneyManagerFragment.map.get(position);

        Resources resources = context.getResources();

        if (hashMap.get("header") == null) {
            final MoneyManager moneyManager = (MoneyManager) hashMap.get("detail");

            DataViewHolder dataViewHolder = (DataViewHolder) holder;
            dataViewHolder.textName.setText(moneyManager.getName());


            if (moneyManager.getExpense() != 0) {
                dataViewHolder.textMoney.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(moneyManager.getExpense()));
                dataViewHolder.textMoney.setTextColor(resources.getColor(R.color.colorExpense));
            } else {

                dataViewHolder.textMoney.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(moneyManager.getIncome()));
                dataViewHolder.textMoney.setTextColor(resources.getColor(R.color.colorIncome));
            }

            dataViewHolder.textDate.setText(moneyManager.getDate());

            //Kurang ic refunds - ic cashback - ic_electronics
            setImageCategory(moneyManager, dataViewHolder);

            setDataToPieChart();

            dataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailTransactionBottomSheet bottomSheet = new DetailTransactionBottomSheet();
                    Bundle b = new Bundle();
                    b.putString("name", moneyManager.getName());
                    b.putLong("expense", moneyManager.getExpense());
                    b.putLong("income", moneyManager.getIncome());
                    b.putString("category", moneyManager.getCategory());
                    b.putString("date", moneyManager.getDate());
                    b.putString("description", moneyManager.getDescription());
                    bottomSheet.show(moneyManagerFragment.getFragmentManager(), "FromAdapter");
                    bottomSheet.setArguments(b);
                }
            });
        } else {

            final Date c = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final String dateNow = sdf.format(c);
            DateViewHolder dateViewHolder = (DateViewHolder) holder;

            String dataDate = (String) hashMap.get("header");

            if (dataDate.equals(dateNow)) {
                dateViewHolder.textDate.setText("Today");
            } else {
                dateViewHolder.textDate.setText((String) hashMap.get("header"));
            }

        }


        getCurrentBalance();

        getTotalIncome();

        getTotalExpense();
    }

    private void setImageCategory(MoneyManager moneyManager, DataViewHolder dataViewHolder) {
        Resources resources = context.getResources();

        switch (moneyManager.getCategory()) {
            case "Food & Drink":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_food_drink));
                break;
            case "Bills":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_bill));
                break;
            case "Shopping":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_shopping));
                break;
            case "Transportation":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_transportation));
                break;
            case "Electronics":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_electronics));
                break;
            case "Health":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_health));
                break;
            case "Education":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_education));
                break;
            case "Office":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_office));
                break;
            case "Salary":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_salary));
                break;
            case "Rewards":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_rewards));
                break;
            case "Cashback":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_cashback));
                break;
            case "Investment":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_invest));
                break;
            case "Refunds":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_refund));
                break;
            case "Lottery":
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_lottery));
                break;
            default:
                dataViewHolder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_more));
                break;
        }
    }


    private void setDataToPieChart() {

        long totalExpense = 0;

        for (MoneyManager moneyManager : moneyManagers) {
            totalExpense += moneyManager.getExpense();
        }
        long totalIncome = 0;

        for (MoneyManager moneyManager : moneyManagers) {
            totalIncome += moneyManager.getIncome();
        }

        Resources res = context.getResources();

        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue(totalExpense, res.getColor(R.color.colorExpense)).setLabel("Expense"));
        values.add(new SliceValue(totalIncome, res.getColor(R.color.colorIncome)).setLabel("Income"));

        PieChartData data = new PieChartData();
        data.setValues(values);
        data.setHasLabels(true);


        moneyManagerFragment.pieChart.setPieChartData(data);
        moneyManagerFragment.pieChart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                String valueOfChart = NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(value.getValue());

                Toast.makeText(context, valueOfChart, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    private void getTotalExpense() {
        long totalExpense = 0;

        for (MoneyManager moneyManager : moneyManagers) {
            totalExpense += moneyManager.getExpense();
        }
        moneyManagerFragment.textTotalExpense.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(totalExpense));
    }

    private void getTotalIncome() {
        long totalIncome = 0;

        for (MoneyManager moneyManager : moneyManagers) {
            totalIncome += moneyManager.getIncome();
        }

        moneyManagerFragment.textTotalIncome.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(totalIncome));
    }

    private void getCurrentBalance() {
        long yourBalance = 0;

        for (MoneyManager moneys : moneyManagers) {
            yourBalance += moneys.getIncome() - moneys.getExpense();
        }

        moneyManagerFragment.textMoney.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(yourBalance));
    }

    @Override
    public int getItemCount() {
        return moneyManagerFragment.map.size();
    }

    @Override
    public int getItemViewType(int position) {
        return moneyManagerFragment.map.get(position).get("header") == null ? THIS_DATA : THIS_DATE;
    }

    private class DateViewHolder extends RecyclerView.ViewHolder {

        TextView textDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.textDate);
        }
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textMoney, textDate;
        ImageView imageCategory;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            textMoney = itemView.findViewById(R.id.textAmount);
            imageCategory = itemView.findViewById(R.id.imageCategory);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}
