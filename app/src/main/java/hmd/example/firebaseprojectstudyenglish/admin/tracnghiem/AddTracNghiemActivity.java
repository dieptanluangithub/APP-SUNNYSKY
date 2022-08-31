package hmd.example.firebaseprojectstudyenglish.admin.tracnghiem;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;

import java.util.ArrayList;

public class AddTracNghiemActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd;
    EditText edtNoiDung, edtDapAnA, edtDapAnB, edtDapAnC, edtDapAnD;
    Spinner spnDapAnTrue;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    int idBTN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracnghiem);
        imgBack = (ImageView) findViewById(R.id.imgBackAddTN);
        imgAdd = (ImageView) findViewById(R.id.imgAddTN);
        edtNoiDung = (EditText) findViewById(R.id.edtCauHoiAddTN);
        edtDapAnA = (EditText) findViewById(R.id.edtDapAnAAddTN);
        edtDapAnB = (EditText) findViewById(R.id.edtDapAnBAddTN);
        edtDapAnC = (EditText) findViewById(R.id.edtDapAnCAddTN);
        edtDapAnD = (EditText) findViewById(R.id.edtDapAnDAddTN);
        spnDapAnTrue = (Spinner) findViewById(R.id.spnDapAnTrueAddTN);
        ArrayList<String> listDapAn = new ArrayList<>();
        listDapAn.add("A");
        listDapAn.add("B");
        listDapAn.add("C");
        listDapAn.add("D");
        ArrayAdapter dapAnAdapter = new ArrayAdapter(AddTracNghiemActivity.this,
                R.layout.support_simple_spinner_dropdown_item, listDapAn);
        spnDapAnTrue.setAdapter(dapAnAdapter);
        idBTN = getIntent().getIntExtra("idBoTracNghiem", -1);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTracNghiemActivity.this, AdminTracNghiemActivity.class);
                intent.putExtra("idBoTracNghiem", idBTN);
                startActivity(intent);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noidung = edtNoiDung.getText().toString();
                String dapanA = edtDapAnA.getText().toString();
                String dapanB = edtDapAnB.getText().toString();
                String dapanC = edtDapAnC.getText().toString();
                String dapanD = edtDapAnD.getText().toString();
                String dapanDung = spnDapAnTrue.getSelectedItem().toString();
                String dapanTrue = "";
                if (noidung == "" || dapanA == "" || dapanB == "" || dapanC == "" || dapanD == ""){
                    Toast.makeText(AddTracNghiemActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    switch (dapanDung) {
                        case "A":
                            dapanTrue = "1";
                            break;
                        case "B":
                            dapanTrue = "2";
                            break;
                        case "C":
                            dapanTrue = "3";
                            break;
                        case "D":
                            dapanTrue = "4";
                            break;
                    }
                    Boolean result = addTracNghiem(idBTN, noidung, dapanA, dapanB, dapanC, dapanD, dapanTrue);
                    if (result == true) {
                        Toast.makeText(AddTracNghiemActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddTracNghiemActivity.this, AdminTracNghiemActivity.class);
                        intent.putExtra("idBoTracNghiem", idBTN);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AddTracNghiemActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Boolean addTracNghiem(int idbo, String noidung, String dapanA, String dapanB, String dapanC, String dapanD, String dapanTrue) {
        database = Database.initDatabase(AddTracNghiemActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Bo", idbo);
        values.put("NoiDung", noidung);
        values.put("DapAn_A", dapanA);
        values.put("DapAn_B", dapanB);
        values.put("DapAn_C", dapanC);
        values.put("DapAn_D", dapanD);
        values.put("DapAn_True", dapanTrue);
        long result = database.insert("TracNghiem", null, values);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

}
