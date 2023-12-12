import java.util.ArrayList;
import java.util.List;

public class SRTF implements Ischeduler {
    private List<Process> processes;
    private List<Process> waitingProcesses; // New list for waiting processes
    private double WaitTime_threshold = 10; // Maximum waiting time threshold

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
        this.waitingProcesses = new ArrayList<>(); // Initialize waitingProcesses list
    }

    private void sortProcessesByBurstTime(List<Process> processesToSort) {
        int n = processesToSort.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (processesToSort.get(j).getBurstTime() < processesToSort.get(minIndex).getBurstTime()) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Process temp = processesToSort.get(minIndex);
                processesToSort.set(minIndex, processesToSort.get(i));
                processesToSort.set(i, temp);
            }
        }
    }

    private void displayExecutedProcesses(List<Process> executedProcesses) {
        for (Process process : executedProcesses) {
            double finishTime = process.getFinishTime();
            double waitingTime = process.getWaitTime();
            double turnaroundTime = process.getTurnaround();

            System.out.println("--------------------------------");
            System.out.println("Time Details for Process " + process.getName() + " :  \n");
            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);
            System.out.println("Waiting Time for Process " + process.getName() + ": " + waitingTime);
            System.out.println("Turnaround Time for Process " + process.getName() + ": " + turnaroundTime);
            System.out.println("--------------------------------");
        }
    }
    @Override
    public void schedule() {
        List<Process> executedProcesses = new ArrayList<>();
        double currentTime = 0;
        int completed = 0;
        Process preemptedProcess = null; // Variable to store preempted process

        while (completed != processes.size() && !processes.isEmpty()) {
            sortProcessesByBurstTime(processes); // Sort processes by burst time

            Process shortest = null;
            for (Process process : processes) {
                if (!executedProcesses.contains(process) && process.getArrivalTime() <= currentTime) {
                    shortest = process;
                    break;
                }
            }

            if (shortest == null) {
                currentTime++;
                continue;
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

                shortest.setFinishTime(finishTime);
                shortest.setWaitTime(waitingTime);
                shortest.setTurnaround(turnaroundTime);

                System.out.println("Process " + shortest.getName() + " completed.");
            } else {
                boolean preempt = false;
                for (Process process : processes) {
                    if (process != shortest && !executedProcesses.contains(process)
                            && process.getArrivalTime() <= currentTime && process.getWaitTime() >= WaitTime_threshold) {
                        preempt = true;
                        break;
                    }
                }

                if (shortest.getWaitTime() >= WaitTime_threshold && preempt) {
                    // Handle preemption
                    System.out.println("****Preempting Process: " + shortest.getName() + " at time " + currentTime);
                    executedProcesses.add(shortest); // Add the preempted process to executed list

                    // Find a new process to execute
                    Process newShortest = null;
                    for (Process process : processes) {
                        if (!executedProcesses.contains(process) && process.getArrivalTime() <= currentTime) {
                            newShortest = process;
                            break;
                        }
                    }

                    if (newShortest != null) {
                        processes.remove(newShortest); // Remove the new process from the list
                        processes.add(0, newShortest); // Add the new process to the front of the list
                        System.out.println("****Pushing Process to Front: " + newShortest.getName() + " at time " + currentTime);
                    }
                }
            }

            // Update waiting times for other processes
            for (Process process : processes) {
                if (!executedProcesses.contains(process) && process != shortest && process.getArrivalTime() <= currentTime) {
                    process.setWaitTime(process.getWaitTime() + 1);
                }
            }
        }

        displayExecutedProcesses(executedProcesses);

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





/*
import java.util.ArrayList;
import java.util.List;

public class SRTF implements Ischeduler {
    private List<Process> processes;
    private List<Process> waitingProcesses; // New list for waiting processes
    private double WaitTime_threshold = 10; // Maximum waiting time threshold

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
        this.waitingProcesses = new ArrayList<>(); // Initialize waitingProcesses list
    }

    private void displayExecutedProcesses(List<Process> executedProcesses) {
        for (Process process : executedProcesses) {
            double finishTime = process.getFinishTime();
            double waitingTime = process.getWaitTime();
            double turnaroundTime = process.getTurnaround();

            System.out.println("--------------------------------");
            System.out.println("Time Details for Process " + process.getName() + " :  \n");
            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);
            System.out.println("Waiting Time for Process " + process.getName() + ": " + waitingTime);
            System.out.println("Turnaround Time for Process " + process.getName() + ": " + turnaroundTime);
            System.out.println("--------------------------------");
        }
    }
    @Override
    public void schedule() {
        List<Process> executedProcesses = new ArrayList<>();
        double currentTime = 0;
        int completed = 0;
        Process preemptedProcess = null; // Variable to store preempted process

        while (completed != processes.size() && !processes.isEmpty()) {
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
                        if (shortest.getBurstTime() > 0) {
                            System.out.println("****Preempting Process: " + shortest.getName() + " at time " + currentTime);
                            preemptedProcess = shortest; // Store the current process to preempt

                            processes.remove(preemptedProcess);

                            if (!waitingProcesses.contains(preemptedProcess)) {
                                waitingProcesses.add(preemptedProcess);
                                System.out.println("****Moving Preempted Process to Waiting List: " + preemptedProcess.getName() + " at time " + currentTime);
                            }

                            processes.add(0, process); // Push the new process to the front
                            System.out.println("****Pushing Process to Front : " + process.getName() + " at time " + currentTime);

                            break;
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

                shortest.setFinishTime(finishTime);
                shortest.setWaitTime(waitingTime);
                shortest.setTurnaround(turnaroundTime);
                executedProcesses.add(shortest);

            }
        }
        displayExecutedProcesses(executedProcesses);

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
*/