
package Views.Settings;

/**
 *
 * @author Gabriel
 */
public class ClaseJcombobox {
    
    private String tiponombre;
    private int tipoid;

    public ClaseJcombobox(String tiponombre, int tipoid) {
        this.tiponombre = tiponombre;
        this.tipoid = tipoid;
    }

    public int getTipoid() {
        return tipoid;
    }

    public void setTipoid(int tipoid) {
        this.tipoid = tipoid;
    }

    public String getTiponombre() {
        return tiponombre;
    }

    public void setTiponombre(String tiponombre) {
        this.tiponombre = tiponombre;
    }
    
    @Override
    public String toString() {
        return tiponombre;
    }
    
}
