package put.iswd;

/**
 * Created by Michal on 13.10.2016.
 */
public class ProblemCase {

    private int n;
    private int[][] weights;
    private int[][] distances;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        weights = new int[n][n];
        distances = new int[n][n];
    }

    public int[][] getWeights() {
        return weights;
    }

    public int[][] getDistances() {
        return distances;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Integer.toString(n));
        builder.append("\n\n");
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                builder.append(Integer.toString(weights[x][y]));
                builder.append(" ");
            }
            builder.append("\n");
        }
        builder.append("\n");
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                builder.append(Integer.toString(distances[x][y]));
                builder.append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
