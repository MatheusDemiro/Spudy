package com.example.spudydev.spudy.entidades.material.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private ArrayList<String> listaMateriais = new ArrayList<>();

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
                montandoArrayListMaterial(dataSnapshot);
                setListViewMateriais(listaMateriais);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Montando array com os materiais da turma
    public void montandoArrayListMaterial(DataSnapshot dataSnapshot) {
        //O getChildren() retorna um Iterable
        Iterable<DataSnapshot> referencia = dataSnapshot.child("material").child(getIntent().getStringExtra("codigoTurma")).getChildren();
        for (DataSnapshot dataSnapshotChild : referencia) {
            if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                Material material = dataSnapshotChild.getValue(Material.class);
                listaMateriais.add(material.getTitulo() + "\n~ " + material.getConteudo());
            }
        }
    }
    //List<String> listMensagem = new RetornoLista<>(); faz add ao lado.
    public void setListViewMateriais(List<String> listViewMateriais){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewMateriais);
        lvMaterialAluno.setAdapter(itemsAdapter);
        lvMaterialAluno.setSelection(lvMaterialAluno.getAdapter().getCount()-1);
    }


}
