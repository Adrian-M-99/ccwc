package org.braavos;

import java.util.Scanner;

/**
 * Class for reading the standard input from the user.
 */
public class CommandReader {

    private static final String EXIT_COMMAND = "exit";

    public void read() {
        Scanner scanner = new Scanner(System.in);
        CommandHelper helper = new CommandHelper();
        System.out.println("Aloha! Please enter your command:");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase(EXIT_COMMAND)) {
            helper.processInput(input);
            System.out.println("\nPlease enter your command:");
            input = scanner.nextLine();
        }
    }
}