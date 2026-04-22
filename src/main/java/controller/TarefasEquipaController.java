package controller;
import view.TarefasEquipas;
import model.projetos;
import model.tarefas;
import javax.swing.*;

public class TarefasEquipaController {
    private TarefasEquipas view;
    private projetos projetoAtual;
    private DefaultListModel<tarefas> modeloFazer;
    private DefaultListModel<tarefas> modeloProgresso;
    private DefaultListModel<tarefas> modeloConcluido;
    private DefaultListModel<String> modeloMembros;

    public TarefasEquipaController(TarefasEquipas view, projetos projetoAtual) {
        this.view = view;
        this.projetoAtual = projetoAtual;

        this.modeloFazer = new DefaultListModel<>();
        this.modeloProgresso = new DefaultListModel<>();
        this.modeloConcluido = new DefaultListModel<>();this.modeloMembros = new DefaultListModel<>();
        this.view.getList1().setModel(modeloMembros);

        if (this.projetoAtual.getEquipaProjeto() == null) {
            this.projetoAtual.setEquipaProjeto(new model.equipa(1));
        }


        atualizarListas();

        this.view.getListaFazer().setModel(modeloFazer);
        this.view.getListaProgresso().setModel(modeloProgresso);
        this.view.getListaConcluido().setModel(modeloConcluido);

        this.view.getAdicionarButton().addActionListener(e -> {adicionarTarefa();});
        this.view.getEliminarButton().addActionListener(e -> {eliminarTarefa();});
        this.view.getEditarButton().addActionListener(e -> {editarTarefa();});
        this.view.getRemoverButton().addActionListener(e -> removerMembro());
        this.view.getAdicionarMembroButton().addActionListener(e -> adicionarMembro());
        this.view.getAtribuirButton().addActionListener(e -> {atribuirTarefa();});

    }
    public void adicionarTarefa() {
        String nome = JOptionPane.showInputDialog(view.getContentPane(), "Nome da nova tarefa:");

        if (nome != null && !nome.trim().isEmpty()) {
            String nomeLimpo = nome.trim();

            boolean nomeRepetido = false;
            for (tarefas t : projetoAtual.getListaTarefas()) {
                if (t.getNome().equalsIgnoreCase(nomeLimpo)) {
                    nomeRepetido = true;
                    break;
                }
            }

            if (nomeRepetido) {
                JOptionPane.showMessageDialog(view.getContentPane(), "Já existe uma tarefa com o mesmo nome");
            } else {
                tarefas novaTarefa = new tarefas(nomeLimpo);

                projetoAtual.adicionarTarefas(novaTarefa);

                atualizarListas();
            }

        } else if (nome != null) {
            JOptionPane.showMessageDialog(view.getContentPane(), "O nome da tarefa não pode estar vazio");}


    }

