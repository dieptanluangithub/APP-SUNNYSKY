package hmd.example.firebaseprojectstudyenglish.admin.tracnghiem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.admin.AdminActivity;
import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTap;
import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTapAdapter;
import hmd.example.firebaseprojectstudyenglish.database.Database;

import java.util.ArrayList;

public class AdminBoTracNghiemActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack;
    ArrayList<BoHocTap> listBoTracNghiem;
    BoHocTapAdapter adapter;
    ListView lvBoTracNghiem;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_botracnghiem);
        lvBoTracNghiem = (ListView) findViewById(R.id.listviewAdminBTN);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminBTN);
        listBoTracNghiem = new ArrayList<>();
        getBoTracNghiem();
        adapter = new BoHocTapAdapter(AdminBoTracNghiemActivity.this, R.layout.row_botracnghiem, listBoTracNghiem);
        lvBoTracNghiem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminBoTracNghiemActivity.this, AdminActivity.class));
            }
        });
        lvBoTracNghiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idbo = listBoTracNghiem.get(position).getIdBo();
                Intent intent = new Intent(AdminBoTracNghiemActivity.this, AdminTracNghiemActivity.class);
                intent.putExtra("idBoTracNghiem", idbo);
                startActivity(intent);
            }
        });
    }

    private void getBoTracNghiem() {
        database = Database.initDatabase(AdminBoTracNghiemActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        listBoTracNghiem.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            int stt = cursor.getInt(1);
            String ten = cursor.getString(2);
            listBoTracNghiem.add(new BoHocTap(id, stt, ten));
        }
    }
}
