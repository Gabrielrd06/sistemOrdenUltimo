/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views.Settings;

import Entitys.DetalleOrden;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

/**
 *
 * @author fesquivelc
 */
public class MT_DetalleOrden extends AbstractTableModel implements ObservableListListener {

    private List<DetalleOrden> datos;
    private String[] columnas = {"Marca", "Descripción", "Garantía", "Unidad", "Cantidad", "PrecioUnit","Total"};

    public MT_DetalleOrden(List<DetalleOrden> datos) {
        this.datos = datos;

        if (datos instanceof ObservableList) {
            ((ObservableList) datos).addObservableListListener(this);
        }
    }
    private static final Logger LOG = Logger.getLogger(MT_DetalleOrden.class.getName());

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        DetalleOrden seleccionada = this.datos.get(rowIndex);

        switch (columnIndex) {
            case 2:
                String garantia = aValue.toString();
                seleccionada.setGarantia(garantia);
                break;
            case 4:
                int cantidad = Integer.parseInt(aValue.toString());
                seleccionada.setCantidad(cantidad);
                break;
            case 5:
                Double precio = (Double) aValue;
                seleccionada.setPrecioorden(precio);
                break;                           
        }

        this.fireTableCellUpdated(rowIndex, columnIndex);        
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2 || columnIndex == 4 || columnIndex ==5; 
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public int getRowCount() {
        return this.datos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {        
        if (columnIndex == 2) {
            return String.class;
        } else if ( columnIndex == 3 || columnIndex == 5) {
            return Double.class;
        } else if (columnIndex == 4) {
            return Integer.class;
        } else {
            return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetalleOrden seleccionada = this.datos.get(rowIndex);
        try {
            switch (columnIndex) {
                case 0:
                    return seleccionada.getIdProducto().getIdMarca().getNombre();
                case 1:
                    return seleccionada.getIdProducto().getDescripcion();
                case 2:
                    if ("".equals(seleccionada.getGarantia())) {
                        return null;
                    } else {
                        return seleccionada.getGarantia();
                    }                   
                case 3:
                    if ("".equals(seleccionada.getUnidad())) {
                        return null;
                    } else {
                        return seleccionada.getUnidad();
                    } 
                case 4:
                    if (seleccionada.getCantidad() ==  0) {
                        return null;
                    } else {
                        return seleccionada.getCantidad();
                    } 
                 case 5:
                    if (seleccionada.getPrecioorden() ==  0) {
                        return null;
                    } else {
                        return seleccionada.getPrecioorden();
                    } 
                case 6:
                    double total = 0;
                    if(seleccionada.getPrecioorden() != 0 && seleccionada.getCantidad() != 0){
                        total = seleccionada.getPrecioorden() * seleccionada.getCantidad();
                    }                    
                    if (total == 0) {
                        return null;
                    } else {                       
                        return total;
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void listElementsAdded(ObservableList ol, int i, int i1) {
        LOG.info("AGREGADO: " + i + " " + i1);
        this.fireTableRowsInserted(i1, i1);
    }

    @Override
    public void listElementsRemoved(ObservableList ol, int i, List list) {
        LOG.info(i + "");
        this.fireTableRowsDeleted(i, i);
    }

    @Override
    public void listElementReplaced(ObservableList ol, int i, Object o) {
        this.fireTableRowsUpdated(i, i);
    }

    @Override
    public void listElementPropertyChanged(ObservableList ol, int i) {
        this.fireTableRowsUpdated(i, i);
    }

}
