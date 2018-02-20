package com.example.spudydev.spudy.entidades.usuario.persistencia;

import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;

/**
 * The type Usuario dao.
 */
public final class UsuarioDAO {

    private UsuarioDAO(){
        //Construtor vazio
    }

    /**
     * Inserir boolean.
     *
     * @param usuario the usuario
     * @return the boolean
     */
//Cadastrar usuario
    public static boolean inserirUsuarioDAO(Usuario usuario){
        boolean verificador;

        try {
            //Obtendo uid do usuário atual
            String uidUsuario = AcessoFirebase.getUidUsuario();
            //Salvando Pessoa e usuario
            AcessoFirebase.getFirebase().child("usuario").child(uidUsuario).setValue(usuario);
            verificador = true;
        } catch (DatabaseException e){
            verificador = false;
        }
        return verificador;
    }

    /**
     * Alterar email boolean.
     *
     * @param novoEmail the novo email
     * @return the boolean
     */
    public static boolean alterarEmailDAO(String novoEmail){
        boolean verificador;

        try {
            AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().updateEmail(novoEmail);
            AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("email").setValue(novoEmail);
            verificador = true;
        }catch (DatabaseException e) {
            verificador = false;
        }
        return verificador;
    }

    /**
     * Alterar instituicao boolean.
     *
     * @param novaInstituicao the nova instituicao
     * @return the boolean
     */
    public static boolean alterarInstituicaoDAO(String novaInstituicao){
        boolean verificador;

        try {
            AcessoFirebase.getFirebase().child("usuario").child(AcessoFirebase.getUidUsuario()).child("instituicao").setValue(novaInstituicao);
            verificador = true;
        }catch (DatabaseException e) {
            verificador = false;
        }
        return verificador;
    }

    /**
     * Alterar senha boolean.
     *
     * @param novaSenha the nova senha
     * @return the boolean
     */
    public static boolean alterarSenhaDAO(String novaSenha){
        boolean verificador = true;

        try {
            FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
            if (user != null) {
                user.updatePassword(novaSenha);
                verificador = true;
            }
        }catch (DatabaseException e) {
            verificador = false;
        }
        return verificador;
    }
}
