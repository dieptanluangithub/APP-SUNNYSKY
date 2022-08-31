package hmd.example.firebaseprojectstudyenglish.admin.sapxepcau;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.sapxepcau.CauSapXep;

public class AdminSapXepCauActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack, imgAdd;
    ArrayList<CauSapXep> listSapXepCau;
    AdminSapXepCauAdapter adapter;
    ListView lvSapXepCau;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sapxepcau);
        Intent idIntent = getIntent();
        idbo = idIntent.getIntExtra("idBoSapXepCau", 0);
        lvSapXepCau = (ListView) findViewById(R.id.listviewAdminSXC);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminSXC);
        imgAdd = (ImageView) findViewById(R.id.imgAddAdminSXC);
        listSapXepCau = new ArrayList<>();
        getSapXepCau();
        adapter = new AdminSapXepCauAdapter(AdminSapXepCauActivity.this, listSapXepCau);
        lvSapXepCau.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSapXepCauActivity.this, AdminBoSapXepCauActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSapXepCauActivity.this, AddSapXepCauActivity.class);
                intent.putExtra("idBoSapXepCau", idbo);
                startActivity(intent);
            }
        });
    }

    private void getSapXepCau() {
        database = Database.initDatabase(AdminSapXepCauActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM SapXepCau WHERE IDBo = ?", new String[]{String.valueOf(idbo)});
        listSapXepCau.clear();
        for (int i = 0; i < cursor.getCount(); i++){
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
    }
}
