package hmd.example.firebaseprojectstudyenglish.admin.tuvung;

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

public class AdminBoTuVungActivity extends AppCompatActivity {
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack;
    ArrayList<BoHocTap> listBoTuVung;
    BoHocTapAdapter adapter;
    ListView lvBoTuVung;
    int idbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_botuvung);
        lvBoTuVung = (ListView) findViewById(R.id.listviewAdminBTV);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminBTV);
        listBoTuVung = new ArrayList<>();
        getBoTuVung();
        adapter = new BoHocTapAdapter(AdminBoTuVungActivity.this, R.layout.row_botuvung, listBoTuVung);
        lvBoTuVung.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminBoTuVungActivity.this, AdminActivity.class));
            }
        });
        lvBoTuVung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idbo = listBoTuVung.get(position).getIdBo();
                Intent intent = new Intent(AdminBoTuVungActivity.this, AdminTuVungActivity.class);
                intent.putExtra("idBoTuVung", idbo);
                startActivity(intent);
            }
        });
    }

    private void getBoTuVung() {
        database = Database.initDatabase(AdminBoTuVungActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        listBoTuVung.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            int stt = cursor.getInt(1);
            String ten = cursor.getString(2);
            listBoTuVung.add(new BoHocTap(id, stt, ten));
        }
    }
}
