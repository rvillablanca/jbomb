package jbomb.server.main;

import com.jme3.system.JmeContext.Type;
import jbomb.server.game.JBombServer;

public class Main {

    public static void main(String[] args) {
        String playersNumber = "four";
        boolean graphic = false;
        if (args.length != 0) {
            playersNumber = args[0];
            if ("true".equals(args[1]))
                graphic = true;
            else if ("false".equals(args[1]))
                graphic = false;
        }
        if (graphic)
            new JBombServer(playersNumber).start(Type.Display);
        else
            new JBombServer(playersNumber).start(Type.Headless);
    }
}
