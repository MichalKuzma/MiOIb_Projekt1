package put.iswd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args)
            throws FileNotFoundException, IOException {
        
        String instance = args[0];
        long minTime = Long.parseLong(args[1]) * 1000;
        long minIterationNumber = Long.parseLong(args[2]);
        
        Parser parser = new Parser();
        ProblemCase problemCase = parser.parseFile(instance + ".dat");
        
//        Optimal solution
        BufferedReader br = new BufferedReader(
                new FileReader(instance + ".sln"));
        String lines = "";
        String line;
        while ((line = br.readLine()) != null) {
            lines = lines + line + " ";
         }
        lines = lines.trim().replaceAll(" +", " ");
        String[] numbers = lines.split(" ");
        
        String optimalResult = numbers[1];
        String optimalSolution = "";
        for (int i = 2; i < numbers.length; i++) {
            optimalSolution += numbers[i];
            if (i < numbers.length - 1) {
                optimalSolution += " ";
            }
        }
        
//        Algorithms      
        long counter;
        long startTime, endTime;
        long algorithmStartTime, time;

//        GREEDY
        counter = 0;
        long avgTimeGreedy = 0;
        algorithmStartTime = System.currentTimeMillis();
        do {
            startTime = System.currentTimeMillis();
            
            Model model = new Model(problemCase);
            model.greedyLocalSearch();
            model.getValueOfModel();
            
            endTime = System.currentTimeMillis();
            
            time = endTime - startTime;
            avgTimeGreedy += time;

            printData("Greedy", model, optimalResult, optimalSolution, time);
            counter++;
        } while((counter < minIterationNumber)
                || (System.currentTimeMillis() - algorithmStartTime < minTime));
        avgTimeGreedy /= counter;
        
//        STEPPER
        counter = 0;
        long avgTimeStepper = 0;
        algorithmStartTime = System.currentTimeMillis();
        do {
            startTime = System.currentTimeMillis();
            
            Model model = new Model(problemCase);
            model.stepperLocalSearch();
            model.getValueOfModel();
            
            endTime = System.currentTimeMillis();
            
            time = endTime - startTime;
            avgTimeStepper += time;

            printData("Steeper", model, optimalResult, optimalSolution, time);
            counter++;
        } while((counter < minIterationNumber)
                || (System.currentTimeMillis() - algorithmStartTime < minTime));
        avgTimeStepper /= counter;
        
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
            
            printData("Random [Greedy time]", bestModel, optimalResult,
                    optimalSolution, time);
            counter++;
        } while((counter < minIterationNumber)
                || (System.currentTimeMillis() - algorithmStartTime < minTime));
        
//        stepper time
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
            } while (avgTimeStepper > time);
            
            printData("Random [Stepper time]", bestModel, optimalResult,
                    optimalSolution, time);
            counter++;
        } while((counter < minIterationNumber)
                || (System.currentTimeMillis() - algorithmStartTime < minTime));

//        HEURYSTICS
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
            
            printData("Heuristics [Greedy time]", bestModel, optimalResult,
                    optimalSolution, time);
            counter++;
        } while((counter < minIterationNumber)
                || (System.currentTimeMillis() - algorithmStartTime < minTime));
        
//        stepper time
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
            } while (avgTimeStepper > time);
            
            printData("Heuristics [Stepper time]", bestModel, optimalResult,
                    optimalSolution, time);
            counter++;
        } while((counter < minIterationNumber)
                || (System.currentTimeMillis() - algorithmStartTime < minTime));
    }
    
    public static void printData(String algorithmName, Model model,
            String optimalResult, String optimalSolution, long time) {
        
        //algorithm name
        System.out.print(algorithmName);
        System.out.print(";");

        //achieved result
        System.out.print(model.getValueOfModel());
        System.out.print(";");

        //optimal result
        System.out.print(optimalResult);
        System.out.print(";");

        //founded permutation
        System.out.print(model.toString());
        System.out.print(";");

        //optimal permutation
        System.out.print(optimalSolution);
        System.out.print(";");

        //running time in milliseconds
        System.out.print(time);
        System.out.print(";");
        
        //instance size
        System.out.print(model.getN());
        System.out.println("");
    }
}
