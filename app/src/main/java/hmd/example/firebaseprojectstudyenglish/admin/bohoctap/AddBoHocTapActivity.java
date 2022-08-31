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

import hmd.example.firebaseprojectstudyenglish.R;
import java.util.ArrayList;

import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTap;
import hmd.example.firebaseprojectstudyenglish.database.Database;

public class AddBoHocTapActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd;
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ArrayList<BoHocTap> listBHT;
    EditText edtBoHocTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bohoctap);
        imgBack = (ImageView) findViewById(R.id.imgBackAddBHT);
        imgAdd = (ImageView) findViewById(R.id.imgAddBHT);
        edtBoHocTap = (EditText) findViewById(R.id.edtAddBoHocTap);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBoHocTapActivity.this, AdminBoHocTapActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtBoHocTap.getText().toString();
                if (ten == "") {
                    Toast.makeText(AddBoHocTapActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
                else {
                    int max = getMaxSTTBoHocTap();
                    Boolean result = addBoHocTap(max + 1, ten);
                    if (result == true) {
                        Toast.makeText(AddBoHocTapActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddBoHocTapActivity.this, AdminBoHocTapActivity.class));
                    }
                    else {
                        Toast.makeText(AddBoHocTapActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private int getMaxSTTBoHocTap() {
        database = Database.initDatabase(AddBoHocTapActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        ArrayList<Integer> listSTT = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int stt = cursor.getInt(1);
            listSTT.add(stt);
        }
        int max = listSTT.get(0);
        for (int i = 1; i < listSTT.size(); i++) {
            if (listSTT.get(i) > max) {
                max = listSTT.get(i);
            }
        }
        return max;
    }
    private Boolean addBoHocTap(int stt, String ten) {
        database = Database.initDatabase(AddBoHocTapActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("STT", stt);
        values.put("TenBoCauHoi", ten);
        long result = database.insert("BoCauHoi", null, values);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }
}
