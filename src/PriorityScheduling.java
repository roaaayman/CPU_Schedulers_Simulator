import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class prioirtySchedling implements Ischeduler {
    private List<Process> processes;

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    private void sortBasedOnArrivalAndpriority() {
        int n = processes.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (processes.get(j).getArrivalTime() < processes.get(minIndex).getArrivalTime()
                        || (processes.get(j).getArrivalTime() == processes.get(minIndex).getArrivalTime()
                        && processes.get(j).getPriority() < processes.get(minIndex).getPriority())) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Process temp = processes.get(minIndex);
                processes.set(minIndex, processes.get(i));
                processes.set(i, temp);
            }
        }
    }


    @Override
    public void schedule() {
        sortBasedOnArrivalAndpriority();
        double currentTime = 0; // Initialize current time to 0

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        List<Process> executedProcesses = new ArrayList<>();

        // Apply aging to process to increase the priority of the processes
        for (Process p : processes) {
            if (p.getPriority() >= 10) {
                int newPriority = (int) (p.getPriority() * 0.1); // Example: reducing priority by 10%
                p.setPriority(newPriority);

            }
        }
        while (!processes.isEmpty()) {
            Process currentProcess = processes.remove(0);
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }
            double startTime = currentTime;
            System.out.println("Executing Process: " + currentProcess.getName() + " from time " + startTime);

            currentTime += currentProcess.getBurstTime();
            double finishTime = currentTime;
            double waitingTime = startTime - currentProcess.getArrivalTime();
            double turnaroundTime = finishTime - currentProcess.getArrivalTime();
            System.out.println("--------------------------------");
            System.out.println("Time Details for Process " + currentProcess.getName() + " :  \n");
            System.out.println("Finish Time for Process " + currentProcess.getName() + ": " + finishTime);
            System.out.println("Waiting Time for " + currentProcess.getName() + ": " + waitingTime);
            System.out.println("Turnaround Time for " + currentProcess.getName() + ": " + turnaroundTime);
            System.out.println("--------------------------------");
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
            executedProcesses.add(currentProcess);

            // Find processes that have arrived but not executed yet
            List<Process> arrivedNotExecuted = new ArrayList<>();
            for (Process p : processes) {
                if (p.getArrivalTime() <= finishTime) {
                    arrivedNotExecuted.add(p);
                }
            }
            // Sort arrived processes based on priority
            arrivedNotExecuted.sort(Comparator.comparingDouble(Process::getPriority));

            if (!arrivedNotExecuted.isEmpty()) {
                Process nextProcess = arrivedNotExecuted.get(0);
                processes.remove(nextProcess);
                processes.add(0, nextProcess);
            }






        }
        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();
        if (!executedProcesses.isEmpty()) {
            System.out.println("Average Waiting Time: " + averageWaitingTime);
            System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        } else {
            System.out.println("No processes executed.");
        }


        }
    }

