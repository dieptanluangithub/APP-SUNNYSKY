package hmd.example.firebaseprojectstudyenglish.admin.dienkhuyet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import hmd.example.firebaseprojectstudyenglish.R;

import java.util.ArrayList;

import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.dienkhuyet.CauDienKhuyet;

public class AdminDienKhuyetAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CauDienKhuyet> list;
    SQLiteDatabase database;

    public AdminDienKhuyetAdapter(Context context, ArrayList<CauDienKhuyet> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_dienkhuyet, null);
        TextView txtTenDienKhuyet = (TextView) view.findViewById(R.id.tvTenDienKhuyet);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEditDK);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteDK);
        CauDienKhuyet dk = list.get(position);
        txtTenDienKhuyet.setText(dk.getNoidung() + "");
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditDienKhuyetActivity.class);
                intent.putExtra("ID_DK", dk.getIdcau());
                context.startActivity(intent);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa câu điền khuyết này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean result = deleteDienKhuyet(dk.getIdcau());
                        list.clear();
                        if (result == true) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        list = getDienKhuyet(dk.getIdbo());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    private Boolean deleteDienKhuyet(int id) {
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        long result = database.delete("DienKhuyet", "ID_Cau = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private ArrayList<CauDienKhuyet> getDienKhuyet(int id) {
        ArrayList<CauDienKhuyet> listDK = new ArrayList<>();
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        Cursor cursor = database.rawQuery("SELECT * FROM DienKhuyet WHERE ID_Bo = ?", new String[]{String.valueOf(id)});
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String noidung = cursor.getString(2);
            String dapan = cursor.getString(3);
            String goiy = cursor.getString(4);
            listDK.add(new CauDienKhuyet(idcau, idbo, noidung, dapan, goiy));
        }
        return listDK;
    }
}
