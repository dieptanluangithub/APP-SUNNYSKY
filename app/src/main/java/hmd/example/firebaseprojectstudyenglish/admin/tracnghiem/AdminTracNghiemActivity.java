package hmd.example.firebaseprojectstudyenglish.admin.tracnghiem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.tracnghiem.CauTracNghiem;

import java.util.ArrayList;

public class AdminTracNghiemActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack, imgAdd;
    ArrayList<CauTracNghiem> listTracNghiem;
    AdminTracNghiemAdapter adapter;
    ListView lvTracNghiem;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tracnghiem);
        Intent idIntent = getIntent();
        idbo = idIntent.getIntExtra("idBoTracNghiem", 0);
        lvTracNghiem = (ListView) findViewById(R.id.listviewAdminTN);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminTN);
        imgAdd = (ImageView) findViewById(R.id.imgAddAdminTN);
        listTracNghiem = new ArrayList<>();
        getTracNghiem();
        adapter = new AdminTracNghiemAdapter(AdminTracNghiemActivity.this, listTracNghiem);
        lvTracNghiem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminTracNghiemActivity.this, AdminBoTracNghiemActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTracNghiemActivity.this, AddTracNghiemActivity.class);
                intent.putExtra("idBoTracNghiem", idbo);
                startActivity(intent);
            }
        });
    }

    private void getTracNghiem() {
        database = Database.initDatabase(AdminTracNghiemActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM TracNghiem WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
        listTracNghiem.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String noidung = cursor.getString(2);
            String dapanA = cursor.getString(3);
            String dapanB = cursor.getString(4);
            String dapanC = cursor.getString(5);
            String dapanD = cursor.getString(6);
            String dapanTrue = cursor.getString(7);
            listTracNghiem.add(new CauTracNghiem(idcau, idbo, noidung, dapanA, dapanB, dapanC, dapanD, dapanTrue));
        }
    }
}
