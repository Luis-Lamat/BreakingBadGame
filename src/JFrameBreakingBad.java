/**
 * AppletJuego
 *
 * Personaje para juego previo Examen
 *
 * @author Luis Alberto Lamadrid - A01191158  
 * @author Jeronimo Martinez - A01191487
 * @version 1.00 2008/6/13
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class JFrameBreakingBad extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private int iVidas;                 // Vidas del juego
    private int iScore;                 // Score del juego
    private int iDireccion;             // Direccion de la Nena
    private int iAcumCorredores;        // Corredores acumulados en la colision
    private Personaje perNena;          // Objeto Nena de la clase personaje
    private LinkedList lstCaminadores;  // Lista de 8 a 10 caminadores
    private LinkedList lstCorredores;   // Lista de 10 a 15 corredores
    private boolean bPausado;           // Pausa
    
    /* objetos de audio */
    private SoundClip aucSonidoSuccess; // Objeto AudioClip sonido Caminador
    private SoundClip aucSonidoFailure; // Objeto AudioClip sonido Corredor
    
    public JFrameBreakingBad(){
        init();
        start();
    }
    	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(700, 800);
        
       
        
              
        // introducir instrucciones para iniciar juego
        // se define el background en color amarillo
	setBackground (Color.yellow);
        addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        // se realiza el ciclo del juego en este caso nunca termina
        while (iVidas > 0) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
               se checa si hubo colisiones para desaparecer jugadores o corregir
               movimientos y se vuelve a pintar todo
            */ 
            if(!bPausado) {
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError)	{
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion del objeto elefante 
     * 
     */
    public void actualiza(){
        
        // mueve a la nena de direccion
        switch (iDireccion){
            case 1: {
                perNena.arriba();
                }
                break;
            case 2: {
                perNena.abajo();
                }
                break;
            case 3: {
                perNena.izquierda();
                }
                break;
            case 4: {
                perNena.derecha();
                }
                break;
        }
        
        // se mueven los caminadores
        for (Object lstCaminador : lstCaminadores){
            Personaje perCaminador = (Personaje) lstCaminador;
            
            // poniendo velocidad entre 3 y 5 cada vez que avanza
            int iVel = (int) ((Math.random() * 2) + 3);
            perCaminador.setVelocidad(iVel);
            
            // moviendo al personaje
            perCaminador.derecha();
        }
        
        // se mueven los corredores
        for (Object lstCorredor : lstCorredores){
            Personaje perCorredor = (Personaje) lstCorredor;
            
            // poniendo velocidad ependiendo de las vidas
            if (iVidas > 0) { 
                perCorredor.setVelocidad(11/iVidas);
            }
            else {
                perCorredor.setVelocidad(0);
            }
                           
            // moviendo al personaje
            perCorredor.abajo();
        }
    }
    
    
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision del objeto elefante
     * con las orillas del <code>Applet</code>.
     * 
     */
    public void checaColision(){
        
    }
	
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }
        
        // creo imagen para el background
        // URL urlImagenEspacio = this.getClass().getResource("espacio.jpg");
        // Image imaImagenEspacio = Toolkit.getDefaultToolkit().getImage(urlImagenEspacio);
        
        // Despliego la imagen
        // graGraficaApplet.drawImage(imaImagenEspacio, 0, 0, 
        //        getWidth(), getHeight(), this);

        // Actualiza la imagen de fondo.
        graGraficaApplet.setColor (getBackground ());

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint_buffer(graGraficaApplet);
        

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint_buffer(Graphics g) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }

    
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // si presiono W  (arriba)
        if(keyEvent.getKeyCode() == KeyEvent.VK_W) {
            iDireccion = 1;
        }   
        // si presiono S  (abajo)
        if(keyEvent.getKeyCode() == KeyEvent.VK_S) {
            iDireccion = 2;

        } 
        // si presiono A  (izquierda)
        if(keyEvent.getKeyCode() == KeyEvent.VK_A) { 
            iDireccion = 3;
        }
        // si presiono D  (derecha)
        if(keyEvent.getKeyCode() == KeyEvent.VK_D) { 
            iDireccion = 4;
        }
        //si presiono P (Pausar)
        if(keyEvent.getKeyCode() == KeyEvent.VK_P) { 
            bPausado = !bPausado;
        }
        // si presiona G (save_file)
