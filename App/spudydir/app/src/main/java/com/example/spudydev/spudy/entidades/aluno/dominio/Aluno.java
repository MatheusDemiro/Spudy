package com.example.spudydev.spudy.entidades.aluno.dominio;

        import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;

public class Aluno {

    private Pessoa pessoa;

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
    //Criar um save padrão
}
