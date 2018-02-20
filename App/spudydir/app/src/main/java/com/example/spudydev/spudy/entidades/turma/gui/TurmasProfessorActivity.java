package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Turmas professor activity.
 */
public class TurmasProfessorActivity extends AppCompatActivity {

    private ListView lvTurmasProfessor;

    private List<String> arrayCodigosTurmas = new ArrayList<>();
    private List<String> arrayNomesTurmas = new ArrayList<>();

    private String uidProfessor;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turmas_professor);
        getSupportActionBar().hide();

        lvTurmasProfessor = findViewById(R.id.lvTurmasProfessor);

        uidProfessor = getIntent().getStringExtra("uidProfessor");

        //Carregando as turmas do professor
        carregarTurmasProfessor();

        lvTurmasProfessor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codigoTurma = arrayCodigosTurmas.get(position);
                //Chamando a camada de negócios e realizando a inserção no banco
                verificarTurma(codigoTurma);
            }
        });
    }
    //Método que verifica se a turma já está adicionada ou não
    private void verificarTurma(final String idTurma){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot refTurmaAluno = dataSnapshot.child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").child(idTurma);
                if (refTurmaAluno.exists()) {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_turma));
                } else {
                    adicionarTurma(idTurma);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Chamando camada de negócio
    private void adicionarTurma(String idTurma){
        if (TurmaServices.adicionarTurmaServices(idTurma)){
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_turma_adicionada_sucesso));
            abrirTelaMainAlunoActivity();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Resgatando as turmas ministradas pelo
    private void carregarTurmasProfessor(){
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
    private void resgatandoTurmasMinistradas(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("professor").child(uidProfessor).child("turmasMinistradas").getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                arrayCodigosTurmas.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Método que resgata o nome das turmas ministradas pelo professor
    private void resgatandoNomeTurmas(DataSnapshot dataSnapshot){
        for (String codigoTurma: arrayCodigosTurmas){
            String nomeTurma = dataSnapshot.child("turma").child(codigoTurma).child("nomeTurma").getValue(String.class);
            arrayNomesTurmas.add(nomeTurma);
        }
        setListViewTurmas();
    }
    //Setando o LIstView
    private void setListViewTurmas(){
        if (!arrayNomesTurmas.isEmpty()) {
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesTurmas);
            lvTurmasProfessor.setAdapter(itemsAdapter);
            lvTurmasProfessor.setSelection(0);
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_professor_sem_turma));
        }
    }
    //Intent para a tela main do aluno
    private void abrirTelaMainAlunoActivity(){
        Intent intent = new Intent(getApplicationContext(), MainAlunoActivity.class);
        startActivity(intent);
    }
}
