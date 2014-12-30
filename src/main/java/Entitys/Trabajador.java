/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entitys;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "trabajador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabajador.findAll", query = "SELECT t FROM Trabajador t"),
    @NamedQuery(name = "Trabajador.findByIdTrabajador", query = "SELECT t FROM Trabajador t WHERE t.idTrabajador = :idTrabajador"),
    @NamedQuery(name = "Trabajador.findByNombres", query = "SELECT t FROM Trabajador t WHERE t.nombres = :nombres"),
    @NamedQuery(name = "Trabajador.findByApellidos", query = "SELECT t FROM Trabajador t WHERE t.apellidos = :apellidos"),
    @NamedQuery(name = "Trabajador.findByDni", query = "SELECT t FROM Trabajador t WHERE t.dni = :dni"),
    @NamedQuery(name = "Trabajador.findByCelularCorp", query = "SELECT t FROM Trabajador t WHERE t.celularCorp = :celularCorp"),
    @NamedQuery(name = "Trabajador.findByCelularPers", query = "SELECT t FROM Trabajador t WHERE t.celularPers = :celularPers"),
    @NamedQuery(name = "Trabajador.findByResponsable", query = "SELECT t FROM Trabajador t WHERE t.responsable = :responsable"),
    @NamedQuery(name = "Trabajador.findByArea", query = "SELECT t FROM Trabajador t WHERE t.idArea = :idArea"),
    @NamedQuery(name = "Trabajador.findByResponsablexArea", query = "SELECT t FROM Trabajador t WHERE t.idArea = :idArea and t.responsable = 1")})
public class Trabajador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTrabajador")
    private Integer idTrabajador;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "dni")
    private String dni;
    @Column(name = "celular_corp")
    private String celularCorp;
    @Column(name = "celular_pers")
    private String celularPers;
    @Basic(optional = false)
    @Column(name = "responsable")
    private boolean responsable;
    @JoinColumn(name = "idArea", referencedColumnName = "idArea")
    @ManyToOne(optional = false)
    private Area idArea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idElaborador")
    private List<OrdenCompra> ordenCompraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSolicitante")
    private List<OrdenCompra> ordenCompraList1;

    public Trabajador() {
    }

    public Trabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Trabajador(Integer idTrabajador, String nombres, String apellidos, String dni, boolean responsable) {
        this.idTrabajador = idTrabajador;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.responsable = responsable;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCelularCorp() {
        return celularCorp;
    }

    public void setCelularCorp(String celularCorp) {
        this.celularCorp = celularCorp;
    }

    public String getCelularPers() {
        return celularPers;
    }

    public void setCelularPers(String celularPers) {
        this.celularPers = celularPers;
    }

    public boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(boolean responsable) {
        this.responsable = responsable;
    }

    public Area getIdArea() {
        return idArea;
    }

    public void setIdArea(Area idArea) {
        this.idArea = idArea;
    }

    @XmlTransient
    public List<OrdenCompra> getOrdenCompraList() {
        return ordenCompraList;
    }

    public void setOrdenCompraList(List<OrdenCompra> ordenCompraList) {
        this.ordenCompraList = ordenCompraList;
    }

    @XmlTransient
    public List<OrdenCompra> getOrdenCompraList1() {
        return ordenCompraList1;
    }

    public void setOrdenCompraList1(List<OrdenCompra> ordenCompraList1) {
        this.ordenCompraList1 = ordenCompraList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTrabajador != null ? idTrabajador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajador)) {
            return false;
        }
        Trabajador other = (Trabajador) object;
        if ((this.idTrabajador == null && other.idTrabajador != null) || (this.idTrabajador != null && !this.idTrabajador.equals(other.idTrabajador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Trabajador[ idTrabajador=" + idTrabajador + " ]";
    }
    
}
