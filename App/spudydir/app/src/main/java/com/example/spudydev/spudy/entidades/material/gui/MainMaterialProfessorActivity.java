package com.example.spudydev.spudy.entidades.material.gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.material.negocio.MaterialServices;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Main material professor activity.
 */
public class MainMaterialProfessorActivity extends AppCompatActivity {

    //Instanciando Buttons | Editexts | ListView
    private ListView lvMateriaisProfessor;
    private EditText edtAlterarConteudo;
    private EditText edtAlterarTitulo;
    private Button btnExcluir;
    private Button btnAlterar;

    //Resgatando a informação advinda de outra activity
    private String codigoTurma;

    //Criando objeto para execução da caixa de diálogo
    private AlertDialog alertDialog;

    //Instanciando objeto para verificar conexão com a internet
    private VerificaConexao verificaConexao = new VerificaConexao(this);

    //ArrayList com os dicionários dos materiais para inserirUsuarioDAO no SimpleAdapter
    private List<HashMap<String, String>> arrayMateriais = new ArrayList<>();

    //ArrayList com os pushs dos materiais
    private List<String> arrayIdsMateriais = new ArrayList<>();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material_professor);
        //Botão flutuante para o professor adicionar materiais
        FloatingActionButton btnFlutuanteAdicionarMaterial = findViewById(R.id.btnFlutuanteAdicionarMaterial);
        btnFlutuanteAdicionarMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAdicionarMaterialActivity();
            }
        });
        //Resgatando informações da intent
        codigoTurma = getIntent().getStringExtra("codigoTurma");
        //Instanciando o ListView do layout
        lvMateriaisProfessor = findViewById(R.id.lvMateriais);
        //Evento de clique para caso o professor selecionar determinada turma
        lvMateriaisProfessor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String titulo = arrayMateriais.get(position).get("titulo");
                String conteudo = arrayMateriais.get(position).get("link");
                String idMaterial = arrayIdsMateriais.get(position);

                Material material = criaMaterial(titulo, conteudo, idMaterial);

                caixaDialogo(material);
            }
        });
        //Método que recupera os materiais do banco e insere no ListView
        carregarMateriais();
    }
    //Instanciando as Views da caixa de diálogo
    private void instanciandoViews(View mView){
        edtAlterarTitulo = mView.findViewById(R.id.edtAlterarTitulo);
        edtAlterarConteudo = mView.findViewById(R.id.edtAlterarConteudo);
        btnExcluir = mView.findViewById(R.id.btnExcluir);
        btnAlterar = mView.findViewById(R.id.btnAlterar);
    }
    //Método que constrói a caixa de diálogo e executa os eventos de clique dos botões
    private void caixaDialogo(final Material material) {
        //Instanciando a caixa de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMaterialProfessorActivity.this);

        //Inflando a caixa de diálogo para o layout personalizado
        View mView = getLayoutInflater().inflate(R.layout.modelo_caixa_dialogo_materiais, null);

        //Instancinado EditText's e Button's
        instanciandoViews(mView);

        //Podemos colocar em um método
        setandoEditTexts(material);

        //Podemos colocar em um método
        builder.setView(mView);
        alertDialog = builder.create();
        alertDialog.show();

        clickExcluir(material);
        clickAlterar(material);
    }
    //Método que executa o evento de click do botão alterar
    private void clickAlterar(final Material material) {
        //Evento para o botão alterar da caixa de diálogo
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()){
                    if (verificarCampos()){
                        //Variáveis que vão compor o objeto material
                        String titulo = edtAlterarTitulo.getText().toString();
                        String conteudo = edtAlterarConteudo.getText().toString();

                        //Chamando método para alterar o material
                        alterarMaterial(criaMaterial(titulo, conteudo, material.getIdMaterial()));
                    }
                }else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    //Método que executa o evento de click do botão excluir
    private void clickExcluir(final Material material) {
        //Evento para o botão cancelar da caixa de diálogo
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaConexao.estaConectado()){
                    if (verificarCampos()){
                        //Chamando método para alterar o material
                        excluirMaterial(material);
                    }
                }else {
                    Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_conexao_falha));
                }
            }
        });
    }
    //Método que seta os EdiTexts com as informações cadastradas do material
    private void setandoEditTexts(Material material) {
        edtAlterarTitulo.setText(material.getTitulo());
        edtAlterarConteudo.setText(material.getConteudo());
    }
    //Criando objeto material
    private Material criaMaterial(String titulo, String conteudo, String idMaterial){
        Material material = new Material();

        material.setConteudo(conteudo);
        material.setTitulo(titulo);
        material.setIdMaterial(idMaterial);

        return material;
    }
    //Chamando camada de negócio - Atualizando Material
    private void alterarMaterial(Material material) {
        if (MaterialServices.atualizarMaterialServices(material, codigoTurma)){
            //Destruindo a caixa de diálogo
            alertDialog.dismiss();
            //Recarregando os materiais
            carregarMateriais();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Chamando a camada de negócio - Excluindo Material
    private void excluirMaterial(Material material){
        if(MaterialServices.excluirMaterialServices(material, codigoTurma)){
            alertDialog.dismiss();
            carregarMateriais();
        }else{
            Auxiliar.criarToast(getApplicationContext(),getString(R.string.sp_excecao_database));
        }

    }
    //Verificando campos da caixa de diálogo
    private boolean verificarCampos(){
        boolean verificador = true;
        if (edtAlterarTitulo.getText().toString().isEmpty() || edtAlterarTitulo.getText().toString().trim().length() == 0){
            edtAlterarTitulo.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        if (edtAlterarConteudo.getText().toString().isEmpty() || edtAlterarConteudo.getText().toString().trim().length() == 0){
            edtAlterarConteudo.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }
        return verificador;
    }
    //Recuperando os materiais do banco e inserindo no ListView
    private void carregarMateriais(){
        limparArrays();
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                montandoAraryListMaterial(dataSnapshot);
                setListViewMateriais();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método que monta a lista de materiais relacionados a turma
    private void montandoAraryListMaterial(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> referencia = dataSnapshot.child("material").child(codigoTurma).getChildren();
        for (DataSnapshot dataSnapshotChild: referencia){
            HashMap<String,String> dicionarioUsuarios = new HashMap<>();

            Material material = dataSnapshotChild.getValue(Material.class);

            dicionarioUsuarios.put("titulo", material.getTitulo());
            dicionarioUsuarios.put("link", material.getConteudo());
            arrayMateriais.add(dicionarioUsuarios);
            arrayIdsMateriais.add(dataSnapshotChild.getKey());
        }
    }
    //Setando ListView com o auxílio de um SimpleAdapter
    private void setListViewMateriais() {
        SimpleAdapter adapter = new SimpleAdapter(this, arrayMateriais, R.layout.modelo_list_view_materiais_professor,
                new String[]{"titulo", "link"},
                new int[]{R.id.txtTituloMaterial,
                        R.id.txtLinkMaterial});
        lvMateriaisProfessor.setAdapter(adapter);
    }
    //Intent para a tela de adicionar materiais
    private void abrirTelaAdicionarMaterialActivity(){
        Intent intent = new Intent(getApplicationContext(), AdicionarMaterialActivity.class);
        intent.putExtra("codigoTurma",codigoTurma);
        startActivity(intent);
        finish();
    }
    //Limpando Arrays para evitar duplicatas
    private void limparArrays() {
        arrayIdsMateriais.clear();
        arrayMateriais.clear();
    }
}