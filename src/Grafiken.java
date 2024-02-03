import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grafiken extends JPanel implements ActionListener {

    static final int WEITE = 800;
    static final int HOEHE = 800;
    static final int TICK_GROESSE = 50; // Ticks pro Sekunde
    static final int SPIELBRETT_GROESSE = (WEITE * HOEHE) / (TICK_GROESSE * TICK_GROESSE);

    final Font Schriftart = new Font("TimesRoman", Font.BOLD, 30);

    int[] snakePosX = new int[SPIELBRETT_GROESSE]; // Schlangenenposition X Achse
    int[] snakePosY = new int[SPIELBRETT_GROESSE]; // Schlangenenposition Y Achse
    int snakeLaenge;

    Futter futter;
    int futterGegessen; // Zähler für gegessenes Futter

    // Sounds
    static String futterSound = "Sounds/essen.wav";
    static String spielende = "Sounds/spielende.wav";

    boolean istBewegung = false; // Boolean ob Schlange in Bewegung
    int delay = 100; // Spielgeschwindigkeit durch Verzögerung in Millisekunden
    final Timer timer = new Timer(delay,this);

    char Richtung = 'R';

    public Grafiken(){
        this.setPreferredSize(new Dimension(WEITE, HOEHE)); // Spielfeldgröße
        this.setBackground(Color.WHITE); // Hintergrundfarbe
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (istBewegung){

                    // Definition der Bewegungsrichtung
                    // Verhindern dass sich Schlange nicht in entgegengesetzter Richtung Bewegen kann
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_LEFT:
                            if(Richtung != 'R'){
                                Richtung = 'L';
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if(Richtung != 'L'){
                                Richtung = 'R';
                            }
                            break;
                        case KeyEvent.VK_UP:
                            if(Richtung != 'D'){
                                Richtung = 'U';
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if(Richtung != 'U'){
                                Richtung = 'D';
                            }
                            break;
                    }
                } else {
                    // nach Spielabbruch neustarten
                    start();
                }
            }
        });
        start();
    }

    protected void start(){
        snakePosX = new int[SPIELBRETT_GROESSE];
        snakePosY = new int[SPIELBRETT_GROESSE];
        snakeLaenge = 4; // Startlänge der Schlange
        futterGegessen = 0;
        Richtung = 'R';
        istBewegung = true;
        erzeugeFutter();
        timer.start();
        Sounds.hintergrundmusik();
    }

    // Für mehrzeiligen Text
    private void drawString(Graphics g, String text, int x, int y) {
        int lineHeight = g.getFontMetrics().getHeight();
        for (String line : text.split("\n"))
            g.drawString(line, x, y += lineHeight);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(istBewegung){

            g.setColor(Color.RED);
            g.fillOval(futter.getPosX(), futter.getPosY(), TICK_GROESSE, TICK_GROESSE);

            g.setColor(Color.DARK_GRAY);
            for(int i = 0; i < snakeLaenge; i++){
                g.fillRect(snakePosX[i], snakePosY[i], TICK_GROESSE, TICK_GROESSE);
            }
        } else {
            String punkteText = String.format("Spielende! Punkte: %d ... \nZum erneuten Spielstart, beliebige Taste drücken.",futterGegessen);
            g.setColor(Color.black);
            g.setFont(Schriftart);
            drawString(g, punkteText, 20,20);
            Sounds.stopHintergrundmusik();
            Sounds.play(spielende);
        }
    }

    protected void bewegung() {
        for (int i = snakeLaenge; i > 0; i--){
            snakePosX[i] = snakePosX[i-1];
            snakePosY[i] = snakePosY[i-1];
        }
        // Festlegen der Richtung in die sich die Schlange pro Tick bewegt
        switch (Richtung){
            case 'U' -> snakePosY[0] -= TICK_GROESSE;
            case 'D' -> snakePosY[0] += TICK_GROESSE;
            case 'L' -> snakePosX[0] -= TICK_GROESSE;
            case 'R' -> snakePosX[0] += TICK_GROESSE;
        }
    }

    // neues Futter generieren
    protected void erzeugeFutter(){
        futter = new Futter();
    }

    protected void futterEssen() {
        // wenn die Position der Schlange X und Y genau gleich der Position des Futters ist
        if ((snakePosX[0]==futter.getPosX()) && (snakePosY[0]==futter.getPosY())){
            Sounds.play(futterSound);
            snakeLaenge++; // Länge der Schlange um 1 erhöhen
            futterGegessen++; // Zähler für gegessenes Futter erhöhen
            erzeugeFutter(); // neues Futter erzeugen
        }
    }

    protected void kollisionsTest(){
        // Spielabbruch bei Kollision mit sich selbst
        for(int i = snakeLaenge; i > 0; i--){
            if ((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])){
                istBewegung = false;
                break;
            }
        }

        // Spielabbruch weil Schlange außerhalb Spielfeld
        if (snakePosX[0] < 0 || snakePosX[0] > WEITE - TICK_GROESSE || snakePosY[0] < 0 || snakePosY[0] > HOEHE - TICK_GROESSE){
            istBewegung = false;
        }

        if(!istBewegung){
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(istBewegung){
            bewegung();
            kollisionsTest();
            futterEssen();
        }
        repaint();
    }
}
