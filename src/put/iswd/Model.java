package put.iswd;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Michał
 */
public class Model {
    
    private final ProblemCase mProblemCase;
    private final int[] solution;
    private final int n;
    private int valueOfModel;
    private boolean modelEvaluated;
    private int[] initialSolution;
    private int reviewedNeighbours;
    private int stepsCounter;
    
    public Model(ProblemCase problemCase) {
        mProblemCase = problemCase;
        n = mProblemCase.getN();
        solution = new int[n];
        modelEvaluated = false;
        initialSolution = new int[n];
    }
    
    public void randomSolution() {
        boolean[] helper = new boolean[n];
        for (int i = 0; i < n; i++) {
            helper[i] = false;
        }

        Random random = new Random();
        
        for (int i = n; i > 0; i--) {
            int r = random.nextInt(n);
            
            while (helper[r]) {
                r++;
                r %= n;
            }
            
            solution[i-1] = r;
            helper[r] = true;
        }
    }
    
    public void heuristicsSolution() {
        
        randomSolution();
        
        getValueOfModel();
        
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                int changeValue = valueOfChanging2Items(i, j);
                if (changeValue < 0) {
                    change(i, j);                
                    valueOfModel += changeValue;
                }
            }
        }
    }
    
    public void greedyLocalSearch() {
        
        randomSolution();
        System.arraycopy(this.solution, 0, initialSolution, 0, this.n);
        
        getValueOfModel();
        boolean end = false;

        reviewedNeighbours = 0;
        stepsCounter = 0;
        while (!end) {
            stepsCounter = getStepsCounter() + 1;
            for (int i = 0; i < n-1; i++) {
                for (int j = i+1; j < n; j++) {
                    reviewedNeighbours = getReviewedNeighbours() + 1;
                    int changeValue = valueOfChanging2Items(i, j);
                    if (changeValue < 0) {
                        change(i, j);                
                        valueOfModel += changeValue;
                        end = false;
                        break;
                    } else {
                        end = true;
                    }
                }
                if (!end) {
                    break;
                }
            }
        }
    }
    
    public void steepestLocalSearch() {
        
        randomSolution();
        System.arraycopy(this.solution, 0, this.initialSolution, 0, this.n);
        
        int pom = getValueOfModel();
        int best = 0;
        int bestI = 0;
        int bestJ = 0;
        
        boolean end = false;

        reviewedNeighbours = 0;
        stepsCounter = 0;
        while (!end) {
            stepsCounter += 1;
            for (int i = 0; i < n-1; i++) {
                for (int j = i+1; j < n; j++) {
                    reviewedNeighbours += 1;
                    int changeValue = valueOfChanging2Items(i, j);
                    if (changeValue < best) {
                        best = changeValue;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }
            
            if (best < 0) {
                change(bestI, bestJ);                
                valueOfModel += best;
                best = 0;
                end = false;
            } else {
                end = true;
            }
        }
    }
    
    public void simulatedAnnealing() {
        randomSolution();
        System.arraycopy(this.solution, 0, this.initialSolution, 0, this.n);
        
        int pom = getValueOfModel();
        int noChangeCounter = 0;
        
        boolean end = false;

        reviewedNeighbours = 0;
        stepsCounter = 0;
        
        double temperature = (double)calculateStartTemperature();
        while (!end) {
            //Markow chain (L = n*n)
            for (int k = 0 ; k < n*n; k++) {
                stepsCounter += 1;

                //rand neighbour
                Random random = new Random();

                int i1 = random.nextInt(n);
                int i2 = random.nextInt(n-1);
                if (i2 >= i1) {
                    i2++;
                }
                if (i2 == n) {
                    i2 = 0;
                }

                //make change
                int changeValue = valueOfChanging2Items(i1, i2);
                if ((double)changeValue < temperature) {
                    change(i1, i2);                
                    valueOfModel += changeValue;
                    noChangeCounter = 0;
                } else {
                    noChangeCounter++;
                }
            }
            
            //stop conditions (P = 0, change of getting worse solution = 0% during last markow chain
            if ((noChangeCounter > 10*n*n) || (temperature < 1.0)) {
                end = true;
            }
            
            //calculate new temperature (alpha = 0.95)
            temperature = 0.95 * temperature;
        }
    }
    
    public int calculateStartTemperature() {
        int temperature = 0;
        int sampleSize = 1000;
        
        int[] values = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            randomSolution();
            modelEvaluated = false;
            getValueOfModel();
            
            Random random = new Random();
        
            int i1 = random.nextInt(n);
            int i2 = random.nextInt(n-1);
            if (i2 >= i1) {
                i2++;
            }
            if (i2 == n) {
                i2 = 0;
            }
            
            values[i] = valueOfChanging2Items(i1, i2);
        }
        
        Arrays.sort(values);
        
        temperature = values[sampleSize * 95 / 100];
        
        if (temperature < 0) temperature = 0;
        
        return temperature;
    }
    
    public void randomChange() {
        Random random = new Random();
        
        int i1 = random.nextInt(n);
        int i2 = random.nextInt(n-1);
        if (i2 >= i1) {
            i2++;
        }
        if (i2 == n) {
            i2 = 0;
        }
        
        change(i1, i2);
    }
    
    private void change(int i1, int i2) {
        int temp = solution[i1];
        solution[i1] = solution[i2];
        solution[i2] = temp;
    }
    
    @Override
    public String toString() {
        return perm2String(solution);
    }

    public static String perm2String(int[] perm) {
        String ret = "";
        for (int i = 0; i < perm.length; i++) {
            ret += Integer.toString(perm[i]);
            if (i < perm.length-1) {
                ret += " ";
            }
        }
        return ret;
    }
    
    private int valueOfChanging2Items(int x, int y) {
        
        int w = 0;
        
        for (int i = 0; i < n; i++) {
            w -= (mProblemCase.getWeights()[x][i]
                    * mProblemCase.getDistances()[solution[x]][solution[i]]);
            w -= (mProblemCase.getWeights()[i][x]
                    * mProblemCase.getDistances()[solution[i]][solution[x]]);
            w -= (mProblemCase.getWeights()[y][i]
                    * mProblemCase.getDistances()[solution[y]][solution[i]]);
            w -= (mProblemCase.getWeights()[i][y]
                    * mProblemCase.getDistances()[solution[i]][solution[y]]);
        }
        w += (mProblemCase.getWeights()[x][x]
                * mProblemCase.getDistances()[solution[x]][solution[x]]);
        w += (mProblemCase.getWeights()[x][y]
                * mProblemCase.getDistances()[solution[x]][solution[y]]);
        w += (mProblemCase.getWeights()[y][x]
                * mProblemCase.getDistances()[solution[y]][solution[x]]);
        w += (mProblemCase.getWeights()[y][y]
                * mProblemCase.getDistances()[solution[y]][solution[y]]);
        
        change(x, y);
        for (int i = 0; i < n; i++) {
            w += (mProblemCase.getWeights()[x][i]
                    * mProblemCase.getDistances()[solution[x]][solution[i]]);
            w += (mProblemCase.getWeights()[i][x]
                    * mProblemCase.getDistances()[solution[i]][solution[x]]);
            w += (mProblemCase.getWeights()[y][i]
                    * mProblemCase.getDistances()[solution[y]][solution[i]]);
            w += (mProblemCase.getWeights()[i][y]
                    * mProblemCase.getDistances()[solution[i]][solution[y]]);
        }
        w -= (mProblemCase.getWeights()[x][x]
                * mProblemCase.getDistances()[solution[x]][solution[x]]);
        w -= (mProblemCase.getWeights()[x][y]
                * mProblemCase.getDistances()[solution[x]][solution[y]]);
        w -= (mProblemCase.getWeights()[y][x]
                * mProblemCase.getDistances()[solution[y]][solution[x]]);
        w -= (mProblemCase.getWeights()[y][y]
                * mProblemCase.getDistances()[solution[y]][solution[y]]);
        change(x, y);
        
        return w;
    }

    public int getValueOfModel() {
        if (!modelEvaluated) {
            modelEvaluated = true;
            
            valueOfModel = getValueOfPermutation(solution);
        }
        
        return valueOfModel;
    }

    public int getValueOfInitSolution() {
        return getValueOfPermutation(initialSolution);
    }

    private int getValueOfPermutation(int[] permutation) {
        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result += (mProblemCase.getWeights()[i][j]
                        * mProblemCase.getDistances()
                        [permutation[i]][permutation[j]]);
            }
        }

        return result;
    }
            
    public int[] getSolution() {
        return solution;
    }

    public int getN() {
        return n;
    }

    public String getInitialSolution() {
        String ret = "";
        for (int i = 0; i < n; i++) {
            ret += Integer.toString(initialSolution[i]);
            if (i < n-1) {
                ret += " ";
            }
        }
        return ret;
    }

    public int getReviewedNeighbours() {
        return reviewedNeighbours;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }
}
