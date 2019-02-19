import java.util.Scanner;

/**
 * @author: Muhammadjon Hakimov
 * created: 23.01.2019 17:10:23
 */

public class Main {

    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        Scanner input = new Scanner(System.in);

        System.out.println("Type \'exit\' to exit programm.");

        String currentCommand;
        while (input.hasNextLine()) {
            currentCommand = input.nextLine();

            if (currentCommand.equals("exit")) {
                break;
            }

            String[] inputSeparated = currentCommand.split("\\s");
            if (inputSeparated.length > 0 && inputSeparated.length < 3) {
                String operation = inputSeparated[0];

                if (inputSeparated.length == 1) {
                    if (operation.equals("dir")) {
                        fileManager.dir();
                    } else {
                        System.out.println("Operation is uncorrect!!!");
                    }
                }

                if (inputSeparated.length == 2) {
                    String name = inputSeparated[1];

                    switch (operation) {
                        case "rm":
                            fileManager.rm(name);
                            break;
                        case "cd":
                            fileManager.cd(name);
                            break;
                        case "create":
                            fileManager.create(name);
                            break;
                        case "mkdir":
                            fileManager.mkdir(name);
                            break;
                        case "rmdir":
                            fileManager.rmdir(name, "");
                            break;
                        default:
                            System.out.println("Operation is uncorrect!!!");
                            break;
                    }
                }
            }
        }
    }
}
