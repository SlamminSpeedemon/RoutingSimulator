import java.util.ArrayList;

public class Router {

    private ArrayList<String> interfaces; //put information coming/going in this
    private ArrayList<ArrayList> routingTable; // each arraylist within has these 3: target ip | interface num | cost metric
    private String routerName;
    private static ArrayList<String> totalRouters = new ArrayList<>();
    private int macAddress;
    private int ipAddress;

    private boolean recievedData;
    private boolean sentData;

    public Router(int numOfInterfaces, String name, int ipAddress) {
        interfaces = new ArrayList<>();
        routingTable = new ArrayList<>();

        for (int i = 0; i < numOfInterfaces; i++) {
            interfaces.add("");
        }

        totalRouters.add(name);
        routerName = name;

        macAddress = (int)(Math.random() * 999999999999.9); //999 billion
        this.ipAddress = ipAddress;

    }

    public Router(int numOfInterfaces, String name) {
        for (int i = 0; i < numOfInterfaces; i++) {
            interfaces.add("");
        }

        totalRouters.add(name);
        routerName = name;

        macAddress = (int)(Math.random() * 999999999999.9); //999 billion
    }

    public void processIncomingData() {
        //checks the data in the interface decides where to send it
        String dataPacket = "";
        String target = "";
        String source = "";
        String dataType = "none";
        String data = "";

        ArrayList<String> dataEntry = new ArrayList<>();

        ArrayList<String> interfaceCopy = interfaces;
        clearInterface();

        for (int i = 0; i < interfaceCopy.size(); i++) {
            dataEntry.clear();

            //substring's begin index is inclusive
            //substring's end index is exclusive

            dataPacket = interfaceCopy.get(i);
            if (dataPacket.length() < 1) {
                continue;
            }

            if (dataPacket.contains("-")) target = dataPacket.substring(0,dataPacket.indexOf("-"));

            if (dataPacket.substring(target.length() + 1, target.indexOf("-")).contains("-")) source = dataPacket.substring(target.length() + 1, target.indexOf("-"));

            if (dataPacket.substring(source.length() + 1, source.indexOf("-")).contains("-")) dataType = dataPacket.substring(source.length() + 1, source.indexOf("-"));

            if (dataPacket.substring(dataType.length() + 1).contains("-")) data = dataPacket.substring(dataType.length() + 1);

            //see if data has at its destination
            if (Integer.valueOf(target) == ipAddress) {
                //packet has reached destination
                System.out.println("Destination has Received packet for "+target+"\n\tPacket contents: "+dataPacket);
            }

            //below are the if statements that handle the data processing
            //todo maybe want to convert them to separate functions and call those instead

            if (dataType.equals("infoRequest")) {
                //record source ip and interface it came from
                //process source routing table
                //do not forward to any interface

                dataEntry.add(source);
                dataEntry.add(""+i);
                dataEntry.add("1");

                //set data to send back
                String returnDataString = "";
                for (ArrayList ipEntry : routingTable) {
                    returnDataString = returnDataString + ipEntry.get(0) + "||"; //ip
                    returnDataString = returnDataString + ipEntry.get(2) + " , "; //cost metric
                }

                //send data back
                interfaces.set(i, source+"-"+ ipAddress +"-"+"infoReturn"+returnDataString);


                //check to see if we already have incoming routing data
                //if not, or if it's a shorter hop, then add or replace it

                while (data.length() > 1) {
                    String currentSplice = data.substring(0,data.indexOf(","));
                    String incomingIp = currentSplice.substring(0,currentSplice.indexOf("|"));
                    String costMetricStr = currentSplice.substring(incomingIp.length()+2);
                    int costMetric = Integer.valueOf(costMetricStr) + 1;

                    boolean add = true;

                    for (ArrayList ipEntry : routingTable) {
                        if (ipEntry.get(0).equals(incomingIp)) {
                            //we already have an ip logged with this,
                            add = false;
                            //see if the new route is shorter
                            if(Integer.valueOf((String)ipEntry.get(2)) > costMetric) {
                                //new cost is less than previous one
                                add = true;
                                routingTable.remove(ipEntry); //hopefully this works?
                            }
                            break;
                        }
                    }

                    ArrayList<String> newEntry = new ArrayList<>();
                    if (add) {
                        newEntry.add(0, incomingIp); //ip
                        newEntry.add(1, ""+i + ""); //interface to send on
                        newEntry.add(2, costMetricStr); //cost
                    }

                    data = data.substring(currentSplice.length()+1);
                }

                //check to see if we should add the router that just sent the infoRequest
                boolean add = true;

                for (ArrayList ipEntry : routingTable) {
                    if (ipEntry.get(0).equals(dataEntry.get(0))) {
                        //we already have an ip logged with this,
                        add = false;
                        //see if the new route is shorter
                        if(Integer.parseInt((String)ipEntry.get(2)) > Integer.parseInt((String)dataEntry.get(2))) {
                            //new cost is less than previous one
                            add = true;
                            routingTable.remove(ipEntry); //hopefully this works?
                        }
                        break;
                    }
                }

                if (add) {
                    routingTable.add(dataEntry);
                }
            }

            if (dataType.equals("infoReturn")) {
                //record source ip and interface it came from
                //process source routing table
                //do not forward to any interface

                dataEntry.add(source);
                dataEntry.add(""+i);
                dataEntry.add("1");

                //no need to send data back

                //check to see if we already have incoming routing data
                //if not, or if it's a shorter hop, then add or replace it

                while (data.length() > 1) {
                    String currentSplice = data.substring(0,data.indexOf(","));
                    String incomingIp = currentSplice.substring(0,currentSplice.indexOf("|"));
                    String costMetricStr = currentSplice.substring(incomingIp.length()+2);
                    int costMetric = Integer.valueOf(costMetricStr) + 1;

                    boolean add = true;

                    for (ArrayList ipEntry : routingTable) {
                        if (ipEntry.get(0).equals(incomingIp)) {
                            //we already have an ip logged with this,
                            add = false;
                            //see if the new route is shorter
                            if(Integer.valueOf((String)ipEntry.get(2)) > costMetric) {
                                //new cost is less than previous one
                                add = true;
                                routingTable.remove(ipEntry); //hopefully this works?
                            }
                            break;
                        }
                    }

                    ArrayList<String> newEntry = new ArrayList<>();
                    if (add) {
                        newEntry.add(0, incomingIp); //ip
                        newEntry.add(1, ""+i + ""); //interface to send on
                        newEntry.add(2, costMetricStr); //cost
                    }

                    data = data.substring(currentSplice.length()+1);
                }

                //check to see if we should add the router that just sent the infoRequest
                boolean add = true;

                for (ArrayList ipEntry : routingTable) {
                    if (ipEntry.get(0).equals(dataEntry.get(0))) {
                        //we already have an ip logged with this,
                        add = false;
                        //see if the new route is shorter
                        if(Integer.valueOf((String)ipEntry.get(2)) > Integer.valueOf((String)dataEntry.get(2))) {
                            //new cost is less than previous one
                            add = true;
                            routingTable.remove(ipEntry); //hopefully this works?
                        }
                        break;
                    }
                }

                if (add) {
                    routingTable.add(dataEntry);
                }
            }

            if (dataType.equals("info")) {
                //send data packet where it needs to go
                int targetInterface = -1;

                target.trim();
                for (ArrayList<String> routingEntry:routingTable) {
                    if(target.equals(routingEntry.get(0))) {
                        //there is entry in routing table
                        if(Integer.valueOf(routingEntry.get(1)) >= 0) {
                            targetInterface = Integer.valueOf(routingEntry.get(1));
                        } else {
                            //we have received this source ip previously
                            //however the target interface is not set yet
                            //flood all interfaces with this ip, except for current interface
                            targetInterface = -2;
                        }
                    }
                }

                if (targetInterface > -1) {
                    interfaces.set(targetInterface, dataPacket);
                } else {
                    //flood all except for the one it came on
                    for (int j = 0; j < interfaces.size(); j++) {
                        if (i == j) {
                            //is same interface it came on
                        } else {
                            interfaces.set(j, dataPacket);
                        }
                    }
                }

                //analyze the source and if we don't have it, add it to the routing table with a cost of 99

                boolean add = true;

                dataEntry.add(0,source);
                dataEntry.add(1,""+i);
                dataEntry.add(2,""+Integer.MAX_VALUE);

                for (ArrayList ipEntry : routingTable) {
                    if (ipEntry.get(0).equals(dataEntry.get(0))) {//compare source ip
                        //we already have an ip logged with this,
                        add = false;
                        break;
                    }
                }

                if (add) {

                    routingTable.add(dataEntry);
                }
            }
        }

    }

    public void setInterfaces(ArrayList<String> incomingData) {
        for (String i : interfaces) {
            interfaces.remove(0);
        }
        interfaces = incomingData;
        System.out.println("Router " + routerName + " has received data " + incomingData);
    }

    public ArrayList<String> getInterfaces() {
        return interfaces;
    }

    public void clearInterface() {
        for (int i = 0; i < interfaces.size(); i++) {
            interfaces.set(i,"");
        }
    }

    public String getRouterName() {
        return routerName;
    }

    public ArrayList<ArrayList> getRoutingTable() {
        return routingTable;
    }
}
