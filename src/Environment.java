import java.util.ArrayList;

public class Environment {

    //generates the virtual environment in which all the routers, clients, and hosts live

    //private int rows;
    //private int cols;
    //private String[][] matrix;

    private ArrayList<Router> routerList;
    private ArrayList<ArrayList<Integer>> connections;//spot 0's arraylist correlates with router 0's interface connections
    //for above, each arraylist within is an int that has the router list index number
    //example Router 0 would be stored as 0, the 0 corresponds to first router stored in the routerList


    public Environment(int numOfRouters, int interfacesPerRouter) {
        //rows = n;
        //cols = n;
        //generateMatrix();

        routerList = new ArrayList<>();

        for (int i = 0; i < numOfRouters; i++) {
            routerList.add(new Router(interfacesPerRouter, "Router "+i,i));
        }

        setUpEnvironment();
    }

    public void setUpEnvironment() {
        //auto generate connections between routers


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
        int remainingConnections; //connections left to assign on current router
        int chosenRouter = startRouterNum;

        while (routersToConnect.size() > 0) {

            for (int faceConnection = 0; faceConnection <  connections.get(currentRouter).size(); faceConnection++) {
                //faceConnection is the interface number we are linking

                if (connections.get(currentRouter).get(faceConnection) <= Integer.MIN_VALUE+1) {
                    //this connection was already done by previous
                    continue;
                }

                //update remainingconnections for current router
                remainingConnections = 0;
                for (int port = 0; port < connections.get(currentRouter).size(); port++) {
                    if (connections.get(currentRouter).get(port) != Integer.MIN_VALUE) {
                        remainingConnections++;
                    }
                }

                if (remainingConnections == 0) {
                    //all connections are already linked
                    break;
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
                    int randomNum = (int) (Math.random() * routersToConnect.size());
                    chosenRouter = routersToConnect.get(randomNum);
                    routersToConnect.remove(randomNum);
                }

                //in chosen router connect the 1st interface to this interface
                connections.get(chosenRouter).set(0,currentRouter); //need to set it to the first open interface, rather than the first one
                connections.get(currentRouter).set(faceConnection,chosenRouter);
            }

            //chosen router should be one with an additional interface, make that the new current router
            currentRouter = chosenRouter;

        }
        //at this point all routers have been made, given a number, and have been given interface connections

        //todo make clients and hosts
    }

    public void processTick() {
        //forwards data between connections then goes through each router and calls the process function
        ArrayList<ArrayList<String>> interfaceHolder = new ArrayList<>();
        ArrayList<String> interfaceHolderEntry;

        for (int i = 0; i < routerList.size(); i++) {
            interfaceHolderEntry = routerList.get(i).getInterfaces();
            for (int j = 0; j < connections.get(i).size(); j++) {
                //todo implement
            }


        }


        for (Router i : routerList) {
            i.processIncomingData();
        }

    }

    public void setUpEnvironment(int numOfRouters, int topology) {
        //todo --> add different topologies
    }

//    private void generateMatrix() {
//        matrix = new String[rows][cols];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                matrix[i][j] = "_";
//            }
//        }
//    }
//
//    public void printMatrix() {
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                System.out.print(matrix[i][j] + "\t");
//            }
//            System.out.println();
//        }
//    }
//
//    public void setMatrix(int row, int col, String input) {
//        matrix[row][col] = input;
//    }
//
//    public String getMatrixVal(int row, int col) {
//        return matrix[row][col];
//    }
//
//    public String[][] getMatrix() {
//        return matrix;
//    }

}
