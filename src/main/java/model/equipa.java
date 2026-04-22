package model;
import java.util.ArrayList;

public class equipa {
    private int numeroEquipa;
    private ArrayList<String> membro;

    public equipa(int numeroEquipa) {
        this.numeroEquipa = numeroEquipa;
        this.membro = new ArrayList<>();
    }


    public ArrayList<String> getMembro() {
        return membro;
    }

    public void adicionarMembro(String nome) {
        this.membro.add(nome);
    }

    public void removerMembro(String nome) {
        this.membro.remove(nome);
    }

    @Override
    public String toString() {
        return "equipa{" +
                "numeroEquipa=" + numeroEquipa +
                '}';
    }
}
