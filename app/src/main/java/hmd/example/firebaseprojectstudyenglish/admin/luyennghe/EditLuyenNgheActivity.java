package hmd.example.firebaseprojectstudyenglish.admin.luyennghe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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


import hmd.example.firebaseprojectstudyenglish.R;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.luyennghe.CauLuyenNghe;

public class EditLuyenNgheActivity extends AppCompatActivity {
    ImageView imgBack, imgHinh, imgEdit;
    Button btnChonHinh;
    EditText edtAudio;
    Spinner spnDapAnTrue;
    final String DATABASE_NAME = "HocNgonNgu.db";
    final int REQUEST_CHOOSE_PHOTO = 321;
    SQLiteDatabase database;
    ArrayList<CauLuyenNghe> listLuyenNghe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_luyennghe);
        imgBack = (ImageView) findViewById(R.id.imgBackEditLN);
        imgHinh = (ImageView) findViewById(R.id.imgHinhEditLN);
        imgEdit = (ImageView) findViewById(R.id.imgEditLN);
        btnChonHinh = (Button) findViewById(R.id.btnChonHinhEditLN);
        edtAudio = (EditText) findViewById(R.id.edtAudioEditLN);
        spnDapAnTrue = (Spinner) findViewById(R.id.spnDapAnTrueEditLN);
        listLuyenNghe = new ArrayList<>();
        ArrayList<String> listDapAn = new ArrayList<>();
        listDapAn.add("A");
        listDapAn.add("B");
        listDapAn.add("C");
        listDapAn.add("D");
        ArrayAdapter dapAnAdapter = new ArrayAdapter(EditLuyenNgheActivity.this,
                R.layout.support_simple_spinner_dropdown_item, listDapAn);
        spnDapAnTrue.setAdapter(dapAnAdapter);
        int idLN = getIntent().getIntExtra("ID_LN", -1);
        CauLuyenNghe luyenNghe = getLuyenNgheByID(idLN);
        edtAudio.setText(luyenNghe.getAudio());
        switch (luyenNghe.getDapanTrue()) {
            case "1":
                spnDapAnTrue.setSelection(0);
                break;
            case "2":
                spnDapAnTrue.setSelection(1);
                break;
            case "3":
                spnDapAnTrue.setSelection(2);
                break;
            case "4":
                spnDapAnTrue.setSelection(3);
                break;
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(luyenNghe.getHinhanh(), 0, luyenNghe.getHinhanh().length);
        imgHinh.setImageBitmap(bmp);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditLuyenNgheActivity.this, AdminLuyenNgheActivity.class);
                intent.putExtra("idBoLuyenNghe", luyenNghe.getIdbo());
                startActivity(intent);
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String audio = edtAudio.getText().toString();
                String dapanDung = spnDapAnTrue.getSelectedItem().toString();
                String dapanTrue = "";
                byte[] hinhAnh = getByteArrayFromImageView(imgHinh);
                if (audio == ""){
                    Toast.makeText(EditLuyenNgheActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
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
                    Boolean result = updateLuyenNghe(luyenNghe.getIdbai(), luyenNghe.getIdbo(), "A", "B", "C", "D", dapanTrue, hinhAnh, audio);
                    if (result == true) {
                        Toast.makeText(EditLuyenNgheActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditLuyenNgheActivity.this, AdminLuyenNgheActivity.class);
                        intent.putExtra("idBoLuyenNghe", luyenNghe.getIdbo());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(EditLuyenNgheActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    private CauLuyenNghe getLuyenNgheByID(int id) {
        database = Database.initDatabase(EditLuyenNgheActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bai = ?", new String[]{String.valueOf(id)});
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
        return listLuyenNghe.get(0);
    }

    private Boolean updateLuyenNghe(int idbai, int idbo, String dapanA, String dapanB, String dapanC, String dapanD, String dapanTrue, byte[] hinhAnh, String audio) {
        database = Database.initDatabase(EditLuyenNgheActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Bai", idbai);
        values.put("ID_Bo", idbo);
        values.put("DapAn_A", dapanA);
        values.put("DapAn_B", dapanB);
        values.put("DapAn_C", dapanC);
        values.put("DapAn_D", dapanD);
        values.put("DapAn_True", dapanTrue);
        values.put("HinhAnh", hinhAnh);
        values.put("Audio", audio);
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bai = ?", new String[]{String.valueOf(idbai)});
        if (cursor.getCount() > 0) {
            long result = database.update("LuyenNghe", values, "ID_Bai = ?", new String[]{String.valueOf(idbai)});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    imgHinh.setImageBitmap(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
