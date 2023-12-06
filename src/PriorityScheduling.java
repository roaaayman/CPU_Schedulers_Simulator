import java.util.Comparator;
import java.util.List;

public class PriorityScheduling implements Ischeduler {
    private List<Process> processes;

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
            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);

            double turnaroundTime = finishTime - process.getArrivalTime();
            double waitingTime = turnaroundTime - process.getBurstTime();
            System.out.println("Waiting Time for Process " + process.getName() + ": " + waitingTime);
            System.out.println("Turnaround Time for Process " + process.getName() + ": " + turnaroundTime);

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
        }

        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnaroundTime = totalTurnaroundTime / processes.size();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
