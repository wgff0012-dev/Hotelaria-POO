package modelo;

public class Quarto {
     int numero;
     String tipo; // [Solteiro], [Casal] ou [Suíte]
     double precoPorNoite;

    public Quarto(int numero, String tipo, double precoPorNoite) {
        this.numero = numero;
        this.tipo = tipo;
        this.precoPorNoite = precoPorNoite;
    }

    public int getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public double getPrecoPorNoite() { return precoPorNoite; }
}