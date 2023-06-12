package com.restaurante;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

import com.restaurante.dao.*;
import com.restaurante.models.*;

public class App {

    private static Scanner input = new Scanner(System.in);
    private static boolean sair;
    private static Funcionario FuncLogado, f;
    private static Cliente c;
    private static Mesa m;
    private static Pedido p;
    private static Prato prt;

    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private static MesaDAO mesaDAO = new MesaDAO();
    private static PedidoDAO pedidoDAO = new PedidoDAO();
    private static PratoDAO pratoDAO = new PratoDAO();

    public static void main(String[] args) {

        // Valida se ja existe funcionários cadastrados
        if (temFunc()) {
            if (!login()) {
                return;
            }
        } else {
            novoFunc();
            FuncLogado = funcionarioDAO.findById(Funcionario.class, 1);
        }

        sair = false;

        do {
            switch (menu()) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    ocuparMesa();
                    break;
                case 3:
                    novoPedido();
                    break;
                case 4:
                    finalizarPedido();
                    break;
                case 5:
                    listarCliente();
                    break;
                case 6:
                    listarMesas();
                    break;
                case 7:
                    listarFuncs();
                    break;
                case 8:
                    listarPratos();
                    break;
                case 9:
                    listarPedidos();
                    break;
                case 10:
                    if (FuncLogado.isGerente()) {
                        novaMesa();
                    }
                    break;
                case 11:
                    if (FuncLogado.isGerente()) {
                        novoFunc();
                    }
                    break;
                case 12:
                    if (FuncLogado.isGerente()) {
                        novoPrato();
                    }
                    break;
                default:
                    sair = true;
            }

        } while (!sair);
    }

    private static int menu() {
        System.out.println("------------------------------------");
        System.out.println("1  - Novo cliente");
        System.out.println("2  - Ocupar mesa");
        System.out.println("3  - Novo pedido");
        // System.out.println("4 - Adicionar pratos ao pedido");
        System.out.println("4  - Finalizar pedido");
        System.out.println("5  - Listar Clientes");
        System.out.println("6  - Listar Mesas");
        System.out.println("7  - Listar Funcionários");
        System.out.println("8  - Listar Pratos");
        System.out.println("9  - Listar Pedidos");
        if (FuncLogado.isGerente()) {
            System.out.println("10 - Nova Mesa");
            System.out.println("11 - Novo Funcionário");
            System.out.println("12 - Novo Prato");
        }
        System.out.println("0  - Sair do programa");
        System.out.println("------------------------------------");

        return input.nextInt();
    }

    private static boolean login() {
        Integer id;
        String senha;
        System.out.println("ID do funcionário:");
        id = input.nextInt();
        input.nextLine();
        System.out.println("Senha:");
        senha = input.nextLine();
        FuncLogado = funcionarioDAO.findById(Funcionario.class, id);
        if (validaFunc(FuncLogado)) {
            if (BCrypt.checkpw(FuncLogado.getSenha(), senha)) {
                return true;
            } else {
                System.out.println("SENHA INVÁLIDA");
                return false;
            }
        } else {
            return false;
        }
    }

    // ----------------------------------------------------------------------------------------------------------------------------
    // 1 OK
    private static void cadastrarCliente() {
        input.nextLine();
        String nome, tel;
        System.out.print("Nome: ");
        nome = input.nextLine();
        System.out.print("Telefone: ");
        tel = input.nextLine();

        c = new Cliente(nome, tel);

        clienteDAO.create(c);

        System.out.println("Cliente cadastrado realizado com sucesso!");
    }

    // 2 OK
    private static void ocuparMesa() {
        System.out.print("ID da mesa: ");
        m = mesaDAO.findById(Mesa.class, input.nextInt());

        if (!validaMesa(m)) {
            return;
        }

        System.out.print("ID do cliente:");
        c = clienteDAO.findByIdLong(Cliente.class, input.nextLong());

        if (!validaCli(c)) {
            return;
        }

        m.setCli(c);
        m.setOcupada(true);

        mesaDAO.update(m);

        System.out.println("Mesa " + m.getId() + " ocupada pelo cliente " + c.getNome());
    }

    // 3 OK
    private static void novoPedido() {
        // Mesa
        System.out.print("ID da mesa: ");
        m = mesaDAO.findById(Mesa.class, input.nextInt());

        if (!validaMesa(m)) {
            return;
        }

        if (m.getCli() == null) {
            System.out.println("Sem cliente na mesa");
            return;
        }

        if (m.getPedido() == null) {
            CriaPedido();
        } else {
            AtualizaPedido();
        }

    }

    // 4 OK
    private static void finalizarPedido() {
        System.out.print("ID da mesa:");
        m = mesaDAO.findById(Mesa.class, input.nextInt());

        if (!validaMesa(m)) {
            return;
        }

        System.out.println("Valor total do pedido: R$ " + m.getPedido().getVlrTot());

        m.setCli(null);
        m.setPedido(null);
        m.setOcupada(false);

        Pedido ped = m.getPedido();
        ped.setFinalizado(true);

        pedidoDAO.update(ped);
        mesaDAO.update(m);
    }

    // 5 OK
    private static void listarCliente() {
        List<Cliente> clientes = clienteDAO.findAll(Cliente.class);

        System.out.println();
        for (Cliente cliente : clientes) {
            System.out
                    .println(cliente.getId() + " - " + cliente.getNome() + " " + cliente.getTelefone());
        }
        System.out.println();
    }

    // 6 OK
    private static void listarMesas() {
        List<Mesa> mesas = mesaDAO.findAll(Mesa.class);

        System.out.println();
        for (Mesa mesa : mesas) {
            System.out
                    .println(mesa.getId() + " - " + (mesa.isOcupada() ? "OCUPADA"
                            : "LIVRE") + " " + (mesa.getCli() == null ? "" : mesa.getCli().getId()));
        }
        System.out.println();
    }

    // 7 OK
    private static void listarFuncs() {
        List<Funcionario> funcs = funcionarioDAO.findAll(Funcionario.class);

        System.out.println();
        for (Funcionario func : funcs) {
            System.out.println(func.getId() + " - " + func.getNome());
        }
        System.out.println();
    }

    // 8 OK
    private static void listarPratos() {
        List<Prato> prts = pratoDAO.findAll(Prato.class);

        System.out.println();
        for (Prato prt : prts) {
            System.out.println(String.format("%d - %s: R$ %.2f", prt.getId(), prt.getNome(), prt.getValor()));
        }
        System.out.println();
    }

    // 9 OK
    private static void listarPedidos() {
        List<Pedido> peds = pedidoDAO.findAll(Pedido.class);

        System.out.println();
        for (Pedido ped : peds) {
            List<Prato> prts = ped.getPrato();

            System.out.println(ped.getId() + " - Mesa: " + ped.getMesa().getId() + " Finalizado: "
                    + (ped.isFinalizado() ? "Sim" : "Não"));
            System.out.println("Pratos: ");
            for (Prato prt : prts) {
                System.out.println(String.format("    %s - %.2f", prt.getNome(), prt.getValor()));
            }
            System.out.println(String.format("    Total: R$ %.2f", ped.getVlrTot()));
        }
        System.out.println();
    }

    // 10 OK
    private static void novaMesa() {
        input.nextLine();

        System.out.println("Quantas mesas deseja inserir?");
        int nMesas = input.nextInt();

        for (int i = 0; i < nMesas; i++) {
            m = new Mesa();
            mesaDAO.create(m);
        }
        System.out.println("Nova(s) mesa(s) cadastrada");
    }

    // 11 OK
    private static void novoFunc() {
        input.nextLine();
        String nome, senhaCrypt;
        boolean ehGerente;

        System.out.print("Nome do Funcionário: ");
        nome = input.nextLine();

        System.out.print("Senha do Funcionário: ");
        senhaCrypt = BCrypt.hashpw(input.nextLine(), BCrypt.gensalt());

        System.out.println("É gerente?");
        System.out.println("1 - Sim\n2 - Não");
        if (input.nextInt() == 1) {
            ehGerente = true;
        } else {
            ehGerente = false;
        }

        f = new Funcionario(nome, senhaCrypt, ehGerente);

        funcionarioDAO.create(f);

        System.out.println("Funcionário cadastrado com sucesso!");
    }

    // 12 OK
    private static void novoPrato() {
        input.nextLine();
        String nome;
        double valor;

        System.out.print("Nome do Prato: ");
        nome = input.nextLine();

        System.out.print("Valor do Prato: ");
        valor = input.nextDouble();

        prt = new Prato(nome, valor);

        pratoDAO.create(prt);

        System.out.println("Prato Cdastrado com sucesso!");
    }
    // ----------------------------------------------------------------------------------------------------------------------------

    public static boolean validaFunc(Funcionario f) {
        if (f == null) {
            System.out.println("Funcionário não cadastrado!  OPERAÇÃO CANCELADA");
            return false;
        } else {
            return true;
        }
    }

    public static boolean validaMesa(Mesa m) {
        if (m == null) {
            System.out.println("Mesa não cadastrada!  OPERAÇÃO CANCELADA");
            return false;
        }
        return true;
    }

    public static boolean validaCli(Cliente c) {
        if (c == null) {
            System.out.println("Cliente não cadastrado!  OPERAÇÃO CANCELADA");
            return false;
        }
        return true;
    }

    public static boolean validaPrato(Prato p) {
        if (p == null) {
            System.out.println("Prato não cadastrado!  OPERAÇÃO CANCELADA");
            return false;
        }
        return true;
    }

    public static void CriaPedido() {

        // Prato
        int id;
        Prato prato;
        List<Prato> pratos = new ArrayList<>();
        System.out.println("Digite 0 quando não houver mais pratos para adicionar");
        do {
            System.out.println("ID do prato: ");
            id = input.nextInt();
            prato = pratoDAO.findById(Prato.class, id);
            if (id != 0 && validaPrato(prato)) {
                pratos.add(prato);
            }
        } while (id != 0);

        // Valor Total
        double vlrTot = 0;
        for (Prato p : pratos) {
            vlrTot += p.getValor();
        }

        p = new Pedido(m, pratos, vlrTot);
        // Cria o pedido
        pedidoDAO.create(p);

        m.setPedido(p);
        // Atualizar a mesa colocando o id do pedido
        mesaDAO.update(m);
    }

    public static void AtualizaPedido() {

        Pedido Pedido = m.getPedido();

        System.out.println("Adicionando pratos ao pedido " + Pedido.getId());

        // Prato
        int id;
        Prato prato;
        List<Prato> pratos = new ArrayList<>();
        System.out.println("Digite 0 quando não houver mais pratos para adicionar");
        do {
            System.out.println("ID do prato: ");
            id = input.nextInt();
            prato = pratoDAO.findById(Prato.class, id);
            if (id != 0 && validaPrato(prato)) {
                Pedido.addPrato(prato);
            }
        } while (id != 0);

        pratos = Pedido.getPrato();

        // Valor Total
        double vlrTot = 0;
        for (Prato prt : pratos) {
            vlrTot += prt.getValor();
        }

        Pedido.setVlrTot(vlrTot);
        // Cria o pedido
        pedidoDAO.update(Pedido);

    }

    public static boolean temFunc() {
        List<Funcionario> funcs = funcionarioDAO.findAll(Funcionario.class);
        if (funcs.size() > 0) {
            return true;
        } else {
            System.out.println(
                    "Não há nenhum funcionário cadastrado\né necessário cadastrar um novo funcionário para logar");
            return false;
        }
    }
}
