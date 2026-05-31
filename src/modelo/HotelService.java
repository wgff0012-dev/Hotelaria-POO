package modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

// ==========================================
// 2. CAMADA DE LÓGICA / SERVIÇO
// ==========================================

public class HotelService {
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Quarto> quartos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private int proximoIdUsuario = 1;
    private int proximoIdReserva = 1;

    public HotelService() {
        // Carga inicial de dados para teste (Quartos cadastrados)
        quartos.add(new Quarto(101, "Solteiro", 150.0));
        quartos.add(new Quarto(102, "Casal", 250.0));
        quartos.add(new Quarto(201, "Suíte", 500.0));
        
        // Criando um usuário administrador padrão
        usuarios.add(new Usuario(proximoIdUsuario++, "Admin", "admin@hotel.com", "admin123", "ADMIN"));
    }

    // Cadastro de Usuário
    public boolean cadastrarUsuario(String nome, String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) return false; // Email já existe
        }
        usuarios.add(new Usuario(proximoIdUsuario++, nome, email, senha, "HOSPEDE"));
        return true;
    }

    // Autenticação (Login)
    public Usuario fazerLogin(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u; // Retorna o usuário logado para controle de sessão
            }
        }
        return null;
    }

    // Buscar todos os quartos cadastrados
    public List<Quarto> listarTodosOsQuartos() {
        return new ArrayList<>(quartos);
    }

    // Verificar se um quarto específico possui choque de datas com alguma reserva ATIVA
    public boolean verificarDisponibilidadeReal(Quarto quarto, LocalDate checkIn, LocalDate checkOut) {
        for (Reserva r : reservas) {
            if (r.getQuarto().getNumero() == quarto.getNumero() && r.getStatus().equals("ATIVA")) {
                // Se o período solicitado sobrepõe uma reserva existente, retorna falso
                if (!(checkOut.isBefore(r.getCheckIn()) || checkIn.isAfter(r.getCheckOut()))) {
                    return false; 
                }
            }
        }
        return true; // Livre para o período informado
    }

    // Buscar quarto por número
    public Quarto buscarQuarto(int numero) {
        for (Quarto q : quartos) {
            if (q.getNumero() == numero) return q;
        }
        return null;
    }

    // Realizar Reserva com checagem dinâmica de datas
    public Reserva realizarReserva(Usuario usuario, Quarto quarto, LocalDate checkIn, LocalDate checkOut) {
        if (verificarDisponibilidadeReal(quarto, checkIn, checkOut)) {
            Reserva novaReserva = new Reserva(proximoIdReserva++, usuario, quarto, checkIn, checkOut);
            reservas.add(novaReserva);
            
            // Simulação de Serviço de Notificação [exigência do roteiro]
            System.out.println("\n[NOTIFICAÇÃO] Email enviado para " + usuario.getEmail() + ": Reserva #" + novaReserva.getId() + " confirmada com sucesso!");
            return novaReserva;
        }
        return null;
    }

    // Listar reservas do usuário logado
    public List<Reserva> listarReservasDoUsuario(Usuario usuario) {
        List<Reserva> minhasReservas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getHospede().getId() == usuario.getId()) {
                minhasReservas.add(r);
            }
        }
        return minhasReservas;
    }

    // Cancelar Reserva
    public boolean cancelarReserva(int idReserva, Usuario usuario) {
        for (Reserva r : reservas) {
            if (r.getId() == idReserva && r.getHospede().getId() == usuario.getId() && r.getStatus().equals("ATIVA")) {
                r.cancelar();
                return true;
            }
        }
        return false;
    }
    
 // Cadastro de Novo Quarto (Apenas para ADMIN)
    public boolean cadastrarNovoQuarto(Usuario usuarioLogado, int numero, String tipo, double precoDiaria) {
        // 1. Validação de Segurança: Verifica se o usuário está logado e é ADMIN
        if (usuarioLogado == null || !usuarioLogado.getTipo().equalsIgnoreCase("ADMIN")) {
            System.out.println("[ERRO] Acesso negado. Apenas administradores podem cadastrar quartos.");
            return false;
        }

        // 2. Validação de Regra de Negócio: Evita duplicar quartos com o mesmo número
        if (buscarQuarto(numero) != null) {
            System.out.println("[ERRO] Já existe um quarto cadastrado com o número " + numero + ".");
            return false;
        }

        // 3. Se passou nas validações, adiciona o quarto
        quartos.add(new Quarto(numero, tipo, precoDiaria));
        System.out.println("[SUCESSO] Quarto " + numero + " (" + tipo + ") cadastrado com sucesso!");
        return true;
    }
}
