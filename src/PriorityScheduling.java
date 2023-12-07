import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriorityScheduling implements Ischeduler {
    private List<Process> processes;
    private List<Process> executedProcesses;
    private List<Process> waitingProcesses;
    int agingThreshold = 5;

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    @Override
    public void schedule() {
        // Sort the processes based on Arrival time
        processes.sort(Comparator.comparingDouble(Process::getArrivalTime));

        // Then sort based on priority within each arrival time group
        processes.sort((p1, p2) -> {
            if (p1.getArrivalTime() == p2.getArrivalTime()) {
                return Double.compare(p1.getPriority(), p2.getPriority());
            }
            return 0;
        });

        executedProcesses = new ArrayList<>();
        waitingProcesses = new ArrayList<>();

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        double currentTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            double startTime = currentTime;
            System.out.println("Executing Process: " + process.getName() + " from time " + startTime);

            currentTime += process.getBurstTime();
            double finishTime = currentTime;
            System.out.println("--------------------------------");
            System.out.println("Time Details for Process " + process.getName() + " :  \n");

            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);

            double turnaroundTime = finishTime - process.getArrivalTime();
            double waitingTime = turnaroundTime - process.getBurstTime();
            System.out.println("Waiting Time for Process " + process.getName() + ": " + waitingTime);
            System.out.println("Turnaround Time for Process " + process.getName() + ": " + turnaroundTime);
            System.out.println("--------------------------------");

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            executedProcesses.add(process);

            // Aging: Increment priority of all waiting processes
            for (Process p : processes) {
                if (p != process && p.getWaitTime() >= agingThreshold) {
                    p.incrementPrioritybyaging(); // Increment priority based on waiting time
                }
            }

            // Find waiting processes
            waitingProcesses.clear();
            for (Process p : processes) {
                if (!executedProcesses.contains(p)) {
                    waitingProcesses.add(p);
                }
            }

            System.out.println("Executed Processes:");
            for (Process p : executedProcesses) {
                System.out.println("[Name: " + p.getName() + ", Arrival Time: " + p.getArrivalTime() +
                        ", Burst Time: " + p.getBurstTime() + ", Priority: " + p.getPriority() + "].");

            }
            System.out.println("         ***********************         ");

            System.out.println("Waiting Processes:");
            for (Process p : waitingProcesses) {
                System.out.println("[Name: " + p.getName() + ", Arrival Time: " + p.getArrivalTime() +
                        ", Burst Time: " + p.getBurstTime() + ", Priority: " + p.getPriority() + "].");

            }

        }

        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnaroundTime = totalTurnaroundTime / processes.size();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
