package dao;

import config.ConexaoBancoDados;
import model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void inserirLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, ano_publicacao) VALUES (?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement preparedStatement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, livro.getTitulo());
            preparedStatement.setString(2, livro.getAutor());
            preparedStatement.setInt(3, livro.getAnoPublicacao());

            preparedStatement.executeUpdate();

            // Obtém o ID gerado para o livro inserido
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                livro.setId(resultSet.getInt(1));
            }

            System.out.println("Livro inserido com sucesso! ID: " + livro.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livro> obterTodosLivros() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";

        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement preparedStatement = conexao.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Livro livro = new Livro();
                livro.setId(resultSet.getInt("id"));
                livro.setTitulo(resultSet.getString("titulo"));
                livro.setAutor(resultSet.getString("autor"));
                livro.setAnoPublicacao(resultSet.getInt("ano_publicacao"));

                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livros;
    }

    public void atualizarLivro(Livro livroSelecionado) {
        String sql = "UPDATE livros SET titulo = ?, autor = ? WHERE id = ?";

        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {

            preparedStatement.setString(1, livroSelecionado.getTitulo());
            preparedStatement.setString(2, livroSelecionado.getAutor());
            preparedStatement.setInt(3, livroSelecionado.getId());

            preparedStatement.executeUpdate();

            System.out.println("Livro atualizado com sucesso! ID: " + livroSelecionado.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirLivro(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Livro excluído com sucesso! ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Livro obterLivroPorId(int idLivro) {
        String sql = "SELECT * FROM livros WHERE id = ?";

        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {

            preparedStatement.setInt(1, idLivro);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Livro livro = new Livro();
                    livro.setId(resultSet.getInt("id"));
                    livro.setTitulo(resultSet.getString("titulo"));
                    livro.setAutor(resultSet.getString("autor"));
                    livro.setAnoPublicacao(resultSet.getInt("ano_publicacao"));
                    return livro;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
