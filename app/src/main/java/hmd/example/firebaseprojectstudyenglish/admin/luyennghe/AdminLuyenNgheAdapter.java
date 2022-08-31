package hmd.example.firebaseprojectstudyenglish.admin.luyennghe;

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
import hmd.example.firebaseprojectstudyenglish.luyennghe.CauLuyenNghe;

public class AdminLuyenNgheAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CauLuyenNghe> list;
    SQLiteDatabase database;

    public AdminLuyenNgheAdapter(Context context, ArrayList<CauLuyenNghe> list) {
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
        View view = inflater.inflate(R.layout.row_luyennghe, null);
        TextView txtTenLuyenNghe = (TextView) view.findViewById(R.id.tvTenLuyenNghe);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEditLN);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteLN);
        CauLuyenNghe ln = list.get(position);
        txtTenLuyenNghe.setText("Bài " + ln.getIdbai() + "");
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditLuyenNgheActivity.class);
                intent.putExtra("ID_LN", ln.getIdbai());
                context.startActivity(intent);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa bài nghe này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean result = deleteLuyenNghe(ln.getIdbai());
                        list.clear();
                        if (result == true) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        list = getLuyenNghe(ln.getIdbo());
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

    private Boolean deleteLuyenNghe(int id) {
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        long result = database.delete("LuyenNghe", "ID_Bai = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private ArrayList<CauLuyenNghe> getLuyenNghe(int id) {
        ArrayList<CauLuyenNghe> listLN = new ArrayList<>();
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bo = ?", new String[]{String.valueOf(id)});
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
            listLN.add(new CauLuyenNghe(idbai, idbo, dapanA, dapanB, dapanC, dapanD, dapanTrue, hinhAnh, audio));
        }
        return listLN;
    }
}
