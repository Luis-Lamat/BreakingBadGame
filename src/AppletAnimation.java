import javax.swing.ImageIcon;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;

/**
	El applet AppletAnimacion muestra una animación en pantalla.
*/
public class AppletAnimation extends Applet implements Runnable{

	//Objeto de la clase Animacion para el manejo de la animación
	private Animacion anim;
	
	//Variables de control de tiempo de la animación
	private long tiempoActual;
	private long tiempoInicial;
	int posX, posY;
	
	/**
		El método init() crea la animación que se mostrará en pantalla.
	*/
	public void init(){
		
		//Se cargan las imágenes(cuadros) para la animación
		Image raton1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet1.png"));
		Image raton2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet2.png"));
		Image raton3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet3.png"));
		Image raton4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet4.png"));
		
		
		//Se crea la animación
		anim = new Animacion();
		anim.sumaCuadro(raton1, 400);
		anim.sumaCuadro(raton2, 400);
		anim.sumaCuadro(raton3, 400);
		anim.sumaCuadro(raton4, 400);
		
		
		//Pinta el fondo del Applet de color amarillo		
		setBackground(Color.yellow);
	}
	
	//El método start() inicializa el thread que utiliza el Applet
	public void start(){
		
		//Crea el thread
		Thread hilo = new Thread(this);
	    //Inicializa el thread
	    hilo.start();
	}
	
	/**
	 * Metodo stop sobrescrito de la clase Applet.
	 * En este metodo se pueden tomar acciones para cuando se termina
	 * de usar el Applet. Usualmente cuando el usuario sale de la pagina
	 * en donde esta este Applet.
	 */
	public void stop() {

	}

	/**
	 * Metodo destroy sobrescrito de la clase Applet.
	 * En este metodo se toman las acciones necesarias para cuando
	 * el Applet ya no va a ser usado. Usualmente cuando el usuario
	 * cierra el navegador.
	 */
	public void destroy() {

	}

	
	/**
		El método run() manda a llamar los métodos atualiza() y repaint(),
		nesecarios para actualizar y mostrar la animación en pantalla.
	*/
	 public void run() {
	 	
	 	//Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();

		//Ciclo principal del Applet. Actualiza y despliega en pantalla la animación hasta que el Applet sea cerrado
        while (true) {
        	
        	//Actualiza la animación
	         actualiza();
	         //Manda a llamar al método paint() para mostrar en pantalla la animación
	         repaint();
            
            //Hace una pausa de 200 milisegundos
            try {
                Thread.sleep(200);
            }
            catch (InterruptedException ex) { }
        }

   	 }
    
    /**
   	 El método actualiza() actualiza la animación
    */
    public void actualiza() {
   	
   		 //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
         long tiempoTranscurrido =
             System.currentTimeMillis() - tiempoActual;
            
         //Guarda el tiempo actual
       	 tiempoActual += tiempoTranscurrido;

         //Actualiza la animación en base al tiempo transcurrido
         anim.actualiza(tiempoTranscurrido);
    }
   
    /**
   	 El método paint() muestra en pantalla la animación
    */
    public void paint(Graphics g) {
   		 posX =  this.getWidth()/2 - anim.getImagen().getWidth(null)/2;
         posY =	this.getHeight()/2 - anim.getImagen().getHeight(null)/2;
         // Muestra en pantalla el cuadro actual de la animación
         if (anim != null) {
        	 g.drawImage(anim.getImagen(), posX, posY, this);
         }

    }
}