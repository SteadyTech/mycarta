package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.bottomsheet.MenuBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.bottomsheet.TransactionBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;
import me.muhammadfaisal.mycarta.v2.model.TransactionModel;

public class DetailCardActivity extends AppCompatActivity {

    private TextView textCardNumber;
    private TextView textBalance;
    private TextView textIncome;
    private TextView textAdvice;
    private TextView textExpense;
    private BarChart barChart;

    private CardModel cardModel;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private Query query;

    private ArrayList<TransactionModel> transactionModels;
    private ArrayList<BarEntry> incomes;
    private ArrayList<BarEntry> expenses;

    private int checkedID = 0;

    {
        this.transactionModels = new ArrayList<>();
        this.incomes = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_detail_card);

        this.extract();

        this.init();

        this.data();

    }
    private void init() {
        this.textCardNumber = findViewById(R.id.textCardNumber);
        this.textBalance = findViewById(R.id.textBalance);
        this.textIncome = findViewById(R.id.textIncome);
        this.textExpense = findViewById(R.id.textExpense);
        this.textAdvice = findViewById(R.id.textAdvice);
        this.barChart = findViewById(R.id.barChart);
        this.barChart.setFitBars(true);
        this.barChart.setPinchZoom(true);
        this.barChart.setTouchEnabled(true);

        this.reference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();
    }


    public void data() {
        this.textCardNumber.setText(CartaHelper.lastCardNumber(this.cardModel.getCardNumber()));

        this.query = this.reference.child(Constant.TRANSACTION_PATH).child(this.auth.getUid()).orderByChild("cardNumber").equalTo(this.cardModel.getCardNumber());
        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TransactionModel transactionModel = snapshot.getValue(TransactionModel.class);

                    DetailCardActivity.this.transactionModels.add(transactionModel);
                }
                DetailCardActivity.this.chart(DetailCardActivity.this.transactionModels);
                long income = 0L;
                long expense = 0L;
                long balance = 0L;
                for (TransactionModel transactionModel : DetailCardActivity.this.transactionModels){

                    if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))){
                        income += Long.valueOf(CartaHelper.decryptString(transactionModel.getAmount()));
                    }else if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.EXPENSE))){
                        expense += Long.valueOf(CartaHelper.decryptString(transactionModel.getAmount()));
                    }

                    if (income > expense){
                        DetailCardActivity.this.textAdvice.setText(DetailCardActivity.this.getResources().getString(R.string.finances_well_controlled));
                    } else if (income < expense) {
                        DetailCardActivity.this.textAdvice.setText(DetailCardActivity.this.getResources().getString(R.string.finances_expense_too_high));
                    }else{
                        DetailCardActivity.this.textAdvice.setText(DetailCardActivity.this.getResources().getString(R.string.finance_balance));
                    }

                    balance = income - expense;

                    DetailCardActivity.this.textIncome.setText(CartaHelper.currencyFormat(income));
                    DetailCardActivity.this.textExpense.setText(CartaHelper.currencyFormat(expense));
                    DetailCardActivity.this.textBalance.setText(CartaHelper.currencyFormat(balance));


                    Log.d("Sum Income", String.valueOf(income));
                    Log.d("Sum Expense", String.valueOf(expense));
                    Log.d("Sum Expense", String.valueOf(balance));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chart(ArrayList<TransactionModel> transactionModels) {
        for (int i = 0; i < transactionModels.size(); i++){

            TransactionModel transactionModel = transactionModels.get(i);

            if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))){
                this.incomes.add(new BarEntry(Float.parseFloat(String.valueOf(i)), Float.parseFloat(CartaHelper.decryptString(transactionModel.getAmount()))));
            }else{
                this.expenses.add(new BarEntry(Float.parseFloat(String.valueOf(i)), Float.parseFloat(CartaHelper.decryptString(transactionModel.getAmount()))));
            }

            BarDataSet incomeSet = new BarDataSet(this.incomes, Constant.CODE.INCOME);
            BarDataSet expenseSet = new BarDataSet(this.expenses, Constant.CODE.EXPENSE);

            this.barChart.animateXY(2000, 2000);

            BarData barData = new BarData(incomeSet, expenseSet);
            incomeSet.setColors(this.getResources().getColor(R.color.colorIncome));
            expenseSet.setColors(this.getResources().getColor(R.color.colorExpense));

            this.barChart.setData(barData);
            this.barChart.setDrawValueAboveBar(true);
        }
    }
    private void extract() {
        this.cardModel = (CardModel) getIntent().getSerializableExtra("card");
    }

    public void openTransaction(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", cardModel);

        TransactionBottomSheetFragment transactionBottomSheetFragment = new TransactionBottomSheetFragment();
        transactionBottomSheetFragment.setArguments(bundle);
        transactionBottomSheetFragment.show(this.getSupportFragmentManager(), Constant.TAG.DETAIL_CARD_ACTIVITY_TAG);
    }

    public void openMenu(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", cardModel);

        MenuBottomSheetFragment menuBottomSheetFragment = new MenuBottomSheetFragment();
        menuBottomSheetFragment.setArguments(bundle);
        menuBottomSheetFragment.show(getSupportFragmentManager(), Constant.TAG.DETAIL_CARD_ACTIVITY_TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.barChart.setVisibility(View.GONE);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void back(View view){
        this.finish();
    }
}
