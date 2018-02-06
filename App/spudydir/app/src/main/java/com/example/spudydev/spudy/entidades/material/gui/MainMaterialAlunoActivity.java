package com.example.spudydev.spudy.entidades.material.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainMaterialAlunoActivity extends AppCompatActivity {

    private ListView lvMaterialAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material_aluno);

        lvMaterialAluno = findViewById(R.id.lvMateriaisAluno);
        carregarMateriais();
    }

    public void carregarMateriais(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.child("material").child(getIntent().getStringExtra("codigoTurma")).getChildren().iterator();
                List<String> listMaterial = new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                        Material material = dataSnapshotChild.getValue(Material.class);
                        listMaterial.add(material.getNome()+"\n~ "+material.getConteudo());
                    }
                }
                setListViewMateriais(listMaterial);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //List<String> listMensagem = new ArrayList<>(); faz add ao lado.
    public void setListViewMateriais(List<String> listViewMateriais){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewMateriais);
        lvMaterialAluno.setAdapter(itemsAdapter);
        lvMaterialAluno.setSelection(lvMaterialAluno.getAdapter().getCount()-1);
    }


}
