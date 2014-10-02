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
import java.util.Iterator;
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
    private LinkedList lstPowerUps;     // Lista de todos los power ups
    private Proyectil proBola;          // Objeto Bola de la clase personaje
    private Personaje perTabla;         // Objeto Tabla de la clase personaje
    private int iDeltaY;                // entero para hacer q bricks se muevan
    private int iTimer;                 // Timer para mover cajas para abajo
    private int iIntervalo;             // Maneja el intervalo de tiempo para 
                                        //  que los ladrillos bajen
    private boolean ChocaAbajoBrick;
    private boolean bPausado;
    private String Text;
    //private boolean bPausado;         // Pausa
    
    /* objetos de audio */
    private SoundClip aucSonidoSuccess; // Objeto AudioClip sonido Caminador
    private SoundClip aucSonidoFailure; // Objeto AudioClip sonido Corredor
    //Objeto de la clase Animacion para el manejo de la animación
	private Animacion anim;
	
	//Variables de control de tiempo de la animación
	private long tiempoActual;
	private long tiempoInicial;
	int posX, posY;
    
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
        
        //Se cargan las imágenes(cuadros) para la animación
		Image raton1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet1.png"));
		Image raton2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet2.png"));
		Image raton3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet3.png"));
		Image raton4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Bullet4.png"));
		
		
		//Se crea la animación
		anim = new Animacion();
		anim.sumaCuadro(raton1, 300);
		anim.sumaCuadro(raton2, 300);
		anim.sumaCuadro(raton3, 300);
		anim.sumaCuadro(raton4, 300);
        ChocaAbajoBrick = false;
        Text = "";
        
        // # de pixeles que las cajas se mueven hacia abajo
        iDeltaY = 5;
        
        // fijando el timer de movimiento de cajas en cero
        iTimer = 0;
        
        // fijando el intervalo de tiempo por el cual los ladrillos bajan
        iIntervalo = 200;
        
        // hago el applet de un tamaño 500,500
        setSize(675, 800);
        
        bPausado = true;
        iScore = 0;
        iVidas = 5;
        lstCajas = new LinkedList();
        
        lstPowerUps = new LinkedList();
        
        // Crear imagen de Tabla y le pone velocidad
        URL urlImagenTabla = this.getClass().getResource("HankUp.png");
        perTabla = new Personaje((getWidth() / 2), (getHeight()  / 4) * 3,
                Toolkit.getDefaultToolkit().getImage(urlImagenTabla));
        perTabla.setVelocidad(6);
        
        // Crear imagen de Bola y le pone direccion y velocidad
        URL urlImagenBola = this.getClass().getResource("Bullet1.png");
        proBola = new Proyectil(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenBola));
        
        proBola.setX(perTabla.getX() + ((perTabla.getAncho() / 2)
                                        - proBola.getAncho() / 2));
        proBola.setY(perTabla.getY() - proBola.getAlto());
        proBola.setVelocidad(4);
        iDireccionProyectil = 0;
        
        // Crear imagen de Cajas en la posicion inicial de 255 , 100
        creandoCajas(25,100,4,6);
                
        // se define el background en color amarillo
	setBackground (Color.white);
        addKeyListener(this);
    }
	
    public void creandoCajas(int iniX, int iniY, int iRows, int iCols) {
               
        int iSEPARACION_PIXELES = 5;
        int iX;
        int iY;
        URL urlImagenCaja = this.getClass().getResource("Caja1.jpg");
        Brick briCajaAux  = new Brick(0,0,
                          Toolkit.getDefaultToolkit().getImage(urlImagenCaja));
        int iAlto  = briCajaAux.getAlto();
        int iAncho = briCajaAux.getAncho();
        
        for (int iC = 0; iC < iRows; iC++){
            iY = iniY + ((iAlto + iSEPARACION_PIXELES) * iC);
            
            for (int iR = 0; iR < iCols; iR++){
                iX = iniX + ((iAncho + iSEPARACION_PIXELES) * iR);
                Brick briCaja = new Brick(iX, iY,
                          Toolkit.getDefaultToolkit().getImage(urlImagenCaja));
                lstCajas.add(briCaja);               
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
        tiempoActual = System.currentTimeMillis();
        
        while (iVidas > 0) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
               se checa si hubo colisiones para desaparecer jugadores o corregir
               movimientos y se vuelve a pintar todo
            */ 
            if (bPausado) {
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
    public void actualiza() {
        
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
         long tiempoTranscurrido =
             System.currentTimeMillis() - tiempoActual;
            
         //Guarda el tiempo actual
       	 tiempoActual += tiempoTranscurrido;

         //Actualiza la animación en base al tiempo transcurrido
         anim.actualiza(tiempoTranscurrido); 
         
        // mueve a la Tabla de direccion
        switch (iDireccion) {
            case 0: { break; }         // se para
            case 1: {
                perTabla.izquierda();  // se mueve a la izquierda
                break;
            }           
            case 2: {
                perTabla.derecha();    // se mueve a la derecha
                break;
            }
        }
        
        /* Mueve al proyectil de direccion 
        *          en diagonal:
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
            case 0: {
                proBola.setX(perTabla.getX() + ((perTabla.getAncho() / 2)
                                        - proBola.getAncho() / 2));
                proBola.setY(perTabla.getY() - proBola.getAlto());
                break;
            }
            case 1: {
                proBola.izquierda_arriba();
                break;
            }
            case 2: {
                proBola.derecha_arriba();
                break;
            }
            case 3: {
                proBola.izquierda_abajo();
                break;
            }
            case 4: {
                proBola.derecha_abajo();
                break;
            } 
        }
        
        // Mnaeja el movimiento hacia abajo de las cajas
        for (Object lstCaja : lstCajas){
            Brick briCaja = (Brick) lstCaja;
            
            if (iTimer >= iIntervalo && briCaja.getEstado() < 3){
                briCaja.setY(briCaja.getY() + iDeltaY);
            }
        }
        if (iTimer >= iIntervalo){
            iTimer = 0;
        } else {
            iTimer++;
        }
        
        if (lstPowerUps.size() > 0){
            for (Object lstPower : lstPowerUps){
                Personaje perPower = (Personaje) lstPower;
                perPower.abajo();
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
        
        // llamada al metodo coliisona proyectil con ladrillos
        checaProyectilChocaConBricks();
        
        
        // llamada al metodo colisiona power ups
        checaColisionPowerUps();
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
        if (proBola.getX() < 0){
            proyectilChocaIzquierda();
        }        
        else if(proBola.getX() + proBola.getAncho() > getWidth()){
            proyectilChocaDerecha();
        }
        
        // checa si el proyectil choca con el techo...
        if (proBola.getY() < 0){
           proyectilChocaArriba();
        }
        // si choca por debajo, que se disminuyan vidas y se reposiciona
        // se baja tambien el intervalo de tiempo para  ue ladrillos bajen
        else if (proBola.getY() + proBola.getAlto() > getHeight()){
            reposicionaProyectil();
            iVidas--;
            iIntervalo -= 40;
        }
    }
    
    /**
     * checaProyectilChocaConTabla
     * 
     * Metodo usado para checar la colision del objeto Proyectil
     * con la tabla (jugador) del <code>JFrame</code>.
     * 
     */
    public void checaProyectilChocaConTabla(){
        if (perTabla.colisionaArriba(proBola)){
            proyectilChocaAbajo();            
        }
    }
    
    /**
     * checaProyectilChocaConBricks
     * 
     * Metodo usado para checar la colision del objeto Proyectil
     * con los ladrillos superiores del <code>JFrame</code>.
     * 
     */    
    public void checaProyectilChocaConBricks(){
        for (Object lstCaja : lstCajas ){
            Brick briCaja = (Brick) lstCaja;
            
            if (briCaja.getEstado() < 3){
                if (briCaja.colisionaAbajo(proBola)){
                    proyectilChocaArriba();
                    briCaja.setEstado(briCaja.getEstado()+1);
                }
                else if (briCaja.colisionaArriba(proBola)){
                    proyectilChocaAbajo();
                    briCaja.setEstado(briCaja.getEstado()+1);
                }
                else if (briCaja.colisionaIzquierda(proBola)){
                    proyectilChocaDerecha();
                    briCaja.setEstado(briCaja.getEstado()+1);
                }
                else if (briCaja.colisionaDerecha(proBola)){
                    proyectilChocaIzquierda();
                    briCaja.setEstado(briCaja.getEstado()+1);
                }
                if (briCaja.getEstado() == 3 && briCaja.has_power_up()){                    
                    agregaRandomPowerUp(briCaja);
                }
            }
        }
    }
    
    
    /**
     * proyectilChocaArriba
     * Metodo para checar si el proyectil choca por su lado superior
     */
    void proyectilChocaArriba() {
        if (iDireccionProyectil == 2)
            iDireccionProyectil = 4;
        else
            iDireccionProyectil = 3; 
        Text = "Choque Arriba!";
    }
    
    /**
     * proyectilChocaAbajo
     * Metodo para checar si el proyectil choca por su lado inferior
     */
    void proyectilChocaAbajo() {
        if (iDireccionProyectil == 4)
            iDireccionProyectil = 2;
        else
            iDireccionProyectil = 1; 
        Text = "Choque Abajo!";
    }
    
    /**
     * proyectilChocaIzquierda
     * Metodo para checar si el proyectil choca por su lado izquierdo
     */
    void proyectilChocaIzquierda() {
        if (iDireccionProyectil == 1)
            iDireccionProyectil = 2;
        else
            iDireccionProyectil = 4; 
        Text = "Choque Izquierda!";
    }
    
    /**
     * proyectilChocaDerecha
     * Metodo para checar si el proyectil choca por su lado derecho
     */
    void proyectilChocaDerecha() {
        if (iDireccionProyectil == 2)
            iDireccionProyectil = 1;
        else
            iDireccionProyectil = 3;  
        Text = "Choque Derecha!";
    }
    
    
    /**
     * reposicionaProyectil
     * 
     * Metodo usado para reposicionar el objeto Proyectil
     * dentro del <code>JFrame</code>
     */
    public void reposicionaProyectil(){
        proBola.setX(perTabla.getX() + ((perTabla.getAncho() / 2)
                                        - proBola.getAncho() / 2));
        proBola.setY(perTabla.getY() - proBola.getAlto());
        iDireccionProyectil = 0;
    }
    
    public void agregaRandomPowerUp(Brick briCaja1){
        URL urlImagenBalaDevuelta = this.getClass().getResource("Bullet.png");
        Personaje perPowerUp = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenBalaDevuelta));
        perPowerUp.setVelocidad(3);
        
        perPowerUp.setY(briCaja1.getY());
        perPowerUp.setX(briCaja1.getX() + (briCaja1.getAncho() / 2) - 
                                         perPowerUp.getAncho() / 2);
        
        lstPowerUps.add(perPowerUp);
    }
    
    public void checaColisionPowerUps(){
        for(Object lstPowerUp : lstPowerUps){
            Personaje perPowerUp = (Personaje) lstPowerUp;
            if (perPowerUp.getY() + perPowerUp.getAlto() <= 0){
                // quitar el objeto de la lista
            }
            else if (perTabla.colisionaArriba(perPowerUp)){
                iDireccionProyectil = 0;
            }
        }
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
        URL urlImagenBackground = this.getClass().getResource("background.jpg");
        Image imaImagenBackground = Toolkit.getDefaultToolkit()
                                           .getImage(urlImagenBackground);

        // Despliego la imagen
        graGraficaApplet.drawImage(imaImagenBackground, 0, 0, 
                getWidth(), getHeight(), this);

        // Actualiza la imagen de fondo.
        graGraficaApplet.setColor (getBackground ());
        
        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint_buffer(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
        

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint_buffer
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint_buffer(Graphics g) {
        
        if (anim != null) {
            g.drawImage(anim.getImagen(),proBola.getX() , proBola.getY(), this);
        }
        // si la imagen ya se cargo
        if (perTabla != null && lstCajas.size() > 0) {
            
            
            
            g.drawImage(perTabla.getImagen() , perTabla.getX(),
                    perTabla.getY(), this);
            
            for (Object objCajas : lstCajas) {
                Brick briCaja = (Brick) objCajas;
                if (briCaja.getEstado() == 0){
                    g.drawImage(briCaja.getImagen() , briCaja.getX(),
                       briCaja.getY(), this);                     
                }
                else if (briCaja.getEstado() == 1){
                    URL urlImagenBola = this.getClass().getResource("caja2.jpg");               
                    briCaja.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenBola));
                    g.drawImage(briCaja.getImagen() , briCaja.getX(),
                       briCaja.getY(), this);                     
                }
                else if (briCaja.getEstado() == 2){
                    URL urlImagenBola = this.getClass().getResource("caja3.jpg");               
                    briCaja.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenBola));
                    g.drawImage(briCaja.getImagen() , briCaja.getX(),
                       briCaja.getY(), this); 
                }
                else if (briCaja.getEstado() == 3){
                    continue;
                }
            }
            
            if (lstPowerUps.size() > 0){
                for (Object lstPower : lstPowerUps){
                    Personaje perPower = (Personaje) lstPower;
                    g.drawImage(perPower.getImagen() , perPower.getX(),
                       perPower.getY(), this);
                }
            }
        }            
        // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                g.drawString("No se cargo la imagen..", 20, 20);
        }
        
        g.setColor(Color.red);
        g.drawString("Score: " + iScore, 20, 50);
        g.drawString("Vidas: " + iVidas, 20, 70);
        g.drawString("Direccion: " + iDireccionProyectil, 20, 90);
        
        g.drawString("X: " + proBola.getX(), 300, 50);
        g.drawString("Y: " + proBola.getY(), 300, 70);
        g.drawString("Colision: " + Text, 300, 90);
        
        if (iDireccionProyectil == 0){
            g.drawString("Presiona SPACEBAR para lanzar la bala", 217, 3*getWidth()/4);
        }
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
        if (iDireccionProyectil == 0 && 
            keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
            iDireccionProyectil = 1;
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
            bPausado = !bPausado;
        }
        // si presiona G (save_file)
        if(keyEvent.getKeyCode() == KeyEvent.VK_G) { 
            try {
                grabaArchivo();
            } catch (IOException ex) {
                Logger.getLogger(JFrameBreakingBad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // si presiona G (save_file)
        if(keyEvent.getKeyCode() == KeyEvent.VK_C) { 
            try {
                leeArchivo();
            } catch (IOException ex) {
                Logger.getLogger(JFrameBreakingBad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public void grabaArchivo() throws IOException{
        // creo el objeto de salida para grabar en un archivo de texto
        PrintWriter prwSalida = new PrintWriter
                                (new FileWriter("save_file.txt"));
        // guardo en  linea 1 el score
    	prwSalida.println("Vidas: ");
        prwSalida.println(iVidas);
        // guardo en  linea Score
        prwSalida.println("Score: ");
        prwSalida.println(iScore);
        prwSalida.println("Hank: ");
        prwSalida.println(perTabla.getX());
        prwSalida.println(perTabla.getY());
        
        prwSalida.println("Bullet: ");
        prwSalida.println(proBola.getX());
        prwSalida.println(proBola.getY());
        prwSalida.println(iDireccionProyectil);
        
        
        int iCount = 1;
        prwSalida.println("Cajas:");
        for (Object objObjeto : lstCajas) {
            Brick briCaja  = (Brick) objObjeto;
            prwSalida.println(iCount + ")---------------------------");
            prwSalida.println(briCaja.getX());
            prwSalida.println(briCaja.getY());
            prwSalida.println(briCaja.getEstado());
            iCount++;
        }
        prwSalida.println("END");
        // cierro el archivo
    	prwSalida.close();
    }
//
    public void leeArchivo() throws IOException{
        
        BufferedReader brwEntrada;
        
    	try{
            // creo el objeto de entrada a partir de un archivo de texto
            brwEntrada = new BufferedReader(new FileReader("save_file.txt"));
    	} catch (FileNotFoundException e){
            // si marca error grabo las posiciones actuales
            grabaArchivo();
            // lo vuelvo a abrir porque el objetivo es leer datos
            brwEntrada = new BufferedReader(new FileReader("save_file.txt"));
    	}
        
        // con el archivo abierto leo los datos que estan guardados
        // primero saco el score que esta en la linea 1
        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
    	iVidas = Integer.parseInt(brwEntrada.readLine());
        
        // despues las vidas
        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
    	iScore = Integer.parseInt(brwEntrada.readLine());
        
        // Lee las posiciones y direccion que tenia Nena
        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
        perTabla.setX(Integer.parseInt(brwEntrada.readLine()));    
        perTabla.setY(Integer.parseInt(brwEntrada.readLine()));
        
        brwEntrada.readLine(); // lee el string de titulo para no procesarlo
        proBola.setX(Integer.parseInt(brwEntrada.readLine()));
        proBola.setY(Integer.parseInt(brwEntrada.readLine()));
        iDireccionProyectil = Integer.parseInt(brwEntrada.readLine());
        
        // Se actualiza la posicion de los caminadores
        brwEntrada.readLine(); // lee el string de titulo "Caminadores:"
        for (Object objObjeto : lstCajas){
            brwEntrada.readLine(); // lee el string de titulo para no procesarlo
            Brick briCaja = (Brick) objObjeto;
            briCaja.setX(Integer.parseInt(brwEntrada.readLine()));
            briCaja.setY(Integer.parseInt(brwEntrada.readLine()));
            briCaja.setEstado(Integer.parseInt(brwEntrada.readLine()));
        }
        
        brwEntrada.readLine(); // lee el string END
    	brwEntrada.close();
    }
}
