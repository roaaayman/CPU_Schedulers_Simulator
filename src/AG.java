import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;
    private double quantum;
    private boolean isPreemptive = false;
    private int currentTime = 0;

    public AG(int quantum) {
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
        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> executedProcesses = new LinkedList<>();
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add arrived processes to the ready queue
            for (Process process : new ArrayList<>(processes)) {
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processes.remove(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                Process currentExecution = readyQueue.poll();
                double remaining = currentExecution.getBurstTime() - quantum;
                double limit = Math.ceil(0.5 * quantum);

                // Scenario iii: The running process finished its job
                if (remaining <= 0) {
                    currentExecution.setBurstTime(0);
                    currentExecution.setPreemptive(true);
                    if (!isPreemptive && currentTime - currentExecution.getstarttime() >= limit) {
                        isPreemptive = true;
                    }
                    currentExecution.setEndtime(currentTime);
                    executedProcesses.add(currentExecution);

                } else {
                    // Scenario i: The running process used all its quantum time
                    if (remaining > limit) {
                        currentExecution.setBurstTime(remaining);
                        currentTime += limit;
                        if (!isPreemptive && currentTime - currentExecution.getstarttime() >= limit) {
                            isPreemptive = false;
                        }
                        quantum += Math.ceil(0.1 * (quantum / 2)); // Increase Quantum time
                        readyQueue.add(currentExecution);
                    }
                    // Scenario ii: The running process didn’t use all its quantum time
                    else {
                        currentExecution.setBurstTime(0);
                        currentExecution.setPreemptive(true);
                        if (!isPreemptive && currentTime - currentExecution.getstarttime() >= limit) {
                            isPreemptive = true;
                        }
                        currentExecution.setEndtime(currentTime);
                        executedProcesses.add(currentExecution);
                    }
                }
            }

            // Preemptive AG: Reorder the ready queue based on AGFactor
            if (isPreemptive && !readyQueue.isEmpty()) {
                List<Process> sortedList = new ArrayList<>(readyQueue);
                sortedList.sort(Comparator.comparingDouble(this::AGFactor));
            }

            // Print history of quantum time
            System.out.println("AG Scheduling: Quantum time updated for each process");
            for (Process process : readyQueue) {
                System.out.println(process.getName() + ": " + quantum);
            }
            currentTime += quantum; // Update the current time
        }

        // Print execution order
        System.out.print("Processes execution order: ");
        for (Process process : executedProcesses) {
            System.out.print(process.getName() + " ");
        }
        System.out.println();

        // Print Waiting Time for each process
        System.out.println("Waiting Time for each process:");
        for (Process process : executedProcesses) {
            double waiting = process.getstarttime() - process.getArrivalTime();
            System.out.println(process.getName() + ": " + waiting);
            totalWaitingTime += waiting;
        }

        // Print Turnaround Time for each process
        System.out.println("Turnaround Time for each process:");
        for (Process process : executedProcesses) {
            double turnaround = process.getEndtime() - process.getArrivalTime();
            System.out.println(process.getName() + ": " + turnaround);
            totalTurnaroundTime += turnaround;
        }

        // Print Average Waiting Time and Average Turnaround Time
        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
