package controller;
import view.Projetos;
import model.projetos;

import javax.swing.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class projetoController {
    private Projetos view;
    private ArrayList<projetos> listaProjetos;
    private DefaultListModel<String> listaModelo;

    public projetoController(Projetos view){
        this.view = view;
        this.listaProjetos = new ArrayList<>();
        this.listaModelo = new DefaultListModel<>();

        this.view.getList1().setModel(listaModelo);

        this.view.getCriarProjeto().addActionListener(e -> criarProjeto());
        this.view.getEliminarButton().addActionListener(e -> eliminarProjeto());
        this.view.getGestaoButton().addActionListener(e -> gestaoProjeto());
        this.view.getEditarButton().addActionListener(e -> editarProjeto());
        this.view.getGuardarButton().addActionListener(e -> guardarDados());
        this.view.getCarregarButton().addActionListener(e -> carregarDados());
        this.view.getList1().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // (2 cliques)
                if (e.getClickCount() == 2) {
                    gestaoProjeto();
                }
            }
        });
    }

    public void criarProjeto(){
        String nome = JOptionPane.showInputDialog(view.getContentPane(), "Nome do novo projeto:");

        if (nome != null && !nome.trim().isEmpty()) {

            boolean nomeRepetido = false;

            for(projetos x : listaProjetos){
                if (x.getNome().equalsIgnoreCase(nome)) {
                    nomeRepetido = true;
                    break;
                }
            }

            if(nomeRepetido){
                JOptionPane.showMessageDialog(view.getContentPane(),"Já existe um projeto com o mesmo nome");
            }
            else{
                projetos novoProjeto = new projetos(nome);
                listaProjetos.add(novoProjeto);
                listaModelo.clear();
                for (projetos p : listaProjetos) {
                    listaModelo.addElement(p.getNome());
                }
            }
        }

        else {
            JOptionPane.showMessageDialog(view.getContentPane(),"Nome vazio");
        }

        }
    public void eliminarProjeto(){
        int index = view.getList1().getSelectedIndex();

        if(index != -1){
            int confirmar = JOptionPane.showConfirmDialog(view.getContentPane(),"Deseja eliminar o projeto?","Confirmar eliminar",JOptionPane.YES_NO_OPTION);

            if(confirmar == JOptionPane.YES_OPTION){
                listaProjetos.remove(index);
                listaModelo.remove(index);
            }
        }
        else {
            JOptionPane.showMessageDialog(view.getContentPane(),"Nenhum projeto selecionado");
        }
    }

    public void editarProjeto(){
        int index = view.getList1().getSelectedIndex();

        if (index != -1){projetos projetoAtual = listaProjetos.get(index);
            String nomeAntigo = projetoAtual.getNome();

            String novoNome = JOptionPane.showInputDialog(view.getContentPane(), "Editar nome do projeto:", nomeAntigo);

            if (novoNome != null && !novoNome.trim().isEmpty()) {
                String nomeLimpo = novoNome.trim();

                if (nomeLimpo.equalsIgnoreCase(nomeAntigo)) {
                    return;
                }

                boolean nomeRepetido = false;
                for (projetos x : listaProjetos) {
                    if (x.getNome().equalsIgnoreCase(nomeLimpo)) {
                        nomeRepetido = true;
                        break;
                    }
                }

                if (nomeRepetido) {
                    JOptionPane.showMessageDialog(view.getContentPane(), "Já existe um projeto com o mesmo nome)");
                } else {
                    projetoAtual.setNome(nomeLimpo);
                    listaModelo.set(index, nomeLimpo);
                }
            } else if (novoNome != null) {
                JOptionPane.showMessageDialog(view.getContentPane(), "O nome não pode estar vazio");
            }
        } else {
            JOptionPane.showMessageDialog(view.getContentPane(), "Por favor, selecione um projeto na lista para editar.");
        }

        }
    public void gestaoProjeto() {
        int index = view.getList1().getSelectedIndex();

        if (index != -1) {
            projetos projetoSelecionado = listaProjetos.get(index);

            JFrame frameGestao = new JFrame("Gestão do Projeto: " + projetoSelecionado.getNome());
            frameGestao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameGestao.setSize(700, 700);

            view.TarefasEquipas janelaTarefas = new view.TarefasEquipas();
            frameGestao.setContentPane(janelaTarefas.getContentPane());
            TarefasEquipaController controllerTarefas = new TarefasEquipaController(janelaTarefas, projetoSelecionado);

            frameGestao.setLocationRelativeTo(null);
            frameGestao.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(), "Selecione um projeto na lista para gerir");
        }
    }
    public void guardarDados() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Dados do Kanban");

        int escolha = fileChooser.showSaveDialog(view.getContentPane());

        if (escolha == JFileChooser.APPROVE_OPTION) {
            java.io.File ficheiroEscolhido = fileChooser.getSelectedFile();
            String caminho = ficheiroEscolhido.getAbsolutePath();

            if (!caminho.endsWith(".txt")) {
                caminho += ".txt";
            }

            try {
                java.util.ArrayList<String> linhas = new java.util.ArrayList<>();
                for (projetos p : listaProjetos) {
                    linhas.add("P;" + p.getNome());
                    if (p.getEquipaProjeto() != null) {
                        for (String membro : p.getEquipaProjeto().getMembro()) {
                            linhas.add("E;" + membro);
                        }
                    }
                    for (model.tarefas t : p.getListaTarefas()) {
                        linhas.add("T;" + t.getNome() + ";" + t.getEstado() + ";" + t.getMembro());
                    }
                }

                String[] arrayDados = linhas.toArray(new String[0]);

                trabalho2.IODataClass io = new trabalho2.IODataClass();
                io.writeData(caminho, arrayDados);

                JOptionPane.showMessageDialog(view.getContentPane(), "Dados guardados com sucesso");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao guardar " + ex.getMessage());
            }
        }
    }

    public void carregarDados() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar ficheiro Kanban (.txt)");

        int escolha = fileChooser.showOpenDialog(view.getContentPane());

        if (escolha == JFileChooser.APPROVE_OPTION) {
            java.io.File ficheiroEscolhido = fileChooser.getSelectedFile();
            String caminho = ficheiroEscolhido.getAbsolutePath();

            try {
                trabalho2.IODataClass io = new trabalho2.IODataClass();
                String[] linhasLidas = io.loadData(caminho);

                listaProjetos.clear();
                projetos projetoAtual = null;

                for (String linha : linhasLidas) {
                    String[] partes = linha.split(";");

                    if (partes[0].equals("P")) {
                        projetoAtual = new projetos(partes[1]);
                        projetoAtual.setEquipaProjeto(new model.equipa(1));
                        listaProjetos.add(projetoAtual);
                    } else if (partes[0].equals("E") && projetoAtual != null) {
                        projetoAtual.getEquipaProjeto().adicionarMembro(partes[1]);
                    } else if (partes[0].equals("T") && projetoAtual != null) {
                        model.tarefas novaTarefa = new model.tarefas(partes[1]);
                        novaTarefa.setEstado(Integer.parseInt(partes[2]));
                        if (!partes[3].equals("null")) {
                            novaTarefa.setMembro(partes[3]);
                        }
                        projetoAtual.adicionarTarefas(novaTarefa);
                    }
                }

                listaModelo.clear();
                for (projetos p : listaProjetos) {
                    listaModelo.addElement(p.getNome());
                }

                JOptionPane.showMessageDialog(view.getContentPane(), "Dados carregados com sucesso");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao carregar o ficheiro");
            }
        }
    }
    }



