package com.example.ferdilarahmi.crud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;//biar bisa OnItemClick

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText etNim, etNama;
    Button btnSimpan, btnReset, btnEdit, btnBatal;
    TextView tvData;
    ListView lvData;
    TextView tvIdEdit;

    Button btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "more info: ferdila.rahmi@student.upi.edu", Toast.LENGTH_SHORT).show();
        myDb = new DatabaseHelper(this);

        etNim = (EditText) findViewById(R.id.et_nim);
        etNama = (EditText) findViewById(R.id.et_nama);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnBatal = (Button) findViewById(R.id.btn_batal);
        tvData = (TextView) findViewById(R.id.tv_data);
        lvData = (ListView) findViewById(R.id.lv_data);
        tvIdEdit = (TextView) findViewById(R.id.tv_id_edit);//main activity

        btnHapus = (Button) findViewById(R.id.btn_hapus);

        insertData();
        viewAll();
        deleteAll();
        setDataEdit();
        cancelEdit();
        updateData();
    }

    public void insertData() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNim.getText().toString().trim().length() > 0 && etNama.getText().toString().trim().length() > 0){
//                    string.trim().equals("");
                    String nim = etNim.getText().toString();
                    String nama = etNama.getText().toString();
                    boolean isInserted = myDb.insertData(nim, nama);
                    if (isInserted == true) {
                        Toast.makeText(MainActivity.this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                    etNim.setText("");
                    etNama.setText("");
//                    editText.getText().clear();
                }else{
                    Toast.makeText(MainActivity.this, "Isikan terlebih dahulu NIM dan Nama", Toast.LENGTH_SHORT).show();
                }
                viewAll();
            }
        });
    }
    public void viewAll(){
        Cursor res = myDb.getAllData();
//        /////////////////////////////////////////////////////// cara 1 tanpa listview
//        if(res.getCount()==0){
//            //no data found
//            tvData.setText("No Data Found");
//        }else{
//            String[] values1 = new String[res.getCount()];
//            int i=0;
////            StringBuffer buffer = new StringBuffer();
//            while(res.moveToNext()){
////                buffer.append("ID : "+ res.getString(0) +"\t");
////                buffer.append("NIM : "+ res.getString(1) +"\t");
////                buffer.append("Nama : "+ res.getString(2) +"\t");
////                buffer.append("Foto : "+ res.getString(3) +"\n\n");
//                values1[i] = res.getString(1);
//                i++;
//            }
//
//            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.data, R.id.tv_nim, values1);
//            lvData.setAdapter(adapter1);
//
////            tvData.setText(buffer.toString());
//        }

        ///////////////////////////////////////////////////////// cara 2 dg listview
        if(res.getCount()==0){
            //no data found
            tvData.setVisibility(View.VISIBLE);
            tvData.setText("Tidak Ada Data Ditemukan");
            lvData.setAdapter(null);
        }else{
            tvData.setVisibility(View.GONE);
            String[] fromFieldNames = new String[]{DatabaseHelper.COL_1, DatabaseHelper.COL_2, DatabaseHelper.COL_3};
            int[] toViewIDs = new int[]{R.id.tv_id, R.id.tv_nim, R.id.tv_nama};
            SimpleCursorAdapter myCursorAdapter;
            myCursorAdapter = new SimpleCursorAdapter(this, R.layout.data, res, fromFieldNames, toViewIDs, 0);
            lvData.setAdapter(myCursorAdapter);
        }
    }

    public void deleteAll(){
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDeleteSemua();
            }
        });
    }
    public void openDialogDeleteSemua() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Hapus Semua?");
        dialog.show();
        Button btnDialogY = (Button) dialog.findViewById(R.id.dialog_yes);
        Button btnDialogN = (Button) dialog.findViewById(R.id.dialog_no);
        TextView tvDialogInfo = (TextView) dialog.findViewById(R.id.dialog_info);
        tvDialogInfo.setText("Semua Data Akan Dihapus");
        btnDialogN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnDialogY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteAll();
                viewAll();
                dialog.dismiss();
                backRestart();
                Toast.makeText(MainActivity.this, "Semua Data Terhapus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteById(View v){
        openDialogDeleteById(v);
    }
    public void openDialogDeleteById(View vListViewId) {
        LinearLayout vwParentRow = (LinearLayout)vListViewId.getParent();

        TextView child1 = (TextView)vwParentRow.findViewById(R.id.tv_id);
        TextView child2 = (TextView)vwParentRow.findViewById(R.id.tv_nim);
        TextView child3 = (TextView)vwParentRow.findViewById(R.id.tv_nama);
//        Button btnChild = (Button)vwParentRow.findViewById(R.id.btn_hapus);
        final String id = child1.getText().toString();
        final String nim = child2.getText().toString();
        final String nama = child3.getText().toString();

        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Hapus "+nim+"?");
        dialog.show();
        Button btnDialogY = (Button) dialog.findViewById(R.id.dialog_yes);
        Button btnDialogN = (Button) dialog.findViewById(R.id.dialog_no);
        TextView tvDialogInfo = (TextView) dialog.findViewById(R.id.dialog_info);
        tvDialogInfo.setText("Nama : "+nama);
        btnDialogN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnDialogY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteById(Integer.valueOf(id));
                viewAll();
                dialog.dismiss();
                backRestart();
                Toast.makeText(MainActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setDataEdit(){
        lvData.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView child1 = (TextView) view.findViewById(R.id.tv_id);
                TextView child2 = (TextView) view.findViewById(R.id.tv_nim);
                TextView child3 = (TextView) view.findViewById(R.id.tv_nama);
//                Button btnChild = (Button) view.findViewById(R.id.btn_hapus);
                tvIdEdit.setText(child1.getText().toString());
                etNim.setText(child2.getText().toString());
                etNama.setText(child3.getText().toString());
                tvIdEdit.setVisibility(View.VISIBLE);

                btnSimpan.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnBatal.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
            }
        });
    }

    private void updateData() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNim.getText().toString().trim().length() > 0 && etNama.getText().toString().trim().length() > 0) {
//                    string.trim().equals("");
                    String nim = etNim.getText().toString();
                    String nama = etNama.getText().toString();
                    long id = Long.valueOf(tvIdEdit.getText().toString());
                    boolean isUpdated = myDb.updateById(id, nim, nama);
                    if (isUpdated == true) {
                        Toast.makeText(MainActivity.this, "NIM :" + nim + " Berhasil Diedit", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Data Gagal Diedit", Toast.LENGTH_SHORT).show();
                    }
                    backRestart();
                } else {
                    Toast.makeText(MainActivity.this, "Isikan terlebih dahulu NIM dan Nama", Toast.LENGTH_SHORT).show();
                }
                viewAll();
            }
        });
    }

    public void cancelEdit(){
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backRestart();
            }
        });
    }

    public void backRestart(){
        tvIdEdit.setText("");
        etNim.setText("");
        etNama.setText("");
        tvIdEdit.setVisibility(View.GONE);

        btnSimpan.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        btnBatal.setVisibility(View.GONE);
        btnReset.setVisibility(View.VISIBLE);
    }
}
