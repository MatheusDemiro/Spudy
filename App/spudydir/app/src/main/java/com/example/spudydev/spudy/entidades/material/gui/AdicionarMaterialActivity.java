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
import com.example.spudydev.spudy.entidades.material.persistencia.MaterialDAO;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;

public class AdicionarMaterialActivity extends AppCompatActivity {

    private EditText edtTituloMaterial;
    private EditText edtLinkMaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_material);

        edtTituloMaterial = findViewById(R.id.edtTituloMaterial);
        edtLinkMaterial = findViewById(R.id.edtLinkMaterial);
        Button btnAddMaterial = findViewById(R.id.btnAddMaterial);

        btnAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarMaterial()){
                    //variáveis para compor o objeto material e inicializar o método adicionarMaterial
                    String titulo = edtTituloMaterial.getText().toString();
                    String link = edtLinkMaterial.getText().toString();
                    String codigoDaTurma = getIntent().getStringExtra("codigoTurma");

                    //chamando o método estático "adicionarMaterial" para salvar no banco
                    MaterialServices materialServices = new MaterialServices();
                    materialServices.cadastrarMaterial(link, titulo, codigoDaTurma);
                    Auxiliar.criarToast(getApplicationContext(),"Material adicionado com sucesso.");
                    abrirTelaMainProfessorAddMaterial();
                }
            }
        });
    }
    //Verificando os campos de título e link do material
    public boolean verificarMaterial(){
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
    public void abrirTelaMainProfessorAddMaterial(){
        Intent intent = new Intent(getApplicationContext(),MainMaterialProfessorActivity.class);
        intent.putExtra("codigoTurma", getIntent().getStringExtra("codigoTurma"));
        startActivity(intent);
        finish();
    }
}
