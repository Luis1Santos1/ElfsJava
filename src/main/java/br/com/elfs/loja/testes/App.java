package br.com.elfs.loja.testes;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;

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

    //cria CRUD para usuario

    public void cadastrarUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuarioExistente = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
        if (usuarioExistente != null) {
            System.out.println("\nUsuário já existe. Não é possível cadastrar novamente.");
            em.close();
            return;
        }

        em.getTransaction().begin();

        Usuario novoUsuario = new Usuario(nomeUsuario, senha);
        usuarioDAO.cadastrar(novoUsuario);

        em.getTransaction().commit();
        em.close();

        System.out.println("\nUsuário cadastrado com sucesso!");
    }

    public Usuario logarUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
        if (usuario == null || !usuarioDAO.verificarCredenciais(nomeUsuario, senha)) {
            System.out.println("Nome de usuário ou senha inválidos. Tente novamente.");
            return null;
        } else {
            System.out.println("Login realizado com sucesso!");
            usuario.setLogado(true);
            logadoOk = true;
            return usuario;
        }
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

    public void atualizarUsuario() {
        listarUsuarios();

        System.out.print("Digite o ID do usuário que deseja atualizar: ");
        Long id = Long.parseLong(scanner.nextLine());

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado. Não é possível atualizar.");
            em.close();
            return;
        }

        System.out.print("Digite o novo nome de usuário: ");
        String novoNomeUsuario = scanner.nextLine();

        em.getTransaction().begin();

        usuario.setNomeUsuario(novoNomeUsuario);
        usuarioDAO.atualizar(usuario);

        em.getTransaction().commit();
        em.close();

        System.out.println("Usuário atualizado com sucesso!");
    }

    public void excluirUsuario() {
        listarUsuarios();

        System.out.print("Digite o ID do usuário que deseja excluir: ");
        Long id = Long.parseLong(scanner.nextLine());

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDAO usuarioDAO = new UsuarioDAO(em);

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado. Não é possível excluir.");
            em.close();
            return;
        }

        em.getTransaction().begin();

        usuarioDAO.excluir(usuario);

        em.getTransaction().commit();
        em.close();

        System.out.println("Usuário excluído com sucesso!");
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

        em.getTransaction().begin();

        Tipo manga = new Tipo(tipoManga);
        tipoDAO.cadastrar(manga);

        Camisa camisa = new Camisa(nome, tamanho, preco, manga, time);
        camisaDAO.cadastrar(camisa);

        em.getTransaction().commit();
        System.out.println("Camisa cadastrada com sucesso!");
        em.close();
    }

    public List<Camisa> buscarTodasCamisas() {
        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        List<Camisa> camisas = camisaDAO.buscarTodos();
        em.close();

        return camisas;
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

    public void atualizarCamisa() {
        System.out.println("=== Atualização de Camisa ===");

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        List<Camisa> camisas = camisaDAO.buscarTodos();
        System.out.println("Camisas cadastradas:");

        for (Camisa camisa : camisas) {
            System.out.println(camisa.getId() + " - " + camisa.getNome());
        }

        System.out.print("Digite o ID da camisa que deseja atualizar: ");
        Long id = Long.parseLong(scanner.nextLine());

        em.getTransaction().begin();

        Camisa camisa = camisaDAO.buscarPorId(id);
        if (camisa == null) {
            System.out.println("Camisa não encontrada.");
            em.getTransaction().commit();
            em.close();
            return;
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
        em.getTransaction().commit();
        em.close();

        System.out.println("Camisa atualizada com sucesso!");
    }

    public void excluirCamisa() {

        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        List<Camisa> camisas = camisaDAO.buscarTodos();
        System.out.println("Camisas disponíveis para exclusão:");
        for (Camisa camisa : camisas) {
            System.out.println(camisa.getId() + " - " + camisa.getNome());
        }

        System.out.print("Digite o ID da camisa que deseja excluir: ");
        Long id = Long.parseLong(scanner.nextLine());

        em.getTransaction().begin();

        Camisa camisa = camisaDAO.buscarPorId(id);
        if (camisa == null) {
            System.out.println("Camisa não encontrada. Nenhuma exclusão realizada.");
            em.getTransaction().commit();
            em.close();
            return;
        }

        camisaDAO.excluir(camisa);

        em.getTransaction().commit();
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
        PagamentoDAO pagamentoDAO = new PagamentoDAO(em);

        Usuario usuario = logarUsuario();
        if (usuario == null) {
            System.out.println("Falha na autenticação. Não é possível cadastrar o pagamento.");
            return;
        }

        Pagamento pagamento = new Pagamento(valor, data, usuario);
        pagamentoDAO.cadastrar(pagamento);
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

        System.out.println("=== Atualização de Pagamento ===");

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

    public static void main(String[] args) {
        App app = new App();

        while (!logadoOk) {
            System.out.println(
                    "Escolha uma opção: -1. Cadastrar Usuario  \n-2. Logar Usuario \n-3.Listar Camisas Cadastradas \n-4.Buscar Camisas pelo Id");
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
        }

        while (logadoOk == true) {
            System.out.println(
                    "\n Escolha uma opção: \n-1. Cadastrar camisa \n");
            String escolha = scanner.nextLine();

            if (escolha.equals("1")) {
                app.cadastrarCamisa();
            }
        }

    }
}
