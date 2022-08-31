package hmd.example.firebaseprojectstudyenglish.admin.tuvung;

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
import hmd.example.firebaseprojectstudyenglish.hoctuvung.TuVung;

import java.util.ArrayList;

public class AdminTuVungActivity extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgBack, imgAdd;
    ArrayList<TuVung> listTuVung;
    AdminTuVungAdapter adapter;
    ListView lvTuVung;
    int idbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tuvung);
        Intent idIntent = getIntent();
        idbo = idIntent.getIntExtra("idBoTuVung", 0);
        lvTuVung = (ListView) findViewById(R.id.listviewAdminTV);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminTV);
        imgAdd = (ImageView) findViewById(R.id.imgAddAdminTV);
        listTuVung = new ArrayList<>();
        getTuVung();
        adapter = new AdminTuVungAdapter(AdminTuVungActivity.this, listTuVung);
        lvTuVung.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminTuVungActivity.this, AdminBoTuVungActivity.class));
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTuVungActivity.this, AddTuVungActivity.class);
                intent.putExtra("idBoTuVung", idbo);
                startActivity(intent);
            }
        });
    }

    private void getTuVung() {
        database = Database.initDatabase(AdminTuVungActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM TuVung WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
        listTuVung.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idtu = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapan = cursor.getString(2);
            String dichnghia = cursor.getString(3);
            String loaitu = cursor.getString(4);
            String audio = cursor.getString(5);
            byte[] anh = cursor.getBlob(6);
            listTuVung.add(new TuVung(idtu,idbo,dapan,dichnghia,loaitu,audio,anh));
        }
    }
}
