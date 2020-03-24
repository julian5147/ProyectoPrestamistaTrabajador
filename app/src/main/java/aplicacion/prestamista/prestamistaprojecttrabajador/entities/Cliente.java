package aplicacion.prestamista.prestamistaprojecttrabajador.entities;

import java.util.Date;

public class Cliente {
    private String clienteId;
    private String nombre;
    private String telefono;
    private String direccion;
    private Date fecha;
    private double valorPrestado;
    private double valorCuota;
    private int numeroCuotas;
    private Cuota[] cuotas;

    public Cliente(String clienteId, String nombre, String telefono, String direccion, Date fecha, double valorPrestado, double valorCuota, int numeroCuotas) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fecha = fecha;
        this.valorPrestado = valorPrestado;
        this.valorCuota = valorCuota;
        this.numeroCuotas = numeroCuotas;
        this.cuotas = new Cuota[numeroCuotas];
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getValorPrestado() {
        return valorPrestado;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }
}
