package put.iswd;

import java.io.*;
import java.util.Scanner;

public class Main {

    private static final String INSTANCE_PATH = "data/qapdata/";
    private static final String[] instances = new String[] {
        "chr12a", // size = 12, sparse
        "nug12", // size = 12
        "rou20", // size = 20
        "nug25", // size = 25
        "bur26e", // size = 26
        "bur26g", // size  26
        "bur26h", // size = 26
        "nug30", // size = 30
        "tai100b", // size = 100
        "tai256c" // size = 256, funny
    };

    public static void main(String[] args)
            throws FileNotFoundException, IOException {
        
//        String instance = args[0];
        long minTime = Long.parseLong(args[1]) * 1000;
        long minIterationNumber = Long.parseLong(args[2]);

        FileWriter resultFile = new FileWriter("qap_algorithms.csv");
        
        for (String instance : instances) {
            instance = INSTANCE_PATH.concat(instance);
            Parser parser = new Parser();
            ProblemCase problemCase = parser.parseFile(instance + ".dat");

            //        Optimal solution
            Scanner scanner = new Scanner(new File(instance.concat(".sln")));
            int n = scanner.nextInt();
            String optimalResult = Integer.toString(scanner.nextInt());
            String optimalSolution = "";
            for (int i = 0; i < n; i++) {
                optimalSolution = optimalSolution.concat(Integer.toString(scanner.nextInt()));
                if (i < n - 1)
                    optimalSolution = optimalSolution.concat(" ");
            }

//            BufferedReader br = new BufferedReader(
//                    new FileReader(instance + ".sln"));
//            String lines = "";
//            String line;
//            while ((line = br.readLine()) != null) {
//                lines = lines + line + " ";
//            }
//            lines = lines.trim().replaceAll(" +", " ");
//            String[] numbers = lines.split(" ");
//
//            String optimalResult = numbers[1];
//            String optimalSolution = "";
//            for (int i = 2; i < numbers.length; i++) {
//                optimalSolution += numbers[i];
//                if (i < numbers.length - 1) {
//                    optimalSolution += " ";
//                }
//            }

            //        Algorithms
            long counter, innerCounter;
            long startTime, endTime;
            long algorithmStartTime;
            double time;

            //        GREEDY
            counter = 0;
            double avgTimeGreedy = 0.0;
            algorithmStartTime = System.currentTimeMillis();
            do {
                startTime = System.currentTimeMillis();

                Model model;
                innerCounter = 0;
                do {
                    model = new Model(problemCase);
                    model.greedyLocalSearch();
                    model.getValueOfModel();

                    endTime = System.currentTimeMillis();
                    innerCounter += 1;
                } while (endTime - startTime < 2);

                time = (double) (endTime - startTime) / innerCounter;
                avgTimeGreedy += time;

                resultFile.write(getResults("Greedy", model, optimalResult, optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));
            avgTimeGreedy /= counter;

            //        STEEPEST
            counter = 0;
            double avgTimeSteepest = 0;
            algorithmStartTime = System.currentTimeMillis();
            do {
                startTime = System.currentTimeMillis();

                Model model;
                innerCounter = 0;
                do {
                    model = new Model(problemCase);
                    model.steepestLocalSearch();
                    model.getValueOfModel();

                    endTime = System.currentTimeMillis();
                    innerCounter += 1;
                } while (endTime - startTime < 2);


                time = (double) (endTime - startTime) / innerCounter;
                avgTimeSteepest += time;

                resultFile.write(getResults("Steepest", model, optimalResult, optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));
            avgTimeSteepest /= counter;

            //        RANDOM
            //        greedy time
            counter = 0;
            algorithmStartTime = System.currentTimeMillis();
            do {
                startTime = System.currentTimeMillis();

                Model bestModel = new Model(problemCase);
                bestModel.randomSolution();

                do {
                    Model model = new Model(problemCase);
                    model.randomSolution();

                    if (bestModel.getValueOfModel() > model.getValueOfModel()) {
                        bestModel = model;
                    }

                    endTime = System.currentTimeMillis();
                    time = endTime - startTime;
                } while (avgTimeGreedy > time);

                resultFile.write(getResults("Random [Greedy time]", bestModel, optimalResult,
                        optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));

            //        Steepest time
            counter = 0;
            algorithmStartTime = System.currentTimeMillis();
            do {
                startTime = System.currentTimeMillis();

                Model bestModel = new Model(problemCase);
                bestModel.randomSolution();

                do {
                    Model model = new Model(problemCase);
                    model.randomSolution();

                    if (bestModel.getValueOfModel() > model.getValueOfModel()) {
                        bestModel = model;
                    }

                    endTime = System.currentTimeMillis();
                    time = endTime - startTime;
                } while (avgTimeSteepest > time);

                resultFile.write(getResults("Random [Steepest time]", bestModel, optimalResult,
                        optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));

            //        HEURISTICS
            //        greedy time
            counter = 0;
            algorithmStartTime = System.currentTimeMillis();
            do {
                startTime = System.currentTimeMillis();

                Model bestModel = new Model(problemCase);
                bestModel.heuristicsSolution();

                do {
                    Model model = new Model(problemCase);
                    model.heuristicsSolution();

                    if (bestModel.getValueOfModel() > model.getValueOfModel()) {
                        bestModel = model;
                    }

                    endTime = System.currentTimeMillis();
                    time = endTime - startTime;
                } while (avgTimeGreedy > time);

                resultFile.write(getResults("Heuristics [Greedy time]", bestModel, optimalResult,
                        optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));

            //        steepest time
            counter = 0;
            algorithmStartTime = System.currentTimeMillis();
            do {
                startTime = System.currentTimeMillis();

                Model bestModel = new Model(problemCase);
                bestModel.heuristicsSolution();

                do {
                    Model model = new Model(problemCase);
                    model.heuristicsSolution();

                    if (bestModel.getValueOfModel() > model.getValueOfModel()) {
                        bestModel = model;
                    }

                    endTime = System.currentTimeMillis();
                    time = endTime - startTime;
                } while (avgTimeSteepest > time);

                resultFile.write(getResults("Heuristics [Steepest time]", bestModel, optimalResult,
                        optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));
        }

        resultFile.close();
    }
    
    public static String getResults(String algorithmName, Model model,
                                    String optimalResult, String optimalSolution, double time, String instanceName) {
        StringBuilder builder = new StringBuilder();

        //algorithm name
        builder.append(algorithmName);
        builder.append(";");

        //achieved result
        builder.append(model.getValueOfModel());
        builder.append(";");

        //optimal result
        builder.append(optimalResult);
        builder.append(";");

        //found permutation
        builder.append(model.toString());
        builder.append(";");

        //optimal permutation
        builder.append(optimalSolution);
        builder.append(";");

        //running time in milliseconds
        builder.append(time);
        builder.append(";");

        //instance size
        builder.append(model.getN());
        builder.append(";");

        //instance name
        builder.append(instanceName);
        builder.append("\n");

        return builder.toString();
    }
}
