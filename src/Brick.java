import java.awt.Image;

/**
 * Brick
 *
 * Modela la definición de todos los objetos de tipo
 * <code>Brick</code>
 *
 * @author Luis Lamadrid & Jeronimo Martinez
 *
 */
public class Brick extends Personaje {

    // iEstado:
    // 1 = choque 1 vez
    // 2 = choque 2 veces
    // 3 = desaparece
    private int iEstado = 0;
    
    public Brick(int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
    }
    
    public void setEstado(int iE){
        this.iEstado = iE;
    }
    
    public int getEstado(){
        return this.iEstado;
    }
    
    
}
