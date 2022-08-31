package hmd.example.firebaseprojectstudyenglish.admin.sapxepcau;

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



import java.util.ArrayList;
import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.Database;
import hmd.example.firebaseprojectstudyenglish.sapxepcau.CauSapXep;

public class AdminSapXepCauAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CauSapXep> list;
    SQLiteDatabase database;

    public AdminSapXepCauAdapter(Context context, ArrayList<CauSapXep> list) {
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
        View view = inflater.inflate(R.layout.row_sapxepcau, null);
        TextView txtTenSapXepCau = (TextView) view.findViewById(R.id.tvTenSapXepCau);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEditSXC);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteSXC);
        CauSapXep sxc = list.get(position);
        txtTenSapXepCau.setText(sxc.getDapan() + "");
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditSapXepCauActivity.class);
                intent.putExtra("ID_SXC", sxc.getIdcau());
                context.startActivity(intent);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa câu sắp xếp này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean result = deleteSapXepCau(sxc.getIdcau());
                        list.clear();
                        if (result == true) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        list = getSapXepCau(sxc.getIdbo());
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

    private Boolean deleteSapXepCau(int id) {
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        long result = database.delete("SapXepCau", "IDCau = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private ArrayList<CauSapXep> getSapXepCau(int id) {
        ArrayList<CauSapXep> listSXC = new ArrayList<>();
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        Cursor cursor = database.rawQuery("SELECT * FROM SapXepCau WHERE IDBo = ?", new String[]{String.valueOf(id)});
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapan = cursor.getString(2);
            String part1 = cursor.getString(3);
            String part2 = cursor.getString(4);
            String part3 = cursor.getString(5);
            String part4 = cursor.getString(6);
            listSXC.add(new CauSapXep(idcau, idbo, dapan, part1, part2, part3, part4));
        }
        return listSXC;
    }
}
