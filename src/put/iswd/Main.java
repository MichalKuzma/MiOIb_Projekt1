package put.iswd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String INSTANCE_PATH = "data/qapdata/";
//    private static final String[] resultTimeInstances = new String[] {
//        "chr12a", // size = 12, sparse
//        "nug12", // size = 12
//        "rou20", // size = 20
//        "nug25", // size = 25
//        "bur26e", // size = 26
//        "bur26g", // size  26
//        "bur26h", // size = 26
//        "nug30" // size = 30
//    };

    private static final String[] initResultInstances = new String[] {
            "bur26e", // size = 26
            "bur26g", // size  26
            "bur26h" // size = 26
    };

    private static final String[] multiRandomInstances = new String[] {
            "bur26e", // size = 26
            "bur26g" // size  26
    };

    private static final String[] resultSimilarityInstances = new String[] {
            "had12",
            "nug14"
    };

    public static void main(String[] args)
            throws FileNotFoundException, IOException {
        
//        String instance = args[0];
        long minTime = Long.parseLong(args[1]) * 1000;
        long minIterationNumber = Long.parseLong(args[2]);

//        for (int i = 0; i < resultTimeInstances.length; i++) {
//            resultTimeInstances[i] = INSTANCE_PATH.concat(resultTimeInstances[i]);
//        }
        for (int i = 0; i < initResultInstances.length; i++) {
            initResultInstances[i] = INSTANCE_PATH.concat(initResultInstances[i]);
        }
        for (int i = 0; i < multiRandomInstances.length; i++) {
            multiRandomInstances[i] = INSTANCE_PATH.concat(multiRandomInstances[i]);
        }
        for (int i = 0; i < resultSimilarityInstances.length; i++) {
            resultSimilarityInstances[i] = INSTANCE_PATH.concat(resultSimilarityInstances[i]);
        }

        List<String> resultTimeInstances = new ArrayList<String>();


        File[] files = new File(INSTANCE_PATH).listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".dat")) {
                String fileName = file.getName();
                resultTimeInstances.add(
                        INSTANCE_PATH.concat(fileName.substring(0, fileName.length() - 4)));
            }
        }
        
        AlgorithmTester tester = new AlgorithmTester();
//        tester.compareResultsTimes(resultTimeInstances.toArray(new String[resultTimeInstances.size()]),
//                new FileWriter("qap_algorithms.csv"), minTime, minIterationNumber);
//        tester.compareGSInitResult(initResultInstances, new FileWriter("gs_init_result.csv"), 200);
//        tester.multiRandom(multiRandomInstances, new FileWriter("multi_random.csv"), 350);
        tester.resultSimilarity(resultSimilarityInstances, new FileWriter("results_similarity.csv"), 10);
    }
}
