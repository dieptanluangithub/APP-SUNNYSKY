package hmd.example.firebaseprojectstudyenglish.admin.luyennghe;

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
import hmd.example.firebaseprojectstudyenglish.luyennghe.CauLuyenNghe;

public class AdminLuyenNgheActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack, imgAdd;
    ArrayList<CauLuyenNghe> listLuyenNghe;
    AdminLuyenNgheAdapter adapter;
    ListView lvLuyenNghe;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_luyennghe);
        Intent idIntent = getIntent();
        idbo = idIntent.getIntExtra("idBoLuyenNghe", 0);
        lvLuyenNghe = (ListView) findViewById(R.id.listviewAdminLN);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminLN);
        imgAdd = (ImageView) findViewById(R.id.imgAddAdminLN);
        listLuyenNghe = new ArrayList<>();
        getLuyenNghe();
        adapter = new AdminLuyenNgheAdapter(AdminLuyenNgheActivity.this, listLuyenNghe);
        lvLuyenNghe.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLuyenNgheActivity.this, AdminBoLuyenNgheActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLuyenNgheActivity.this, AddLuyenNgheActivity.class);
                intent.putExtra("idBoLuyenNghe", idbo);
                startActivity(intent);
            }
        });
    }

    private void getLuyenNghe() {
        database = Database.initDatabase(AdminLuyenNgheActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
        listLuyenNghe.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idbai = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapanA = cursor.getString(2);
            String dapanB = cursor.getString(3);
            String dapanC = cursor.getString(4);
            String dapanD = cursor.getString(5);
            String dapanTrue = cursor.getString(6);
            byte[] hinhAnh = cursor.getBlob(7);
            String audio = cursor.getString(8);
            listLuyenNghe.add(new CauLuyenNghe(idbai, idbo, dapanA, dapanB, dapanC, dapanD, dapanTrue, hinhAnh, audio));
        }
    }
}
