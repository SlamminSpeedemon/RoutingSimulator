import java.util.ArrayList;

public class Environment {

    //generates the virtual environment in which all the routers, clients, and hosts live

    private int rows;
    private int cols;

    private String[][] matrix;

    private ArrayList<Router> routerList;
    private ArrayList<ArrayList<Integer>> connections;//spot 0's arraylist correlates with router 0's interface connections
    //for above, each arraylist within is an int that has the router list index number
    //example Router 0 would be stored as 0, the 0 corresponds to first router stored in the routerList


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

        //decide how many of the existing interfaces will be connected to other routers for each router
        int randInt;
        ArrayList<Integer> connectionsAdd = new ArrayList<>();
        for (int i = 0; i < routerList.size(); i++) {
            randInt = (int) (Math.random() * 4) + 1;
            connectionsAdd.clear();

            for (int adder = 0; adder < randInt; adder++) {
                connectionsAdd.add(Integer.MIN_VALUE);
            }

            connections.add(connectionsAdd);
        }

        //connect connections between routers

        //search router list for first router with only 1 interface

        int startRouterNum = -1;
        int minRequirement = 1;
        while (startRouterNum == -1) {
            for (int i = 0; i < connections.size(); i++) {
                if (connections.get(i).size() == minRequirement) {
                    startRouterNum = i;
                    break;
                }
            }
            if (startRouterNum == -1) minRequirement++;
        }


        //create list of all other routers' nums
        ArrayList<Integer> routersToConnect = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++) {
            if (i != startRouterNum) routersToConnect.add(i);
        }


        int currentRouter = startRouterNum;
        int remainingConnections = connections.get(startRouterNum).size(); //connections left to assign on current router
        int chosenRouter, routersLeft;

        while (routersToConnect.size() > 0) {
            routersLeft = routersToConnect.size();

            for (int faceConnection = 0; faceConnection <  connections.get(currentRouter).size(); faceConnection++) {
                //faceConnection is the interface number we are linking

                //update remainingconnections for current router
                remainingConnections = 0;
                for (int port = 0; port < connections.get(currentRouter).size(); port++) {
                    if (connections.get(currentRouter).get(port) != Integer.MIN_VALUE) {
                        remainingConnections++;
                    }
                }

                chosenRouter = -1;

                //choose a router
                if (remainingConnections == 1) {
                    //chose router with 2+ interfaces
                    for (int i = 0; i < routersToConnect.size(); i++) {
                        if (connections.get(routersToConnect.get(i)).size() >= 2) {
                            chosenRouter = routersToConnect.get(i);
                            routersToConnect.remove(i);
                            break;
                        }
                    }
                } else {
                    //choose any router

                }

                //in chosen router connect the 1st interface to this interface
                connections.get(chosenRouter).set(0,faceConnection);
                connections.get(currentRouter).set(faceConnection,chosenRouter);
            }





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
