package hmd.example.firebaseprojectstudyenglish.admin.dienkhuyet;

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



import java.util.ArrayList;
import java.util.Arrays;
import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.dienkhuyet.CauDienKhuyet;

public class EditDienKhuyetActivity extends AppCompatActivity {
    ImageView imgBack, imgEdit;
    EditText edtNoiDung, edtGoiY, edtDapAn;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ArrayList<CauDienKhuyet> listDienKhuyet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dienkhuyet);
        imgBack = (ImageView) findViewById(R.id.imgBackEditDK);
        imgEdit = (ImageView) findViewById(R.id.imgEditDK);
        edtNoiDung = (EditText) findViewById(R.id.edtCauHoiEditDK);
        edtGoiY = (EditText) findViewById(R.id.edtGoiYEditDK);
        edtDapAn = (EditText) findViewById(R.id.edtDapAnEditDK);
        listDienKhuyet = new ArrayList<>();
        int idDK = getIntent().getIntExtra("ID_DK", -1);
        CauDienKhuyet dienKhuyet = getDienKhuyetByID(idDK);
        String[] goiyArr = dienKhuyet.getGoiy().split(" ");
        edtNoiDung.setText(dienKhuyet.getNoidung() + "");
        edtGoiY.setText(dienKhuyet.getGoiy() + "");
        edtDapAn.setText(dienKhuyet.getDapan() + "");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDienKhuyetActivity.this, AdminDienKhuyetActivity.class);
                intent.putExtra("idBoDienKhuyet", dienKhuyet.getIdbo());
                startActivity(intent);
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noidung = edtNoiDung.getText().toString();
                String dapan = edtDapAn.getText().toString();
                String goiy = edtGoiY.getText().toString();
                if (noidung == "" || dapan == "" || goiy == ""){
                    Toast.makeText(EditDienKhuyetActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean check = checkDapAnInGoiY(dapan, goiy);
                    if (check == true) {
                        Boolean result = updateDienKhuyet(dienKhuyet.getIdcau(), dienKhuyet.getIdbo(), noidung, dapan, goiy);
                        if (result == true) {
                            Toast.makeText(EditDienKhuyetActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditDienKhuyetActivity.this, AdminDienKhuyetActivity.class);
                            intent.putExtra("idBoDienKhuyet", dienKhuyet.getIdbo());
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(EditDienKhuyetActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(EditDienKhuyetActivity.this, "Vui lòng nhập đúng đáp án", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private CauDienKhuyet getDienKhuyetByID(int id){
        database = Database.initDatabase(EditDienKhuyetActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM DienKhuyet WHERE ID_Cau = ?", new String[]{String.valueOf(id)});
        listDienKhuyet.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String noidung = cursor.getString(2);
            String dapan = cursor.getString(3);
            String goiy = cursor.getString(4);
            listDienKhuyet.add(new CauDienKhuyet(idcau, idbo, noidung, dapan, goiy));
        }
        return listDienKhuyet.get(0);
    }

    private Boolean updateDienKhuyet(int idcau, int idbo, String noidung, String dapan, String goiy) {
        database = Database.initDatabase(EditDienKhuyetActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Cau", idcau);
        values.put("ID_Bo", idbo);
        values.put("NoiDung", noidung);
        values.put("DapAn", dapan);
        values.put("GoiY", goiy);
        Cursor cursor = database.rawQuery("SELECT * FROM DienKhuyet WHERE ID_Cau = ?", new String[]{String.valueOf(idcau)});
        if (cursor.getCount() > 0) {
            long result = database.update("DienKhuyet", values, "ID_Cau = ?", new String[]{String.valueOf(idcau)});
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

    private Boolean checkDapAnInGoiY(String dapan, String goiy) {
        goiy = goiy.replaceAll("\\W", " ");
        goiy = goiy.trim().replaceAll("\\s{2,}", " ");
        String[] dapAn = goiy.split(" ");
        if (Arrays.asList(dapAn).contains(dapan)) {
            return true;
        }
        else {
            return false;
        }
    }
}
