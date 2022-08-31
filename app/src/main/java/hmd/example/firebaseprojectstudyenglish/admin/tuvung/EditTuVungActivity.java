package hmd.example.firebaseprojectstudyenglish.admin.tuvung;

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
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.hoctuvung.TuVung;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditTuVungActivity extends AppCompatActivity {
    ImageView imgBack, imgHinh, imgEdit;
    Button btnChonHinh;
    EditText edtTuVung, edtNghia, edtAudio;
    Spinner spnTuLoai;
    final String DATABASE_NAME = "HocNgonNgu.db";
    final int REQUEST_CHOOSE_PHOTO = 321;
    SQLiteDatabase database;
    ArrayList<TuVung> listTuVung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tuvung);
        imgBack = (ImageView) findViewById(R.id.imgBackEditTV);
        imgHinh = (ImageView) findViewById(R.id.imgHinhEditTV);
        imgEdit = (ImageView) findViewById(R.id.imgEditTV);
        btnChonHinh = (Button) findViewById(R.id.btnChonHinhEditTV);
        edtTuVung = (EditText) findViewById(R.id.edtTuVungEditTV);
        edtNghia = (EditText) findViewById(R.id.edtNghiaEditTV);
        edtAudio = (EditText) findViewById(R.id.edtAudioEditTV);
        spnTuLoai = (Spinner) findViewById(R.id.spnLoaiTuEditTV);
        listTuVung = new ArrayList<>();
        ArrayList<String> listTuLoai = new ArrayList<>();
        listTuLoai.add("Danh từ");
        listTuLoai.add("Động từ");
        listTuLoai.add("Tính từ");
        listTuLoai.add("Trạng từ");
        listTuLoai.add("Giới từ");
        ArrayAdapter tuLoaiAdapter = new ArrayAdapter(EditTuVungActivity.this,
                R.layout.support_simple_spinner_dropdown_item, listTuLoai);
        spnTuLoai.setAdapter(tuLoaiAdapter);
        int idTV = getIntent().getIntExtra("ID_TV", -1);
        TuVung tuVung = getTuVungByID(idTV);
        edtTuVung.setText(tuVung.getDapan() + "");
        edtNghia.setText(tuVung.getDichnghia() + "");
        edtAudio.setText(tuVung.getAudio() + "");
        switch (tuVung.getLoaitu()) {
            case "Danh từ":
                spnTuLoai.setSelection(0);
                break;
            case "Động từ":
                spnTuLoai.setSelection(1);
                break;
            case "Tính từ":
                spnTuLoai.setSelection(2);
                break;
            case "Trạng từ":
                spnTuLoai.setSelection(3);
                break;
            case "Giới từ":
                spnTuLoai.setSelection(4);
                break;
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(tuVung.getAnh(), 0, tuVung.getAnh().length);
        imgHinh.setImageBitmap(bmp);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTuVungActivity.this, AdminTuVungActivity.class);
                intent.putExtra("idBoTuVung", tuVung.getIdbo());
                startActivity(intent);
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dapan = edtTuVung.getText().toString();
                String nghia = edtNghia.getText().toString();
                String audio = edtAudio.getText().toString();
                String loaitu = spnTuLoai.getSelectedItem().toString();
                byte[] anh = getByteArrayFromImageView(imgHinh);
                if (dapan == "" || nghia == "" || audio == "" || loaitu == "") {
                    Toast.makeText(EditTuVungActivity.this, "Chưa điền đầy thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean result = updateTuVung(tuVung.getIdtu(), tuVung.getIdbo(), dapan, nghia, loaitu, audio, anh);
                    if (result == true) {
                        Toast.makeText(EditTuVungActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditTuVungActivity.this, AdminTuVungActivity.class);
                        intent.putExtra("idBoTuVung", tuVung.getIdbo());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(EditTuVungActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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

    private TuVung getTuVungByID(int id) {
        database = Database.initDatabase(EditTuVungActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM TuVung WHERE ID_Tu = ?", new String[]{String.valueOf(id)});
        listTuVung.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idtu = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapan = cursor.getString(2);
            String nghia = cursor.getString(3);
            String loaitu = cursor.getString(4);
            String audio = cursor.getString(5);
            byte[] anh = cursor.getBlob(6);
            listTuVung.add(new TuVung(idtu, idbo, dapan, nghia, loaitu, audio, anh));
        }
        return listTuVung.get(0);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    private Boolean updateTuVung(int idtu, int idbo, String dapan, String nghia, String loaitu, String audio, byte[] anh) {
        database = Database.initDatabase(EditTuVungActivity.this, DATABASE_NAME);
        ContentValues values = new ContentValues();
        values.put("ID_Tu", idtu);
        values.put("ID_Bo", idbo);
        values.put("DapAn", dapan);
        values.put("DichNghia", nghia);
        values.put("LoaiTu", loaitu);
        values.put("Audio", audio);
        values.put("HinhAnh", anh);
        Cursor cursor = database.rawQuery("SELECT * FROM TuVung WHERE ID_Tu = ?", new String[]{String.valueOf(idtu)});
        if (cursor.getCount() > 0) {
            long result = database.update("TuVung", values, "ID_Tu = ?", new String[]{String.valueOf(idtu)});
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
