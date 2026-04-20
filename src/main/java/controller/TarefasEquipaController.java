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


        // Atualizar as listas todas ao abrir
        atualizarListas();

        this.view.getListaFazer().setModel(modeloFazer);
        this.view.getListaProgresso().setModel(modeloProgresso);
        this.view.getListaConcluido().setModel(modeloConcluido);

        System.out.println("A gerir as tarefas do projeto: " + projetoAtual.getNome());
        this.view.getAdicionarButton().addActionListener(e -> {adicionarTarefa();});
        this.view.getEliminarButton().addActionListener(e -> {eliminarTarefa();});
        this.view.getEditarButton().addActionListener(e -> {editarTarefa();});
        this.view.getRemoverButton().addActionListener(e -> removerMembro());
        this.view.getAdicionarMembroButton().addActionListener(e -> adicionarMembro());
        this.view.getAtribuirButton().addActionListener(e -> {atribuirTarefa();});

    }
    public void adicionarTarefa() {
        // 1. Pede o nome da nova tarefa
        String nome = JOptionPane.showInputDialog(view.getContentPane(), "Nome da nova tarefa:");

        // 2. Verifica se o utilizador não cancelou e se escreveu algo
        if (nome != null && !nome.trim().isEmpty()) {
            String nomeLimpo = nome.trim();

            // 3. Verifica se já existe uma tarefa com este nome no projeto
            boolean nomeRepetido = false;
            for (tarefas t : projetoAtual.getListaTarefas()) {
                if (t.getNome().equalsIgnoreCase(nomeLimpo)) {
                    nomeRepetido = true;
                    break;
                }
            }

            if (nomeRepetido) {
                JOptionPane.showMessageDialog(view.getContentPane(),
                        "Já existe uma tarefa com o nome '" + nomeLimpo + "' neste projeto.");
            } else {
                tarefas novaTarefa = new tarefas(nomeLimpo);

                // 5. Adiciona aos dados do projeto
                projetoAtual.adicionarTarefas(novaTarefa);

                atualizarListas();
            }

        } else if (nome != null) {
            // Se clicou OK mas deixou vazio
            JOptionPane.showMessageDialog(view.getContentPane(),
                    "O nome da tarefa não pode estar vazio!)");}
    }public void eliminarTarefa() {
        tarefas tarefaSelecionada = null;

        if (view.getListaFazer().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaFazer().getSelectedValue();
        } else if (view.getListaProgresso().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaProgresso().getSelectedValue();
        } else if (view.getListaConcluido().getSelectedValue() != null) {
            tarefaSelecionada = (tarefas) view.getListaConcluido().getSelectedValue();
        }

        if (tarefaSelecionada != null) {
            int resposta = JOptionPane.showConfirmDialog(
                    view.getContentPane(),
                    "Tem a certeza que deseja eliminar a tarefa '" + tarefaSelecionada.getNome() + "'?",
                    "Confirmar Eliminação",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                // Remove a tarefa dos dados do projeto
                projetoAtual.getListaTarefas().remove(tarefaSelecionada);

                // Limpa seleção
                view.getListaFazer().clearSelection();
                view.getListaProgresso().clearSelection();
                view.getListaConcluido().clearSelection();

                atualizarListas();
            }

        } else {
            JOptionPane.showMessageDialog(
                    view.getContentPane(),
                    "Por favor, selecione uma tarefa numa das listas para eliminar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
        public void editarTarefa() {
            // 1. Descobrir qual das 3 listas tem uma tarefa selecionada
            tarefas tarefaSelecionada = null;

            if (view.getListaFazer().getSelectedValue() != null) {
                tarefaSelecionada = (tarefas) view.getListaFazer().getSelectedValue();
            } else if (view.getListaProgresso().getSelectedValue() != null) {
                tarefaSelecionada = (tarefas) view.getListaProgresso().getSelectedValue();
            } else if (view.getListaConcluido().getSelectedValue() != null) {
                tarefaSelecionada = (tarefas) view.getListaConcluido().getSelectedValue();
            }

            // 2. Se encontrou a tarefa
            if (tarefaSelecionada != null) {

                // --- PARTE 1: EDITAR O NOME ---
                String nomeAntigo = tarefaSelecionada.getNome();
                String novoNome = JOptionPane.showInputDialog(view.getContentPane(),
                        "Editar nome da tarefa:",
                        nomeAntigo);

                // Verifica se não cancelou o nome e não deixou vazio
                if (novoNome != null && !novoNome.trim().isEmpty()) {
                    tarefaSelecionada.setNome(novoNome.trim()); // Guarda o novo nome

                    // --- PARTE 2: EDITAR O ESTADO (MOVER) ---
                    String[] opcoes = {"A Fazer", "Em Progresso", "Concluído"};
                    int estadoEscolhido = JOptionPane.showOptionDialog(view.getContentPane(),
                            "Em que estado está a tarefa '" + novoNome.trim() + "' agora?",
                            "Estado da Tarefa",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opcoes,
                            opcoes[tarefaSelecionada.getEstado()]);

                    if (estadoEscolhido >= 0 && estadoEscolhido <= 2) {
                        tarefaSelecionada.setEstado(estadoEscolhido);
                    }
                    atualizarListas();

                } else if (novoNome != null) {
                    JOptionPane.showMessageDialog(view.getContentPane(), "O nome da tarefa não pode estar vazio!");
                }

            } else {
                JOptionPane.showMessageDialog(view.getContentPane(), "Por favor, selecione uma tarefa numa das listas para editar.");
            }
        }
    public void atualizarListas() {
        // 1. Limpa todos os motores de uma só vez
        modeloFazer.clear();
        modeloProgresso.clear();
        modeloConcluido.clear();
        modeloMembros.clear(); // Limpa também a lista da equipa

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

            // Vai buscar a lista de membros atual para ver se este nome já lá está
            boolean jaExiste = projetoAtual.getEquipaProjeto().getMembro().contains(nomeLimpo);

            if (jaExiste) {
                JOptionPane.showMessageDialog(view.getContentPane(),
                        "Já existe um membro com o nome '" + nomeLimpo + "' nesta equipa.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                // Adiciona o membro ao modelo de dados (Model)
                projetoAtual.getEquipaProjeto().adicionarMembro(nomeLimpo);

                // Redesenha a lista no ecrã (View)
                atualizarListas();
            }
        } else if (nome != null) {
            JOptionPane.showMessageDialog(view.getContentPane(), "O nome do membro não pode estar vazio!");
        }
    }
    public void removerMembro() {
        // 1. Tentar capturar o nome do membro
        String membroSelecionado = (String) view.getList1().getSelectedValue();

        if (membroSelecionado != null) {

            int resposta = JOptionPane.showConfirmDialog(view.getContentPane(),
                    "Tem a certeza que deseja remover '" + membroSelecionado + "' da equipa?\n" +
                            "(Isto também irá remover esta pessoa de todas as tarefas associadas)", // Aviso extra!
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {

                // --- PASSO 1: REMOVER DA EQUIPA ---
                projetoAtual.getEquipaProjeto().removerMembro(membroSelecionado);

                // --- PASSO 2: A TUA NOVA IDEIA (LIMPAR AS TAREFAS) ---
                // Percorremos todas as tarefas do projeto à procura do nome dele
                for (tarefas t : projetoAtual.getListaTarefas()) {
                    // Verificamos se a tarefa tem alguém atribuído, e se é a pessoa que estamos a apagar
                    if (membroSelecionado.equals(t.getMembro())) {
                        t.setMembro(null); // Retira a pessoa da tarefa!
                    }
                }

                // --- PASSO 3: LIMPEZAS VISUAIS ---
                view.getList1().clearSelection();
                atualizarListas(); // Redesenha tudo (o nome vai desaparecer das tarefas visualmente!)
            }

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(),
                    "Por favor, selecione um membro na lista para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    public void atribuirTarefa() {
        // 1. Saber qual é o membro selecionado na lista de equipas
        String membroSelecionado = (String) view.getList1().getSelectedValue();

        if (membroSelecionado != null) {

            // 2. Verificar se o projeto já tem alguma tarefa criada!
            int numeroDeTarefas = projetoAtual.getListaTarefas().size();
            if (numeroDeTarefas == 0) {
                JOptionPane.showMessageDialog(view.getContentPane(),
                        "Ainda não existem tarefas neste projeto para atribuir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return; // Sai do método logo aqui
            }

            // 3. Transformar a nossa lista de tarefas num Array para o Menu Dropdown conseguir ler
            tarefas[] arrayTarefas = new tarefas[numeroDeTarefas];
            projetoAtual.getListaTarefas().toArray(arrayTarefas);

            // 4. Mostrar a janela mágica com o Dropdown!
            tarefas tarefaEscolhida = (tarefas) JOptionPane.showInputDialog(
                    view.getContentPane(),
                    "Escolha a tarefa que deseja atribuir a '" + membroSelecionado + "':",
                    "Atribuir Tarefa",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    arrayTarefas, // As opções do dropdown
                    arrayTarefas[0] // A opção que aparece por defeito
            );

            // 5. Se ele escolheu uma tarefa e clicou OK
            if (tarefaEscolhida != null) {

                // Vai ao modelo da Tarefa e guarda lá o nome do membro!
                tarefaEscolhida.setMembro(membroSelecionado);

                JOptionPane.showMessageDialog(view.getContentPane(),
                        "Tarefa '" + tarefaEscolhida.getNome() + "' atribuída a " + membroSelecionado + " com sucesso!");

                // Redesenha tudo para garantir que fica atualizado
                atualizarListas();
            }

        } else {
            JOptionPane.showMessageDialog(view.getContentPane(),
                    "Por favor, selecione um membro na lista primeiro.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }



    }
