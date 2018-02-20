package com.example.spudydev.spudy.entidades.avaliacao.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.avaliacao.dominio.Avaliacao;
import com.example.spudydev.spudy.entidades.avaliacao.negocio.AvaliacaoServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * The type Avaliacao activity.
 */
public class AvaliacaoActivity extends AppCompatActivity {

    private float estrelas;
    private RatingBar rbAvaliacaoTurma;
    private TextView tvNotaAvaliacao;
    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        rbAvaliacaoTurma = findViewById(R.id.rbAvaliacaoTurma);
        tvNotaAvaliacao = findViewById(R.id.tvNotaAvaliacaoTurma);
        Button btnConfirmarAvaliacao = findViewById(R.id.btnConfirmarAvaliacaoTurma);

        verificaConexao = new VerificaConexao(this);

        //Resgatando avaliação
        carregarAvaliacao();

        //Evento de click para o RatingBar
        rbAvaliacaoTurma.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String notaAvaliacao = "Nota: " + rating;
                estrelas = rating;
                tvNotaAvaliacao.setText(notaAvaliacao);
            }
        });

        btnConfirmarAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()) {
                    if (verificarAvaliacao()) {
                        inserirAvaliacao();
                    } else {
                        Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_avaliacao));
                    }
                } else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    //Método que resgata a avaliacao da turma feita pelo aluno
    private void carregarAvaliacao(){
        AcessoFirebase.getFirebase().child("avaliacao").child(AcessoFirebase.getUidUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotChild = dataSnapshot.child(getIntent().getStringExtra("codigoTurma"));
                if (dataSnapshotChild.exists()) {
                    Float avaliacao = dataSnapshotChild.child("nota").getValue(Float.class);
                    rbAvaliacaoTurma.setRating(avaliacao);
                } else {
                    rbAvaliacaoTurma.setRating(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Chamando método da camada de negócio
    private void inserirAvaliacao() {
        Avaliacao avaliacao = new Avaliacao();
        //Utilizando apenas a nota da avaliação para inserirUsuarioDAO no banco
        avaliacao.setNota(estrelas);
        //Chamando método da camada de negócio
        if (AvaliacaoServices.inserirAvaliacaoTurma(avaliacao, getIntent().getStringExtra("codigoTurma"))){
            Auxiliar.criarToast(getApplicationContext(),getString(R.string.sp_avaliacao_sucesso));
        } else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Verificando se o número de estrelas é igual a zero
    private boolean verificarAvaliacao(){
        boolean verificador = true;
        if (rbAvaliacaoTurma.getRating() == 0.0){
            verificador = false;
        }
        return verificador;
    }
}