//        if(keyEvent.getKeyCode() == KeyEvent.VK_G) { 
//            try {
//                grabaArchivo();
//            } catch (IOException ex) {
//                Logger.getLogger(JFrameBreakingBad.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        // si presiona G (save_file)
//        if(keyEvent.getKeyCode() == KeyEvent.VK_C) { 
//            try {
//                leeArchivo();
//            } catch (IOException ex) {
//                Logger.getLogger(JFrameBreakingBad.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
    }
//    public void grabaArchivo() throws IOException{
//        // creo el objeto de salida para grabar en un archivo de texto
//        PrintWriter prwSalida = new PrintWriter
//                                (new FileWriter("save_file.txt"));
//        // guardo en  linea 1 el score
//    	prwSalida.println("Vidas: ");
//        prwSalida.println(iVidas);
//        // guardo en  linea Score
//        prwSalida.println("Score: ");
//        prwSalida.println(iScore);
//        prwSalida.println("posNena: ");
//        prwSalida.println(perNena.getX());
//        prwSalida.println(perNena.getY());
//        prwSalida.println(iDireccion);
//        
//        int iCount = 1;
//        prwSalida.println("Caminadores:");
//        for (Object lstCaminador : lstCaminadores) {
//            Personaje perCaminador = (Personaje) lstCaminador;
//            prwSalida.println(iCount + ")---------------------------");
//            prwSalida.println(perCaminador.getX());
//            prwSalida.println(perCaminador.getY());
//            iCount++;
//        }
//        iCount = 1;
//        prwSalida.println("Corredores:");
//        for (Object lstCorredor : lstCorredores) {
//                    Personaje perCorredor = (Personaje) lstCorredor;
//                    prwSalida.println(iCount + ")---------------------------");
//            prwSalida.println(perCorredor.getX());
//            prwSalida.println(perCorredor.getY());
//            iCount++;
//        }
//        prwSalida.println("END");
//        // cierro el archivo
//    	prwSalida.close();
//    }
//
//    public void leeArchivo() throws IOException{
//        
//        BufferedReader brwEntrada;
//        
//    	try{
//            // creo el objeto de entrada a partir de un archivo de texto
//            brwEntrada = new BufferedReader(new FileReader("save_file.txt"));
//    	} catch (FileNotFoundException e){
//            // si marca error grabo las posiciones actuales
//            grabaArchivo();
//            // lo vuelvo a abrir porque el objetivo es leer datos
//            brwEntrada = new BufferedReader(new FileReader("save_file.txt"));
//    	}
//        
//        // con el archivo abierto leo los datos que estan guardados
//        // primero saco el score que esta en la linea 1
//        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
//    	iVidas = Integer.parseInt(brwEntrada.readLine());
//        
//        // despues las vidas
//        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
//    	iScore = Integer.parseInt(brwEntrada.readLine());
//        
//        // Lee las posiciones y direccion que tenia Nena
//        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
//        perNena.setX(Integer.parseInt(brwEntrada.readLine()));    
//        perNena.setY(Integer.parseInt(brwEntrada.readLine()));
//        iDireccion = Integer.parseInt(brwEntrada.readLine());
//        
//        // Se actualiza la posicion de los caminadores
//        brwEntrada.readLine(); // lee el string de titulo "Caminadores:"
//        for (Object lstCaminador : lstCaminadores){
//            brwEntrada.readLine(); // lee el string de titulo para no procesarlo
//            Personaje perCaminador = (Personaje) lstCaminador;
//            perCaminador.setX(Integer.parseInt(brwEntrada.readLine()));
//            perCaminador.setY(Integer.parseInt(brwEntrada.readLine()));
//        }
//        
//        // Se actualiza la posicion de los corredores
//        brwEntrada.readLine(); // lee el string de titulo "Caminadores:"
//        for (Object lstCorredor : lstCorredores){
//            brwEntrada.readLine(); // lee el string de titulo para no procesarlo
//            Personaje perCorredor = (Personaje) lstCorredor;
//            perCorredor.setX(Integer.parseInt(brwEntrada.readLine()));
//            perCorredor.setY(Integer.parseInt(brwEntrada.readLine()));
//        }
//        
//        brwEntrada.readLine(); // lee el string END
//    	brwEntrada.close();
//    }
}
