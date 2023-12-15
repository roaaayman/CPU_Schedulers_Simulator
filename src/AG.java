import java.util.*;
class AG implements Ischeduler {
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
        process.agFactor = AGFactor;
        return AGFactor;
    }

    // Helper method to find the process with the smallest AG-Factor from the ready queue
    private Process findNextProcess(List<Process> readyQueue, int currentTime) {
        if (readyQueue.isEmpty()) {
            return null;
        }

        Process nextProcess = readyQueue.get(0);
        double smallestAGFactor = calculateAGFactor(nextProcess);

        for (Process process : readyQueue) {
            double agFactor = calculateAGFactor(process);
            process.agFactor = agFactor;
            if (agFactor < smallestAGFactor && process.getArrivalTime() >= currentTime) {
                smallestAGFactor = agFactor;
                nextProcess = process;
            }
        }

        return nextProcess;
    }

    // Helper method to calculate the mean of the quantum values in the ready queue
    private double getMeanQuantum(List<Process> readyQueue) {
        if (readyQueue.isEmpty()) {
            return 0;
        }

        double totalQuantum = 0;
        for (Process process : readyQueue) {
            totalQuantum += process.getQuantumTime();
        }

        return totalQuantum / readyQueue.size();
    }

    public void preemptive()

    {

    }
    @Override
    public void schedule() {
        for(Process p:processes) {
            p.setQuantumTime(quantum);
        }
        List<Process> readyQueue = new ArrayList<>(processes); // Make a copy of processes to act as the ready queue
        List<Process> dieList = new ArrayList<>(); // List to hold finished processes
        Process runningProcess = null; // Track the currently running process
        int time = 0;

        double meanOfQuantum = getMeanQuantum(readyQueue);
        System.out.println("AG Factors:");
        for (Process process : processes) {
            double agFactor = calculateAGFactor(process);
            System.out.println("Process: " + process.getName() + ", AG Factor: " + agFactor);
        }


        while (!readyQueue.isEmpty()) {
            for(Process p:readyQueue) {
                calculateAGFactor(p);
            }
            readyQueue.sort(Comparator.comparing(Process::getAgFactor));
            System.out.println("Current Time: " + time);

            for (Process p : readyQueue) {
                if (p.getArrivalTime() <= time) {
                    runningProcess = readyQueue.remove(readyQueue.indexOf(p));
                    runningProcess.setPreemptive(false);
                    break;
                }
            }


            if (!runningProcess.getPreemptive()) {
                int halfQuantum = (int) Math.ceil(runningProcess.getRemainingQuantum() * 0.5); // 50% of the quantum time
                for (int i = 0; i <= halfQuantum; i++) {
                    System.out.println("Time " + time + ":" + runningProcess.getName() + " is running");
                    runningProcess.decreaseRemainingQuantum();
                    runningProcess.execute();
                    time++;
                }
                runningProcess.setPreemptive(true);
                System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getRemainingQuantum());
            }

            while(runningProcess.hasJobToDo()) {
                Process newProcess = findNextProcess(readyQueue, time);
                if(newProcess == null || newProcess.getAgFactor() > runningProcess.getAgFactor()) {
                    System.out.println("Time " + time + ":" + runningProcess.getName() + " is running");
                    time++;
                    runningProcess.execute();
                    System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                } else {
                    break;
                }
            }




            // Check the scenarios based on the completion of the quantum time
            if (runningProcess.hasJobToDo()) {
                if (runningProcess.getRemainingQuantum() == 0) {
                    // Scenario i: Process used all its quantum time and still has job to do
                    int newQuantum = quantum + (int) Math.ceil(0.1 * (meanOfQuantum));
                    System.out.println("1- Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                    runningProcess.setQuantumTime(newQuantum);
                    readyQueue.add(runningProcess);
                } else {
                    // Scenario ii: Process didn't use all its quantum time based on another process conversion
                    double newQuantum = runningProcess.getRemainingQuantum() + runningProcess.getQuantumTime();
                    runningProcess.setQuantumTime((int) newQuantum);
                    System.out.println("2- Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());

                    readyQueue.add(runningProcess); // Add the process back to the end of the queue
                }
            } else if (!runningProcess.hasJobToDo()) {
                // Scenario iii: Process finished its job
                runningProcess.setQuantumTime(0);
                System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                dieList.add(runningProcess);
            }

            System.out.println("Running Process: " + (runningProcess.getBurstTime() == 0 ? runningProcess.getName() : "None"));
            // System.out.println("Ready Queue Size: " + readyQueue.size());
        }
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process process : dieList) {
            double turnaroundTime = process.getCompletionTime() - process.getArrivalTime();
            double waitingTime = turnaroundTime - process.getBurstTime();

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            System.out.println("Process " + process.getName() + " - Waiting Time: " + waitingTime + ", Turnaround Time: " + turnaroundTime);
        }

        // Calculate averages
        double averageWaitingTime = (double) totalWaitingTime / dieList.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / dieList.size();

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
