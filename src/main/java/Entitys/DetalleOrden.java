/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entitys;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "detalle_orden")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleOrden.findAll", query = "SELECT d FROM DetalleOrden d"),
    @NamedQuery(name = "DetalleOrden.findByIdDetalleOrden", query = "SELECT d FROM DetalleOrden d WHERE d.idDetalleOrden = :idDetalleOrden"),
    @NamedQuery(name = "DetalleOrden.findByGarantia", query = "SELECT d FROM DetalleOrden d WHERE d.garantia = :garantia"),
    @NamedQuery(name = "DetalleOrden.findByUnidad", query = "SELECT d FROM DetalleOrden d WHERE d.unidad = :unidad"),
    @NamedQuery(name = "DetalleOrden.findByCantidad", query = "SELECT d FROM DetalleOrden d WHERE d.cantidad = :cantidad")})
public class DetalleOrden implements Serializable {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precioorden")
    private Double precioorden;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalle_Orden")
    private Integer idDetalleOrden;
    @Column(name = "garantia")
    private String garantia;
    @Column(name = "unidad")
    private String unidad;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Producto idProducto;
    @JoinColumn(name = "idOrden_Compra", referencedColumnName = "idOrden_Compra")
    @ManyToOne(optional = false)
    private OrdenCompra idOrdenCompra;

    public DetalleOrden() {
    }

    public DetalleOrden(Integer idDetalleOrden) {
        this.idDetalleOrden = idDetalleOrden;
    }

    public DetalleOrden(Integer idDetalleOrden, int cantidad) {
        this.idDetalleOrden = idDetalleOrden;
        this.cantidad = cantidad;
    }

    public Integer getIdDetalleOrden() {
        return idDetalleOrden;
    }

    public void setIdDetalleOrden(Integer idDetalleOrden) {
        this.idDetalleOrden = idDetalleOrden;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public OrdenCompra getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(OrdenCompra idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleOrden != null ? idDetalleOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleOrden)) {
            return false;
        }
        DetalleOrden other = (DetalleOrden) object;
        if ((this.idDetalleOrden == null && other.idDetalleOrden != null) || (this.idDetalleOrden != null && !this.idDetalleOrden.equals(other.idDetalleOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.DetalleOrden[ idDetalleOrden=" + idDetalleOrden + " ]";
    }

    public Double getPrecioorden() {
        return precioorden;
    }

    public void setPrecioorden(double precioorden) {
        this.precioorden = precioorden;
    }
    
}
