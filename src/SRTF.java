import java.util.ArrayList;
import java.util.List;

public class SRTF implements Ischeduler {
    private List<Process> processes;
    private static final double WaitTime_threshold = 10; // Maximum waiting time threshold

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
                if (!executedProcesses.contains(process) && process.getArrivalTime() <= currentTime
                        && process.getBurstTime() < minBurst) {
                    shortestIndex = i;
                    minBurst = process.getBurstTime();
                }
            }

            if (shortestIndex == -1) {
                currentTime++;
                continue;
            }

            Process shortest = processes.get(shortestIndex);

            // Check and handle processes exceeding the wait time threshold
            for (Process process : processes) {
                if (!executedProcesses.contains(process) && process != shortest
                        && process.getArrivalTime() <= currentTime) {
                    process.setWaitTime(process.getWaitTime() + 1); // Increment wait time for waiting processes

                    if (process.getWaitTime() >= WaitTime_threshold) {
                        // If wait time exceeds or equals the threshold, push the process to the front
                        processes.remove(process);
                        processes.add(0, process);
                        System.out.println("****Pushing Process to Front : " + process.getName() + " at time " + currentTime);

                        // Preempt the current process and execute the pushed process immediately
                        if (shortest.getBurstTime() > 0) {
                            System.out.println("****Preempting Process: " + shortest.getName() + " at time " + currentTime);
                            Process preemptedProcess = shortest; // Temporarily store the current process
                            processes.remove(preemptedProcess);
                            processes.add(0, process); // Move the pushed process to the front
                            processes.add(process); // Add the preempted process back to the end of the queue
                            break; // Exit the loop to execute the pushed process immediately
                        }
                    }
                }
            }
            double startTime = currentTime;
            System.out.println("Executing Process: " + shortest.getName() + " from time " + startTime);

            shortest.setBurstTime(shortest.getBurstTime() - 1);
            currentTime++;

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