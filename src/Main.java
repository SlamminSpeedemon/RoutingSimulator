import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //System.out.println("Environment is being instantiated");
        Environment environment = new Environment(10,3);

        for (int i = 0; i < 10; i++) {
            environment.printConnection(i);
        }

        //System.out.println("The above completed");

        Scanner scan = new Scanner(System.in);
        //scan.nextLine();


        //environment.navigateEnvironment();


    }
}
