package com.example.ewigkeit.searching_flat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ewigkeit.searching_flat.Adapters.ImageGalleryAdapter;
import com.example.ewigkeit.searching_flat.Tables.Address;
import com.example.ewigkeit.searching_flat.Tables.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EstateActivity extends AppCompatActivity {
    @BindView(R.id.rv_images)
    RecyclerView recyclerView;
    @BindView(R.id.district_one_estate)
    TextView district;
    @BindView(R.id.cost_one_estate)
    TextView cost;
    @BindView(R.id.repairtype_one_estate)
    TextView repairType;
    @BindView(R.id.roomnumber_one_estate)
    TextView roomNumber;
    @BindView(R.id.square_one_estate)
    TextView square;
    @BindView(R.id.description_one_estate)
    TextView description;

    private DatabaseHelper databaseHelper;
    private int userId;
    private int estateId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        estateId = intent.getIntExtra("id", 0);
        userId = intent.getIntExtra("userId",1);
        databaseHelper = App.getDatabaseInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ImageGalleryAdapter recyclerAdapter = new ImageGalleryAdapter(this, databaseHelper.getPhotosDao().getPhotosByEstateId(estateId));
        recyclerView.setAdapter(recyclerAdapter);
        square.setText("Площадь: " + databaseHelper.getEstateDao().getEstateById(estateId).getSquare());
        cost.setText(databaseHelper.getEstateDao().getEstateById(estateId).getCost() + " руб.");
        repairType.setText("Тип ремонта: " + databaseHelper.getRepairTypeDao().getRepairTypeById(databaseHelper.getEstateDao().getEstateById(estateId).getRepairTypeId()).getDescription());
        roomNumber.setText("Кол-во комнат: " + databaseHelper.getEstateDao().getEstateById(estateId).getRoomNumber());
        description.setText(databaseHelper.getEstateDao().getEstateById(estateId).getDescription());
        Address address = databaseHelper.getAddressDao().getAddressById(databaseHelper.getEstateDao().getEstateById(estateId).getId());
        district.setText("Район: " + databaseHelper.getDistrictDao().getDistrictById(address.getDistrictId()).getDescription());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(EstateActivity.this,BigPhotoActivity.class);
                        intent.putExtra("photo",databaseHelper.getPhotosDao().getPhotosByEstateId(estateId).get(position).getUrl());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    @OnClick(R.id.request_button)
    public void onRequestClick()  {
        Request request = new Request(userId,estateId);
        if(databaseHelper.getRequestDao().getRequestEquals(userId,estateId)!=null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle("Ошибка отправки отклика");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Отклик уже был отправлен")
                    .setCancelable(false)
                    .setNegativeButton("ОК", (dialog, id) -> {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }else {
            databaseHelper.getRequestDao().insert(request);
            if (databaseHelper.getRequestDao().getRequestEquals(userId, estateId) != null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set title
                alertDialogBuilder.setTitle("Подтверждение отклика");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Отклик отправлен!")
                        .setCancelable(false)
                        .setNegativeButton("ОК", (dialog, id) -> {
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
}
