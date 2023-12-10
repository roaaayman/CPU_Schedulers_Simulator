import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;
    private int quantum;
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

    private double calculateAGFactor(Process process) {
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
        Queue<Process> readyQueue = new LinkedList<>(processes);
        List<Process> executedProcesses = new ArrayList<>();
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
            double agFactor = calculateAGFactor(currentProcess);


            double remainingQuantum = quantum;

            while (remainingQuantum > 0 && currentProcess.getBurstTime() > 0)  // loop until the process finishes
            {
                double executionTime = Math.min(remainingQuantum, currentProcess.getBurstTime());
                currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);

                // Check if the process is finished
                if (currentProcess.getBurstTime() <= 0) {
                    executedProcesses.add(currentProcess);

                    System.out.println("Time Details for Process " + currentProcess.getName() + " :  \n");
                    System.out.println("Finish Time for Process " + currentProcess.getName() + ": " + (currentTime + executionTime));
                    double turnaround = (currentTime + executionTime) - currentProcess.getArrivalTime();
                    double waiting = currentTime - currentProcess.getArrivalTime();
                    System.out.println("Waiting Time for " + currentProcess.getName() + ": " + waiting);
                    System.out.println("Turnaround Time for " + currentProcess.getName() + ": " + turnaround);
                    System.out.println("--------------------------------");
                    totalWaitingTime += waiting;
                    totalTurnaroundTime += turnaround;
                    //scenario 3: The running process finished its job
                    readyQueue.remove(currentProcess);


                } else {
                    //scenario 1: The running process used all its quantum time and it still have job to
                    //do
                    if (remainingQuantum == quantum) {
                        quantum += Math.ceil(0.1 * (quantum / 2));
                        readyQueue.add(currentProcess);
                    }
                    // scenario 2:The running process didn’t use all its quantum time based on another
                    //process converted from ready to running
                    else if (remainingQuantum > 0 && currentProcess.getBurstTime() > 0) {
                        readyQueue.add(currentProcess);
                        quantum += remainingQuantum;
                    }
                }

                remainingQuantum -= executionTime;

                // Print history of quantum time
                System.out.println("Quantum time updated for " + currentProcess.getName() + ": " + quantum);
                System.out.println("--------------------------------");

                // preemptive process
                if (!currentProcess.isPreemptive() && remainingQuantum > (0.5 * quantum)) {
                    currentProcess.setPreemptive(true);
                    readyQueue.add(currentProcess);

                }
            }
            currentTime += quantum; // Update the current time

        }
        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("--------------------------------");

        // Print the order of executed processes
        System.out.println("Processes execution order:");
        for (Process process : executedProcesses) {
            System.out.println(process.getName());
        }
    }
}
