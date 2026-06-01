package modelo;

import java.util.ArrayList;
import java.util.List;

public class HotelService {
    List<Usuario> usuarios = new ArrayList<>();
    List<Quarto> quartos = new ArrayList<>();
    List<Reserva> reservas = new ArrayList<>();
    
    int proximoIdUsuario = 1;
    int proximoIdReserva = 1;

    public HotelService() {
        quartos.add(new Quarto(101, "Solteiro", 150.0));
        quartos.add(new Quarto(102, "Casal", 250.0));
        quartos.add(new Quarto(201, "Suíte", 500.0));

        usuarios.add(new Usuario(proximoIdUsuario++, "Admin", "admin@hotel.com", "admin123", "ADMIN"));
    }

    public boolean cadastrarUsuario(String nome, String email, String senha) {
        usuarios.add(new Usuario(proximoIdUsuario++, nome, email, senha, "HOSPEDE"));
        return true;
    }

    public Usuario fazerLogin(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    public List<Quarto> listarTodosOsQuartos() {
        return quartos;
    }

    public Quarto buscarQuarto(int numero) {
        for (Quarto q : quartos) {
            if (q.getNumero() == numero) return q;
        }
        return null;
    }

    public boolean realizarReserva(Usuario usuario, Quarto quarto) {
        // 1. TESTE DE OCUPAÇÃO (Olha se o quarto já está reservado)
        for (Reserva r : reservas) {
            if (r.getQuarto().getNumero() == quarto.getNumero() && r.getStatus().equals("ATIVA")) {
                System.out.println("[ERRO] Esse quarto já está ocupado!");
                return false; 
            }
        }
        Reserva novaReserva = new Reserva(proximoIdReserva++, usuario, quarto);
        reservas.add(novaReserva);
        return true;
    }

    public List<Reserva> listarReservasDoUsuario(Usuario usuario) {
        List<Reserva> minhasReservas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getHospede().getId() == usuario.getId()) {
                minhasReservas.add(r);
            }
        }
        return minhasReservas;
    }

    public boolean cancelarReserva(int idReserva, Usuario usuario) {
        for (Reserva r : reservas) {
            if (r.getId() == idReserva && r.getHospede().getId() == usuario.getId() && r.getStatus().equals("ATIVA")) {
                r.cancelar(); 
                return true;
            }
        }
        return false;
    }
    
    public boolean cadastrarNovoQuarto(Usuario usuarioLogado, int numero, String tipo, double precoDiaria) {
        if (usuarioLogado == null || !usuarioLogado.getTipo().equalsIgnoreCase("ADMIN")) {
            return false; 
        }
        if (buscarQuarto(numero) != null) {
            return false; 
        }
        quartos.add(new Quarto(numero, tipo, precoDiaria));
        return true;
    }
}