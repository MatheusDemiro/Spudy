package com.example.spudydev.spudy.entidades.material.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.material.dominio.Material;
import com.example.spudydev.spudy.entidades.material.negocio.MaterialServices;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.VerificaConexao;

/**
 * The type Adicionar material activity.
 */
public class AdicionarMaterialActivity extends AppCompatActivity {

    private EditText edtTituloMaterial;
    private EditText edtLinkMaterial;

    private VerificaConexao verificaConexao;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_material);

        edtTituloMaterial = findViewById(R.id.edtTituloMaterial);
        edtLinkMaterial = findViewById(R.id.edtLinkMaterial);
        Button btnAddMaterial = findViewById(R.id.btnAddMaterial);

        verificaConexao = new VerificaConexao(this);

        btnAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConexao.estaConectado()) {
                    if (verificarMaterial()) {
                        //variáveis para compor o objeto material e inicializar o método cadastrarMaterialDAO
                        String titulo = edtTituloMaterial.getText().toString();
                        String link = edtLinkMaterial.getText().toString();
                        String codigoDaTurma = getIntent().getStringExtra("codigoTurma");

                        Material material = criarMaterial(link, titulo);

                        //chamando o método estático da camada de negócio
                        cadastrarMaterial(material, codigoDaTurma);
                    }
                }
            }
        });
    }
    //Método que chama a camada de negócio
    private void cadastrarMaterial(Material material, String codigoDaTurma) {
        if (MaterialServices.cadastrarMaterialServices(material, codigoDaTurma)) {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_material_cadastrado_sucesso));
            abrirTelaMainMaterialProfessorActivity();
        }else {
            Auxiliar.criarToast(getApplicationContext(), getString(R.string.sp_excecao_database));
        }
    }
    //Criando o objeto material para ser inserido no banco
    private Material criarMaterial(String conteudo, String titulo){
        Material material = new Material();
        material.setConteudo(conteudo);
        material.setTitulo(titulo);

        return material;
    }
    //Verificando os campos de título e link do material
    private boolean verificarMaterial(){
        boolean verificador = true;

        if (edtTituloMaterial.getText().toString().trim().isEmpty()){
            edtTituloMaterial.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = false;
        }if (edtLinkMaterial.getEditableText().toString().trim().isEmpty()) {
            edtLinkMaterial.setError(getString(R.string.sp_excecao_campo_vazio));
            verificador = true;
        }
        return verificador;
    }
    //Intent para a tela main de materiais do professor
    private void abrirTelaMainMaterialProfessorActivity(){
        Intent intent = new Intent(getApplicationContext(), MainMaterialProfessorActivity.class);
        intent.putExtra("codigoTurma", getIntent().getStringExtra("codigoTurma"));
        startActivity(intent);
        finish();
    }
}
