package put.iswd;

import java.io.*;

public class Main {

    private static final String INSTANCE_PATH = "data/qapdata/";
    private static final String[] resultTimeInstances = new String[] {
        "chr12a", // size = 12, sparse
        "nug12", // size = 12
        "rou20", // size = 20
        "nug25", // size = 25
        "bur26e", // size = 26
        "bur26g", // size  26
        "bur26h", // size = 26
        "nug30" // size = 30
    };

    private static final String[] initResultInstances = new String[] {
            "bur26e", // size = 26
            "bur26g", // size  26
            "bur26h" // size = 26
    };

    private static final String[] multiRandomInstances = new String[] {
            "bur26e", // size = 26
            "bur26g" // size  26
    };

    public static void main(String[] args)
            throws FileNotFoundException, IOException {
        
//        String instance = args[0];
        long minTime = Long.parseLong(args[1]) * 1000;
        long minIterationNumber = Long.parseLong(args[2]);

        for (int i = 0; i < resultTimeInstances.length; i++) {
            resultTimeInstances[i] = INSTANCE_PATH.concat(resultTimeInstances[i]);
        }
        for (int i = 0; i < initResultInstances.length; i++) {
            initResultInstances[i] = INSTANCE_PATH.concat(initResultInstances[i]);
        }
        for (int i = 0; i < multiRandomInstances.length; i++) {
            multiRandomInstances[i] = INSTANCE_PATH.concat(multiRandomInstances[i]);
        }
        
        AlgorithmTester tester = new AlgorithmTester();
        tester.compareResultsTimes(resultTimeInstances, new FileWriter("qap_algorithms.csv"),
                minTime, minIterationNumber);
        tester.compareGSInitResult(initResultInstances, new FileWriter("gs_init_result.csv"), 200);
        tester.multiRandom(multiRandomInstances, new FileWriter("multi_random.csv"), 350);
    }
}
