package com.example.spudydev.spudy.entidades.material.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Main material aluno activity.
 */
public class MainMaterialAlunoActivity extends AppCompatActivity {

    private ListView lvMateriaisAluno;

    //ArrayList com os dicionários dos materiais para inserirUsuarioDAO no SimpleAdapter
    private List<HashMap<String, String>> arrayMateriais = new ArrayList<>();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material_aluno);

        lvMateriaisAluno = findViewById(R.id.lvMateriaisAluno);
        carregarMateriais();
    }
    //Método que recupera os materiais da turma e insere no ListView
    private void carregarMateriais(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                montandoArrayListMaterial(dataSnapshot);
                setListViewMateriais();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Montando array com os materiais da turma
    private void montandoArrayListMaterial(DataSnapshot dataSnapshot) {
        //O getChildren() retorna um Iterable
        Iterable<DataSnapshot> referencia = dataSnapshot.child("material").child(getIntent().getStringExtra("codigoTurma")).getChildren();
        for (DataSnapshot dataSnapshotChild : referencia) {
            HashMap<String,String> dicionarioMateriais = new HashMap<>();

            Material material = dataSnapshotChild.getValue(Material.class);

            dicionarioMateriais.put("titulo", material.getTitulo());
            dicionarioMateriais.put("link", material.getConteudo());
            arrayMateriais.add(dicionarioMateriais);
        }
    }
    //setando ListView com o auxílio de um SimpleAdapter
    private void setListViewMateriais() {
        if (!arrayMateriais.isEmpty()) {
            SimpleAdapter adapter = new SimpleAdapter(this, arrayMateriais, R.layout.modelo_list_view_materiais_aluno,
                    new String[]{"titulo", "link"},
                    new int[]{R.id.txtTituloMaterial,
                            R.id.txtLinkMaterial});
            lvMateriaisAluno.setAdapter(adapter);
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_sem_materiais));
        }
    }
}