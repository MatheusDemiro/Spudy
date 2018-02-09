package com.example.spudydev.spudy.entidades.turma.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TurmasProfessorActivity extends AppCompatActivity {

    private ListView lvTurmasProfessor;

    private ArrayList<String> arrayCodigosTurmas = new ArrayList<>();
    private ArrayList<String> arrayNomesTurmas = new ArrayList<>();

    private String uidProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turmas_professor);
        getSupportActionBar().hide();

        lvTurmasProfessor = findViewById(R.id.lvTurmasProfessor);

        uidProfessor = getIntent().getStringExtra("uidProfessor");

        lvTurmasProfessor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codigoTurma = arrayCodigosTurmas.get(position);
                //Chamando a camada de negócios e realizando a inserção no banco
                TurmaServices.criarTurmaAluno(codigoTurma, getApplicationContext());
            }
        });

        //Carregando as turmas do professor
        carregarTurmasProfessor();
    }
    //Resgatando as turmas ministradas pelo
    public void carregarTurmasProfessor(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resgatandoTurmasMinistradas(dataSnapshot);
                resgatandoNomeTurmas(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método que resgata as turmas ministradas pelo professor
    public void resgatandoTurmasMinistradas(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("professor").child(uidProfessor).child("turmasMinistradas").getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                arrayCodigosTurmas.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Método que resgata o nome das turmas ministradas pelo professor
    public void resgatandoNomeTurmas(DataSnapshot dataSnapshot){
        for (String codigoTurma: arrayCodigosTurmas){
            String nomeTurma = dataSnapshot.child("turma").child(codigoTurma).child("nomeTurma").getValue(String.class);
            arrayNomesTurmas.add(nomeTurma);
        }
        setListViewTurmas();
    }
    //Setando o LIstView
    public void setListViewTurmas(){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesTurmas);
        lvTurmasProfessor.setAdapter(itemsAdapter);
        lvTurmasProfessor.setSelection(lvTurmasProfessor.getAdapter().getCount() - 1);
    }
}
