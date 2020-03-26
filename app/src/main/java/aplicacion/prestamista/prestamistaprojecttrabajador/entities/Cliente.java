package aplicacion.prestamista.prestamistaprojecttrabajador.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

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
    //private Cuota[] cuotas;

    public Cliente(String clienteId, String nombre) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        //this.cuotas = new Cuota[numeroCuotas];
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

        public Cliente build() {
            return this.cliente;
        }
    }
}