    public void eliminarTarefa() {
        tarefas tarefaSelecionada = null;

        if (view.getListaFazer().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaFazer().getSelectedValue();
        } else if (view.getListaProgresso().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaProgresso().getSelectedValue();
        } else if (view.getListaConcluido().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaConcluido().getSelectedValue();
        }

        if (tarefaSelecionada != null) {
            int resposta = JOptionPane.showConfirmDialog(view.getContentPane(), "Tem a certeza que deseja eliminar a tarefa?", "Confirmar eliminar", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                projetoAtual.getListaTarefas().remove(tarefaSelecionada);

                view.getListaFazer().clearSelection();
                view.getListaProgresso().clearSelection();
                view.getListaConcluido().clearSelection();

                atualizarListas();
            }

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(), "Selecione uma tarefa numa das listas para eliminar");
        }
    }

    public void editarTarefa() {
        tarefas tarefaSelecionada = null;

        if (view.getListaFazer().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaFazer().getSelectedValue();
        } else if (view.getListaProgresso().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaProgresso().getSelectedValue();
        } else if (view.getListaConcluido().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaConcluido().getSelectedValue();
        }

        if (tarefaSelecionada != null) {


            int resposta = JOptionPane.showConfirmDialog(view.getContentPane(),"Deseja alterar o nome?", "Mudar nome", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION){
                String novoNome = JOptionPane.showInputDialog(view.getContentPane(),"Novo Nome:");
                if (novoNome == null){
                    return;
                } else if (novoNome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view.getContentPane(),"Nome em branco");
                } else {
                    tarefaSelecionada.setNome(novoNome.trim());
                }
            }

            String[] opcoes = {"A Fazer", "Em Progresso", "Concluído"};
            int estadoEscolhido = JOptionPane.showOptionDialog(view.getContentPane(),
                    "Em que estado está a tarefa?", "Estado da Tarefa", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opcoes, opcoes[tarefaSelecionada.getEstado()]);

            if (estadoEscolhido >= 0 && estadoEscolhido <= 2) {
                tarefaSelecionada.setEstado(estadoEscolhido);
            }
            atualizarListas();

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(), "Selecione uma tarefa numa das listas para editar");
        }
    }
    public void atualizarListas() {
        modeloFazer.clear();
        modeloProgresso.clear();
        modeloConcluido.clear();
        modeloMembros.clear();

        for (tarefas t : projetoAtual.getListaTarefas()) {
            if (t.getEstado() == 0) {
                modeloFazer.addElement(t);
            } else if (t.getEstado() == 1) {
                modeloProgresso.addElement(t);
            } else if (t.getEstado() == 2) {
                modeloConcluido.addElement(t);
            }
        }

        for (String nomeMembro : projetoAtual.getEquipaProjeto().getMembro()) {
            modeloMembros.addElement(nomeMembro);
        }
    }

    public void adicionarMembro() {
        String nome = JOptionPane.showInputDialog(view.getContentPane(), "Nome do novo membro:");

        if (nome != null && !nome.trim().isEmpty()) {
            String nomeLimpo = nome.trim();

            boolean jaExiste = projetoAtual.getEquipaProjeto().getMembro().contains(nomeLimpo);

            if (jaExiste) {
                JOptionPane.showMessageDialog(view.getContentPane(), "Ja existe um membro com o mesmo nome");
            } else {
                projetoAtual.getEquipaProjeto().adicionarMembro(nomeLimpo);

                atualizarListas();
            }
        } else if (nome != null) {
            JOptionPane.showMessageDialog(view.getContentPane(), "O nome do membro não pode estar vazio!");
        }
    }
    public void removerMembro() {
        String membroSelecionado = (String) view.getList1().getSelectedValue();

        if (membroSelecionado != null) {

            int resposta = JOptionPane.showConfirmDialog(view.getContentPane(), "Tem a certeza que deseja remover " + membroSelecionado + " da equipa?", "Confirmar remover", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {

                projetoAtual.getEquipaProjeto().removerMembro(membroSelecionado);

                for (tarefas t : projetoAtual.getListaTarefas()) {
                    if (membroSelecionado.equals(t.getMembro())) {
                        t.setMembro(null);
                    }
                }

                view.getList1().clearSelection();
                atualizarListas();
            }

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(), "Selecione um membro na lista para remover");
        }
    }
    public void atribuirTarefa() {
        String membroSelecionado = (String) view.getList1().getSelectedValue();

        if (membroSelecionado != null) {

            int numeroDeTarefas = projetoAtual.getListaTarefas().size();
            if (numeroDeTarefas == 0) {
                JOptionPane.showMessageDialog(view.getContentPane(), "Não existem tarefas neste projeto para atribuir");
                return;
            }

            tarefas[] arrayTarefas = new tarefas[numeroDeTarefas];
            projetoAtual.getListaTarefas().toArray(arrayTarefas);

            tarefas tarefaEscolhida = (tarefas) JOptionPane.showInputDialog(
                    view.getContentPane(),
                    "Escolha a tarefa a atribuir a " + membroSelecionado ,
                    "Atribuir Tarefa",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    arrayTarefas,
                    arrayTarefas[0]
            );

            if (tarefaEscolhida != null) {

                tarefaEscolhida.setMembro(membroSelecionado);

                JOptionPane.showMessageDialog(view.getContentPane(), "Tarefa '" + tarefaEscolhida.getNome() + "' atribuída a " + membroSelecionado);

                atualizarListas();
            }

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(), "Selecione um membro na lista primeiro");
        }
    }



    }
