package Reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;

public class Datos {
    protected Connection conexion = null;
    protected ResultSet rs = null;
    protected CachedRowSet crs = null;

    protected void Abrir() throws Exception
    {
        try {     
                String servidor = "127.0.0.1";
                String puerto = "3036";
                String basedatos = "bd_tactical";
                String usuario = "mysql";
                String contrasena = "root";
                String cadenaconexion = "jdbc:mysql://" + servidor + ":" + puerto + "/" + basedatos+"/?allowMultiQueries=on";                
                conexion = DriverManager.getConnection(cadenaconexion,usuario,contrasena);
                
        }
        catch(Exception e) {
            throw e;
        }
    }

    protected boolean EstaCerrada() throws Exception {
        try {
           if(conexion!=null) {
                return conexion.isClosed();
           }
           else {
               return true;
           }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void Cerrar() throws Exception {
        try {
            if(conexion != null) {
                conexion.close();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
        
    public Connection getConexion(){
        return this.conexion;
    }
}