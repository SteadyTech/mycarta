package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.activity.DetailCardActivity;
import me.muhammadfaisal.mycarta.v2.adapter.SpinnerAdapter;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;
import me.muhammadfaisal.mycarta.v2.model.firebase.TransactionModel;
import me.muhammadfaisal.mycarta.v2.model.realm.BalanceDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTransactionBottomSheetFragment extends BottomSheetDialogFragment implements MaterialButton.OnCheckedChangeListener, View.OnClickListener {

    private Spinner spinner;
    private TextView textCardNumber;
    private TextInputEditText inputName;
    private TextInputEditText inputAmount;
    private TextInputEditText inputDescription;
    private MaterialButtonToggleGroup materialButtonToggleGroup;
    private MaterialButton btnIncome;
    private MaterialButton btnExpense;
    private MaterialButton btnSave;

    private CardModel cardModel;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Realm realm;

    private String checked;


    public CreateTransactionBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_transaction_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.init(view);
        this.data();
    }

    private void data() {
        this.textCardNumber.setText(CartaHelper.dot().concat(CartaHelper.lastCardNumber(this.cardModel.getCardNumber())));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        DetailCardActivity detailCardActivity = (DetailCardActivity) this.getActivity();
        Objects.requireNonNull(detailCardActivity).reload();
    }

    private void init(View view) {
        this.inputAmount = view.findViewById(R.id.inputAmount);
        this.inputName = view.findViewById(R.id.inputName);
        this.inputDescription = view.findViewById(R.id.inputDescription);
        this.spinner = view.findViewById(R.id.spinnerCategory);
        this.textCardNumber = view.findViewById(R.id.textCardNumber);
        this.materialButtonToggleGroup = view.findViewById(R.id.btgType);
        this.btnIncome = view.findViewById(R.id.btnIncome);
        this.btnExpense = view.findViewById(R.id.btnExpense);
        this.btnSave = view.findViewById(R.id.buttonSave);

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.realm = Realm.getDefaultInstance();

        this.cardModel = (CardModel) Objects.requireNonNull(this.getArguments()).getSerializable("card");

        if (this.btnIncome.isChecked()) {
            String[] INCOME = {"--Select Category--", "Salary", "Rewards", "Cashback", "Investment", "Refunds", "Lottery", "Other"};
            SpinnerAdapter adapter = new SpinnerAdapter(this.getActivity(), android.R.layout.simple_spinner_item, INCOME);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
            this.checked = Constant.CODE.INCOME;
        }

        this.btnIncome.addOnCheckedChangeListener(this);
        this.btnExpense.addOnCheckedChangeListener(this);
        this.btnSave.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(MaterialButton button, boolean isChecked) {
        if (button == this.btnIncome) {
            String[] INCOME = {"--Select Category--", "Salary", "Rewards", "Cashback", "Investment", "Refunds", "Lottery", "Other"};
            SpinnerAdapter adapter = new SpinnerAdapter(this.getActivity(), android.R.layout.simple_spinner_item, INCOME);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
            this.checked = Constant.CODE.INCOME;
        }else{
            String[] EXPENSE = {"--Select Category--","Food & Drink", "Bills", "Shopping", "Transportation", "Electronics","Health", "Office", "Education", "Other"};
            SpinnerAdapter adapter = new SpinnerAdapter(this.getActivity(), android.R.layout.simple_spinner_item, EXPENSE);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
            this.checked = Constant.CODE.EXPENSE;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.btnSave)){
            this.save();
        }
    }

    private void save() {
        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String dateNow = sdf.format(c);

        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm transaction) {
                BalanceDatabase balanceDatabase = transaction.where(BalanceDatabase.class).equalTo("cardID", CreateTransactionBottomSheetFragment.this.cardModel.getCardNumber()).findFirst();

                if (balanceDatabase == null) {
                    Log.d("MainApplication", "BalanceDatabase is NULL");
                    balanceDatabase = new BalanceDatabase();
                    balanceDatabase.setCardID(CreateTransactionBottomSheetFragment.this.cardModel.getCardNumber());
                    balanceDatabase.setBalance(String.valueOf(CreateTransactionBottomSheetFragment.this.inputAmount.getText()));
                } else {
                    Log.d("MainApplication", balanceDatabase.getCardID());
                    balanceDatabase.setBalance(String.valueOf(CreateTransactionBottomSheetFragment.this.inputAmount.getText()));
                }

                transaction.copyToRealmOrUpdate(balanceDatabase);
            }
        });

        this.databaseReference.child(Constant.TRANSACTION_PATH)
                .child(this.firebaseAuth.getUid())
                .push()
                .setValue(new TransactionModel(
                        this.cardModel.getCardNumber(),
                        CartaHelper.encryptString(this.spinner.getSelectedItem().toString()),
                        CartaHelper.encryptString(dateNow),
                        CartaHelper.encryptString(this.checked),
                        CartaHelper.encryptString(String.valueOf(this.inputName.getText())),
                        CartaHelper.encryptString(String.valueOf(this.inputDescription.getText())),
                        CartaHelper.encryptString(String.valueOf(this.inputAmount.getText()))
                )).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Success Record Transaction", Toast.LENGTH_SHORT).show();
                    CreateTransactionBottomSheetFragment.this.dismiss();
                }else{
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
