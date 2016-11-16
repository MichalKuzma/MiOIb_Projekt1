package put.iswd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by al3x9 on 14.11.2016.
 */
public class AlgorithmTester {
    public void compareResultsTimes(String[] instances, FileWriter resultFile, long minTime, long minIterationNumber) throws IOException {
        Parser parser = new Parser();
        for (String instance : instances) {
            System.out.println("Testing data file: " + instance);

            ProblemCase problemCase = parser.parseFile(instance + ".dat");

            //        Optimal solution
            File solFile = new File(instance.concat(".sln"));
            if(!solFile.exists() || solFile.isDirectory()) {
                continue;
            }
            Scanner scanner = new Scanner(new File(instance.concat(".sln")));
            String optimalResult;
            String optimalSolution;
            try {
                int n = scanner.nextInt();

                if (n >= 100)
                    continue;

                optimalResult = Integer.toString(scanner.nextInt());
                optimalSolution = "";
                for (int i = 0; i < n; i++) {
                    optimalSolution = optimalSolution.concat(Integer.toString(scanner.nextInt()));
                    if (i < n - 1)
                        optimalSolution = optimalSolution.concat(" ");
                }
            } catch (InputMismatchException e) {
                continue;
            }

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

                resultFile.write(getResultTimeLine("Greedy", model, optimalResult, optimalSolution, time, instance));
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

                resultFile.write(getResultTimeLine("Steepest", model, optimalResult, optimalSolution, time, instance));
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

                resultFile.write(getResultTimeLine("Random", bestModel, optimalResult,
                        optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));

            //        Steepest time
//            counter = 0;
//            algorithmStartTime = System.currentTimeMillis();
//            do {
//                startTime = System.currentTimeMillis();
//
//                Model bestModel = new Model(problemCase);
//                bestModel.randomSolution();
//
//                do {
//                    Model model = new Model(problemCase);
//                    model.randomSolution();
//
//                    if (bestModel.getValueOfModel() > model.getValueOfModel()) {
//                        bestModel = model;
//                    }
//
//                    endTime = System.currentTimeMillis();
//                    time = endTime - startTime;
//                } while (avgTimeSteepest > time);
//
//                resultFile.write(getResultTimeLine("Random [Steepest time]", bestModel, optimalResult,
//                        optimalSolution, time, instance));
//                counter++;
//            } while ((counter < minIterationNumber)
//                    || (System.currentTimeMillis() - algorithmStartTime < minTime));

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

                resultFile.write(getResultTimeLine("Heuristics", bestModel, optimalResult,
                        optimalSolution, time, instance));
                counter++;
            } while ((counter < minIterationNumber)
                    || (System.currentTimeMillis() - algorithmStartTime < minTime));

            //        steepest time
//            counter = 0;
//            algorithmStartTime = System.currentTimeMillis();
//            do {
//                startTime = System.currentTimeMillis();
//
//                Model bestModel = new Model(problemCase);
//                bestModel.heuristicsSolution();
//
//                do {
//                    Model model = new Model(problemCase);
//                    model.heuristicsSolution();
//
//                    if (bestModel.getValueOfModel() > model.getValueOfModel()) {
//                        bestModel = model;
//                    }
//
//                    endTime = System.currentTimeMillis();
//                    time = endTime - startTime;
//                } while (avgTimeSteepest > time);
//
//                resultFile.write(getResultTimeLine("Heuristics [Steepest time]", bestModel, optimalResult,
//                        optimalSolution, time, instance));
//                counter++;
//            } while ((counter < minIterationNumber)
//                    || (System.currentTimeMillis() - algorithmStartTime < minTime));
        }

