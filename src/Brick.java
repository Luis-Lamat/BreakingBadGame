import java.awt.Image;
import java.awt.Rectangle;

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
    private boolean bHasPowerUp;
    
    public Brick(int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
        int x = (int)((Math.random() * 10) + 1);
        if (x == 7) bHasPowerUp = true;
        else bHasPowerUp = false;
    }
    
    public void setEstado(int iE){
        this.iEstado = iE;
    }
    
    public int getEstado(){
        return this.iEstado;
    }
    
    public boolean has_power_up(){
        return bHasPowerUp;
    }
    
    /** 
     * colisionaArriba
     * 
     * Metodo para revisar si un objeto <code>Personaje</code> colisiona por
     * arriba del <code>Brick</code>
     * 
     * @param perParametro es el objeto <code>Personaje</code> con el que se compara
     * @return  un valor true si esta colisionando y false si no
     * 
     */
    public boolean colisionaArriba(Personaje perParametro) {
        // creo un objeto rectangulo a partir de este objeto Personaje
        Rectangle recObjeto = new Rectangle(this.getX(),this.getY(),
                this.getAncho(), 10);
        
        // creo un objeto rectangulo a partir del objeto Personaje parametro
        Rectangle recParametro = new Rectangle(perParametro.getX(),
                perParametro.getY(), perParametro.getAncho(),
                perParametro.getAlto());
        
        // si se colisionan regreso verdadero, sino regreso falso
        return recObjeto.intersects(recParametro);
    }
    
    /** 
     * colisionaAbajo
     * 
     * Metodo para revisar si un objeto <code>Personaje</code> colisiona por
     * abajo del <code>Brick</code>
     * 
     * @param perParametro es el objeto <code>Personaje</code> con el que se compara
     * @return  un valor true si esta colisionando y false si no
     * 
     */
    public boolean colisionaAbajo(Personaje perParametro) {
        // creo un objeto rectangulo a partir de este objeto Personaje
        Rectangle recObjeto = new Rectangle(this.getX(), (this.getY() - 
                (this.getAlto() - 10)), this.getAncho(), 10);
        
        // creo un objeto rectangulo a partir del objeto Personaje parametro
        Rectangle recParametro = new Rectangle(perParametro.getX(),
                perParametro.getY(), perParametro.getAncho(),
                perParametro.getAlto());
        
        // si se colisionan regreso verdadero, sino regreso falso
        return recObjeto.intersects(recParametro);
    }

    /** 
     * colisionaIzquierda
     * 
     * Metodo para revisar si un objeto <code>Personaje</code> colisiona por
     * la izquierda del <code>Brick</code>
     * 
     * @param perParametro es el objeto <code>Personaje</code> con el que se compara
     * @return  un valor true si esta colisionando y false si no
     * 
     */
    public boolean colisionaIzquierda(Personaje perParametro) {
        // creo un objeto rectangulo a partir de este objeto Personaje
        Rectangle recObjeto = new Rectangle(this.getX(), (this.getY() + 
                (this.getAlto() - 10)), 10, this.getAlto());
        
        // creo un objeto rectangulo a partir del objeto Personaje parametro
        Rectangle recParametro = new Rectangle(perParametro.getX(),
                perParametro.getY(), perParametro.getAncho(),
                perParametro.getAlto());
        
        // si se colisionan regreso verdadero, sino regreso falso
        return recObjeto.intersects(recParametro);
    }

     /** 
     * colisionaDerecha
     * 
     * Metodo para revisar si un objeto <code>Personaje</code> colisiona por
     * la derecha del <code>Brick</code>
     * 
     * @param perParametro es el objeto <code>Personaje</code> con el que se compara
     * @return  un valor true si esta colisionando y false si no
     * 
     */
    public boolean colisionaDerecha(Personaje perParametro) {
        // creo un objeto rectangulo a partir de este objeto Personaje
        Rectangle recObjeto = new Rectangle((this.getX()+ (this.getAncho() - 5))
                , (this.getY() + (this.getAlto() - 10)), 10, this.getAlto());
        
        // creo un objeto rectangulo a partir del objeto Personaje parametro
        Rectangle recParametro = new Rectangle(perParametro.getX(),
                perParametro.getY(), perParametro.getAncho(),
                perParametro.getAlto());
        
        // si se colisionan regreso verdadero, sino regreso falso
        return recObjeto.intersects(recParametro);
    }
}
