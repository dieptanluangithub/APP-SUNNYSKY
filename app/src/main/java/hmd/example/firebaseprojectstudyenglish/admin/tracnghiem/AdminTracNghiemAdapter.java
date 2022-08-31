package hmd.example.firebaseprojectstudyenglish.admin.tracnghiem;

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
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.tracnghiem.CauTracNghiem;

import java.util.ArrayList;

public class AdminTracNghiemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CauTracNghiem> list;
    SQLiteDatabase database;

    public AdminTracNghiemAdapter(Context context, ArrayList<CauTracNghiem> list) {
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
        View view = inflater.inflate(R.layout.row_tracnghiem, null);
        TextView txtTenTracNghiem = (TextView) view.findViewById(R.id.tvTenTracNghiem);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEditTN);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteTN);
        CauTracNghiem tn = list.get(position);
        txtTenTracNghiem.setText(tn.getNoidung() + "");
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTracNghiemActivity.class);
                intent.putExtra("ID_TN", tn.getIdcau());
                context.startActivity(intent);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa câu trắc nghiệm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean result = deleteTracNghiem(tn.getIdcau());
                        list.clear();
                        if (result == true) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        list = getTracNghiem(tn.getIdbo());
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

    private Boolean deleteTracNghiem(int id) {
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        long result = database.delete("TracNghiem", "ID_Cau = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }
    private ArrayList<CauTracNghiem> getTracNghiem(int id) {
        ArrayList<CauTracNghiem> listTN = new ArrayList<>();
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        Cursor cursor = database.rawQuery("SELECT * FROM TracNghiem WHERE ID_Bo = ?", new String[]{String.valueOf(id)});
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String noidung = cursor.getString(2);
            String dapanA = cursor.getString(3);
            String dapanB = cursor.getString(4);
            String dapanC = cursor.getString(5);
            String dapanD = cursor.getString(6);
            String dapanTrue = cursor.getString(7);
            listTN.add(new CauTracNghiem(idcau, idbo, noidung, dapanA, dapanB, dapanC, dapanD, dapanTrue));
        }
        return listTN;
    }
}
