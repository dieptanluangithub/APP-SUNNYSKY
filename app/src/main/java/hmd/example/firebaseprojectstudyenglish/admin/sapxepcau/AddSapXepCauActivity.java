package hmd.example.firebaseprojectstudyenglish.admin.sapxepcau;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hmd.example.firebaseprojectstudyenglish.database.Database;

import hmd.example.firebaseprojectstudyenglish.R;
public class AddSapXepCauActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd;
    EditText edtNoiDung, edtPart1, edtPart2, edtPart3, edtPart4;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    int idBSXC = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sapxepcau);
        imgBack = (ImageView) findViewById(R.id.imgBackAddSXC);
        imgAdd = (ImageView) findViewById(R.id.imgAddSXC);
        edtNoiDung = (EditText) findViewById(R.id.edtCauHoiAddSXC);
        edtPart1 = (EditText) findViewById(R.id.edtPart1AddSXC);
        edtPart2 = (EditText) findViewById(R.id.edtPart2AddSXC);
        edtPart3 = (EditText) findViewById(R.id.edtPart3AddSXC);
        edtPart4 = (EditText) findViewById(R.id.edtPart4AddSXC);
        idBSXC = getIntent().getIntExtra("idBoSapXepCau", -1);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSapXepCauActivity.this, AdminSapXepCauActivity.class);
                intent.putExtra("idBoSapXepCau", idBSXC);
                startActivity(intent);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dapan = edtNoiDung.getText().toString();
                String part1 = edtPart1.getText().toString();
                String part2 = edtPart2.getText().toString();
                String part3 = edtPart3.getText().toString();
                String part4 = edtPart4.getText().toString();
                if (dapan == "" || part1 == "" || part2 == "" || part3 == "" || part4 == ""){
                    Toast.makeText(AddSapXepCauActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean check = checkPartInResult(dapan, part1, part2, part3, part4);
                    if (check == true) {
                        Boolean result = addSapXepCau(idBSXC, dapan, part1, part2, part3, part4);
                        if (result == true) {
                            Toast.makeText(AddSapXepCauActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddSapXepCauActivity.this, AdminSapXepCauActivity.class);
                            intent.putExtra("idBoSapXepCau", idBSXC);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(AddSapXepCauActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddSapXepCauActivity.this, "Vui lòng nhập đúng nội dung tách của câu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Boolean addSapXepCau(int idbo, String dapan, String part1, String part2, String part3, String part4) {
        database = Database.initDatabase(AddSapXepCauActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("IDBo", idbo);
        values.put("DapAn", dapan);
        values.put("Part1", part1);
        values.put("Part2", part2);
        values.put("Part3", part3);
        values.put("Part4", part4);
        long result = database.insert("SapXepCau", null, values);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    private Boolean checkPartInResult(String dapan, String part1, String part2, String part3, String part4) {
        String part = String.join(" ", part1, part2, part3, part4);
        if (dapan.equals(part) == true) {
            return true;
        }
        else {
            return false;
        }
    }
}
