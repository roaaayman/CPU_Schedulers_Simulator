import java.util.ArrayList;
import java.util.List;

public class SRTF implements Ischeduler {
    private List<Process> processes;

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    @Override
    public void schedule() {
        List<Process> executedProcesses = new ArrayList<>();
        double currentTime = 0;
        int completed = 0;

        while (completed != processes.size()) {
            double minBurst = Double.MAX_VALUE;
            int shortestIndex = -1;

            for (int i = 0; i < processes.size(); i++) {
                Process process = processes.get(i);
                if (!executedProcesses.contains(process) && process.getArrivalTime() <= currentTime && process.getBurstTime() < minBurst) {
                    shortestIndex = i;
                    minBurst = process.getBurstTime();
                }
            }

            if (shortestIndex == -1) {
                currentTime++;
                continue;
            }

            Process shortest = processes.get(shortestIndex);
            double startTime = currentTime;
            System.out.println("Executing Process: " + shortest.getName() + " from time " + startTime);

            shortest.setBurstTime(shortest.getBurstTime() - 1);
            currentTime++;

            // Aging: Decrement priority for waiting processes
            for (Process process : processes) {
                if (!executedProcesses.contains(process) && process != shortest) {
                    int priority = process.getPriority();
                    if (currentTime - process.getArrivalTime() > 0) {
                        if (priority < 1) {
                            process.setPriority(1);
                        } else {
                            process.setPriority(priority);
                        }
                    }
                }
            }

            if (shortest.getBurstTime() == 0) {
                executedProcesses.add(shortest);
                completed++;

                double finishTime = currentTime;
                double turnaroundTime = finishTime - shortest.getArrivalTime();
                double waitingTime = turnaroundTime - shortest.getOriginalBurstTime();

                shortest.setWaitTime(waitingTime);
                shortest.setTurnaround(turnaroundTime);
                System.out.println("--------------------------------");
                System.out.println("Time Details for Process " + shortest.getName() + " :  \n");
                System.out.println("Finish Time for Process " + shortest.getName() + ": " + finishTime);
                System.out.println("Waiting Time for Process " + shortest.getName() + ": " + waitingTime);
                System.out.println("Turnaround Time for Process " + shortest.getName() + ": " + turnaroundTime);
                System.out.println("--------------------------------");
            } else {
                for (Process process : processes) {
                    if (!executedProcesses.contains(process) && process != shortest &&
                            process.getArrivalTime() <= currentTime && process.getBurstTime() < shortest.getBurstTime()) {
                        System.out.println("****Preempting Process: " + shortest.getName() + " at time " + currentTime);
                        break;
                    }
                }
            }
        }

        // Calculate averages
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : executedProcesses) {
            totalWaitingTime += process.getWaitTime();
            totalTurnaroundTime += process.getTurnaround();
        }

        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
