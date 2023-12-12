import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;
    private double quantum;
    private int currentTime = 0;

    public AG(double quantum) {
        this.quantum = quantum;
        this.processes = new ArrayList<>();
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public static int randomFactor() {
        Random random = new Random();
        return random.nextInt(21); // Generates a number between 0 and 20
    }

    private double AGFactor(Process process) {
        double arrivalTime = process.getArrivalTime();
        double burstTime = process.getBurstTime();
        double priority = process.getPriority();

        int randomValue = process.getRandom();
        double AGFactor;

        if (randomValue < 10) {
            AGFactor = randomValue + arrivalTime + burstTime;
        } else if (randomValue > 10) {
            AGFactor = 10 + arrivalTime + burstTime;
        } else {
            AGFactor = priority + arrivalTime + burstTime;
        }
        return AGFactor;
    }

    @Override
    public void schedule() {
        List<Process> readyQueue = new ArrayList<>();
        List<Process> executedProcesses = new ArrayList<>();
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Move processes from the original list to the ready queue based on arrival time
            Iterator<Process> iterator = processes.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            // If there is a process in the ready queue
            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.remove(0);

                // Execute quantum time
                double remainingQuantum = Math.min(quantum, currentProcess.getBurstTime());
                double executionTime = remainingQuantum;
                currentTime += executionTime;
                currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);

                System.out.println("At time " + currentTime);
                System.out.println("CPU: Process " + currentProcess.getName() + " used " + executionTime + " quantum time.");

                // Check if the process finished its job
                if (currentProcess.getBurstTime() <= 0) {
                    System.out.println("Process " + currentProcess.getName() + " finished at time " + currentTime);
                    double waitingTime = currentTime - currentProcess.getArrivalTime();
                    double turnaroundTime = waitingTime + executionTime;
                    System.out.println("Waiting Time for " + currentProcess.getName() + ": " + waitingTime);
                    System.out.println("Turnaround Time for " + currentProcess.getName() + ": " + turnaroundTime);
                    totalWaitingTime += waitingTime;
                    totalTurnaroundTime += turnaroundTime;
                    executedProcesses.add(currentProcess);
                } else {
                    // Check for preemptive condition after 50% of quantum time
                    if (executionTime >= Math.ceil(0.5 * quantum)) {
                        currentProcess.setPreemptive(true);
                        // Scenario i): The running process used all its quantum time
                        // and it still has a job to do
                        readyQueue.add(currentProcess);
                        double additionalQuantum = Math.ceil(0.1 * (quantum / 2));
                        currentProcess.setQuantum(currentProcess.getQuantum() + additionalQuantum);
                    } else {
                        // Scenario ii): The running process didn’t use all its quantum time
                        // based on another process converted from ready to running
                        readyQueue.add(currentProcess);
                    }
                }
            }
        }

        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();

        System.out.println("Processes execution order:");
        for (Process process : executedProcesses) {
            System.out.print(process.getName() + " ");
        }
        System.out.println();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}