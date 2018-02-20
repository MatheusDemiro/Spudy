package com.example.spudydev.spudy.entidades.turma.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.aluno.gui.MainAlunoActivity;
import com.example.spudydev.spudy.entidades.avaliacao.persistencia.SlopeOne;
import com.example.spudydev.spudy.entidades.turma.negocio.TurmaServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Adicionar turma activity.
 */
public class AdicionarTurmaActivity extends AppCompatActivity {

    private EditText edtPesquisa;
    private ListView lvEntidade;
    private Spinner spnPesquisa;
    private ArrayAdapter<String> itemsAdapter;

    //ArrayList com o nome do professor da pesquisa
    private List<String> arrayNomesProfessores = new ArrayList<>();
    //ArrayList com o(s) UID(s) do professor(es)
    private List<String> arrayUidsProfessores = new ArrayList<>();

    //ArrayList com os códigos da turma
    private List<String> arrayIdsTurmas = new ArrayList<>();
    //ArrayList com os nomes das turmas recomendadas
    private List<String> arrayNomesTurmas = new ArrayList<>();

    //ArrayList com os códigos das turmas recomendadas
    private List<String> arrayIdsTurmasRecomendadas = new ArrayList<>();
    //ArrayList com os nomes das turmas recomendadas
    private List<String> arrayNomesTurmasRecomendadas = new ArrayList<>();

