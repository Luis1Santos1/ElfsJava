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

    public void cadastrarUsuario() {
        System.out.print("Digite o nome de usuário: ");
    String nomeUsuario = scanner.nextLine();

    System.out.print("Digite a senha: ");
    String senha = scanner.nextLine();

    EntityManager em = JPAUtil.getEntityManager();
    UsuarioDAO usuarioDAO = new UsuarioDAO(em);

    Usuario usuarioExistente = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
    if (usuarioExistente != null) {
        System.out.println("Usuário já existe. Não é possível cadastrar novamente.");
        em.close();
        return;
    }

    em.getTransaction().begin();

    Usuario novoUsuario = new Usuario(nomeUsuario, senha);
    usuarioDAO.cadastrar(novoUsuario);

    em.getTransaction().commit();
    em.close();

    System.out.println("Usuário cadastrado com sucesso!");
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
        em.close();
    }

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

    public static void main(String[] args) {
        App app = new App();
        
        while(!logadoOk){
            System.out.println("Escolha uma opção: -1. Cadastrar Usuario  \n-2. Logar Usuario \n");
            String escolha = scanner.nextLine();

            if(escolha.equals("1")){
                app.cadastrarUsuario();
            }
            else if(escolha.equals("2")){
                app.logarUsuario();
            }
        }

        while(logadoOk==true){
        app.cadastrarCamisa();
        }


        EntityManager em = JPAUtil.getEntityManager();
        CamisaDAO camisaDAO = new CamisaDAO(em);

        Camisa c = camisaDAO.buscarPorId(1l);
        System.out.println(c.getPreco());

        List<Camisa> todos = camisaDAO.buscarPorNomeDaCategoria("MangaCurta");
        todos.forEach(c2 -> System.out.println(c.getNome()));

        app.scanner.close();
    }
}
