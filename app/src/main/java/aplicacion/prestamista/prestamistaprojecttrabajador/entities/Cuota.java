package aplicacion.prestamista.prestamistaprojecttrabajador.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Cuota {

    private String cuotaId;
    private Date fecha;
    private double valorPagado;

    public Cuota(String cuotaId, Date fecha, double valorPagado) {
        this.cuotaId = cuotaId;
        this.fecha = fecha;
        this.valorPagado = valorPagado;
    }

    public String getCuotaId() {
        return cuotaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getValorPagado() {
        return valorPagado;
    }
}
