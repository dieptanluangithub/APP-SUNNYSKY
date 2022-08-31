package hmd.example.firebaseprojectstudyenglish.admin.sapxepcau;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.admin.AdminActivity;
import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTap;
import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTapAdapter;
import hmd.example.firebaseprojectstudyenglish.database.Database;

public class AdminBoSapXepCauActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack;
    ArrayList<BoHocTap> listBoSapXepCau;
    BoHocTapAdapter adapter;
    ListView lvBoSapXepCau;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bosapxepcau);
        lvBoSapXepCau = (ListView) findViewById(R.id.listviewAdminBSXC);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminBSXC);
        listBoSapXepCau = new ArrayList<>();
        getBoSapXepCau();
        adapter = new BoHocTapAdapter(AdminBoSapXepCauActivity.this, R.layout.row_bosapxepcau, listBoSapXepCau);
        lvBoSapXepCau.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminBoSapXepCauActivity.this, AdminActivity.class));
            }
        });
        lvBoSapXepCau.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idbo = listBoSapXepCau.get(position).getIdBo();
                Intent intent = new Intent(AdminBoSapXepCauActivity.this, AdminSapXepCauActivity.class);
                intent.putExtra("idBoSapXepCau", idbo);
                startActivity(intent);
            }
        });
    }

    private void getBoSapXepCau() {
        database = Database.initDatabase(AdminBoSapXepCauActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        listBoSapXepCau.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            int stt = cursor.getInt(1);
            String ten = cursor.getString(2);
            listBoSapXepCau.add(new BoHocTap(id, stt, ten));
        }
    }
}
