package model;

public class tarefas {
    private String nome;
    private int estado;
    private String membro;

    public tarefas(String nome){
        this.nome = nome;
        this.estado = 0;
        this.membro = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        if (estado >= 0 && estado <=2)
            this.estado = estado;
        else
            System.out.println("Estado inválido");
    }

    public String getMembro() {
        return membro;
    }

    public void setMembro(String membro) {
        this.membro = membro;
    }

    @Override
    public String toString() {
        if (membro != null && !membro.trim().isEmpty()) {
            return nome + " [" + membro + "]";
        }
        return nome;
    }
}
