package put.iswd;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Michal on 13.10.2016.
 */
public class Parser {
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());

    public ProblemCase parseFile(String fileName) {
        ProblemCase result = new ProblemCase();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Encountered problem while parsing file:\n" + e.toString(), e);
            return null;
        }

        result.setN(scanner.nextInt());

        for (int i = 0; i < result.getN(); i++) {
            for (int j = 0; j < result.getN(); j++) {
                result.getWeights()[i][j] = scanner.nextInt();
            }
        }


        for (int i = 0; i < result.getN(); i++) {
            for (int j = 0; j < result.getN(); j++) {
                result.getDistances()[i][j] = scanner.nextInt();
            }
        }

        return result;

    }

    public static boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
