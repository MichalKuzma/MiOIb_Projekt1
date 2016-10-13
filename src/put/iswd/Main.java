package put.iswd;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        ProblemCase problemCase = parser.parseFile("./data/qapdata/bur26a.dat");

        System.out.println(problemCase.toString());

    }
}
