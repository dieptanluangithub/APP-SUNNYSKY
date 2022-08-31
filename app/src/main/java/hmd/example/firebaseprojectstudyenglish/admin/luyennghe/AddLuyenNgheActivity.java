package hmd.example.firebaseprojectstudyenglish.admin.luyennghe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;

public class AddLuyenNgheActivity extends AppCompatActivity {
    ImageView imgBack, imgHinh, imgAdd;
    Button btnChonHinh;
    EditText edtAudio;
    Spinner spnDapAnTrue;
    final String DATABASE_NAME = "HocNgonNgu.db";
    final int REQUEST_CHOOSE_PHOTO = 321;
    SQLiteDatabase database;
    int idBLN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_luyennghe);
        imgBack = (ImageView) findViewById(R.id.imgBackAddLN);
        imgHinh = (ImageView) findViewById(R.id.imgHinhAddLN);
        imgAdd = (ImageView) findViewById(R.id.imgAddLN);
        btnChonHinh = (Button) findViewById(R.id.btnChonHinhAddLN);
        edtAudio = (EditText) findViewById(R.id.edtAudioAddLN);
        spnDapAnTrue = (Spinner) findViewById(R.id.spnDapAnTrueAddLN);
        ArrayList<String> listDapAn = new ArrayList<>();
        listDapAn.add("A");
        listDapAn.add("B");
        listDapAn.add("C");
        listDapAn.add("D");
        ArrayAdapter dapAnAdapter = new ArrayAdapter(AddLuyenNgheActivity.this,
                R.layout.support_simple_spinner_dropdown_item, listDapAn);
        spnDapAnTrue.setAdapter(dapAnAdapter);
        idBLN = getIntent().getIntExtra("idBoLuyenNghe", -1);
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLuyenNgheActivity.this, AdminLuyenNgheActivity.class);
                intent.putExtra("idBoLuyenNghe", idBLN);
                startActivity(intent);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String audio = edtAudio.getText().toString();
                String dapanDung = spnDapAnTrue.getSelectedItem().toString();
                String dapanTrue = "";

                if (audio == "" || imgHinh.getDrawable() == null){
                    Toast.makeText(AddLuyenNgheActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    byte[] hinhAnh = getByteArrayFromImageView(imgHinh);
                    switch (dapanDung) {
                        case "A":
                            dapanTrue = "1";
                            break;
                        case "B":
                            dapanTrue = "2";
                            break;
                        case "C":
                            dapanTrue = "3";
                            break;
                        case "D":
                            dapanTrue = "4";
                            break;
                    }
                    Boolean result = addLuyenNghe(idBLN, "A", "B", "C", "D", dapanTrue, hinhAnh, audio);
                    if (result == true) {
                        Toast.makeText(AddLuyenNgheActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddLuyenNgheActivity.this, AdminLuyenNgheActivity.class);
                        intent.putExtra("idBoLuyenNghe", idBLN);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AddLuyenNgheActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Boolean addLuyenNghe(int idbo, String dapanA, String dapanB, String dapanC, String dapanD, String dapanTrue, byte[] hinhAnh, String audio) {
        database = Database.initDatabase(AddLuyenNgheActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Bo", idbo);
        values.put("DapAn_A", dapanA);
        values.put("DapAn_B", dapanB);
        values.put("DapAn_C", dapanC);
        values.put("DapAn_D", dapanD);
        values.put("DapAn_True", dapanTrue);
        values.put("HinhAnh", hinhAnh);
        values.put("Audio", audio);
        long result = database.insert("LuyenNghe", null, values);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }
    private byte[] getByteArrayFromImageView(ImageView img) {
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    imgHinh.setImageBitmap(bmp);
                }
                catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
