package hmd.example.firebaseprojectstudyenglish.admin.bohoctap;

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
import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTap;
import hmd.example.firebaseprojectstudyenglish.database.Database;

public class EditBoHocTapActivity extends AppCompatActivity {
    ImageView imgBack, imgEdit;
    EditText edtBoHocTap;
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ArrayList<BoHocTap> listBHT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bohoctap);
        imgBack = (ImageView) findViewById(R.id.imgBackEditBHT);
        imgEdit = (ImageView) findViewById(R.id.imgEditBHT);
        edtBoHocTap = (EditText) findViewById(R.id.edtEditBoHocTap);
        listBHT = new ArrayList<>();
        int idBHT = getIntent().getIntExtra("ID_BHT", -1);
        BoHocTap boHocTap = getBoHocTapByID(idBHT);
        edtBoHocTap.setText(boHocTap.getTenBo() + "");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditBoHocTapActivity.this, AdminBoHocTapActivity.class));
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtBoHocTap.getText().toString();
                if (ten == "") {
                    Toast.makeText(EditBoHocTapActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean result = updateBoHocTap(boHocTap.getIdBo(), boHocTap.getStt(), ten);
                    if (result == true) {
                        Toast.makeText(EditBoHocTapActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditBoHocTapActivity.this, AdminBoHocTapActivity.class));
                    }
                    else {
                        Toast.makeText(EditBoHocTapActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private BoHocTap getBoHocTapByID(int id) {
        database = Database.initDatabase(EditBoHocTapActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi WHERE ID_Bo = ?", new String[]{String.valueOf(id)});
        listBHT.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int stt = cursor.getInt(1);
            String ten = cursor.getString(2);
            listBHT.add(new BoHocTap(idbo, stt, ten));
        }
        return listBHT.get(0);
    }

    private Boolean updateBoHocTap(int id, int stt, String ten) {
        database = Database.initDatabase(EditBoHocTapActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Bo", id);
        values.put("STT", stt);
        values.put("TenBoCauHoi", ten);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi WHERE ID_Bo = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = database.update("BoCauHoi", values, "ID_Bo = ?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
}
