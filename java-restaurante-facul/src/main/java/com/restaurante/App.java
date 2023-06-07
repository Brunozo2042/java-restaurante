package com.restaurante;

import java.util.Scanner;

import com.restaurante.dao.*;
import com.restaurante.models.*;

public class App {

    private static Scanner input = new Scanner(System.in);
    private static boolean sair;

    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private static MesaDAO mesaDAO = new MesaDAO();
    private static PedidoDAO pedidoDAO = new PedidoDAO();
    private static PratoDAO pratoDAO = new PratoDAO();

    public static void main(String[] args) {

        if (!login()) {
            return;
        }
        sair = false;

        do {
            switch (menu()) {
                case 1:
                    cadastrarCliente();
                    break;
                default:
                    sair = true;
            }

        } while (!sair);
    }

    private static int menu() {
        System.out.println("------------------------------------");
        System.out.println("1 - Novo cliente");
        System.out.println("2 - Nova mesa para cliente");
        System.out.println("3 - Novo pedido");
        System.out.println("4 - Finalizar mesa");
        System.out.println("0 - Sair do programa");
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
        if (validaFunc(id, senha)) {
            return true;
        } else {
            return false;
        }
    }

    private static void cadastrarCliente() {

        String nome, tel;
        System.out.println("Nome:");
        nome = input.nextLine();
        System.out.println("Telefone:");
        tel = input.nextLine();

        Cliente c = new Cliente(nome, tel);

        clienteDAO.create(c);
    }

    public static boolean validaFunc(Integer id, String senha) {
        Funcionario func = funcionarioDAO.findById(Funcionario.class, id);
        if (func.getSenha().equals(senha)) {
            return true;
        }
        return false;
    }
}
