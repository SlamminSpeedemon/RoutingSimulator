import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UI extends JComponent {

    //put each router in a circle
    //place lines to symbolize connections
        //during processing the lines can change color when button is pressed to
            //pressing button closes current screen and opens a new one with stuff

    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Router> routerList;
    private ArrayList<ArrayList<Integer>> connections;
    private ArrayList<ArrayList<Integer>> routerConnectPts;

    private ArrayList<JLabel> routerJLabels;

    private JFrame window;

    public void paint(Graphics g) {
        for (Line l : lines) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setStroke(new BasicStroke(2));

            g.setColor(l.color);
            g.drawLine(l.x1, l.y1, l.x2, l.y2);
        }
    }

    public UI (ArrayList<Router> listOfRouters, ArrayList<ArrayList<Integer>> listOfConnections ) {
        routerList = listOfRouters;
        connections = listOfConnections;

        routerJLabels = new ArrayList<>();
        routerConnectPts = new ArrayList<>();

        // creating object of JFrame(Window popup)
        window = new JFrame();

        // setting closing operation
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setting size of the pop window
        window.setBounds(620, 100, 700, 700);
    }

    public void startComponents() {
        //have the router list, place them in circle
        //use connections list to get cords for lines between them
            //make Line objects and add to lines list

        for (Router r : routerList) {
            JLabel temp = new JLabel();
            temp.setText(" " + r.getRouterName());
            temp.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));

            routerJLabels.add(temp);
        }


        for (JLabel j : routerJLabels) {
            window.add(j);
        }


        double circleRadius = 250;
        int centerX = window.getWidth()/2 - 50;
        int centerY = window.getHeight()/2 - 50;
        double stepSize = (Math.PI * 2)/routerJLabels.size();

        //start at right and go clockwise

        for (int i = 0; i < routerJLabels.size(); i++) {
            int x = (int)(centerX + circleRadius * Math.cos(stepSize * i));
            int y = (int)(centerY + circleRadius * Math.sin(stepSize * i));

            routerJLabels.get(i).setBounds(x,y,70,30);
            ArrayList<Integer> temp = new ArrayList<>();

            //find what point to anchor lines from using step size and router number i
            int xCorrect = 0;
            int yCorrect = 0;

            //TODO correct the lines so that they don't criss cross over routers
            //read above to get idea for implementation

            temp.add(x + xCorrect);
            temp.add(y + yCorrect);
            routerConnectPts.add(temp);
        }

        // setting canvas for draw
        window.getContentPane().add(this);

        // set visibility
        window.setVisible(true);

        generateLines();
    }

    public void generateLines() {
        ArrayList<ArrayList<Integer>> doneConnections = new ArrayList<>(); //keeps track of already done connections, so it is not done twice
        //each slot in main list is router, the array list of ints within it represents the connections already done

        for (ArrayList<Integer> lol : connections) {
            doneConnections.add(new ArrayList<>());
        }


        for (int i = 0; i < connections.size(); i++) {//i is the current router
            ArrayList<Integer> currentConnects = connections.get(i);

            for (int j = 0; j < currentConnects.size(); j++) {
                int targetRouter = currentConnects.get(j);
                boolean add = true;

                if (targetRouter < -50) {
                    continue;
                }

                //check if line connection already done
                for (int k = 0; k < doneConnections.get(targetRouter).size(); k++) {
                    if (doneConnections.get(targetRouter).get(k) == i) {
                        add = false;
                    }
                }

                if (add) {
                    //make the line connection
                    Line line = new Line(routerConnectPts.get(i).get(0), routerConnectPts.get(i).get(1), routerConnectPts.get(targetRouter).get(0), routerConnectPts.get(targetRouter).get(1));
                    lines.add(line);
                }

            }
        }
    }



    //this static class serves as an easy way to store info on line coordinates
    static class Line {
        final int x1;
        final int y1;
        final int x2;
        final int y2;
        final Color color;
        public Line(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = Color.BLACK;
        }

    }

    static class JLabelUnits {

    }
}

