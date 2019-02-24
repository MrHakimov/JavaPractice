package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) throws MdException, IOException {
        if (args.length == 2) {
            String inputFileName = args[0], outputFileName = args[1];
            FileMdSource inputFile = new FileMdSource(inputFileName);
            PrintWriter outputFile = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileName), StandardCharsets.UTF_8));
            MdParser parser = new MdParser(inputFile, outputFile);
            parser.parse();
            inputFile.close(); 
            outputFile.close();
        } else {
            System.err.println("Please enter names of 2 files!" +
                    "\nExample: \"input.txt\" \"output.txt\"");
        }
    }
}
