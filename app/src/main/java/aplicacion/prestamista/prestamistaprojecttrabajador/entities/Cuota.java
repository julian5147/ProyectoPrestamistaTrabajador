package aplicacion.prestamista.prestamistaprojecttrabajador.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Clase que se encarga de representar un objeto cuota, el cual cada cliente tiene varias de estás
 * @author Julián Salgado
 * @version 1.0
 */
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
