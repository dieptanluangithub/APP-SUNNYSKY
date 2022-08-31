package hmd.example.firebaseprojectstudyenglish.admin.dienkhuyet;

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
import hmd.example.firebaseprojectstudyenglish.dienkhuyet.CauDienKhuyet;

public class AdminDienKhuyetActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack, imgAdd;
    ArrayList<CauDienKhuyet> listDienKhuyet;
    AdminDienKhuyetAdapter adapter;
    ListView lvDienKhuyet;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dienkhuyet);
        Intent idIntent = getIntent();
        idbo = idIntent.getIntExtra("idBoDienKhuyet", 0);
        lvDienKhuyet = (ListView) findViewById(R.id.listviewAdminDK);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminDK);
        imgAdd = (ImageView) findViewById(R.id.imgAddAdminDK);
        listDienKhuyet = new ArrayList<>();
        getDienKhuyet();
        adapter = new AdminDienKhuyetAdapter(AdminDienKhuyetActivity.this, listDienKhuyet);
        lvDienKhuyet.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDienKhuyetActivity.this, AdminBoDienKhuyetActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDienKhuyetActivity.this, AddDienKhuyetActivity.class);
                intent.putExtra("idBoDienKhuyet", idbo);
                startActivity(intent);
            }
        });
    }

    private void getDienKhuyet() {
        database = Database.initDatabase(AdminDienKhuyetActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM DienKhuyet WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
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
    }
}
