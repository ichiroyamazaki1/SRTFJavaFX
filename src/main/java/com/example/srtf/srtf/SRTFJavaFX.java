package src.main.java.com.example.srtf.srtf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SRTFJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scanner sc = new Scanner(System.in);
        char restart;
        do {
            System.out.println(----------------------------------------);
            System.out.println(     "Shortest Remaining Time First"      );
            System.out.println(----------------------------------------);
            int numProcesses = 0;
            boolean validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter the number of processes ");
                    numProcesses = sc.nextInt();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println();
                    System.out.println("Please put a numerical number only.");
                    sc.nextLine();
                }
            }

            int[] arrivalTimes = new int[numProcesses];
            int[] burstTimes = new int[numProcesses];
            int[] remainingTimes = new int[numProcesses];
            int[] completionTimes = new int[numProcesses];
            int[] waitingTimes = new int[numProcesses];
            int[] turnaroundTimes = new int[numProcesses];

            for (int i = 0; i  numProcesses; i++) {
                int arrivalTime;
                int burstTime;
                boolean validArrivalInput = false;
                boolean validBurstInput = false;

                while (!validArrivalInput) {
                    try {
                        System.out.print(Enter Arrival Time for Process  + (i + 1) +  );
                        arrivalTime = sc.nextInt();
                        validArrivalInput = true;
                        arrivalTimes[i] = arrivalTime;
                    } catch (InputMismatchException e) {
                        System.out.println();
                        System.out.println(Please put a numerical number only for Arrival Time.);
                        sc.nextLine();
                    }
                }

                while (!validBurstInput) {
                    try {
                        System.out.print(Enter Burst Time for Process  + (i + 1) +  );
                        burstTime = sc.nextInt();
                        validBurstInput = true;
                        burstTimes[i] = burstTime;
                        remainingTimes[i] = burstTime;
                    } catch (InputMismatchException e) {
                        System.out.println();
                        System.out.println(Please put a numerical number only for Burst Time.);
                        sc.nextLine();
                    }
                }
            }

            int currentTime = 0;
            int completedProcesses = 0;
            while (completedProcesses  numProcesses) {
                int shortestTime = Integer.MAX_VALUE;
                int shortestIndex = -1;

                for (int i = 0; i  numProcesses; i++) {
                    if (arrivalTimes[i] = currentTime && remainingTimes[i]  shortestTime && remainingTimes[i]  0) {
                        shortestTime = remainingTimes[i];
                        shortestIndex = i;
                    }
                }

                if (shortestIndex == -1) {
                    currentTime++;
                } else {
                    remainingTimes[shortestIndex]--;
                    currentTime++;

                    if (remainingTimes[shortestIndex] == 0) {
                        completedProcesses++;
                        completionTimes[shortestIndex] = currentTime;
                        turnaroundTimes[shortestIndex] = completionTimes[shortestIndex] - arrivalTimes[shortestIndex];
                        waitingTimes[shortestIndex] = turnaroundTimes[shortestIndex] - burstTimes[shortestIndex];
                    }
                }
            }

            System.out.println();
            System.out.println(Process Table);
            System.out.println(------------------------------------------------------------------------------);
            System.out.println( PID  Arrival Time  Burst Time  Completed Time  Turnaround Time  Waiting Time );
            System.out.println(------------------------------------------------------------------------------);
            for (int i = 0; i  numProcesses; i++) {
                System.out.printf( %-3d  %-12d  %-10d  %-14d  %-15d  %-12d %n, i + 1, arrivalTimes[i],
                        burstTimes[i], completionTimes[i], turnaroundTimes[i], waitingTimes[i]);
            }
            System.out.println(------------------------------------------------------------------------------);
            System.out.println();

            float totalTime = currentTime;
            float averageWaitingTime = 0;
            float averageTurnaroundTime = 0;

            for (int i = 0; i  numProcesses; i++) {
                averageWaitingTime += waitingTimes[i];
                averageTurnaroundTime += turnaroundTimes[i];
            }
            averageWaitingTime = numProcesses;
            averageTurnaroundTime = numProcesses;

            System.out.printf(Total Time %.0f%n, totalTime);
            System.out.println();
            System.out.printf(Average Waiting Time %.2fms%n, averageWaitingTime);
            System.out.printf(Average Turnaround Time %.2fms%n, averageTurnaroundTime);

            launchGanttChartWindow(primaryStage, completionTimes, (int) totalTime, averageWaitingTime,
                    averageTurnaroundTime);

            System.out.println();
            System.out.print(Process completed. Do you want to restart (YN) );
            restart = Character.toLowerCase(sc.next().charAt(0));

            while (restart != 'y' && restart != 'n') {
                System.out.println(Please enter a valid answer (YN) );
                restart = Character.toLowerCase(sc.next().charAt(0));
            }
        } while (restart == 'y');

        System.out.println();
        System.out.println(Program terminated. Showing the Gantt Chart Table in 5 seconds...);
        System.out.println();
        System.out.println(Created by Ichiro P. Yamazaki);
        System.out.println(From BSCpE-2A, a Task Performance in Operating System (Finals));
        System.out.println(Powered by JavaFX);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sc.close();
    }

    private void launchGanttChartWindow(Stage primaryStage, int[] completionTimes, int totalTime,
                                        float averageWaitingTime, float averageTurnaroundTime) {
        Pane root = new Pane();
        double scale = 50.0;

        Text title = new Text(Gantt Chart Table);
        title.setFont(new Font(Arial, 20));
        title.setX(30);
        title.setY(30);

        Text totalTimeText = new Text(Total Time  + totalTime);
        totalTimeText.setFont(new Font(Arial, 12));
        totalTimeText.setX(30);
        totalTimeText.setY(120);

        Text avgWaitingTimeText = new Text(Average Waiting Time  + String.format(%.2fms, averageWaitingTime));
        avgWaitingTimeText.setFont(new Font(Arial, 12));
        avgWaitingTimeText.setX(30);
        avgWaitingTimeText.setY(140);

        Text avgTurnaroundTimeText = new Text(
                Average Turnaround Time  + String.format(%.2fms, averageTurnaroundTime));
        avgTurnaroundTimeText.setFont(new Font(Arial, 12));
        avgTurnaroundTimeText.setX(30);
        avgTurnaroundTimeText.setY(160);

        root.getChildren().addAll(title, totalTimeText, avgWaitingTimeText, avgTurnaroundTimeText);

        double xPos = 0;
        for (int i = 0; i  completionTimes.length; i++) {
            double width = (i == 0  completionTimes[i]  completionTimes[i] - completionTimes[i - 1])  scale;

            Rectangle rect = new Rectangle(xPos, 50, width, 20);
            rect.setStroke(Color.BLACK);
            rect.setFill(Color.TRANSPARENT);

            Text text = new Text(xPos + 5, 65, P + (i + 1));

            root.getChildren().addAll(rect, text);
            xPos += width;
        }

        for (int i = 0; i = totalTime; i++) {
            Text timeText = new Text(scale  i - (i  10  3  7), 85, String.valueOf(i));
            root.getChildren().add(timeText);
        }

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setPrefViewportWidth(600);
        scrollPane.setPrefViewportHeight(300);
        scrollPane.setPannable(true);

        primaryStage.setTitle(Print  Shortest Remaining Time First);
        primaryStage.setScene(new Scene(scrollPane));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
