/**
 * BFT Dti implementation (interactive client).
 *
 */
package dti.bftdti;

import java.io.Console;
import java.io.IOException;

public class BFTDtiInteractiveClient {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Use: java BFTDtiServer <client id>");
            System.exit(-1);
        }
        int clientId = args[0];
        BFTDti<Integer, String> bftDti = new BFTDti<>(clientId);

        Console console = System.console();

        System.out.println("\nCommands:\n");
        System.out.println("\MY_COINS: Insert value into the map");
        System.out.println("\MINT: Retrieve value from the map");
        System.out.println("\SPEND: Retrieve the size of the map");

        while (true) {
            String cmd = console.readLine("\n  > ");

            if (cmd.equalsIgnoreCase("MY_COINS")) {

                System.out.println("\Your coins:" + bftDti.myCoins(clientId) + "\n");
                
            }         
        }
    }

}
