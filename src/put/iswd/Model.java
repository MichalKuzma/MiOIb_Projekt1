package put.iswd;

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
    
    public Model(ProblemCase problemCase) {
        mProblemCase = problemCase;
        n = mProblemCase.getN();
        solution = new int[n];
        modelEvaluated = false;
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
        
        getValueOfModel();
        boolean end = false;
        
        while (!end) {
            for (int i = 0; i < n-1; i++) {
                for (int j = i+1; j < n; j++) {
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
    
    public void stepperLocalSearch() {
        
        randomSolution();
        
        int pom = getValueOfModel();
        int best = 0;
        int bestI = 0;
        int bestJ = 0;
        
        boolean end = false;
        
        while (!end) {
            for (int i = 0; i < n-1; i++) {
                for (int j = i+1; j < n; j++) {
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
        String ret = "";
        for (int i = 0; i < n; i++) {
            ret += Integer.toString(solution[i]);
            if (i < n-1) {
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
            
            valueOfModel = 0;
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    valueOfModel += (mProblemCase.getWeights()[i][j]
                            * mProblemCase.getDistances()
                            [solution[i]][solution[j]]);
                }
            }
        }
        
        return valueOfModel;
    }
            
    public int[] getSolution() {
        return solution;
    }

    public int getN() {
        return n;
    }
}
