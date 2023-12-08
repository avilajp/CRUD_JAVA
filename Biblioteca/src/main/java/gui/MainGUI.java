package gui;

import dao.LivroDAO;
import model.Livro;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainGUI extends JFrame {

    private LivroDAO livroDAO;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAno;
    private DefaultListModel<String> listaModel;
    private JList<String> listaLivros;
    private Livro livroSelecionado;

    public MainGUI() {

        livroDAO = new LivroDAO();

        // Configurar componentes da interface aqui...
        JButton btnInserir = new JButton("Inserir Livro");
        JButton btnListar = new JButton("Listar Livros");
        JButton btnAtualizar = new JButton("Atualizar Livro");
        JButton btnExcluir = new JButton("Excluir Livro");

        txtTitulo = new JTextField(20);
        txtAutor = new JTextField(20);
        txtAno = new JTextField(4);

        listaModel = new DefaultListModel<>();
        listaLivros = new JList<>(listaModel);
        JScrollPane scrollPaneLista = new JScrollPane(listaLivros);


        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Título:"));
        add(txtTitulo);

        add(new JLabel("Autor:"));
        add(txtAutor);

        add(new JLabel("Ano de Publicação:"));
        add(txtAno);

        add(new JLabel("Lista de Livros no Banco (é possível selecionar para editar ou excluir)"));
        add(scrollPaneLista);

        add(btnInserir);
        add(btnListar);
        add(btnAtualizar);
        add(btnExcluir);


        btnInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirLivro(txtTitulo.getText(), txtAutor.getText(), Integer.parseInt(txtAno.getText()));
            }
        });

        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarLivros();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (livroSelecionado != null) {
                    atualizarLivro(livroSelecionado);
                } else {
                    JOptionPane.showMessageDialog(MainGUI.this, "Selecione um livro para atualizar.");
                }
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (livroSelecionado != null) {
                    excluirLivro(livroSelecionado.getId());
                } else {
                    JOptionPane.showMessageDialog(MainGUI.this, "Selecione um livro para excluir.");
                }
            }
        });


        listaLivros.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && listaLivros.getSelectedIndex() != -1) {
                    int idLivro = Integer.parseInt(listaLivros.getSelectedValue().split(":")[0].trim());
                    livroSelecionado = livroDAO.obterLivroPorId(idLivro);
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setVisible(true);
    }

    private void inserirLivro(String titulo, String autor, int ano) {
        Livro novoLivro = new Livro();
        novoLivro.setTitulo(titulo);
        novoLivro.setAutor(autor);
        novoLivro.setAnoPublicacao(ano);

        livroDAO.inserirLivro(novoLivro);
        limparCampos();
        listarLivros();
    }

    private void listarLivros() {
        listaModel.clear();
        List<Livro> livros = livroDAO.obterTodosLivros();

        for (Livro livro : livros) {
            listaModel.addElement(livro.getId() + ": " + livro.getTitulo() + " - " + livro.getAutor());
        }
    }

    private void atualizarLivro(Livro livro) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField txtNovoTitulo = new JTextField(20);
        JTextField txtNovoAutor = new JTextField(20);
        JTextField txtNovoAno = new JTextField(4);

        panel.add(new JLabel("Novo Título:"));
        panel.add(txtNovoTitulo);

        panel.add(new JLabel("Novo Autor:"));
        panel.add(txtNovoAutor);

        panel.add(new JLabel("Novo Ano de Publicação:"));
        panel.add(txtNovoAno);

        panel.add(new JLabel("Livro Atual:"));
        panel.add(new JLabel(livro.getId() + ": " + livro.getTitulo() + " - " + livro.getAutor()));

        int result = JOptionPane.showConfirmDialog(MainGUI.this, panel, "Atualizar Livro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            livro.setTitulo(txtNovoTitulo.getText());
            livro.setAutor(txtNovoAutor.getText());
            livro.setAnoPublicacao(Integer.parseInt(txtNovoAno.getText()));

            livroDAO.atualizarLivro(livro);
            listarLivros();
        }
    }


    private void excluirLivro(int livroId) {
        int opcao = JOptionPane.showConfirmDialog(MainGUI.this,
                "Deseja realmente excluir o livro ID: " + livroId + "?", "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            livroDAO.excluirLivro(livroId);
            livroSelecionado = null;
            listarLivros();
        }
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAno.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }
}
