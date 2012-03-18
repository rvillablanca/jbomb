package jbomb.common.game.main;

public class Main {

    public static void main(String[] args) {
        try {
            boolean exception = true;
            if (args.length > 0) {
                if (args[0].equals("server")) {
                    if (args[1].equals("two") || args[1].equals("three") || args[1].equals("four")) {
                        if (args[2].equals("true") || args[2].equals("false")) {
                            jbomb.server.main.Main.main(new String[]{args[1], args[2]});
                            exception = false;
                        }
                    }
                } else if (args[0].equals("client")) {
                    if (args.length == 2) {
                        jbomb.client.main.Main.main(new String[]{args[1]});
                        exception = false;
                    }
                }
            }
            if (exception) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Usages:");
            System.out.println("java -jar jbomb.jar client [ip]");
            System.out.println("java -jar jbomb.jar server <two | three | four> <true | false>");
        }

    }
}
