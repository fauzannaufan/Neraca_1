package com.example.user.neraca_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.test.R;

import java.util.ArrayList;

/**
 * Created by User on 7/21/2016.
 */
public class ObjekTransaksiActivity extends ActionBarActivity implements View.OnClickListener {

    DBHelper mydb;
    String jenisTransaksi;
    String objekMasuk;
    String objekKeluar;
    LinearLayout container;
    int jumlah_barang;
    String[][] list_barang = new String[100][100];
    AlertDialog.Builder builder;
    String[] list_item;
    String[] list_satuan;
    String[] list_dummy = {"-Item Baru-", "Indomie"};

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.objek_penjualan);

        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        Intent intentExtras = getIntent();
        Bundle extraBundle = intentExtras.getExtras();

        jenisTransaksi = extraBundle.getString("jenis");
        objekMasuk = extraBundle.getString("waktu");
        objekKeluar = extraBundle.getString("keterangan");

        TextView objekMasukLabel = (TextView) findViewById(R.id.jenisMasukLabel);
        objekMasukLabel.setText(objekMasuk);

        TextView objekKeluarLabel = (TextView) findViewById(R.id.jenisKeluarLabel);
        objekKeluarLabel.setText(objekKeluar);

        container = (LinearLayout) findViewById(R.id.container);
        jumlah_barang = 0;
        list_barang = populateOptions();
        Log.i("ObjekTransaksiActivity", list_barang[0][0]);
        list_item = list_barang[0];
        list_satuan = list_barang[1];
        Button addItemButton = (Button)findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                builder = new AlertDialog.Builder(ObjekTransaksiActivity.this);
                builder.setTitle("Pilih Barang");
                builder.setItems(list_dummy, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        jumlah_barang++;
                        Log.i("ObjekTransaksiActivity", "input 3 sukses");
                        LayoutInflater layoutInflater =
                                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView;
                        Log.i("ObjekTransaksiActivity", "input 4 sukses");
                        if (which == 0) {
                            addView = layoutInflater.inflate(R.layout.tambah_barang_baru, null);
                            Log.i("ObjekTransaksiActivity", "input a sukses");
                        } else {
                            addView = layoutInflater.inflate(R.layout.tambah_barang, null);
                            TextView nama = (TextView) addView.findViewById(R.id.nama_barang);
                            nama.setText("Indomie");
                            TextView satuan = (TextView) addView.findViewById(R.id.satuan_barang);
                            satuan.setText("Bungkus");
                        }
                        Button buttonRemove = (Button) addView.findViewById(R.id.deleteButton);
                        Log.i("ObjekTransaksiActivity", "input 5 sukses");
                        buttonRemove.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                Log.i("ObjekTransaksiActivity", "input 6 sukses");
                            }
                        });

                        container.addView(addView);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void insertTransaksi (ArrayList<String> data){

        mydb = new DBHelper(this);

        mydb.insertTransaksi(data);
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> data = new ArrayList<String>();

        data.add(jenisTransaksi);
        data.add(objekMasuk);
        data.add(objekKeluar);

        insertTransaksi(data);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String[][] populateOptions(){
        String[][] barang = new String[1000][1000];
        DBHelper mydb;
        int i = 1;
        mydb = new DBHelper(this);

        Cursor res = mydb.getListBarang();
        res.moveToFirst();
        barang[0][0] = "-Barang Baru-";
        barang[1][0] = "--";
        while(res.isAfterLast() == false) {
            barang[0][i] = new String(res.getString(res.getColumnIndex("nama")));
            barang[1][i] = new String(res.getString(res.getColumnIndex("satuan")));
            i++;
        }

        return barang;
    }
}