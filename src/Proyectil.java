
import java.awt.Image;


/**
 * Proyectil
 *
 * Modela la definición de todos los objetos de tipo
 * <code>Proyectil</code>
 *
 * @author Luis Lamadrid & Jeronimo Martinez
 *
 */
public class Proyectil extends Personaje {

    public Proyectil(int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
    }
    
    public void derecha_arriba(){
        this.setX(this.getX() + this.getVelocidad());
        this.setY(this.getY() - this.getVelocidad());
    }
    
    public void derecha_abajo(){
        this.setX(this.getX() + this.getVelocidad());
        this.setY(this.getY() + this.getVelocidad());
    }
    
    public void izquierda_arriba(){
        this.setX(this.getX() - this.getVelocidad());
        this.setY(this.getY() - this.getVelocidad());
    }
    
    public void izquierda_abajo(){
        this.setX(this.getX() - this.getVelocidad());
        this.setY(this.getY() + this.getVelocidad());
    }
}