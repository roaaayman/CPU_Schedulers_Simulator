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
    private Process findNextProcess(List<Process> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }

        Process nextProcess = readyQueue.get(0);
        double smallestAGFactor = calculateAGFactor(nextProcess);

        for (Process process : readyQueue) {
            double agFactor = calculateAGFactor(process);
            if (agFactor < smallestAGFactor) {
                smallestAGFactor = agFactor;
                nextProcess = process;
            }
        }

        readyQueue.remove(nextProcess); // Remove the selected process from the queue
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


    @Override
    public void schedule() {
        List<Process> readyQueue = new ArrayList<>(processes); // Make a copy of processes to act as the ready queue
        List<Process> dieList = new ArrayList<>(); // List to hold finished processes
        Process runningProcess = null; // Track the currently running process
        int time = 0;

        double meanOfQuantum = getMeanQuantum(readyQueue);

        readyQueue.sort(Comparator.comparing(Process::getAgFactor));

        while (!readyQueue.isEmpty()) {
            System.out.println("Current Time: " + time);
            if (runningProcess == null) {
                for (Process p : readyQueue) {
                    if (p.getArrivalTime() <= time) {
                        runningProcess = readyQueue.remove(readyQueue.indexOf(p));
                        runningProcess.setPreemptive(false);
                        break;
                    }

                }
            }

            if (runningProcess != null) {
                System.out.println("Time " + time + ":" + runningProcess.getName() + " is running");

                // Non-preemptive AG phase
                if (!runningProcess.getPreemptive()) {
                    int halfQuantum = (int) Math.ceil(runningProcess.getQuantumTime() * 0.5);

                    for (int i = 0; i < halfQuantum; i++) {
                        runningProcess.execute();
                        time++;
                    }

                    runningProcess.setPreemptive(true);
                    System.out.println("Switched to Preemptive AG for " + runningProcess.getName() + " with quantum time: " + runningProcess.getQuantumTime());
                } else {
                    time++;
                    runningProcess.execute();
                }

                // Check scenarios based on completion of the quantum time
                if (runningProcess.hasJobToDo()) {
                    if (runningProcess.getRemainingQuantum() == 0) {
                        // Scenario i: Process used all its quantum time and still has a job to do
                        int newQuantum = (int) Math.ceil(1.1 * (quantum + meanOfQuantum));
                        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                        runningProcess.setQuantumTime(newQuantum);
                        readyQueue.add(runningProcess);
                        runningProcess = null;
                    } else {
                        // Scenario ii: Process didn't use all its quantum time based on another process conversion
                        double newQuantum = runningProcess.getRemainingQuantum() + runningProcess.getQuantumTime();
                        runningProcess.setQuantumTime((int) newQuantum);
                        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                        readyQueue.add(runningProcess);
                        runningProcess = null;
                    }
                } else {
                    // Scenario iii: Process finished its job
                    runningProcess.setQuantumTime(0);
                    System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                    dieList.add(runningProcess);
                    runningProcess = null;
                }
            } else {
                // No process is running at this time
                time++;
            }
        }
    }}
