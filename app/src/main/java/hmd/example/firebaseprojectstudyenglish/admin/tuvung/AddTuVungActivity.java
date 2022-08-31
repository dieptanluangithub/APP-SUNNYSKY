package hmd.example.firebaseprojectstudyenglish.admin.tuvung;

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

import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddTuVungActivity extends AppCompatActivity {

    ImageView imgBack, imgHinh, imgAdd;
    Button btnChonHinh;
    EditText edtTuVung, edtNghia, edtAudio;
    Spinner spnTuLoai;
    final String DATABASE_NAME = "HocNgonNgu.db";
    final int REQUEST_CHOOSE_PHOTO = 321;
    SQLiteDatabase database;
    int idBTV = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tuvung);
        imgBack = (ImageView) findViewById(R.id.imgBackAddTV);
        imgHinh = (ImageView) findViewById(R.id.imgHinhAddTV);
        imgAdd = (ImageView) findViewById(R.id.imgAddTV);
        btnChonHinh = (Button) findViewById(R.id.btnChonHinhAddTV);
        edtTuVung = (EditText) findViewById(R.id.edtTuVungAddTV);
        edtNghia = (EditText) findViewById(R.id.edtNghiaAddTV);
        edtAudio = (EditText) findViewById(R.id.edtAudioAddTV);
        spnTuLoai = (Spinner) findViewById(R.id.spnLoaiTuAddTV);
        ArrayList<String> listTuLoai = new ArrayList<>();
        listTuLoai.add("Danh từ");
        listTuLoai.add("Động từ");
        listTuLoai.add("Tính từ");
        listTuLoai.add("Trạng từ");
        listTuLoai.add("Giới từ");
        ArrayAdapter tuLoaiAdapter = new ArrayAdapter(AddTuVungActivity.this,
                R.layout.support_simple_spinner_dropdown_item, listTuLoai);
        spnTuLoai.setAdapter(tuLoaiAdapter);
        idBTV = getIntent().getIntExtra("idBoTuVung", -1);
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTuVungActivity.this, AdminTuVungActivity.class);
                intent.putExtra("idBoTuVung", idBTV);
                startActivity(intent);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dapan = edtTuVung.getText().toString();
                String nghia = edtNghia.getText().toString();
                String audio = edtAudio.getText().toString();
                String loaitu = spnTuLoai.getSelectedItem().toString();
                if (dapan == "" || nghia == "" || audio == "" || loaitu == "" || imgHinh.getDrawable() == null) {
                    Toast.makeText(AddTuVungActivity.this, "Chưa điền đầy thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    byte[] anh = getByteArrayFromImageView(imgHinh);
                    Boolean result = addTuVung(idBTV, dapan, nghia, loaitu, audio, anh);
                    if (result == true) {
                        Toast.makeText(AddTuVungActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddTuVungActivity.this, AdminTuVungActivity.class);
                        intent.putExtra("idBoTuVung", idBTV);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AddTuVungActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Boolean addTuVung(int idbo, String dapan, String nghia, String loaitu, String audio, byte[] anh) {
        database = Database.initDatabase(AddTuVungActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Bo", idbo);
        values.put("DapAn", dapan);
        values.put("DichNghia", nghia);
        values.put("LoaiTu", loaitu);
        values.put("Audio", audio);
        values.put("HinhAnh", anh);
        long result = database.insert("TuVung", null, values);
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
