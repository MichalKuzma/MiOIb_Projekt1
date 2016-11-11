package put.iswd;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

        Path filePath = Paths.get(fileName);
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, ENCODING);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Encountered problem while parsing file:\n" + e.toString(), e);
            return null;
        }

        int lineIndex = 0;

        boolean nSet = false;
        while (!nSet && lineIndex < lines.size()) {
            String trimmedLine = lines.get(lineIndex).trim();
            if (isInt(trimmedLine)) {
                result.setN(Integer.parseInt(trimmedLine));
                nSet = true;
            }
            lineIndex += 1;
        }

        int weightsLinesSet = 0;
        while (weightsLinesSet < result.getN() && lineIndex < lines.size()) {
            String trimmedLine = lines.get(lineIndex).trim();
            String[] parts = trimmedLine.split(" ");
            String[] nums = new String[result.getN()];
            int partNum = 0;
            for (String part : parts)
                if (isInt(part)) {
                    nums[partNum] = part;
                    partNum += 1;
                }
            if (partNum == result.getN()) {
                for (int i = 0; i < result.getN(); i++) {
                    result.getWeights()[i][weightsLinesSet] = Integer.parseInt(nums[i]);
                }
                weightsLinesSet += 1;
            }
            lineIndex += 1;
        }

        int distancesLinesSet = 0;
        while (distancesLinesSet < result.getN() && lineIndex < lines.size()) {
            String trimmedLine = lines.get(lineIndex).trim();
            String[] parts = trimmedLine.split("\\s+");
            boolean valid = true;
            if (parts.length != result.getN())
                valid = false;
            for (String part : parts)
                if (!isInt(part))
                    valid = false;
            if (valid) {
                for (int i = 0; i < parts.length; i++) {
                    result.getDistances()[i][distancesLinesSet] = Integer.parseInt(parts[i]);
                }
                distancesLinesSet += 1;
            }
            lineIndex += 1;
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
