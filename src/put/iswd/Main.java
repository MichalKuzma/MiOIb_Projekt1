package put.iswd;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        ProblemCase problemCase = parser.parseFile("./data/qapdata/bur26a.dat");

//        System.out.println(problemCase.toString());
        
        double w = 0;        
        int counter = 0;
//        long startTimeNano = System.nanoTime();
        long startTime = System.currentTimeMillis();
        
        do {
            Model model = new Model(problemCase);
            model.randomSolution();
            w += model.getValueOfModel();
            counter++;
        } while (System.currentTimeMillis() - startTime < 1000);
        
//        long estimatedTimeNano = System.nanoTime() - startTimeNano;
        long estimatedTime = System.currentTimeMillis() - startTime;
        
//        System.out.println("Estimatet time in nanoseconds: " + Long.toString(estimatedTimeNano));
        System.out.println("Estimatet time in milliseconds: " + Long.toString(estimatedTime));
        System.out.println("Counter: " + Integer.toString(counter));
        System.out.println("Average time (milliseconds): "
                + Double.toString((double)estimatedTime/(double)counter));
        System.out.println("Average value of random : " + Double.toString((double)w/(double)counter));
        

//        HEURYSTICS
        w = 0.0;
        counter = 1000;
        for (int i = 0; i < counter; i++) {
                Model model = new Model(problemCase);
                model.heuristicsSolution();
                w += model.getValueOfModel();
        }
        System.out.println("Average value of heurys : " + Double.toString((double)w/(double)counter));
    
    
//        LOCAL SEARCHs
//        GREEDY
        w = 0.0;
        counter = 100;
        for (int i = 0; i < counter; i++) {
                Model model = new Model(problemCase);
                model.greedyLocalSearch();
                w += model.getValueOfModel();
        }
        System.out.println("Average value of greedy : " + Double.toString((double)w/(double)counter));
        
//        STEPPER
        w = 0.0;
        counter = 100;
        for (int i = 0; i < counter; i++) {
                Model model = new Model(problemCase);
                model.stepperLocalSearch();
                w += model.getValueOfModel();
        }
        System.out.println("Average value of stepper: " + Double.toString((double)w/(double)counter));
    }
}
