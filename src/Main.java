import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Process> processes = new ArrayList<>();
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.next();

            System.out.print("Arrival Time: ");
            double arrivalTime = scanner.nextDouble();

            System.out.print("Burst Time: ");
            double burstTime = scanner.nextDouble();

            System.out.println("Priority Num: ");
            int priorityNum = scanner.nextInt();

            processes.add(new Process(name, arrivalTime, burstTime, priorityNum));
        }

        System.out.println("Select Scheduler:");
        System.out.println("1. SJF (Shortest Job First)");
        System.out.println("2. SRTF (Shortest Remaining Time First)");
        System.out.println("3. Priority Scheduling");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Execute SJF Scheduler
                SJF sjfScheduler = new SJF();
                sjfScheduler.setProcesses(new ArrayList<>(processes));

                System.out.println("Executing SJF Scheduling Algorithm:");
                sjfScheduler.schedule();
                break;
            case 2:
                // Execute SRTF Scheduler
                SRTF srtfScheduler = new SRTF();
                srtfScheduler.setProcesses(new ArrayList<>(processes));

                System.out.println("\nExecuting SRTF Scheduling Algorithm:");
                srtfScheduler.schedule();
                break;
            case 3:
                // Execute Priority Scheduler
                PriorityScheduling priorityScheduler = new PriorityScheduling();
                priorityScheduler.setProcesses(new ArrayList<>(processes));

                System.out.println("Executing Priority Scheduling Algorithm:");
                priorityScheduler.schedule();
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }
}
