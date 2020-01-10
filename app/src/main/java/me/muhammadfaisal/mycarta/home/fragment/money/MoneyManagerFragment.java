package me.muhammadfaisal.mycarta.home.fragment.money;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import lecho.lib.hellocharts.view.PieChartView;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.fragment.money.adapter.MoneyManagerAdapter;
import me.muhammadfaisal.mycarta.home.fragment.money.bottom_sheet.AddIncomeOrExpenseBottomSheet;
import me.muhammadfaisal.mycarta.home.fragment.money.model.MoneyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoneyManagerFragment extends Fragment implements View.OnClickListener{

    public RecyclerView recyclerView;
    MoneyManagerAdapter adapter;
    ArrayList<MoneyManager> moneyManagers  = new ArrayList<>();
    RecyclerView.LayoutManager manager;
    ProgressBar progressBar;

    public TextView textMoney, textTotalIncome, textTotalExpense;

    public PieChartView pieChart;

    Button buttonIncome, buttonExpense;

    CardView card;

    public ArrayList<HashMap<String, Object>> map;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    RelativeLayout dataBottomSheet;

    public MoneyManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_money_manager, container, false);

        initWidgets(v);

        initFirebase();

        return v;
    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        final String userId = auth.getUid();

        reference.child("money_manager").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                methodGettingData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodGettingData(DataSnapshot dataSnapshot) {
        moneyManagers.clear();

        String stringDate = null;
        map = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

            HashMap<String , Object> hashMap = new HashMap<>();

            MoneyManager moneyManager = snapshot.getValue(MoneyManager.class);

            if(stringDate == null){
                stringDate = moneyManager.getDate();
                hashMap.put("header", stringDate);
                map.add(hashMap);
            }else{
                if(!Objects.equals(stringDate, moneyManager.getDate())){
                    stringDate = moneyManager.getDate();
                    hashMap.put("header", stringDate);
                    map.add(hashMap);
                }
            }

            moneyManager.setKey(snapshot.getKey());
            hashMap = new HashMap<>();
            moneyManagers.add(moneyManager);
            hashMap.put("detail", moneyManager);
            map.add(hashMap);
        }
        
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void initWidgets(View v) {
        buttonIncome = v.findViewById(R.id.buttonIncome);
        buttonExpense = v.findViewById(R.id.buttonExpense);
        textMoney = v.findViewById(R.id.textCurrentBalance);
        textTotalIncome = v.findViewById(R.id.textTotalIncome);
        textTotalExpense = v.findViewById(R.id.textTotalTransaction);
        card = v.findViewById(R.id.cardView);
        pieChart = v.findViewById(R.id.chartPie);

        buttonIncome.setOnClickListener(this);
        buttonExpense.setOnClickListener(this);

        dataBottomSheet = v.findViewById(R.id.dataMoneyManagerBottomSheet);


        final BottomSheetBehavior behavior = BottomSheetBehavior.from(dataBottomSheet);
        behavior.setPeekHeight(120);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        initBottomSheet(v);
    }

    private void initBottomSheet(View v) {

        progressBar = v.findViewById(R.id.progressBar);
        recyclerView = v.findViewById(R.id.recyclerMoney);

        adapter = new MoneyManagerAdapter(moneyManagers,getActivity(), this);

        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        String zero = NumberFormat.getCurrencyInstance(new Locale("id","id")).format(0);

        if (moneyManagers.size() == 0){
            textMoney.setText(zero);
            textTotalIncome.setText(zero);
            textTotalExpense.setText(zero);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonIncome){
            showIncomeBottomSheet();
        }else{
            showExpenseBottomSheet();
        }
    }

    private void showExpenseBottomSheet() {
        AddIncomeOrExpenseBottomSheet bottomSheet = new AddIncomeOrExpenseBottomSheet();
        bottomSheet.show(getFragmentManager(), "showExpense");
    }

    private void showIncomeBottomSheet() {
        AddIncomeOrExpenseBottomSheet bottomSheet = new AddIncomeOrExpenseBottomSheet();
        bottomSheet.show(getFragmentManager(), "showIncome");
    }
}
