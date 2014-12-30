package Views.Settings;


 public abstract class Ventana extends javax.swing.JInternalFrame{
    
    public abstract void presentarDatos();    
    public abstract void llenarCampos(Object objeto);
    public abstract void presentarDatos(String opcion);
     /**
      * Llena el formulario automaticamente
      * @param opcion puede ser modificar o insertar
      * @param objeto datos del formulario
      */
    public abstract void llenarCampos(String opcion,Object objeto);
    public abstract void limpiarFormulario();
}
