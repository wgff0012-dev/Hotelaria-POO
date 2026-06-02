package teste;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate; 
import java.time.format.DateTimeFormatter; // Adicionado para formatar as datas
import modelo.HotelService;
import modelo.Quarto;
import modelo.Reserva;
import modelo.Usuario;

public class SistemaHotel {
    static HotelService hotelService = new HotelService(); 
    static Usuario usuarioLogado = null; 
    static Scanner teclado = new Scanner(System.in);
    static DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n--- SISTEMA DE RESERVAS DE HOTEL ---");
            
            if (usuarioLogado == null) {
                System.out.println("1. Cadastrar Conta\n2. Fazer Login\n0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = teclado.nextInt(); teclado.nextLine(); 

                switch (opcao) {
                    case 1 -> menuCadastro();
                    case 2 -> menuLogin();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } 
            else if (usuarioLogado.getTipo().equalsIgnoreCase("ADMIN")) {
                System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + " (ADMIN)!");
                System.out.println("1. Listar Todos os Quartos\n2. Cadastrar Novo Quarto\n3. Fazer Logout\n0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = teclado.nextInt(); teclado.nextLine();

                switch (opcao) {
                    case 1 -> consultarDisponibilidade();
                    case 2 -> menuCadastrarQuarto();
                    case 3 -> { 
                        usuarioLogado = null; 
                        System.out.println("Logout feito.");
                        opcao = -1; 
                    }
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } 
            else {
                System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
                System.out.println("1. Listar Todos os Quartos\n2. Realizar Reserva\n3. Minhas Reservas\n4. Fazer Logout\n0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = teclado.nextInt(); teclado.nextLine();

                switch (opcao) {
                    case 1 -> consultarDisponibilidade();
                    case 2 -> efetuarReserva();
                    case 3 -> gerenciarReservas();
                    case 4 -> { 
                        usuarioLogado = null; 
                        System.out.println("Logout feito.");
                        opcao = -1; 
                    }
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            }
        } while (opcao != 0);
    }

    private static void menuCadastro() {
        System.out.println("\n--- CADASTRO ---");
        System.out.print("Nome: "); String nome = teclado.nextLine();
        System.out.print("Email: "); String email = teclado.nextLine();
        System.out.print("Senha: "); String senha = teclado.nextLine();

        if (hotelService.cadastrarUsuario(nome, email, senha)) {
            System.out.println("Cadastrado com sucesso! Já pode fazer login.");
        }
    }
    
    private static void menuLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: "); String email = teclado.nextLine();
        System.out.print("Senha: "); String senha = teclado.nextLine();

        usuarioLogado = hotelService.fazerLogin(email, senha);

        if (usuarioLogado != null) {
            System.out.println("Logado com sucesso!");
        } else {
            System.out.println("Email ou senha errados.");
        }
    }
    
    private static void consultarDisponibilidade() {
        // Mudamos o título para deixar claro o que está aparecendo
        System.out.println("\n--- QUARTOS DISPONÍVEIS ---");
        
        List<Quarto> disponiveis = hotelService.listarQuartosDisponiveis();
        
        if (disponiveis.isEmpty()) {
            System.out.println("Desculpe, todos os quartos estão ocupados no momento!");
            return;
        }

        for (Quarto q : disponiveis) {
            System.out.println("Quarto " + q.getNumero() + " | " + q.getTipo() + " | R$ " + q.getPrecoPorNoite());
        }
    }

    private static void efetuarReserva() {
        System.out.println("\n--- REALIZAR RESERVA ---");
        System.out.print("Número do quarto desejado: ");
        int numeroQuarto = teclado.nextInt(); 
        teclado.nextLine();
        
        Quarto quarto = hotelService.buscarQuarto(numeroQuarto);
        if (quarto == null) {
            System.out.println("Quarto não existe.");
            return;
        }
            System.out.print("Data de Chegada (dd/mm/aaaa): ");
            String textoEntrada = teclado.nextLine();
            LocalDate dataEntrada = LocalDate.parse(textoEntrada, formatador);

            System.out.print("Data de Saída (dd/mm/aaaa): ");
            String textoSaida = teclado.nextLine();
            LocalDate dataSaida = LocalDate.parse(textoSaida, formatador); 
        if (dataEntrada.isBefore(dataSaida)) {
            hotelService.realizarReserva(usuarioLogado, quarto, dataEntrada, dataSaida);
            System.out.println("Sucesso! Sua reserva foi confirmada.");
        } else {
        	System.out.println("Erro! Sua reserva não foi realizada, data invalida.");
        }
    }
    
    private static void gerenciarReservas() {
        System.out.println("\n--- MINHAS RESERVAS ---");
        List<Reserva> minhas = hotelService.listarReservasDoUsuario(usuarioLogado);
        
        if (minhas.isEmpty()) {
            System.out.println("Você não tem nenhuma reserva.");
            return;
        }

        for (Reserva r : minhas) {
            // Ajustado para formatar e mostrar o período bonitinho na listagem (dd/MM/yyyy)
            String entradaFormatada = r.getDataEntrada().format(formatador);
            String saidaFormatada = r.getDataSaida().format(formatador);
            
            System.out.println(" | ID: " + r.getId() + 
            				   " | Quarto: " + r.getQuarto().getNumero() + 
                               " | Período: " + entradaFormatada + " até " + saidaFormatada + 
                               " | Status: " + r.getStatus());
        }

        System.out.print("\nDeseja cancelar alguma reserva? (S/N): ");
        String resposta = teclado.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            System.out.print("Digite o ID da reserva para cancelar: ");
            int id = teclado.nextInt(); 
            teclado.nextLine();

            if (hotelService.cancelarReserva(id, usuarioLogado)) {
                System.out.println("Reserva cancelada com sucesso!");
            } else {
                System.out.println("Não foi possível cancelar. Verifique o ID.");
            }
        }
    }
    
    private static void menuCadastrarQuarto() {
        System.out.println("\n--- CADASTRAR QUARTO ---");
        System.out.print("Número do Quarto: ");
        int numero = teclado.nextInt(); 
        teclado.nextLine();
        System.out.println("Tipos permitidos: [Solteiro], [Casal] ou [Suíte]");
        System.out.print("Digite o tipo: ");
        String tipo = teclado.nextLine();
        System.out.print("Preço da Diária: R$ ");
        double preco = teclado.nextDouble();
        teclado.nextLine();

        if (hotelService.cadastrarNovoQuarto(usuarioLogado, numero, tipo, preco)) {
            System.out.println("Quarto cadastrado com sucesso!");
        } else {
            System.out.println("Erro: Quarto já existe ou você não é ADMIN.");
        }
    }
}