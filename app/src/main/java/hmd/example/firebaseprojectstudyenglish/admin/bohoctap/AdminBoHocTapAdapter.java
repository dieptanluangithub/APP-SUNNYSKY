package hmd.example.firebaseprojectstudyenglish.admin.bohoctap;

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
import hmd.example.firebaseprojectstudyenglish.bohoctap.BoHocTap;
import hmd.example.firebaseprojectstudyenglish.database.Database;

public class AdminBoHocTapAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BoHocTap> list;
    SQLiteDatabase database;

    public AdminBoHocTapAdapter(Context context, ArrayList<BoHocTap> list) {
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
        View view = inflater.inflate(R.layout.row_bohoctap, null);
        TextView txtTenBo = (TextView) view.findViewById(R.id.tvTenBo);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEditBHT);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteBHT);
        BoHocTap bht = list.get(position);
        txtTenBo.setText(bht.getTenBo() + "");
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditBoHocTapActivity.class);
                intent.putExtra("ID_BHT", bht.getIdBo());
                context.startActivity(intent);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa bộ học tập này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean result = deleteBoHocTap(bht.getIdBo());
                        list.clear();
                        if (result == true) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        list = getBoHocTap();
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

    private Boolean deleteBoHocTap(int id) {
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        long result = database.delete("BoCauHoi", "ID_Bo = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private ArrayList<BoHocTap> getBoHocTap() {
        ArrayList<BoHocTap> listBHT = new ArrayList<>();
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int  stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            listBHT.add(new BoHocTap(idbo, stt, tenbo));
        }
        return listBHT;
    }
}
