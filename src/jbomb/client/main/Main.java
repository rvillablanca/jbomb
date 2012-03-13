package jbomb.client.main;

import com.jme3.system.JmeContext.Type;
import jbomb.client.game.JBombClient;

public class Main {

    public static void main(String[] args) {
        String ip = null;
        if (args.length != 0)
            ip = args[0];
        new JBombClient(ip).start(Type.Display);
    }
}
