package aplicacion.prestamista.prestamistaprojecttrabajador.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Clase que se encarga de modelar un objeto cliente para representarlos en la app
 * @author Julián Salgado
 * @version 1.0
 */
@IgnoreExtraProperties
public class Cliente  {
    private String clienteId;
    private String nombre;
    private String telefono;
    private String direccion;
    private Date fecha;
    private double valorPrestado;
    private double valorCuota;
    private int numeroCuotas;
    private List<Cuota> cuotas;

    public Cliente(String clienteId, String nombre) {
        this.clienteId = clienteId;
        this.nombre = nombre;
    }

    public static Builder builder(String id, String nombre) {
        return new Builder(id, nombre);
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

    public List<Cuota> getCuotas() {
        return cuotas;
    }

    /**
     * Clase que se encarga de construir un objeto cliente, hace referencia a un patrón de diseño
     * llamado patrón Builder
     */
    public static class Builder {

        private Cliente cliente;

        private Builder(String id, String nombre) {
            this.cliente = new Cliente(id, nombre);
        }

        public Builder nombre(String nombre) {
            this.cliente.nombre = nombre;
            return this;
        }

        public Builder telefono(String telefono) {
            this.cliente.telefono = telefono;
            return this;
        }

        public Builder direccion(String direccion) {
            this.cliente.direccion = direccion;
            return this;
        }

        public Builder fecha(Date fecha) {
            this.cliente.fecha = fecha;
            return this;
        }

        public Builder valorPrestado(double valorPrestado) {
            this.cliente.valorPrestado = valorPrestado;
            return this;
        }

        public Builder valorCuota(double valorCuota) {
            this.cliente.valorCuota = valorCuota;
            return this;
        }

        public Builder numeroCuotas(int numeroCuotas) {
            this.cliente.numeroCuotas = numeroCuotas;
            return this;
        }

        public Builder cuotas(List<Cuota> cuotas){
            this.cliente.cuotas = cuotas;
            return this;
        }

        public Cliente build() {
            return this.cliente;
        }
    }
}
