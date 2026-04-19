package view;

import javax.swing.*;

public class Projetos {
    private JPanel contentPane;
    private JList list1;
    private JButton carregarButton;
    private JButton guardarButton;
    private JButton criarButton;
    private JButton eliminarButton;
    private JButton gestaoButton;
    private JButton editarButton;

    public JButton getCriarProjeto() {
        return criarButton;
    }

    public JList getList1() {
        return list1;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JButton getEliminarButton() {
        return eliminarButton;
    }

    public JButton getGestaoButton() {
        return gestaoButton;
    }

    public JButton getCarregarButton() {
        return carregarButton;
    }

    public JButton getEditarButton() {
        return editarButton;
    }
};
