package bsuir.client.clientconsole;

import java.util.Scanner;

public class CommandReader {
    private final Scanner scan = new Scanner(System.in);

    public String getCommand(){
        return scan.nextLine();
    }
}
