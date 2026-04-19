package main;

import view.Projetos;
import controller.projetoController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Gestor Kanban");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);


        Projetos janelaProjetos = new Projetos();
        frame.setContentPane(janelaProjetos.getContentPane());
        projetoController controller = new projetoController(janelaProjetos);

        frame.setVisible(true);
    }
}