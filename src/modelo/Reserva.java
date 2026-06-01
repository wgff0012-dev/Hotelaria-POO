package modelo;

public class Reserva {
    private int id;
    private Usuario hospede;
    private Quarto quarto;
    private String status; // "ATIVA", "CANCELADA"

    public Reserva(int id, Usuario hospede, Quarto quarto) {
        this.id = id;
        this.hospede = hospede;
        this.quarto = quarto;
        this.status = "ATIVA";
    }

    public int getId() { return id; }
    public Usuario getHospede() { return hospede; }
    public Quarto getQuarto() { return quarto; }
    public String getStatus() { return status; }
    
    public void cancelar() {
        this.status = "CANCELADA";
    }
}