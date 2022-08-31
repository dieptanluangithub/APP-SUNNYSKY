package hmd.example.firebaseprojectstudyenglish.admin.tuvung;

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
import hmd.example.firebaseprojectstudyenglish.hoctuvung.TuVung;

import java.util.ArrayList;

public class AdminTuVungAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TuVung> list;
    SQLiteDatabase database;

    public AdminTuVungAdapter(Context context, ArrayList<TuVung> list) {
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
        View view = inflater.inflate(R.layout.row_tuvung, null);
        TextView txtTenTuVung = (TextView) view.findViewById(R.id.tvTenTuVung);
        ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEditTV);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteTV);
        TuVung tv = list.get(position);
        txtTenTuVung.setText(tv.getDapan() + "");
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTuVungActivity.class);
                intent.putExtra("ID_TV", tv.getIdtu());
                context.startActivity(intent);
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn chắc chắn muốn xóa từ vựng này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean result = deleteTuVung(tv.getIdtu());
                        list.clear();
                        if (result == true) {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        list = getTuVung(tv.getIdbo());
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

    private Boolean deleteTuVung(int id) {
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        long result = database.delete("TuVung", "ID_Tu = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }
    private ArrayList<TuVung> getTuVung(int id) {
        ArrayList<TuVung> listTV = new ArrayList<>();
        database = Database.initDatabase((Activity)context, "HocNgonNgu.db");
        Cursor cursor = database.rawQuery("SELECT * FROM TuVung WHERE ID_Bo = ?", new String[]{String.valueOf(id)});
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idtu = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapan = cursor.getString(2);
            String dichnghia = cursor.getString(3);
            String loaitu = cursor.getString(4);
            String audio = cursor.getString(5);
            byte[] anh = cursor.getBlob(6);
            listTV.add(new TuVung(idtu,idbo,dapan,dichnghia,loaitu,audio,anh));
        }
        return listTV;
    }
}
