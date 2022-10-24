import java.util.ArrayList;
import java.util.Dictionary;

public class Router {

    private ArrayList<String> interfaces; //put information coming/going in this
    private ArrayList<ArrayList> routingTable; // each arraylist within has these 3: target ip | interface num | cost metric
    private String routerName;
    private static ArrayList<String> numOfRouters;
    private int macAddress;
    private int ip;

    public Router(int numOfInterfaces, String name, int ipAddress) {
        for (int i = 0; i < numOfInterfaces; i++) {
            interfaces.add("");
        }

        numOfRouters.add(name);
        routerName = name;

        macAddress = (int)(Math.random() * 999999999999.9); //999 billion
        ip = ipAddress;
    }

    public Router(int numOfInterfaces, String name) {
        for (int i = 0; i < numOfInterfaces; i++) {
            interfaces.add("");
        }

        numOfRouters.add(name);
        routerName = name;

        macAddress = (int)(Math.random() * 999999999999.9); //999 billion
    }

    public void processIncomingData() {
        //checks the data in the interface decides where to send it
        String dataPacket = "";
        String target = "";
        String source = "";
        String dataType = "";
        String data = "";

        ArrayList<String> dataEntry = new ArrayList<>();

        for (int i = 0; i < interfaces.size(); i++) {
            dataEntry.clear();

            //substring's begin index is inclusive
            //substring's end index is exclusive

            dataPacket = interfaces.get(i);
            target = dataPacket.substring(0,dataPacket.indexOf("-"));
            source = dataPacket.substring(target.length() + 1, target.indexOf("-"));
            dataType = dataPacket.substring(source.length() + 1, source.indexOf("-"));
            data = dataPacket.substring(dataType.length() + 1);

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
                interfaces.set(i, source+"-"+ip+"-"+"infoReturn"+returnDataString);



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
                        newEntry.add(1, i + ""); //interface to send on
                        newEntry.add(2, costMetricStr); //cost
                    }

                    data = data.substring(currentSplice.length()+1);
                }
                routingTable.add(dataEntry);




            }



        }

    }

    public void setInterfaces(ArrayList<String> incomingData) {
        interfaces = incomingData;
    }

    public ArrayList<String> getInterfaces() {
        return interfaces;
    }

    public void clearInterface() {
        for (int i = 0; i < interfaces.size(); i++) {
            interfaces.set(i,"");
        }
    }
}
