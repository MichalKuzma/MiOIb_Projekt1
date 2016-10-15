package put.iswd;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        ProblemCase problemCase = parser.parseFile("./data/qapdata/bur26a.dat");

//        System.out.println(problemCase.toString());
        
        long counter = 0;
//        long startTimeNano = System.nanoTime();
        long startTime = System.currentTimeMillis();
        
        do {
            Model model = new Model(problemCase);
            model.randomSolution();
            counter++;
        } while (System.currentTimeMillis() - startTime < 1000);
        
//        long estimatedTimeNano = System.nanoTime() - startTimeNano;
        long estimatedTime = System.currentTimeMillis() - startTime;
        
//        System.out.println("Estimatet time in nanoseconds: " + Long.toString(estimatedTimeNano));
        System.out.println("Estimatet time in milliseconds: " + Long.toString(estimatedTime));
        System.out.println("Counter: " + Long.toString(counter));
        System.out.println("Average time (milliseconds): "
                + Double.toString((double)estimatedTime/(double)counter));
        
//        System.out.println(model.toString());
//        
//        model.randomChange();
//        System.out.println(model.toString());
    }
}
