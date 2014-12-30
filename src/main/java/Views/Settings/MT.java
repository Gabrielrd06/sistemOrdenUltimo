package Views.Settings;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class MT<T> extends AbstractTableModel{
    protected String[] columnas;
    protected Object[][] datos;
    protected List<T> coleccion;

    public MT(String[] columnas, Object[][] datos, List<T> coleccion) {
        this.columnas = columnas;
        this.datos = datos;
        this.coleccion = coleccion;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public int getRowCount() {
        return datos.length;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return datos[rowIndex][columnIndex];
    }
    public List<T> getColeccion(){
        return this.coleccion;
    }
    public T getSeleccion(int fila){
        return this.coleccion.get(fila);
    }
    
    public double getSumaColumna(int columna){
        double suma = 0;
        for(int i = 0;i<datos.length;i++){
            suma += Double.parseDouble(datos[i][columna].toString());
        }
        return suma;
    }
}