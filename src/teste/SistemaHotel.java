package teste;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import modelo.HotelService;
import modelo.Quarto;
import modelo.Reserva;
import modelo.Usuario;

// ==========================================
// 3. CAMADA DE VISÃO (MENU INTERATIVO)
// ==========================================

public class SistemaHotel {
    private static HotelService hotelService = new HotelService();
    private static Usuario usuarioLogado = null; // Controle de sessão
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n--- SISTEMA DE RESERVAS DE HOTEL ---");
            if (usuarioLogado == null) {
                System.out.println("1. Cadastrar Conta");
                System.out.println("2. Fazer Login");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1 -> menuCadastro();
                    case 2 -> menuLogin();
                    case 0 -> System.out.println("Encerrando o sistema...");
                    default -> System.out.println("Opção inválida!");
                }
            } else {
                System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipo() + ")!");
                
                // --- MENU EXCLUSIVO PARA ADMIN ---
                if (usuarioLogado.getTipo().equalsIgnoreCase("ADMIN")) {
                    System.out.println("1. Consultar Disponibilidade de Quartos por Data");
                    System.out.println("2. Cadastrar Novo Quarto");
                    System.out.println("3. Fazer Logout");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    switch (opcao) {
                        case 1 -> consultarDisponibilidade();
                        case 2 -> menuCadastrarQuarto();
                        case 3 -> {
                            usuarioLogado = null;
                            System.out.println("Logout realizado com sucesso.");
                        }
                        case 0 -> System.out.println("Encerrando o sistema...");
                        default -> System.out.println("Opção inválida!");
                    }
                } 
                // --- MENU EXCLUSIVO PARA HÓSPEDE ---
                else {
                    System.out.println("1. Consultar Disponibilidade de Quartos por Data");
                    System.out.println("2. Realizar uma Reserva");
                    System.out.println("3. Minhas Reservas e Cancelamentos");
                    System.out.println("4. Fazer Logout");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    switch (opcao) {
                        case 1 -> consultarDisponibilidade();
                        case 2 -> efetuarReserva();
                        case 3 -> gerenciarReservas();
                        case 4 -> {
                            usuarioLogado = null;
                            System.out.println("Logout realizado com sucesso.");
                        }
                        case 0 -> System.out.println("Encerrando o sistema...");
                        default -> System.out.println("Opção inválida!");
                    }
                }
            }
        } while (opcao != 0);
    }

    
// Menu Cadastro
    
    private static void menuCadastro() {
        System.out.println("\n--- CADASTRO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (hotelService.cadastrarUsuario(nome, email, senha)) {
            System.out.println("Cadastro realizado com sucesso! Faça o login.");
        } else {
            System.out.println("Erro: Este email já está cadastrado.");
        }
    }
    
 // Menu Login
    
    private static void menuLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        usuarioLogado = hotelService.fazerLogin(email, senha); // Inicia sessão

        if (usuarioLogado != null) {
            System.out.println("Login efetuado com sucesso!");
        } else {
            System.out.println("Email ou senha incorretos.");
        }
    }
    
// Consultar Disponibilidade

    private static void consultarDisponibilidade() {
        System.out.println("\n--- CONSULTAR DISPONIBILIDADE POR PERÍODO ---");
        try {
            System.out.print("Data de Chegada Pretendida (dd/mm/aaaa): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine(), formatter);
            System.out.print("Data de Saída Pretendida (dd/mm/aaaa): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine(), formatter);

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                System.out.println("Erro: A data de check-out deve ser posterior ao check-in.");
                return;
            }

            List<Quarto> todos = hotelService.listarTodosOsQuartos();
            System.out.println("\nResultado para o período de " + checkIn.format(formatter) + " até " + checkOut.format(formatter) + ":");
            
            boolean encontrouAlgum = false;
            for (Quarto q : todos) {
                if (hotelService.verificarDisponibilidadeReal(q, checkIn, checkOut)) {
                    System.out.println("Quarto Nº: " + q.getNumero() + " | Tipo: " + q.getTipo() + " | Diária: R$ " + q.getPrecoPorNoite());
                    encontrouAlgum = true;
                }
            }
            if (!encontrouAlgum) {
                System.out.println("Infelizmente não há quartos vagos neste período.");
            }
        } catch (Exception e) {
            System.out.println("Erro no formato das datas. Use dd/MM/aaaa.");
        }
    }

// Efetuar Reserva
    
    private static void efetuarReserva() {
        System.out.println("\n--- REALIZAR RESERVA ---");
        System.out.print("Digite o número do quarto desejado: ");
        int numeroQuarto = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        
        Quarto quarto = hotelService.buscarQuarto(numeroQuarto);
        if (quarto == null) {
            System.out.println("Quarto não encontrado no sistema.");
            return;
        }

        try {
            System.out.print("Data de Check-in (dd/mm/aaaa): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine(), formatter);
            System.out.print("Data de Check-out (dd/mm/aaaa): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine(), formatter);

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                System.out.println("Erro: A data de check-out deve ser posterior ao check-in.");
                return;
            }

            Reserva reserva = hotelService.realizarReserva(usuarioLogado, quarto, checkIn, checkOut);
            if (reserva != null) {
                System.out.println("Reserva realizada com sucesso! Código da Reserva: " + reserva.getId());
            } else {
                System.out.println("Não foi possível finalizar. O quarto já está reservado por outro hóspede nesse período.");
            }
        } catch (Exception e) {
            System.out.println("Erro no formato das datas. Operação cancelada.");
        }
    }
    
// Gerenciar Reservas

    private static void gerenciarReservas() {
        System.out.println("\n--- MINHAS RESERVAS ---");
        List<Reserva> minhas = hotelService.listarReservasDoUsuario(usuarioLogado);
        
        if (minhas.isEmpty()) {
            System.out.println("Você não possui nenhuma reserva registrada.");
            return;
        }

        for (Reserva r : minhas) {
            System.out.println("Reserva ID: " + r.getId() + 
                               " | Quarto: " + r.getQuarto().getNumero() + " (" + r.getQuarto().getTipo() + ")" +
                               " | Período: " + r.getCheckIn().format(formatter) + " a " + r.getCheckOut().format(formatter) +
                               " | Status: " + r.getStatus());
        }

        System.out.print("\nDeseja cancelar alguma reserva ativa? (S/N): ");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            System.out.print("Digite o ID da reserva para cancelar: ");
            int idCancelamento = scanner.nextInt();
            scanner.nextLine();

            if (hotelService.cancelarReserva(idCancelamento, usuarioLogado)) {
                System.out.println("Reserva cancelada com sucesso. O quarto está livre para esse período.");
            } else {
                System.out.println("Não foi possível cancelar. Verifique o ID ou se a reserva já foi cancelada.");
            }
        }
    }
    
 // Menu de Administrador - Cadastrar Quarto

    private static void menuCadastrarQuarto() {
        System.out.println("\n--- [ADMIN] CADASTRAR NOVO QUARTO ---");
        System.out.print("Número do Quarto: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        System.out.print("Tipo do Quarto (ex: Solteiro, Casal, Suíte): ");
        String tipoQuarto = scanner.nextLine();

        System.out.print("Preço da Diária: R$ ");
        double precoDiaria = scanner.nextDouble();
        scanner.nextLine(); // Limpar buffer

        hotelService.cadastrarNovoQuarto(usuarioLogado, numero, tipoQuarto, precoDiaria);
    }
}