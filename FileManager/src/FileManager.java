import java.io.File;
import java.io.IOException;

/**
 * @author: Muhammadjon Hakimov
 * created: 23.01.2019 17:12:12
 */

public class FileManager {
    private static String path = System.getProperty("user.dir");

    private static String getAbsolutePath(String fileName) {
        return path + System.getProperty("file.separator") + fileName;
    }

    public static void dir() {
        System.out.println("Current directory: " + path);
    }

    public static void rm(String fileName) {
        try {
            File currentFile = new File(getAbsolutePath(fileName));
            if (currentFile.delete()) {
                System.out.println("File " + fileName + " was deleted successfully from current directory.");
            } else {
                System.out.println("File " + fileName + " doesn't exist in the current directory, " +
                        "or some error occurred while deleting " +
                        "(maybe permission denied)!!!");
            }
        } catch (NullPointerException e) {
            System.out.println("File's name is NULL!!!");
        }
    }

    public static void cd(String directoryName) {
        String newPath = getAbsolutePath(directoryName);
        try {
            File newFile = new File(newPath);
            if (newFile.exists() && newFile.isDirectory()) {
                path = newPath;
                dir();
            } else {
                System.out.println("Directory with the name You entered doesn't exist " +
                        "or some error occurred while entering to it!!!");
            }
        } catch (NullPointerException e) {
            System.out.println("File's name is NULL!!!");
        }
    }

    public static void create(String fileName) {
        try {
            File newFile = new File(getAbsolutePath(fileName));
            try {
                if (!newFile.createNewFile()) {
                    System.out.println("File " + fileName + " is exist!!!");
                } else {
                    System.out.println("File " + fileName + " created successfully!");
                }
            } catch (IOException e) {
                System.out.println("Some error occurred while creating file " +
                        fileName + "!!!");
            }
        } catch (NullPointerException e) {
            System.out.println("File's name is NULL!!!");
        }
    }

    public static void mkdir(String directoryName) {
        File newDirectory = new File(getAbsolutePath(directoryName));

        if (!newDirectory.exists()) {
            boolean result = false;

            try {
                newDirectory.mkdir();
                result = true;
            } catch (SecurityException e) {
                System.out.println("Directory can't be created!!!");
            }

            if (result) {
                System.out.println("Directory " + directoryName + " created successfully!!!");
            } else {
                System.out.println("Directory can't be created! Some error occurred!!!");
            }
        }
    }

    public static void rmdir(String directoryName, String additionalPath) {
        File currentDirectory = new File(getAbsolutePath(directoryName));

        if (!currentDirectory.exists()) {
            System.out.println("Directory " + directoryName + " doesn't exist!!!");
        }

        File[] files = currentDirectory.listFiles();
        if (files != null) {
            additionalPath = directoryName;
            for (File file: files) {
                if (file.isDirectory()) {
                    String mainAddPath = additionalPath;
                    String newPath = additionalPath;
                    System.out.println(newPath);
                    rmdir(file.getName(), newPath);
                    additionalPath = mainAddPath;
                } else {
                    rm(getAbsolutePath(file.getName()));
                }
            }
        }

        currentDirectory.delete();
    }
}
