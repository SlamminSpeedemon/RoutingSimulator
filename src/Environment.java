import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Environment {

    //generates the virtual environment in which all the routers, clients, and hosts live

    //private int rows;
    //private int cols;
    //private String[][] matrix;

    private ArrayList<Router> routerList;
    private ArrayList<ArrayList<Integer>> connections;//spot 0's arraylist correlates with router 0's interface connections
    //for above, each arraylist within is an int that has the router list index number
    //example Router 0 would be stored as 0, the 0 corresponds to first router stored in the routerList
    private int maxInterfaceInRouter;


    public Environment(int numOfRouters, int interfacesPerRouter) throws InterruptedException {
        //rows = n;
        //cols = n;
        //generateMatrix();

        connections = new ArrayList<>();

        routerList = new ArrayList<>();

        maxInterfaceInRouter = interfacesPerRouter;

        for (int i = 0; i < numOfRouters; i++) {
            routerList.add(new Router(interfacesPerRouter, "Router "+i,i));
        }

        //System.out.println("Environment is being set up");
        setUpEnvironment();
    }

    public void navigateEnvironment() {
        Scanner getInput = new Scanner(System.in);
        System.out.println("What do you want to do?\n\t1-view all router connections\n\t2-view router's connection");
        int decision = getInput.nextInt();

        switch (decision) {
            case 1:
                for (int i = 0; i < routerList.size(); i++) {
                    printConnection(i);
                }
                break;
            case 2:
                System.out.println("What router would you like to see? Max is " + (connections.size()-1));
                printConnection(getInput.nextInt());
                break;
            default:
                System.out.println("Invalid input");
        }
    }
    public void setUpEnvironment() throws InterruptedException {
        //auto generate connections between routers


        //decide how many of the existing interfaces will be connected to other routers for each router
        int randInt;
        Random randomizer = new Random();
        ArrayList<Integer> connectionsAdd;
        for (int i = 0; i < routerList.size(); i++) {
            connectionsAdd = new ArrayList<>();


            randInt = randomizer.nextInt(maxInterfaceInRouter)+1;
            //System.out.println("\t\tGenerating random int --> "+randInt);


            for (int adder = 0; adder < randInt; adder++) {
                connectionsAdd.add(Integer.MIN_VALUE);
            }

            connections.add(connectionsAdd);
        }

        //System.out.println("Connections has been made to be " + connections.size() + " long");
        for (int i = 0; i < connections.size(); i++) {
            System.out.println("\tRouter "+i+" will have " + connections.get(i).size()+" conections");
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

        int runsCounter = 0;

        System.out.println("Going to start connecting routers with "+routersToConnect.size()+" routers to connect");

        System.out.println("\n\t\t\tHere is routersToConnect list");
        for (int i : routersToConnect) {
            System.out.println("\t\t\t\t"+i);
        }

        while (routersToConnect.size() > 0 && runsCounter < 999) {

            runsCounter++;

            for (int faceConnection = 0; faceConnection <  connections.get(currentRouter).size(); faceConnection++) {
                //faceConnection is the interface number we are linking

                if (connections.get(currentRouter).get(faceConnection) > Integer.MIN_VALUE+1) {
                    //this connection was already done by previous
                    System.out.println("Skipping connection");
                    continue;
                }

                //update remainingconnections for current router
                remainingConnections = 0;
                for (int port = 0; port < connections.get(currentRouter).size(); port++) {
                    if (connections.get(currentRouter).get(port) == Integer.MIN_VALUE) {
                        remainingConnections++;
                    }
                }

                if (remainingConnections == 0) {
                    //all connections are already linked
                    System.out.println("\tall connections are linked for " + currentRouter);
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

                    if (chosenRouter < 0) {
                        System.out.println("\tCouldn't choose a router with 2+ connections " + chosenRouter + "\n\t\tMaking a random router have an additional connection");

                        if (routersToConnect.size() > 0) {
                            chosenRouter = routersToConnect.get((int) (Math.random() * routersToConnect.size()));

                            if(connections.get(chosenRouter).size() < 2) {
                                connections.get(chosenRouter).add(Integer.MIN_VALUE);
                            }

                            System.out.println("Added connection to router: " + chosenRouter);
                        } else {
                            System.out.println("\tNo routers left to connect... skipping");
                            break;
                        }



                    } else {
                        System.out.println("\tChose a router with 2+ connections " + chosenRouter);
                    }
                } else {
                    //choose any router
                    int randomNum = (int) (Math.random() * routersToConnect.size());
                    chosenRouter = routersToConnect.get(randomNum);
                    routersToConnect.remove(randomNum);

                    System.out.println("\tChose a random router " + chosenRouter);
                }

                //in chosen router connect the 1st interface to this interface
                for (int i = 0; i < connections.get(chosenRouter).size(); i++) {
                    //go through all connections in chosen router
                    if (connections.get(chosenRouter).get(i) < 0) {
                        //find the first open port, connect it
                        connections.get(chosenRouter).set(i,currentRouter);
                        break;
                    }
                }
                connections.get(currentRouter).set(faceConnection,chosenRouter);
                System.out.println("\tConnections updated");
            }

            //chosen router should be one with an additional interface, make that the new current router
            currentRouter = chosenRouter;
            System.out.println(routersToConnect.size() + " routers left to connect");

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

    public void printConnection(int routerNum) {
        if (connections.size() >= routerNum) {
            System.out.println(routerList.get(routerNum).getRouterName() + " is connected on "+connections.get(routerNum).size()+" interfaces\n");
            for (int i = 0; i < connections.get(routerNum).size(); i++) {
                System.out.println("\t Port "+(i)+" - goes to router " + connections.get(routerNum).get(i));
            }
        } else {
            System.out.println("Router "+routerNum+" does not exist");
        }
        System.out.println();

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
