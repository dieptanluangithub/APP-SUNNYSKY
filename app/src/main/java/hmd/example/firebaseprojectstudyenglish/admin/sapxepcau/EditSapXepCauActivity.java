package hmd.example.firebaseprojectstudyenglish.admin.sapxepcau;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.sapxepcau.CauSapXep;

import java.util.ArrayList;

public class EditSapXepCauActivity extends AppCompatActivity {
    ImageView imgBack, imgEdit;
    EditText edtNoiDung, edtPart1, edtPart2, edtPart3, edtPart4;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ArrayList<CauSapXep> listSapXepCau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sapxepcau);
        imgBack = (ImageView) findViewById(R.id.imgBackEditSXC);
        imgEdit = (ImageView) findViewById(R.id.imgEditSXC);
        edtNoiDung = (EditText) findViewById(R.id.edtCauHoiEditSXC);
        edtPart1 = (EditText) findViewById(R.id.edtPart1EditSXC);
        edtPart2 = (EditText) findViewById(R.id.edtPart2EditSXC);
        edtPart3 = (EditText) findViewById(R.id.edtPart3EditSXC);
        edtPart4 = (EditText) findViewById(R.id.edtPart4EditSXC);
        listSapXepCau = new ArrayList<>();
        int idSXC = getIntent().getIntExtra("ID_SXC", -1);
        CauSapXep sapxepCau = getSapXepCauByID(idSXC);
        edtNoiDung.setText(sapxepCau.getDapan() + "");
        edtPart1.setText(sapxepCau.getPart1() + "");
        edtPart2.setText(sapxepCau.getPart2() + "");
        edtPart3.setText(sapxepCau.getPart3() + "");
        edtPart4.setText(sapxepCau.getPart4() + "");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSapXepCauActivity.this, AdminSapXepCauActivity.class);
                intent.putExtra("idBoSapXepCau", sapxepCau.getIdbo());
                startActivity(intent);
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dapan = edtNoiDung.getText().toString();
                String part1 = edtPart1.getText().toString();
                String part2 = edtPart2.getText().toString();
                String part3 = edtPart3.getText().toString();
                String part4 = edtPart4.getText().toString();
                if (dapan == "" || part1 == "" || part2 == "" || part3 == "" || part4 == ""){
                    Toast.makeText(EditSapXepCauActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean check = checkPartInResult(dapan, part1, part2, part3, part4);
                    if (check == true) {
                        Boolean result = updateSapXepCau(sapxepCau.getIdcau(), sapxepCau.getIdbo(), dapan, part1, part2, part3, part4);
                        if (result == true) {
                            Toast.makeText(EditSapXepCauActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditSapXepCauActivity.this, AdminSapXepCauActivity.class);
                            intent.putExtra("idBoSapXepCau", sapxepCau.getIdbo());
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(EditSapXepCauActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(EditSapXepCauActivity.this, "Vui lòng nhập đúng nội dung tách của câu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private CauSapXep getSapXepCauByID(int id){
        database = Database.initDatabase(EditSapXepCauActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM SapXepCau WHERE IDCau = ?", new String[]{String.valueOf(id)});
        listSapXepCau.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapan = cursor.getString(2);
            String part1 = cursor.getString(3);
            String part2 = cursor.getString(4);
            String part3 = cursor.getString(5);
            String part4 = cursor.getString(6);
            listSapXepCau.add(new CauSapXep(idcau, idbo, dapan, part1, part2, part3, part4));
        }
        return listSapXepCau.get(0);
    }

    private Boolean updateSapXepCau(int idcau, int idbo, String dapan, String part1, String part2, String part3, String part4) {
        database = Database.initDatabase(EditSapXepCauActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("IDCau", idcau);
        values.put("IDBo", idbo);
        values.put("DapAN", dapan);
        values.put("Part1", part1);
        values.put("Part2", part2);
        values.put("Part3", part3);
        values.put("Part4", part4);
        Cursor cursor = database.rawQuery("SELECT * FROM SapXepCau WHERE IDCau = ?", new String[]{String.valueOf(idcau)});
        if (cursor.getCount() > 0) {
            long result = database.update("SapXepCau", values, "IDCau = ?", new String[]{String.valueOf(idcau)});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
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
