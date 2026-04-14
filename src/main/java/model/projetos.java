package model;

import java.util.ArrayList;

public class projetos {
    private String nome;
    private ArrayList<tarefas> listaTarefas;
    private equipa equipaProjeto;

    public projetos(String nome){
        this.nome=nome;
        this.listaTarefas = new ArrayList<>();
        this.equipaProjeto = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<tarefas> getListaTarefas() {
        return listaTarefas;
    }

    public void adicionarTarefas(tarefas novaTarefa) {
        this.listaTarefas.add(novaTarefa);
    }

    public equipa getEquipaProjeto() {
        return equipaProjeto;
    }

    public void setEquipaProjeto(equipa equipaProjeto) {
        this.equipaProjeto = equipaProjeto;
    }

    @Override
    public String toString() {
        return "projetos{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
