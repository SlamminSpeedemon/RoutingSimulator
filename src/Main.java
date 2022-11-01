import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //System.out.println("Environment is being instantiated");
        Environment environment = new Environment(20,3);

        for (int i = 0; i < 20; i++) {
            environment.printConnection(i);
        }

        //System.out.println("The above completed");

        Scanner scan = new Scanner(System.in);
        //scan.nextLine();


        //environment.navigateEnvironment();

        UI ui = new UI(environment.getRouterList(), environment.getConnections());
        ui.startComponents();

        while(true) {
            environment.navigateEnvironment();
        }


    }
}
