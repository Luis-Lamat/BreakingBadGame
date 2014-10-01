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
    private int ValorFor;
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private int iVidas;                 // Vidas del juego
    private int iScore;                 // Score del juego
    private int iDireccion;             // Direccion de la Tabla
    private int iDireccionProyectil;    // Direccion del proyectil
    private LinkedList lstCajas;        // Lista Cajas
    private Proyectil perBola;          // Objeto Bola de la clase personaje
    private Personaje perTabla;         // Objeto Tabla de la clase personaje
    private Personaje perCaja;          // Objeto Caja de la clase personaje
    private boolean ColisionTablaProy;  // TEST
    //private boolean bPausado;         // Pausa
    
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
        ColisionTablaProy = false;
        
        // hago el applet de un tamaño 500,500
        setSize(675, 800);
        
        iScore = 0;
        iVidas = 5;
        lstCajas = new LinkedList();
        
        // Crear imagen de Bola y le pone direccion y velocidad
        URL urlImagenBola = this.getClass().getResource("Black Cube.jpg");
        perBola = new Proyectil(getWidth() / 2, getHeight()  / 2,
                Toolkit.getDefaultToolkit().getImage(urlImagenBola));
        perBola.setVelocidad(3);
        iDireccionProyectil = 1;
        
        // Crear imagen de Tabla y le pone velocidad
        URL urlImagenTabla = this.getClass().getResource("BrownTable.jpg");
        perTabla = new Personaje((getWidth() / 2), (getHeight()  / 4) * 3,
                Toolkit.getDefaultToolkit().getImage(urlImagenTabla));
        perTabla.setVelocidad(6);
        
        // Crear imagen de Cajas
        creandoCajas();
        
        // se define el background en color amarillo
	setBackground (Color.white);
        addKeyListener(this);
    }
	
    public void creandoCajas() {
        int iXTemp = 0;
        int iYTemp = 0;
        int iValInicial = 25;
        int iSeparDePixeles = 5;
        
        URL urlImagenCaja = this.getClass().getResource("BrownTable.jpg");
        for(int iI = 0; iI < 20; iI++) {
            perCaja = new Personaje(100, 100,
                Toolkit.getDefaultToolkit().getImage(urlImagenCaja));
            // Valores de X y Y despues de crear 
            if(lstCajas.size() > 0) {
                if (iI < 6) {
                    perCaja.setX(iXTemp + perCaja.getAncho() + iSeparDePixeles);
                    perCaja.setY(100);
                } 
                else if (iI == 7) {
                    perCaja.setX(iValInicial);
                    perCaja.setY(100 + perCaja.getAlto() + iSeparDePixeles);
                    }
                else if (iI < 13 && iI > 7) {
                    perCaja.setX(iXTemp + perCaja.getAncho() + iSeparDePixeles);
                    perCaja.setY(100 + perCaja.getAlto() + iSeparDePixeles);
                }
                else if (iI == 14) {
                    perCaja.setX(iValInicial);
                    perCaja.setY(100 + perCaja.getAlto() * 2 + 
                            iSeparDePixeles * 2);
                }
                else if (iI < 20 && iI > 14) {
                    perCaja.setX(iXTemp + perCaja.getAncho() + iSeparDePixeles);
                    perCaja.setY(100 + perCaja.getAlto() * 2 + 
                            iSeparDePixeles * 2);
                }
                lstCajas.add(perCaja);
                //se guarda valor de X temporal para siguiente caja
                iXTemp = perCaja.getX();
                //iYTemp = perCaja.getY();
                ValorFor = iI;
                if (iI == 6 || iI == 14) {
                    iXTemp = iValInicial;
                }
                
            }
            //Valores de X y Y default
            else {
                perCaja.setX(iValInicial);
                perCaja.setY(100);
                //se guarda valor de X temporal para siguiente caja
                iXTemp = perCaja.getX();
                iYTemp = perCaja.getY();
                lstCajas.add(perCaja);
            }
        }
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
           
            actualiza();
            checaColision();
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
    public void actualiza() {
        
        // mueve a la Tabla de direccion
        switch (iDireccion) {
            case 0: {
                break;
            }
            case 1: {
                perTabla.izquierda();
                break;
            }           
            case 2: {
                perTabla.derecha();
                break;
            }
        }
        
        /* Mueve a la bola de direccion:
        *
        *              |
        *         1    |    2
        *              | 
        *      --------+---------    
        *              |
        *         3    |    4
        *              | 
        */
        switch (iDireccionProyectil){
            case 1: {
                perBola.izquierda_arriba();
                break;
            }
            case 2: {
                perBola.derecha_arriba();
                break;
            }
            case 3: {
                perBola.izquierda_abajo();
                break;
            }
            case 4: {
                perBola.derecha_abajo();
                break;
            } 
        }  
    }
    
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision de los objetos del JFrame
     * con las orillas del <code>JFrame</code> y entre si.
     * 
     */
    public void checaColision(){
        //si tabla choca con las paredes
        if(perTabla.getX() < 0) {
            perTabla.setX(0);
        }
        else if (perTabla.getX() + perTabla.getAncho() > getWidth()) {
            perTabla.setX(getWidth() - perTabla.getAncho());
        }
        
        // llamada al metodo colisiona de proyectil con muros
        checaProyectilChocaConMuros();
        
        // llamada al metodo coliisona proyectil con tabla
        checaProyectilChocaConTabla();
    }
    
    
    /**
     * checaProyectilChocaConMuros
     * 
     * Metodo usado para checar la colision del objeto Proyectil
     * con las orillas del <code>JFrame</code>.
     * 
     */
    public void checaProyectilChocaConMuros(){
        // checa si proyectil colisiona con las paredes
        // de la derecha y la izquierda...
        if (perBola.getX() < 0){
            proyectilChocaIzquierda();
        }        
        else if(perBola.getX() + perBola.getAncho() > getWidth()){
            proyectilChocaDerecha();
        }
        
        // checa si el proyectil choca con el techo...
        if (perBola.getY() < 0){
           proyectilChocaArriba();
        }
        // si choca por debajo, que se disminuyan vidas y se reposicione
        else if (perBola.getY() + perBola.getAlto() > getHeight()){
            reposicionaProyectil();
            iVidas--;
        }
    }
    
    /**
     * checaProyectilChocaConMuros
     * 
     * Metodo usado para checar la colision del objeto Proyectil
     * con la tabla (jugador) del <code>JFrame</code>.
     * 
     */
    public void checaProyectilChocaConTabla(){
        if (perBola.colisiona(perTabla)){
            if (perBola.getY() + perBola.getAlto() >= perTabla.getY()){
                proyectilChocaAbajo();
            }
        }
    }
    
    
    /**
     * proyectilChocaArriba
     * Metodo para checar si el proyectil choca por su lado superior
     */
    void proyectilChocaArriba() {
        iDireccionProyectil = (iDireccionProyectil == 2) ? 4 : 3;     
    }
    
    /**
     * proyectilChocaArriba
     * Metodo para checar si el proyectil choca por su lado inferior
     */
    void proyectilChocaAbajo() {
        iDireccionProyectil = (iDireccionProyectil == 4) ? 2 : 1;
    }
    
    /**
     * proyectilChocaArriba
     * Metodo para checar si el proyectil choca por su lado izquierdo
     */
    void proyectilChocaIzquierda() {
        iDireccionProyectil = (iDireccionProyectil == 1) ? 2 : 4;    
    }
    
    /**
     * proyectilChocaArriba
     * Metodo para checar si el proyectil choca por su lado derecho
     */
    void proyectilChocaDerecha() {
        iDireccionProyectil = (iDireccionProyectil == 2) ? 1 : 3;        
    }
    
    
    /**
     * reposicionaProyectil
     * 
     * Metodo usado para reposicionar el objeto Proyectil
     * dentro del <code>JFrame</code>
     */
    public void reposicionaProyectil(){
        perBola.setX(getWidth() / 2);
        perBola.setY(getHeight() / 2);
        iDireccionProyectil = 1;
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

        // Actualiza la imagen de fondo.
        graGraficaApplet.setColor (getBackground ());
        graGraficaApplet.fillRect (0, 0, this.getSize().width, 
                this.getSize().height);
        
        
        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint_buffer(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
        

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
        // si la imagen ya se cargo
        if (perBola != null && perTabla != null && lstCajas.size() > 0) {
            
            g.drawImage(perBola.getImagen() , perBola.getX(),
                    perBola.getY(), this);
            
            g.drawImage(perTabla.getImagen() , perTabla.getX(),
                    perTabla.getY(), this);
            
            for (Object objCajas : lstCajas) {
                Personaje perCaja = (Personaje) objCajas;
                g.drawImage(perCaja.getImagen() , perCaja.getX(),
                    perCaja.getY(), this);
            
        }
            
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                g.drawString("No se cargo la imagen..", 20, 20);
        }
        
        g.setColor(Color.red);
        g.drawString("Score: " + iScore, 20, 50);
        g.drawString("Vidas: " + iVidas, 20, 70);
        g.drawString("Colision?: " + ColisionTablaProy, 20, 90);      
        g.drawString("For: "+ ValorFor, 200, 50);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            iDireccion = 1;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            iDireccion = 2;
        }
    }

    
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT || 
           keyEvent.getKeyCode() == KeyEvent.VK_RIGHT ){
            iDireccion = 0;
        }
        
        //si presiono P (Pausar)
        if(keyEvent.getKeyCode() == KeyEvent.VK_P) { 
           // bPausado = !bPausado;
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