        resultFile.close();
    }

    public void compareGSInitResult(String[] instances, FileWriter resultFile, int iterationsNum) throws IOException {
        Parser parser = new Parser();
        for (String instance : instances) {
            ProblemCase problemCase = parser.parseFile(instance + ".dat");

            long counter;

            counter = 0;
            do {
                Model model;
                model = new Model(problemCase);
                model.greedyLocalSearch();
                model.getValueOfModel();

                resultFile.write(getGSInitResultLine("Greedy", model, instance));
                counter++;
            } while ((counter < iterationsNum));

            //        STEEPEST
            counter = 0;
            do {Model model;
                model = new Model(problemCase);
                model.steepestLocalSearch();
                model.getValueOfModel();
                resultFile.write(getGSInitResultLine("Steepest", model, instance));
                counter++;
            } while ((counter < iterationsNum));
        }
        resultFile.close();
    }

    public void multiRandom(String[] instances, FileWriter resultFile, int maxIterationsNum) throws IOException {
        Parser parser = new Parser();
        for (String instance : instances) {
            ProblemCase problemCase = parser.parseFile(instance + ".dat");

            Model model;

            long counter;
            double avgScore = 0.0;
            double bestScore = -1.0;

            counter = 0;
            do {
                model = new Model(problemCase);
                model.greedyLocalSearch();
                int score = model.getValueOfModel();
                avgScore += (double) score;
                if (bestScore == -1.0 || score < bestScore)
                    bestScore = score;

                counter++;
                resultFile.write(getMultiRandomLine("Greedy", bestScore, "Best", instance, counter));
                resultFile.write(getMultiRandomLine("Greedy", avgScore / (double) counter, "Average", instance, counter));
            } while ((counter < maxIterationsNum));

            bestScore = -1.0;
            //        STEEPEST
            counter = 0;
            avgScore = 0.0;
            do {
                model = new Model(problemCase);
                model.steepestLocalSearch();
                int score = model.getValueOfModel();
                avgScore += (double) score;
                if (bestScore == -1.0 || score < bestScore)
                    bestScore = score;
                counter++;
                resultFile.write(getMultiRandomLine("Steepest", bestScore, "Best", instance, counter));
                resultFile.write(getMultiRandomLine("Steepest", avgScore / (double) counter, "Average", instance, counter));
            } while ((counter < maxIterationsNum));
        }
        resultFile.close();
    }

    public void resultSimilarity(String[] instances, FileWriter resultFile, int numIterations) throws IOException {
        Parser parser = new Parser();
        for (String instance : instances) {
            ProblemCase problemCase = parser.parseFile(instance.concat(".dat"));

            Model model;

            long counter;

            List<int[]> results = new ArrayList<>();
            List<Integer> scores = new ArrayList<>();

            counter = 0;
            do {
                model = new Model(problemCase);
                model.steepestLocalSearch();
                counter++;
                results.add(model.getSolution());
                scores.add(model.getValueOfModel());
            } while ((counter < numIterations));

            for (int i = 0; i < numIterations; i++) {
                for (int j = i + 1; j < numIterations; j++) {
                    resultFile.write(getResultSimilarityLine(results.get(i), results.get(j),
                            scores.get(i), scores.get(j), instance));
                }
            }
        }
        resultFile.close();
    }

    private static int solutionSimilarity(int[] sol1, int[] sol2) {
        assert sol1.length == sol2.length;
        int score = 0;
        for (int i = 0; i < sol1.length; i++) {
            if (sol1[i] == sol2[i])
                score += 1;
        }
        return score;
    }

    private static String getResultTimeLine(String algorithmName, Model model,
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
        builder.append(";");

        //initial permutation
        builder.append(model.getInitialSolution());
        builder.append(";");

        //reviewed neighbours
        builder.append(model.getReviewedNeighbours());
        builder.append(";");

        //steps counter
        builder.append(model.getStepsCounter());
        builder.append("\n");

        return builder.toString();
    }

    private static String getGSInitResultLine(String algorithmName, Model model, String instanceName) {
        StringBuilder builder = new StringBuilder();

        //algorithm name
        builder.append(algorithmName);
        builder.append(";");

        //instance name
        builder.append(instanceName);
        builder.append(";");

        //achieved result
        builder.append(model.getValueOfModel());
        builder.append(";");

        //initial result
        builder.append(model.getValueOfInitSolution());
        builder.append("\n");

        return builder.toString();
    }

    private static String getMultiRandomLine(String algorithmName, double result, String resultType, String instanceName, long counter) {
        StringBuilder builder = new StringBuilder();

        //algorithm name
        builder.append(algorithmName);
        builder.append(";");

        //instance name
        builder.append(instanceName);
        builder.append(";");

        //result
        builder.append(result);
        builder.append(";");

        //result type
        builder.append(resultType);
        builder.append(";");

        //iteration number
        builder.append(counter);
        builder.append("\n");

        return builder.toString();
    }

    private static String getResultSimilarityLine(int[] sol1, int[] sol2, int score1, int score2, String instanceName) {
        StringBuilder builder = new StringBuilder();

        //permutation 1
        builder.append(Model.perm2String(sol1));
        builder.append(";");

        //permutation 2
        builder.append(Model.perm2String(sol2));
        builder.append(";");

        //similarity
        builder.append(solutionSimilarity(sol1, sol2));
        builder.append(";");

        //score 1
        builder.append(score1);
        builder.append(";");

        //score 2
        builder.append(score2);
        builder.append(";");

        //instance name
        builder.append(instanceName);
        builder.append("\n");

        return builder.toString();
    }
}
