
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Process> processes = new ArrayList<>();
        /*System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        System.out.print("Enter time quantum for Round Robin: ");
        int timeQuantum = scanner.nextInt();
        System.out.print("Enter the context switch cost for SJF: ");
        int contextSwitchCost = scanner.nextInt();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.next();

            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();

            System.out.print("Burst Time: ");
            double burstTime = scanner.nextDouble();
            double originalBurstTime = burstTime;

            System.out.println("Priority Num: ");
            int priorityNum = scanner.nextInt();


            System.out.println("random: ");
            int random = scanner.nextInt();
            System.out.println("color: ");
            String color = String.valueOf(Color.decode(scanner.next()));
            }*/
            /*processes.add(new Process("p1", 0, 8, 8, 4,3,null));
        processes.add(new Process("p2", 1, 4, 4, 9,8,null));
        processes.add(new Process("p3", 2, 9, 9, 2,10,null));
        processes.add(new Process("p4", 3, 5, 5, 8,12,null));

        SRTF srtfScheduler = new SRTF();
        srtfScheduler.setProcesses(new ArrayList<>(processes));

        System.out.println("\nExecuting SRTF Scheduling Algorithm: \n");
        srtfScheduler.schedule();
*/

        processes.add(new Process("p1", 0, 17, 17, 4,3,null));
        processes.add(new Process("p2", 3, 6, 6, 9,8,null));
        processes.add(new Process("p3", 4, 10, 10, 2,10,null));
        processes.add(new Process("p4", 29, 4, 4, 8,12,null));

        AG ag = new AG(4);
        ag.setProcesses(processes);
        ag.schedule();


        /*System.out.println("Select Scheduler:");
        System.out.println("1. SJF (Shortest Job First)");
        System.out.println("2. SRTF (Shortest Remaining Time First)");
        System.out.println("3. Priority Scheduling");
        System.out.println("4. AG scheduling");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Execute SJF Scheduler
                SJF sjfScheduler = new SJF(contextSwitchCost);
                sjfScheduler.setProcesses(new ArrayList<>(processes));

                System.out.println("Executing SJF Scheduling Algorithm: \n");
                sjfScheduler.schedule();
                break;
            case 2:
                // Execute SRTF Scheduler
                SRTF srtfScheduler = new SRTF();
                srtfScheduler.setProcesses(new ArrayList<>(processes));

                System.out.println("\nExecuting SRTF Scheduling Algorithm: \n");
                srtfScheduler.schedule();
                break;
            case 3:
                // Execute Priority Scheduler
                prioirtySchedling priorityScheduler = new prioirtySchedling();
                priorityScheduler.setProcesses(new ArrayList<>(processes));

                System.out.println("\nExecuting Priority Scheduling Algorithm: \n");
                priorityScheduler.schedule();
                break;
            case 4:
                // Execute Round Robin Scheduler
                //RR roundRobinScheduler = new RR(timeQuantum);
                //roundRobinScheduler.setProcesses(new ArrayList<>(processes));

                //System.out.println("\nExecuting Round Robin Scheduling Algorithm: \n");
                //roundRobinScheduler.schedule();
                AG ag = new AG(timeQuantum);
                ag.setProcesses(new ArrayList<>(processes));
                ag.schedule();
                break;

            default:
                System.out.println("Invalid choice. Exiting...");
        }
*/
        scanner.close();
    }
}
