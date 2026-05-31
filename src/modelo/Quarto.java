package modelo;

public class Quarto {
    private int numero;
    private String tipo;
    private double precoPorNoite;

    public Quarto(int numero, String tipo, double precoPorNoite) {
        this.numero = numero;
        this.tipo = tipo;
        this.precoPorNoite = precoPorNoite;
    }

    public int getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public double getPrecoPorNoite() { return precoPorNoite; }
}