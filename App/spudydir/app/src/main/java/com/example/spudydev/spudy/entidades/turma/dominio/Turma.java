package com.example.spudydev.spudy.entidades.turma.dominio;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Turma.
 */
public class Turma {

    private String nome;

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param string the string
     */
    public void setNome(String string) {
        this.nome = string;
    }

    /**
     * To map turma map.
     *
     * @return the map
     */
//Esta chamada vai ser feita pelo PROFESSOR
    public Map<String, Object> toMapTurma(){
        Map<String, Object> hashMapTurma = new HashMap<>();
        hashMapTurma.put("nomeTurma", getNome());

        return hashMapTurma;
    }
}