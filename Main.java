import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Main {
    public static void main(String[] args) {
        new Vent().setVisible(true);
    }
}



class Linea {
    private double lenVariationMax;
    private int lenMax,bR,bG,bB;
    private double uR,uG,uB,D;
    private double lenVariationThreshold;
    private double angle;
    private double angleThreshold;
    private double thirdBranchChance;

    //constructores
    public Linea() {
        this(7.0/10, Math.PI/6, 1.0/2, 1.0/2, 1.0/2);
    }
    public Linea(double lenVariation, double angle) {
        this(lenVariation, angle, 1.0/2, 1.0/2, 1.0/2);
    }
    public Linea(double lenVariation, double angle, double lenVariationThreshold) {
        this(lenVariation, angle, lenVariationThreshold, 1.0/2, 1.0/2);
    }
    public Linea(double lenVariation, double angle, double lenVariationThreshold, double thirdBranchChance) {
        this(lenVariation, angle, lenVariationThreshold, thirdBranchChance, 1.0/2);
    }
    //constructor con la configuracion del arbol
    public Linea(double lenVariation, double angle, double lenVariationThreshold, double thirdBranchChance, double angleThreshold) {
        //porcentaje maximo de variacion de la longitud de una rama
        this.lenVariationMax = lenVariation;
        //angulo base entre las ramas
        this.angle = angle;
        //porcentaje minimo de variacion de la longitud de una rama
        this.lenVariationThreshold = lenVariationThreshold;
        //probabilidad de crear una tercera rama
        this.thirdBranchChance = thirdBranchChance;
        //variacion de los angulos
        this.angleThreshold = angleThreshold;
    }

    public void paint(Graphics2D g, int len){
        //si la longitud de la rama es 0 
        if (len <= 0) return;

        //Define el grosor de la linea
        g.setStroke(new BasicStroke(len*(3/20.0f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        //asigna color a la linea
        int pasos = (int)((-(D/lenMax)*len) + D);
        g.setColor(new Color((int)(bR+(uR*pasos)),(int)(bG+(uG*pasos)),(int)(bB+(uB*pasos))));
        //pinta la linea y se mueve al extremo
        g.drawLine(0, 0, 0, -len);
        g.translate(0, -len);
        //define los angulos entre las lineas
        double angle1 = angle + angle * angleThreshold * (Math.random() * 2 - 1);
        double angle2 = angle + angle * angleThreshold * (Math.random() * 2 - 1);
        double angle3 = angle * angleThreshold * (Math.random() * 2 - 1);
        //agrega una tercera rama
        boolean terceraRama = Math.random() < thirdBranchChance;
        if (terceraRama) {
            g.rotate(angle3);
            paint(g, longitudRama(len));
            g.rotate(-angle3);
        }

        //apunta en la direccion de la siguiente linea
        g.rotate(angle1);
        paint(g, longitudRama(len));
        
        //apunta en la direccion de la rama de al lado ?
        g.rotate(-angle1 - angle2);
        //g.rotate(-2*angle);
        paint(g, longitudRama(len));

        //Devuelve el puntero a la posicion y direccion originales
        g.rotate(angle2);
        g.translate(0, len);

    }
    public void setMax(int Max){
        this.lenMax = Max;
    }
    public void setColores(int bR,int bG,int bB,int eR,int eG,int eB){
        //RGB del color base del arbol
        this.bR = bR;
        this.bG = bG;
        this.bB = bB;
        double u = Math.sqrt(Math.pow(eR, 2)+Math.pow(eG, 2)+Math.pow(eB, 2));
        D = u;
        //Vector unitario RGB que apunta al color de las puntas
        this.uR = (eR-bR)/u;
        this.uG = (eG-bG)/u;
        this.uB = (eB-bB)/u;
    }
    //metodo para variar la longitud de la rama
    private int longitudRama(int len) {
        double max = this.lenVariationMax;
        double min = max * (1 - this.lenVariationThreshold);
        double aleatoreidad = Math.random() * (max - min)/max;
        double retorno = len * (min + aleatoreidad);
        return (int)retorno;
    }
}


class Vent extends JFrame{
  public Vent(){

        int tamanio = 500;
        setSize(tamanio,tamanio);

        Linea Arbol = new Linea(1, Math.PI/8, 0.6);
        //Linea Arbol = new Linea(0.8, Math.PI/8, 0);
        //creacion de un panel redefiniendo su metodo paint
        JPanel panel = new JPanel(){
        public void paint (Graphics g){
            super.paint(g);
            //ubicacion del arbol
            g.translate(this.getWidth()/2, this.getHeight());
            //longitud maxima de las ramas del arbol
            Arbol.setMax(this.getHeight()/8);
            //asignacion de los colores en componentes rgb
            Arbol.setColores(68, 64, 26, 237, 97, 185);
            //pintado del arbol
            Arbol.paint((Graphics2D)g, this.getHeight()/8);
            //reposicionamiento
            g.translate(-this.getWidth()/2, -this.getHeight());
        }
        };
        
        setContentPane(panel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}