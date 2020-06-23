package me.muhammadfaisal.mycarta.v1.home.fragment.money.bottom_sheet;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.MoneyManagerFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.adapter.MoneyManagerAdapter;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.model.MoneyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddIncomeOrExpenseBottomSheet extends BottomSheetDialogFragment {

    Toolbar toolbar;
    TextView textTitle;
    Button buttonSave;
    Spinner spinnerCategory;

    EditText inputName,inputMoney,inputDesription;
    TextInputLayout inputLayoutName , inputLayoutMoney, inputLayoutDescription;

    FirebaseAuth firebaseAuth;
    String userID;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    MoneyManagerAdapter adapter;
    MoneyManagerFragment moneyManagerFragment;

    public AddIncomeOrExpenseBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_add_income_or_expense, container, false);

        String getFromWhereThisOpen = this.getTag();

        moneyManagerFragment = new MoneyManagerFragment();

        initFirebase();

        initWidget(v);

        adapter = new MoneyManagerAdapter(new ArrayList<MoneyManager>(), getActivity(), null);

        if (getFromWhereThisOpen.equals("showExpense")){
            methodExpense();
        }else{
            methodIncome();
        }
        return v;
    }

    private void initWidget(View v) {
        inputLayoutMoney = v.findViewById(R.id.inputLayoutMoney);
        inputLayoutName = v.findViewById(R.id.inputLayoutName);
        inputLayoutDescription = v.findViewById(R.id.inputLayoutDescription);

        toolbar = v.findViewById(R.id.toolbar);
        textTitle = v.findViewById(R.id.titleToolbar);
        buttonSave = v.findViewById(R.id.buttonSave);
        spinnerCategory = v.findViewById(R.id.spinnerCategory);
        inputName = v.findViewById(R.id.inputName);
        inputMoney = v.findViewById(R.id.inputMoneys);
        inputDesription = v.findViewById(R.id.inputDescription);

        ImageView icClose = v.findViewById(R.id.icClose);

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    private void methodIncome() {

        initSpinnerIncome();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodClickButtonIncome();
            }
        });
    }

    private void methodClickButtonIncome() {

        final String nameIncome = inputName.getText().toString();
        final String stringTotalIncome = inputMoney.getText().toString();
        final String descriptionIncome = inputDesription.getText().toString();
        final String categoryIncome = spinnerCategory.getSelectedItem().toString();

        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String dateNow = sdf.format(c);

        Long totalIncome = Long.parseLong(stringTotalIncome);

        final SweetAlertDialog dialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogLoading.setContentText("Loading");
        dialogLoading.show();
        dialogLoading.setCancelable(false);

        if (nameIncome.isEmpty() || stringTotalIncome.isEmpty()){
            dialogLoading.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            dialogLoading.setContentText("Uh Oh.. You haven't fill required fields");
        }else{
            reference.child("money_manager").child(userID).push()
                    .setValue(new MoneyManager(nameIncome, categoryIncome, dateNow, totalIncome ,0L, descriptionIncome))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialogLoading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                dialogLoading.setContentText("Your income has been recorded!");
                                dismiss();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

        }
    }

    private void initSpinnerIncome() {
        String[] INCOME = {"Salary", "Rewards", "Cashback", "Investment", "Refunds", "Lottery", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, INCOME);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void methodExpense() {
        Resources res = getActivity().getResources();

        initSpinnerExpense();

        textTitle.setTextColor(res.getColor(R.color.colorExpense));
        textTitle.setText("Add Expense");
        buttonSave.setText("Save Expense");
        buttonSave.setBackground(res.getDrawable(R.drawable.button_expense));
        spinnerCategory.setBackground(res.getDrawable(R.drawable.bg_spinner_expense));

        inputLayoutName.setHint("Expense Name*");
        inputLayoutMoney.setHint("Total Spending*");


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodClickButtonExpense();
            }
        });
    }

    private void methodClickButtonExpense() {

        final String nameExpense = inputName.getText().toString();
        final String stringTotalExpense = inputMoney.getText().toString();
        final String descriptionExpense = inputDesription.getText().toString();
        final String categoryExpense = spinnerCategory.getSelectedItem().toString();

        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String dateNow = sdf.format(c);

        Long totalExpense = Long.parseLong(stringTotalExpense);

        final SweetAlertDialog dialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogLoading.setTitle("Loading");
        dialogLoading.show();
        dialogLoading.setCancelable(false);

        if (nameExpense.isEmpty() || stringTotalExpense.isEmpty()){
            dialogLoading.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            dialogLoading.setContentText("Uh Oh.. You haven't fill required fields");
        }else{
            dialogLoading.cancel();
            reference.child("money_manager").child(userID).push()
                    .setValue(new MoneyManager(nameExpense, categoryExpense, dateNow, 0L, totalExpense ,descriptionExpense))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialogLoading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                dialogLoading.setContentText("Your expense has been recorded!");
                                dismiss();
                                adapter.notifyDataSetChanged();
                            }else{
                                dialogLoading.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                dialogLoading.setContentText("Uh Oh... Error " + task.getException());
                            }
                        }
                    });

        }
    }

    private void initSpinnerExpense() {
        String[] EXPENSE = {"Food & Drink", "Bills", "Shopping", "Transportation", "Electronics","Health", "Office", "Education", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, EXPENSE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
}
