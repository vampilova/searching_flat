package com.example.ewigkeit.searching_flat.AddData;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.ewigkeit.searching_flat.App;
import com.example.ewigkeit.searching_flat.DatabaseHelper;
import com.example.ewigkeit.searching_flat.R;
import com.example.ewigkeit.searching_flat.Tables.Address;
import com.example.ewigkeit.searching_flat.Tables.District;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ewigkeit on 01/12/2018.
 */

public class AddDataDistrictActivity extends AppCompatActivity {
    @BindView(R.id.description_district)
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_district);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_district)
    public void onSaveClick() {
        DatabaseHelper databaseHelper = App.getDatabaseInstance();
        District model = new District(description.getText().toString());
        if(databaseHelper.getDistrictDao().getDistrictEquals(description.getText().toString())==null) {

            databaseHelper.getDistrictDao().insert(model);

            finish();
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle("Ошибка ввода");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Такой район уже существует")
                    .setCancelable(false)
                    .setNegativeButton("Продолжить", (dialog, id) -> {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }
}
