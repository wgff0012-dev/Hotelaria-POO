package modelo;

import java.time.LocalDate; 

public class Reserva {
     int id;
     Usuario hospede;
     Quarto quarto;
     LocalDate dataEntrada; 
     LocalDate dataSaida;   
     String status; 

    public Reserva(int id, Usuario hospede, Quarto quarto, LocalDate dataEntrada, LocalDate dataSaida) {
        this.id = id;
        this.hospede = hospede;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.status = "ATIVA";
    }

    public int getId() { return id; }
    public Usuario getHospede() { return hospede; }
    public Quarto getQuarto() { return quarto; }
    public LocalDate getDataEntrada() { return dataEntrada; } 
    public LocalDate getDataSaida() { return dataSaida; }     
    public String getStatus() { return status; }
    
    public void cancelar() {
        this.status = "CANCELADA";
    }
}