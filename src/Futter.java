import java.util.Random;

public class Futter {
    private final int posX;
    private final int posY;

    public Futter(){
        posX = generierePosition(Grafiken.WEITE);
        posY = generierePosition(Grafiken.HOEHE);
    }

    private int generierePosition(int size){
        Random zufall = new Random();
        return zufall.nextInt(size / Grafiken.TICK_GROESSE) * Grafiken.TICK_GROESSE;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
