/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entitys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "orden_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenCompra.findAll", query = "SELECT o FROM OrdenCompra o"),
    @NamedQuery(name = "OrdenCompra.findByIdOrdenCompra", query = "SELECT o FROM OrdenCompra o WHERE o.idOrdenCompra = :idOrdenCompra"),
    @NamedQuery(name = "OrdenCompra.findByNumeroOrden", query = "SELECT o FROM OrdenCompra o WHERE o.numeroOrden = :numeroOrden"),
    @NamedQuery(name = "OrdenCompra.findByFechaSolicitud", query = "SELECT o FROM OrdenCompra o WHERE o.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "OrdenCompra.findByFechaProcenvio", query = "SELECT o FROM OrdenCompra o WHERE o.fechaProcenvio = :fechaProcenvio"),
    @NamedQuery(name = "OrdenCompra.findByFechaEntrega", query = "SELECT o FROM OrdenCompra o WHERE o.fechaEntrega = :fechaEntrega"),
    @NamedQuery(name = "OrdenCompra.findByFechaRecepcion", query = "SELECT o FROM OrdenCompra o WHERE o.fechaRecepcion = :fechaRecepcion"),
    @NamedQuery(name = "OrdenCompra.findByNomProyecto", query = "SELECT o FROM OrdenCompra o WHERE o.nomProyecto = :nomProyecto"),
    @NamedQuery(name = "OrdenCompra.findByReferenciaCompra", query = "SELECT o FROM OrdenCompra o WHERE o.referenciaCompra = :referenciaCompra"),
    @NamedQuery(name = "OrdenCompra.findByCondicionPago", query = "SELECT o FROM OrdenCompra o WHERE o.condicionPago = :condicionPago"),
    @NamedQuery(name = "OrdenCompra.findByComprobantePago", query = "SELECT o FROM OrdenCompra o WHERE o.comprobantePago = :comprobantePago"),
    @NamedQuery(name = "OrdenCompra.findByMoneda", query = "SELECT o FROM OrdenCompra o WHERE o.moneda = :moneda"),
    @NamedQuery(name = "OrdenCompra.findByGarantia", query = "SELECT o FROM OrdenCompra o WHERE o.garantia = :garantia")})
public class OrdenCompra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idOrden_Compra")
    private Integer idOrdenCompra;
    @Basic(optional = false)
    @Column(name = "numero_orden")
    private String numeroOrden;
    @Basic(optional = false)
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Column(name = "fecha_procenvio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProcenvio;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "fecha_recepcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecepcion;
    @Basic(optional = false)
    @Column(name = "nom_proyecto")
    private String nomProyecto;
    @Basic(optional = false)
    @Column(name = "referencia_compra")
    private String referenciaCompra;
    @Basic(optional = false)
    @Column(name = "condicion_pago")
    private String condicionPago;
    @Basic(optional = false)
    @Column(name = "comprobante_pago")
    private String comprobantePago;
    @Column(name = "moneda")
    private String moneda;
    @Column(name = "garantia")
    private String garantia;
    @JoinColumn(name = "idElaborador", referencedColumnName = "idTrabajador")
    @ManyToOne(optional = false)
    private Trabajador idElaborador;
    @JoinColumn(name = "idSolicitante", referencedColumnName = "idTrabajador")
    @ManyToOne(optional = false)
    private Trabajador idSolicitante;
    @JoinColumn(name = "idContacto", referencedColumnName = "idContacto")
    @ManyToOne(optional = false)
    private Contacto idContacto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOrdenCompra")
    private List<DetalleOrden> detalleOrdenList;

    public OrdenCompra() {
    }

    public OrdenCompra(Integer idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public OrdenCompra(Integer idOrdenCompra, String numeroOrden, Date fechaSolicitud, String nomProyecto, String referenciaCompra, String condicionPago, String comprobantePago) {
        this.idOrdenCompra = idOrdenCompra;
        this.numeroOrden = numeroOrden;
        this.fechaSolicitud = fechaSolicitud;
        this.nomProyecto = nomProyecto;
        this.referenciaCompra = referenciaCompra;
        this.condicionPago = condicionPago;
        this.comprobantePago = comprobantePago;
    }

    public Integer getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Integer idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaProcenvio() {
        return fechaProcenvio;
    }

    public void setFechaProcenvio(Date fechaProcenvio) {
        this.fechaProcenvio = fechaProcenvio;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getNomProyecto() {
        return nomProyecto;
    }

    public void setNomProyecto(String nomProyecto) {
        this.nomProyecto = nomProyecto;
    }

    public String getReferenciaCompra() {
        return referenciaCompra;
    }

    public void setReferenciaCompra(String referenciaCompra) {
        this.referenciaCompra = referenciaCompra;
    }

    public String getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        this.condicionPago = condicionPago;
    }

    public String getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(String comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public Trabajador getIdElaborador() {
        return idElaborador;
    }

    public void setIdElaborador(Trabajador idElaborador) {
        this.idElaborador = idElaborador;
    }

    public Trabajador getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(Trabajador idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public Contacto getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Contacto idContacto) {
        this.idContacto = idContacto;
    }

    @XmlTransient
    public List<DetalleOrden> getDetalleOrdenList() {
        return detalleOrdenList;
    }

    public void setDetalleOrdenList(List<DetalleOrden> detalleOrdenList) {
        this.detalleOrdenList = detalleOrdenList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrdenCompra != null ? idOrdenCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenCompra)) {
            return false;
        }
        OrdenCompra other = (OrdenCompra) object;
        if ((this.idOrdenCompra == null && other.idOrdenCompra != null) || (this.idOrdenCompra != null && !this.idOrdenCompra.equals(other.idOrdenCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.OrdenCompra[ idOrdenCompra=" + idOrdenCompra + " ]";
    }
    
}
