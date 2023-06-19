package br.com.elfs.loja.testes;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;

import org.mindrot.jbcrypt.BCrypt;

import br.com.elfs.loja.dao.CamisaDAO;
import br.com.elfs.loja.dao.PagamentoDAO;
import br.com.elfs.loja.dao.TipoDAO;
import br.com.elfs.loja.dao.UsuarioDAO;
import br.com.elfs.loja.modelo.Camisa;
import br.com.elfs.loja.modelo.Pagamento;
import br.com.elfs.loja.modelo.Tipo;
import br.com.elfs.loja.modelo.Usuario;
import br.com.elfs.loja.util.JPAUtil;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean logadoOk = false;

    // cria CRUD para usuario

    public void cadastrarUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        try {
            em.getTransaction().begin();

            Usuario usuarioExistente = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
            if (usuarioExistente != null) {
                System.out.println("\nUsuário já existe. Não é possível cadastrar novamente.");
                return;
            }

            Usuario novoUsuario = new Usuario(nomeUsuario, senhaCriptografada);
            usuarioDAO.cadastrar(novoUsuario);

            em.getTransaction().commit();
            System.out.println("\nUsuário cadastrado com sucesso!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("\nErro ao cadastrar usuário: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Usuario logarUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
        if (usuario == null) {
            System.out.println("\nUsuário não encontrado.");
            em.close();
            return null;
        }

        if (!BCrypt.checkpw(senha, usuario.getSenha())) {
            System.out.println("\nSenha incorreta.");
            em.close();
            return null;
        }

        usuario.setLogado(true);
        usuarioDAO.atualizar(usuario);
        logadoOk = true;

        em.close();

        System.out.println("\nLogin realizado com sucesso!");
        return usuario;
    }

    public Usuario deslogarUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
        if (usuario == null || !BCrypt.checkpw(senha, usuario.getSenha())) {
            System.out.println("Nome de usuário ou senha inválidos. Tente novamente.");
            em.close();
            return null;
        }
        usuario.setLogado(false);
        usuarioDAO.atualizar(usuario);
        logadoOk = false;
        em.close();
        System.out.println("Usuário deslogado com sucesso!");

        return usuario;
    }

    public void listarUsuarios() {
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        System.out.println("Lista de Usuários:");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getId() + ", " + usuario.getNomeUsuario());
        }

        em.close();
    }

    public Usuario atualizarUsuario() {
        listarUsuarios();

        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
        if (usuario == null) {
            System.out.println("Usuário não encontrado. Não é possível atualizar.");
            em.close();
            return null;
        }

        if (!BCrypt.checkpw(senha, usuario.getSenha())) {
            System.out.println("Senha incorreta.");
            em.close();
            return null;
        }

        if (!usuario.isLogado()) {
            System.out.println("Usuário não está logado nesta conta. Não é possível atualizar.");
            em.close();
            return null;
        }

        System.out.print("Digite o novo nome de usuário (ou pressione Enter para manter o mesmo): ");
        String novoNomeUsuario = scanner.nextLine();
        if (novoNomeUsuario.isEmpty()) {
            novoNomeUsuario = usuario.getNomeUsuario(); // Mantém o valor existente
        } else {
            // Verifica se o novo nome de usuário já existe no banco
            Usuario usuarioExistente = usuarioDAO.buscarPorNomeUsuario(novoNomeUsuario);
            if (usuarioExistente != null) {
                System.out.println("O novo nome de usuário já está em uso. Não é possível atualizar.");
                em.close();
                return null;
            }
        }

        System.out.print("Digite a nova senha (ou pressione Enter para manter a mesma): ");
        String novaSenha = scanner.nextLine();
        if (novaSenha.isEmpty()) {
            novaSenha = usuario.getSenha(); // Mantém o valor existente
        } else {
            // Criptografa a nova senha
            novaSenha = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
        }

        usuario.setNomeUsuario(novoNomeUsuario);
        usuario.setSenha(novaSenha);
        usuarioDAO.atualizar(usuario);

        em.close();

        System.out.println("Usuário atualizado com sucesso!");
        return usuario;
    }

    public Usuario excluirUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
        if (usuario == null) {
            System.out.println("\nUsuário não encontrado.");
            em.close();
            return null;
        }

        if (!BCrypt.checkpw(senha, usuario.getSenha())) {
            System.out.println("\nSenha incorreta.");
            em.close();
            return null;
        }

        usuarioDAO.excluir(usuario);
        em.close();

        System.out.println("Usuário excluído com sucesso!");
        logadoOk = false;
        return usuario;
    }

    // CRUD's para a classe Camisa

    public void cadastrarCamisa() {
        System.out.println("=== Cadastro de Camisa ===");

        System.out.print("Digite o nome da camisa: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o tamanho da camisa: ");
        String tamanho = scanner.nextLine();

        System.out.print("Digite o preço da camisa: ");
        BigDecimal preco = new BigDecimal(scanner.nextLine());

        System.out.print("Digite o tipo de manga da camisa: ");
        String tipoManga = scanner.nextLine();

        System.out.print("Digite o time da camisa: ");
        String time = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);
        TipoDAO tipoDAO = new TipoDAO(em);

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Tipo manga = new Tipo(tipoManga);
            tipoDAO.cadastrar(manga);

            Camisa camisa = new Camisa(nome, tamanho, preco, manga, time);
            camisaDAO.cadastrar(camisa);
            System.out.println("Camisa cadastrada com sucesso!");

            if (transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Erro ao cadastrar camisa: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void buscarTodasCamisas() {

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        List<Camisa> camisas = camisaDAO.buscarTodos();
        System.out.println("Camisas cadastradas:");

        for (Camisa camisa : camisas) {
            System.out.println(camisa.getId() + " - " + camisa.getNome());
        }
    }

    public void buscarCamisaPorNome() {
    
        System.out.print("Digite o nome da camisa que deseja buscar: ");
        String nomeCamisa = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        List<Camisa> camisas = camisaDAO.buscarPorNome(nomeCamisa);

        if (camisas.isEmpty()) {
            System.out.println("Nenhuma camisa encontrada com o nome informado.");
        } else {
            System.out.println("Detalhes da camisa:");
            for (Camisa camisa : camisas) {
                System.out.println("ID: " + camisa.getId());
                System.out.println("Nome: " + camisa.getNome());
                System.out.println("Tamanho: " + camisa.getTamanho());
                System.out.println("Time: " + camisa.getTime());
                System.out.println("Tipo de Manga: " + camisa.getTipo());
                System.out.println("Preço: " + camisa.getPreco());
            }
        }

        em.close();
    }

    public void buscarCamisaPorTipo() {
    System.out.print("Digite o tipo de camisa que deseja buscar: ");
    String tipoCamisa = scanner.nextLine();

    EntityManager em = JPAUtil.getEntityManager();
    CamisaDAO camisaDAO = new CamisaDAO(em);

    List<Camisa> camisas = camisaDAO.buscarPorTipo(tipoCamisa);

    if (camisas.isEmpty()) {
        System.out.println("Nenhuma camisa encontrada com o tipo informado.");
    } else {
        System.out.println("Camisas encontradas:");
        for (Camisa camisa : camisas) {
            System.out.println("ID: " + camisa.getId());
            System.out.println("Nome: " + camisa.getNome());
            System.out.println("Tamanho: " + camisa.getTamanho());
            System.out.println("Time: " + camisa.getTime());
            System.out.println("Tipo de Manga: " + camisa.getTipo());
            System.out.println("Preço: " + camisa.getPreco());
        }
    }

    em.close();
}
    
    public Camisa atualizarCamisa() {

        buscarTodasCamisas();

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        System.out.print("Digite o ID da camisa que deseja atualizar: ");
        Long id = Long.parseLong(scanner.nextLine());

        Camisa camisa = camisaDAO.buscarPorId(id);
        if (camisa == null) {
            System.out.println("Camisa não encontrada.");
            em.getTransaction().commit();
            em.close();
            return null;
        }

        System.out.print("Digite o novo nome da camisa (ou enter para manter o atual): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            camisa.setNome(novoNome);
        }

        System.out.print("Digite o novo tamanho da camisa (ou enter para manter o atual): ");
        String novoTamanho = scanner.nextLine();
        if (!novoTamanho.isEmpty()) {
            camisa.setTamanho(novoTamanho);
        }

        System.out.print("Digite o novo preço da camisa (ou enter para manter o atual): ");
        String novoPrecoStr = scanner.nextLine();
        if (!novoPrecoStr.isEmpty()) {
            BigDecimal novoPreco = new BigDecimal(novoPrecoStr);
            camisa.setPreco(novoPreco);
        }

        System.out.print("Digite o novo tipo de manga da camisa (ou enter para manter o atual): ");
        String novoTipoManga = scanner.nextLine();
        if (!novoTipoManga.isEmpty()) {
            TipoDAO tipoDAO = new TipoDAO(em);
            Tipo manga = tipoDAO.buscarPorNome(novoTipoManga);
            if (manga == null) {
                manga = new Tipo(novoTipoManga);
                tipoDAO.cadastrar(manga);
            }
            camisa.setTipo(manga);
        }

        System.out.print("Digite o novo time da camisa (ou enter para manter o atual): ");
        String novoTime = scanner.nextLine();
        if (!novoTime.isEmpty()) {
            camisa.setTime(novoTime);
        }

        camisaDAO.atualizar(camisa);
        em.close();

        System.out.println("Camisa atualizada com sucesso!");

        return camisa;
    }

    public void excluirCamisa() {

        System.out.println("Camisas disponíveis para exclusão:");
        buscarTodasCamisas();

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        System.out.print("Digite o ID da camisa que deseja excluir: ");
        Long id = Long.parseLong(scanner.nextLine());

        Camisa camisa = camisaDAO.buscarPorId(id);
        if (camisa == null) {
            System.out.println("Camisa não encontrada. Nenhuma exclusão realizada.");
            em.getTransaction().commit();
            em.close();
            return;
        }

        camisaDAO.excluir(camisa);
        em.close();

        System.out.println("Camisa excluída com sucesso!");
    }

    // CRUD da classe pagamento

    public void CadastrarPagamento() {
        System.out.print("Digite o valor do pagamento: ");
        double valor = Double.parseDouble(scanner.nextLine());

        System.out.print("Digite a data do pagamento (formato: dd/mm/aaaa): ");
        String dataStr = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data;

        try {
            data = new java.sql.Date(dateFormat.parse(dataStr).getTime());
        } catch (ParseException e) {
            System.out.println("Formato de data inválido. Não foi possível cadastrar o pagamento.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        PagamentoDAO pagamentoDAO = new PagamentoDAO(em);
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuarioLogado = usuarioDAO.buscarUsuarioLogado();
        if (usuarioLogado == null) {
            System.out.println("Nenhum usuário está logado. Não é possível cadastrar o pagamento.");
            return;
        }

        try {
            transaction.begin();

            Long idUsuario = usuarioLogado.getId();

            Pagamento pagamento = new Pagamento(valor, data, idUsuario);
            pagamento.setUsuario(usuarioLogado);
            pagamentoDAO.cadastrar(pagamento);

            transaction.commit();

            System.out.println("Pagamento cadastrado");
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Erro ao cadastrar o pagamento. Transação foi rollbacked.");
        } finally {
            em.close();
        }
    }

    public void listarPagamentos() {
        EntityManager em = JPAUtil.getEntityManager();
        PagamentoDAO pagamentoDAO = new PagamentoDAO(em);

        List<Pagamento> pagamentos = pagamentoDAO.buscarTodos();

        if (pagamentos.isEmpty()) {
            System.out.println("Não há pagamentos cadastrados.");
        } else {
            System.out.println("Lista de Pagamentos:");
            for (Pagamento pagamento : pagamentos) {
                System.out.println("ID: " + pagamento.getId() + ", Data: " + pagamento.getData() + ", Usuário: "
                        + pagamento.getUsuario().getNomeUsuario());
            }
        }

        em.close();
    }

    public void atualizarPagamento() {
        listarPagamentos();

        System.out.print("Digite o ID do pagamento que deseja atualizar: ");
        Long pagamentoId = Long.parseLong(scanner.nextLine());

        EntityManager em = JPAUtil.getEntityManager();
        PagamentoDAO pagamentoDAO = new PagamentoDAO(em);

        em.getTransaction().begin();

        Pagamento pagamento = pagamentoDAO.buscarPorId(pagamentoId);
        if (pagamento == null) {
            System.out.println("Pagamento não encontrado.");
        } else {
            System.out.println("Opções de atualização:");
            System.out.println("1. Atualizar preço");
            System.out.println("2. Atualizar data");
            System.out.println("3. Atualizar usuário");
            System.out.println("Pressione Enter para manter o valor anterior");

            System.out.print("Digite a opção desejada: ");
            String opcao = scanner.nextLine();

            if (opcao.equals("1")) {
                System.out.print("Digite o novo preço: ");
                String precoStr = scanner.nextLine();
                double novoPreco = precoStr.isEmpty() ? pagamento.getValor() : Double.parseDouble(precoStr);
                pagamento.setValor(novoPreco);
            } else if (opcao.equals("2")) {
                System.out.print("Digite a nova data (formato: dd/mm/aaaa): ");
                String dataStr = scanner.nextLine();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date novaData;
                try {
                    novaData = dataStr.isEmpty() ? pagamento.getData() : dateFormat.parse(dataStr);
                    java.sql.Date novaDataSql = new java.sql.Date(novaData.getTime());
                    pagamento.setData(novaDataSql);
                } catch (ParseException e) {
                    System.out.println("Formato de data inválido. Não foi possível atualizar o pagamento.");
                    em.getTransaction().rollback();
                    em.close();
                    return;
                }
            } else if (opcao.equals("3")) {
                UsuarioDAO usuarioDAO = new UsuarioDAO(em);
                listarUsuarios();
                System.out.print("Digite o ID do novo usuário: ");
                Long novoUsuarioId = Long.parseLong(scanner.nextLine());
                Usuario novoUsuario = usuarioDAO.buscarPorId(novoUsuarioId);
                if (novoUsuario == null) {
                    System.out.println("Usuário não encontrado. Não foi possível atualizar o pagamento.");
                    em.getTransaction().rollback();
                    em.close();
                    return;
                }
                pagamento.setUsuario(novoUsuario);
            }

            pagamentoDAO.atualizar(pagamento);
            em.getTransaction().commit();
            System.out.println("Pagamento atualizado com sucesso.");
        }

        em.close();
    }

    public void deletarPagamento() {
        listarPagamentos();

        System.out.print("Digite o ID do pagamento que deseja excluir: ");
        Long pagamentoId = Long.parseLong(scanner.nextLine());

        EntityManager em = JPAUtil.getEntityManager();
        PagamentoDAO pagamentoDAO = new PagamentoDAO(em);

        em.getTransaction().begin();

        Pagamento pagamento = pagamentoDAO.buscarPorId(pagamentoId);
        if (pagamento == null) {
            System.out.println("Pagamento não encontrado.");
        } else {
            pagamentoDAO.excluir(pagamento);
            em.getTransaction().commit();

            System.out.println("Pagamento excluído com sucesso.");
        }

        em.close();
    }

    public void finalizarPrograma() {
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        for (Usuario usuario : usuarios) {
            usuario.setLogado(false);
            usuarioDAO.atualizar(usuario);
        }

        em.close();

        System.out.println("Programa finalizado.");
    }

    public static void main(String[] args) {
        App app = new App();
        boolean rodarPrograma = true;

        while (rodarPrograma) {
            while (!logadoOk && rodarPrograma) {
                System.out.println(
                        "Escolha uma opção: -1. Cadastrar Usuario  \n-2. Logar Usuario \n-3.Listar Camisas Cadastradas \n-4.Buscar Camisas pelo Id \n-X. Para parar o programa \n");
                String escolha = scanner.nextLine();

                if (escolha.equals("1")) {
                    app.cadastrarUsuario();
                } else if (escolha.equals("2")) {
                    app.logarUsuario();
                } else if (escolha.equals("3")) {
                    app.buscarTodasCamisas();
                } else if (escolha.equals("4")) {
                    app.buscarCamisaPorNome();
                }

                else if (escolha.equals("X")) {
                    app.finalizarPrograma();
                }
            }

            while (logadoOk && rodarPrograma) {
                System.out.println(
                        "\n Escolha uma opção: \n-1. Cadastrar camisa \n-2. Atualizar camisa \n-3. Buscar Todas camisas \n-4. Buscar camisa por nome \n-5. Excluir camisa cadastrada \n-6. Cadastrar pagamento \n-7. Atualizar pagamento \n-8. Listar pagamentos \n-9. Deletar pagamento \n-10. Atualizar usuario \n-11. Excluir usuario \n-12. Deslogar usuario \n -X. Para parar o programa");
                String escolha = scanner.nextLine();

                if (escolha.equals("1")) {
                    app.cadastrarCamisa();
                } else if (escolha.equals("2")) {
                    app.atualizarCamisa();
                } else if (escolha.equals("3")) {
                    app.buscarTodasCamisas();
                } else if (escolha.equals("4")) {
                    app.buscarCamisaPorNome();
                }else if (escolha.equals("5")) {
                    app.buscarCamisaPorTipo();
                } else if (escolha.equals("6")) {
                    app.excluirCamisa();
                } else if (escolha.equals("7")) {
                    app.CadastrarPagamento();
                } else if (escolha.equals("8")) {
                    app.atualizarPagamento();
                } else if (escolha.equals("9")) {
                    app.listarPagamentos();
                } else if (escolha.equals("10")) {
                    app.deletarPagamento();
                } else if (escolha.equals("11")) {
                    app.atualizarUsuario();
                } else if (escolha.equals("12")) {
                    app.excluirUsuario();
                } else if (escolha.equals("13")) {
                    app.deslogarUsuario();
                }

                else if (escolha.equals("X")) {
                    app.finalizarPrograma();
                }
            }
        }

    }
}
