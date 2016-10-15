/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
            ret += " ";
        }
        return ret;
    }

    public int getValueOfModel() {
        if (!modelEvaluated) {
            modelEvaluated = true;
            
            //TODO obliczenie wartosci modelu
            valueOfModel = 0;
        }
        
        return valueOfModel;
    }
            
    public int[] getSolution() {
        return solution;
    }
}
