package com.example.spudydev.spudy.entidades.falta.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.falta.dominio.Falta;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Main falta aluno activity.
 */
public class MainFaltaAlunoActivity extends AppCompatActivity {

    private ListView lvFaltasAluno;
    private List<Map<String, String>> listaFrequencia = new ArrayList<>();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_falta_aluno);

        lvFaltasAluno = findViewById(R.id.lvFaltasAluno);

        //Chamando método para carregar as faltas
        carregarFaltas();
    }
    //Método que resgata as faltas do banco
    private void carregarFaltas(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                montandoIterable(dataSnapshot);
                setListViewFaltasAluno();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Montando Iterable com a frequência do aluno
    private void montandoIterable(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("falta").child(getIntent().getStringExtra("codigoTurma"))
                .child(AcessoFirebase.getUidUsuario()).getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            Falta falta = dataSnapshotChild.getValue(Falta.class);
            HashMap<String, String> dicionarioFaltaAluno = new HashMap<>();
            //Método que monta o dicionario com data e presença cadastradas no banco
            montandoDicionarioFaltaAluno(falta, dicionarioFaltaAluno);
        }
    }
    //Montando dicionario com informações da frequência do aluno
    private void montandoDicionarioFaltaAluno(Falta falta, Map<String, String> dicionarioFaltaAluno) {
        if (falta.isPresenca()){
            dicionarioFaltaAluno.put("data", falta.getData());
            dicionarioFaltaAluno.put("presenca", "Presente");

            listaFrequencia.add(dicionarioFaltaAluno);
        }else {
            dicionarioFaltaAluno.put("data", falta.getData());
            dicionarioFaltaAluno.put("presenca", "Faltou");

            listaFrequencia.add(dicionarioFaltaAluno);
        }
    }

    //setando ListView com o auxílio de um SimpleAdapter
    private void setListViewFaltasAluno() {
        if (!listaFrequencia.isEmpty()) {
            SimpleAdapter adapter = new SimpleAdapter(this, listaFrequencia, R.layout.modelo_list_view_aluno_chamada,
                    new String[]{"data", "presenca"}, new int[]{R.id.txtData, R.id.txtPresenca});
            lvFaltasAluno.setAdapter(adapter);
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_sem_faltas));
        }
    }
}