    //Private com a string selecionada do spinner
    private String pesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_adicionar_turma);

        edtPesquisa = findViewById(R.id.edtPesquisa);
        lvEntidade = findViewById(R.id.lvTurmas);
        spnPesquisa = findViewById(R.id.spnPesquisa);

        //Evento para quando os elementos do Spinner forem selecionados
        spnPesquisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Salvando o método de pesquisa (turma ou professor) em uma variável
                pesquisa = spnPesquisa.getItemAtPosition(position).toString().toLowerCase();
                if (pesquisa.equals("professor")){
                    resgantandoProfessores();
                }if (pesquisa.equals("turma")){
                    resgatandoTurmas();
                } else if (pesquisa.equals("turmas recomendadas")){
                    recomendacao();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //Evento para quando os elementos do ListView forem selecionados
        lvEntidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pesquisa.equals("professor")) {
                    String nomeProfessor = lvEntidade.getItemAtPosition(position).toString();
                    selecionarProfessor(nomeProfessor);
                } if (pesquisa.equals("turma")){
                    String nomeTurma = lvEntidade.getItemAtPosition(position).toString();
                    String idTurma = selecionarTurma(nomeTurma);
                    Log.d("ID TURMA", idTurma);
                    //Método que verifica a turma e chamada a camada de negócio para inserirUsuarioDAO turma no banco
                    verificarTurma(idTurma);
                }else if (pesquisa.equals("turmas recomendadas")){
                    String nomeTurma = lvEntidade.getItemAtPosition(position).toString();
                    String idTurma = selecionarTurmaRecomendada(nomeTurma);
                    //Método que verifica a turma e chamada a camada de negócio para inserirUsuarioDAO turma no banco
                    verificarTurma(idTurma);
                }
            }
        });
        //Realizando pesquisa baseado no ArrayAdapter
        edtPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itemsAdapter != null){
                    itemsAdapter.getFilter().filter(edtPesquisa.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void recomendacao() {
        //Realiazando a recomendação
        limparArrayList();
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SlopeOne slopeOne = new SlopeOne();

                slopeOne.getAlunos(dataSnapshot);
                slopeOne.getTurma(dataSnapshot);
                slopeOne.criarMatrizDados(dataSnapshot);

                montaPreferencias(slopeOne.recomendar());
                resgatandoNomesTurmasRecomendadas(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Retornando as possíveis preferências do usuário
    private void montaPreferencias(Map<String,Double> predicoes) {
        //Caso seja menor que 5 serão exibidas todas as turmas da predição.
        if (predicoes.size() < 5) {
            for (String k: predicoes.keySet()) {
                arrayIdsTurmasRecomendadas.addAll(predicoes.keySet());
                return;
            }
        }
        //Caso contrário - será montado um top 5.
        final int contadorTop5 = 5;

        for (int i = 0; i < contadorTop5; i++) {
            double contAux = 0.0;
            String auxUidTurma = null;
            for (String k : predicoes.keySet()) {
                if (predicoes.get(k) > contAux) {
                    contAux = predicoes.get(k);
                    auxUidTurma = k;
                }
            }
            arrayIdsTurmasRecomendadas.add(auxUidTurma);
            predicoes.remove(auxUidTurma);
        }
    }
    //Resgatando os nomes das turmas recomendadas
    private void resgatandoNomesTurmasRecomendadas(DataSnapshot dataSnapshot){
        for (String uidTurma: arrayIdsTurmasRecomendadas){
            String nomeTurma = dataSnapshot.child("turma").child(uidTurma).child("nomeTurma").getValue(String.class);
            arrayNomesTurmasRecomendadas.add(nomeTurma);
        }
        setListViewPreferencias();
    }
    //Método que resgata as turmas cadastradas no banco
    private void resgatandoTurmas(){
        limparArrayList();
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resgatandoIdsTurmas(dataSnapshot.child("turma").getChildren());
                resgatandoNomeTurmas(dataSnapshot);
                setListViewTurmas();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Resgatando os ids das turmas cadastradas no banco
    private void resgatandoIdsTurmas(Iterable<DataSnapshot> dataSnapshot) {
        for (DataSnapshot dataSnapshotChild: dataSnapshot){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                //Montando o ArrayList com os ids das turmas
                arrayIdsTurmas.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Resgatando o nome das turmas que estão no ArrayList "arrayIdsTurmas"
    private void resgatandoNomeTurmas(DataSnapshot dataSnapshot){
        for (String idTurma: arrayIdsTurmas){
            String nomeTurma = dataSnapshot.child("turma").child(idTurma).child("nomeTurma").getValue(String.class);
            arrayNomesTurmas.add(nomeTurma.toUpperCase());
        }
    }
    //Método que preenche a lista de uid dos professores cadastrados no sistema
    private void resgantandoProfessores() {
        limparArrayList();

        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resgatandoUidProfessores(dataSnapshot);
                resgatandoNomesProfessores(dataSnapshot);
                setListViewProfessores();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Método que recupera os UIDs dos professores cadastrados no banco
    private void resgatandoUidProfessores(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child(pesquisa).getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                arrayUidsProfessores.add(dataSnapshotChild.getKey());
            }
        }
    }
    //Método que preenche o ArrayList com os nomes do professores
    private void resgatandoNomesProfessores(DataSnapshot dataSnapshot){
        for (String i: arrayUidsProfessores){
            String nomeProfessor = dataSnapshot.child("pessoa").child(i).child("nome").getValue(String.class);
            arrayNomesProfessores.add(nomeProfessor);
        }
    }
    //Preenchendo ListView com as turmas recomendadas
    private void setListViewPreferencias() {
        if (arrayNomesTurmasRecomendadas.isEmpty()) {
            String[] aviso = {"Sem recomendações."};
            itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, aviso);
            lvEntidade.setAdapter(itemsAdapter);
        } else {
            itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesTurmasRecomendadas);
            lvEntidade.setAdapter(itemsAdapter);
        }
    }
    //Preenchendo o ListView com os nomes das turmas
    private void setListViewTurmas(){
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesTurmas);
        lvEntidade.setAdapter(itemsAdapter);
        lvEntidade.setSelection(0);
    }
    //Preenchendo o ListView com os nomes dos professores
    private void setListViewProfessores(){
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNomesProfessores);
        lvEntidade.setAdapter(itemsAdapter);
        lvEntidade.setSelection(0);

        Auxiliar.criarToast(getApplicationContext(),getString(R.string.sp_selecionar_professor));
    }
    //Método que captura a turma recomendada selcionada pelo aluno e chama a camada de negócio
    private String selecionarTurmaRecomendada(String nomeTurma){
        int contador = 0;
        for (String nome: arrayNomesTurmasRecomendadas){
            if (nome.equals(nomeTurma)){
                return arrayIdsTurmasRecomendadas.get(contador);
            }
            contador++;
        }
        return null;
    }
    //Método que captura a turma selecionada pelo aluno e chama a camada de negócio
    private String selecionarTurma(String nomeTurma){
        int contador = 0;
        for (String nome: arrayNomesTurmas){
            if (nome.equals(nomeTurma)){
                return arrayIdsTurmas.get(contador);
            }
            contador++;
        }
        return null;
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
    //Método que captura o professor selecionado pelo aluno e direciona para a activity com as turmas do professor
    private void selecionarProfessor(String nomeProfessor){
        int contador = 0;
        for (String nome: arrayNomesProfessores){
            if (nome.equals(nomeProfessor)){
                abrirTelaTurmasProfessorActivity(arrayUidsProfessores.get(contador));
            }
            contador++;
        }
    }
    //Método para limpar as listas e evitar que o ListView fique com nomes duplicados
    private void limparArrayList() {
        arrayIdsTurmas.clear();
        arrayNomesTurmas.clear();
        arrayNomesProfessores.clear();
        arrayUidsProfessores.clear();
        arrayIdsTurmasRecomendadas.clear();
        arrayNomesTurmasRecomendadas.clear();
    }
    //Intent para a tela com as turmas do professor selecionado no ListView
    private void abrirTelaTurmasProfessorActivity(String uidProfessor) {
        Intent intent = new Intent(AdicionarTurmaActivity.this, TurmasProfessorActivity.class);
        intent.putExtra("uidProfessor", uidProfessor);
        startActivity(intent);
    }//Intent para a tela onde serão apresentadas as turmas que o usuário é cadastrado
    private void abrirTelaMainAlunoActivity(){
        Intent intent = new Intent(AdicionarTurmaActivity.this, MainAlunoActivity.class);
        startActivity(intent);
    }
}