package com.example.spudydev.spudy.entidades.material.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class MainMaterialProfessorActivity extends AppCompatActivity {

    private ListView lvMateriais;
    private ArrayList<String> listaMateriais = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material_professor);

        FloatingActionButton btnFlutuanteAdicionarMaterial = findViewById(R.id.btnFlutuanteAdicionarMaterial);
        btnFlutuanteAdicionarMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAddMaterial();
            }
        });

        lvMateriais = findViewById(R.id.lvMateriais);
        carregarMateriais();
    }
    //Recuperando os materiais do banco
    public void carregarMateriais(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                montandoAraryListMaterial(dataSnapshot);
                setListViewMateriais(listaMateriais);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método que monta a lista de materiais relacionados a turma
    public void montandoAraryListMaterial(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("material").child(getIntent().getStringExtra("codigoTurma")).getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")) {
                Material material = dataSnapshotChild.getValue(Material.class);
                listaMateriais.add(material.getTitulo()+"\n~ " + material.getConteudo());
            }
        }
    }
    //Setando o ListView com os materiais cadastrados no banco
    public void setListViewMateriais(List<String> listViewMateriais){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewMateriais);
        lvMateriais.setAdapter(itemsAdapter);
        lvMateriais.setSelection(lvMateriais.getAdapter().getCount()-1);
    }
    //Intent
    public void abrirTelaAddMaterial(){
        Intent intent = new Intent(getApplicationContext(), AdicionarMaterialActivity.class);
        intent.putExtra("codigoTurma",getIntent().getStringExtra("codigoTurma"));
        startActivity(intent);
        finish();
    }
}