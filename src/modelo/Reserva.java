package modelo;

import java.time.LocalDate;

public class Reserva {
    private int id;
    private Usuario hospede;
    private Quarto quarto;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status; // "ATIVA", "CANCELADA"

    public Reserva(int id, Usuario hospede, Quarto quarto, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.hospede = hospede;
        this.quarto = quarto;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = "ATIVA";
    }

    public int getId() { return id; }
    public Usuario getHospede() { return hospede; }
    public Quarto getQuarto() { return quarto; }
    public String getStatus() { return status; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    
    public void cancelar() {
        this.status = "CANCELADA";
    }
}