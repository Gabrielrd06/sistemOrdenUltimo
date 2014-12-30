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
@Table(name = "contacto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contacto.findAll", query = "SELECT c FROM Contacto c"),
    @NamedQuery(name = "Contacto.findByIdContacto", query = "SELECT c FROM Contacto c WHERE c.idContacto = :idContacto"),
    @NamedQuery(name = "Contacto.findByNombre", query = "SELECT c FROM Contacto c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Contacto.findByTelFijo", query = "SELECT c FROM Contacto c WHERE c.telFijo = :telFijo"),
    @NamedQuery(name = "Contacto.findByCelMovistar", query = "SELECT c FROM Contacto c WHERE c.celMovistar = :celMovistar"),
    @NamedQuery(name = "Contacto.findByCelClaro", query = "SELECT c FROM Contacto c WHERE c.celClaro = :celClaro"),
    @NamedQuery(name = "Contacto.findByCelNextel", query = "SELECT c FROM Contacto c WHERE c.celNextel = :celNextel"),
    @NamedQuery(name = "Contacto.findByEmail", query = "SELECT c FROM Contacto c WHERE c.email = :email"),
    @NamedQuery(name = "Contacto.findByProveedor", query = "SELECT c FROM Contacto c WHERE c.idProveedor = :proveedor")})
public class Contacto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idContacto")
    private Integer idContacto;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "tel_fijo")
    private String telFijo;
    @Column(name = "cel_movistar")
    private String celMovistar;
    @Column(name = "cel_claro")
    private String celClaro;
    @Column(name = "cel_nextel")
    private String celNextel;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "idProveedor", referencedColumnName = "idProveedor")
    @ManyToOne(optional = false)
    private Proveedor idProveedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContacto")
    private List<OrdenCompra> ordenCompraList;

    public Contacto() {
    }

    public Contacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Contacto(Integer idContacto, String nombre, String email) {
        this.idContacto = idContacto;
        this.nombre = nombre;
        this.email = email;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelFijo() {
        return telFijo;
    }

    public void setTelFijo(String telFijo) {
        this.telFijo = telFijo;
    }

    public String getCelMovistar() {
        return celMovistar;
    }

    public void setCelMovistar(String celMovistar) {
        this.celMovistar = celMovistar;
    }

    public String getCelClaro() {
        return celClaro;
    }

    public void setCelClaro(String celClaro) {
        this.celClaro = celClaro;
    }

    public String getCelNextel() {
        return celNextel;
    }

    public void setCelNextel(String celNextel) {
        this.celNextel = celNextel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    @XmlTransient
    public List<OrdenCompra> getOrdenCompraList() {
        return ordenCompraList;
    }

    public void setOrdenCompraList(List<OrdenCompra> ordenCompraList) {
        this.ordenCompraList = ordenCompraList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContacto != null ? idContacto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.idContacto == null && other.idContacto != null) || (this.idContacto != null && !this.idContacto.equals(other.idContacto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Contacto[ idContacto=" + idContacto + " ]";
    }
    
}
