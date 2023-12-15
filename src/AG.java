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
    private Process findSmallestAGFactor(List<Process> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }

        Process smallestprocess = readyQueue.get(0);
        double smallestAGFactor = calculateAGFactor(smallestprocess);

        for (int i=1 ; i<readyQueue.size();i++) {
            Process process=readyQueue.get(i);
            double agFactor = calculateAGFactor(process);
            if (agFactor < smallestAGFactor) {
                smallestAGFactor = agFactor;
                smallestprocess = process;
            }
        }

        readyQueue.remove(smallestprocess);
        return smallestprocess;
    }

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

    private void scenario_1(Process runningProcess,List<Process> readyQueue,double meanOfQuantum)
    {

        int newQuantum = (int) Math.ceil(1.1 * (quantum + meanOfQuantum));
        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
        runningProcess.setQuantumTime(newQuantum);
        readyQueue.add(runningProcess);
        runningProcess = null;
    }
    private void scenario_2(Process runningProcess,List<Process> readyQueue)
    {

        double newQuantum = runningProcess.getRemainingQuantum() + runningProcess.getQuantumTime();
        runningProcess.setQuantumTime((int) newQuantum);
        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
        readyQueue.add(runningProcess);
        runningProcess = null;
    }

    @Override
    public void schedule() {
        List<Process> readyQueue = new ArrayList<>(processes);
        List<Process> dieList = new ArrayList<>();
        Process runningProcess = null;
        int time = 0;




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
                    System.out.println("Preemption: Switched to " + runningProcess.getName() + " with quantum time: " + runningProcess.getQuantumTime());
                } else {
                    time++;
                    runningProcess.execute();
                    Process lowestAGProcess = findSmallestAGFactor(readyQueue);

                    if (lowestAGProcess != null && calculateAGFactor(lowestAGProcess) < calculateAGFactor(runningProcess)) {
                        System.out.println("**Preempting Process: " + runningProcess.getName() + " at time " + time);
                        readyQueue.add(runningProcess);  // Add the preempted process back to the ready queue
                        runningProcess = lowestAGProcess; // Set the running process to the one with the least AG factor
                        time--; // Stay at the same time to re-evaluate the new running process
                    }
                }

                if (runningProcess.hasJobToDo()) {
                    if (runningProcess.getRemainingQuantum() == 0) {
                        // Process used all its quantum time and still has a job to do
                        scenario_1(runningProcess,readyQueue,getMeanQuantum(readyQueue));

                    } else {
                        // Scenario ii: Process didn't use all its quantum time based on another process conversion
                        scenario_2(runningProcess,readyQueue);
                    }
                } else {
                    // Scenario iii: Process finished its job
                    runningProcess.setQuantumTime(0);
                    int completionTime=time;
                    runningProcess.setCompletionTime(completionTime);
                    System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                    dieList.add(runningProcess);
                    runningProcess = null;
                }
            } else {
                // No process is running at this time
                time++;
            }
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