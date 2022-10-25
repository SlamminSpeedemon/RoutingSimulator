import java.util.ArrayList;

public class Environment {

    //generates the nxn matrix in which all the routers, clients, and hosts live

    //TODO in the future maybe at a GUI with circles and lines

    private int rows;
    private int cols;

    private String[][] matrix;

    private ArrayList<Router> routerList;
    private ArrayList<ArrayList> connections;//spot 0's arraylist correlates with router 0's interface connections
    //for above, each arraylist within is a string that has the router name


    public Environment(int n) {
        rows = n;
        cols = n;
        generateMatrix();
    }

    public void setUpEnvironment(int numOfRouters) {
        //auto generate connections between routers
        routerList = new ArrayList<>();

        for (int i = 0; i < numOfRouters; i++) {
            routerList.add(new Router(4, "Router "+i,i));
        }





    }

    public void setUpEnvironment(int numOfRouters, int topology) {
        //todo --> add different topologies
    }

    private void generateMatrix() {
        matrix = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = "_";
            }
        }
    }

    public void printMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void setMatrix(int row, int col, String input) {
        matrix[row][col] = input;
    }

    public String getMatrixVal(int row, int col) {
        return matrix[row][col];
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public Environment(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        generateMatrix();
    }
}
