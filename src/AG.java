import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;

    private int quantum;

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

        int randomValue = randomFactor();
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
        List<Process> finishedProcesses = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll(); //removes and returns the head of the queue.
            double agFactor = calculateAGFactor(currentProcess);

            boolean isPreemptive = false;
            double remainingQuantum = quantum;

            while (remainingQuantum > 0)
            {
                double executionTime = Math.min(currentProcess.getBurstTime(), remainingQuantum);

                // Non-preemptive AG logic
                if (!isPreemptive && remainingQuantum <= (0.5 * quantum)) {
                    isPreemptive = true;
                    currentProcess.setPreemptive(true);
                }

                // Process execution based on preemptive/non-preemptive AG
                if (!currentProcess.isPreemptive())
                {
                    // Non-preemptive AG
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);

                    if (currentProcess.getBurstTime() <= 0) {
                        executedProcesses.add(currentProcess);
                        break;
                    }
                } else {
                    // Preemptive AG
                    if (currentProcess.isPreemptive()) {
                        // Check if there's a smaller AG-Factor process that arrived
                        for (Process p : readyQueue) {
                            if (calculateAGFactor(p) < agFactor) {
                                readyQueue.add(currentProcess); // Add the current process back to the queue
                                currentProcess = p; // Set the current process to the new smaller AG-Factor process
                                agFactor = calculateAGFactor(p); // Update AG-Factor
                                break;
                            }
                        }
                        // Execute the current process for the shortest time between burst time and remaining quantum
                        currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);

                        if (currentProcess.getBurstTime() <= 0) {
                            executedProcesses.add(currentProcess);
                            break;
                        }
                        //scenario 1: check if the running process used all its quantum and has more job to do if yes add it to the queue and increase its quantum
                        if (remainingQuantum <= 0 && currentProcess.getBurstTime()>0)
                        {
                            quantum+=Math.ceil(0.1*(quantum/2));
                            readyQueue.add(currentProcess);
                            break;
                        }
                        //scenario 2: check if the running process didnt use all of its quantum time because it was interupted
                        else if ( remainingQuantum>0 && currentProcess.getBurstTime()>0)
                        {
                            readyQueue.add(currentProcess);
                            quantum+=remainingQuantum;
                            break;

                        }
                        //scenario 3: check if the running process finished its job
                        else if(remainingQuantum == 0 && currentProcess.getBurstTime()<=0)
                        {
                            finishedProcesses.add(currentProcess);
                            readyQueue.remove(currentProcess);
                            quantum=0;
                            break;
                        }


                    }
                }
                remainingQuantum -= executionTime;
            }

            //check if process has completely finished or not
            if (currentProcess.getBurstTime() > 0)
            {

                readyQueue.add(currentProcess);
            } else {
                finishedProcesses.add(currentProcess);
            }
        }
    }
}