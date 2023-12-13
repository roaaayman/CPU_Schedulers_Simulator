import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJF implements Ischeduler {
    private List<Process> processes;
    public int context_switch_cost;

    public SJF(int contextSwitchCost) {
        context_switch_cost = contextSwitchCost;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    private void sortBasedOnArrivalAndBurstTime() {
        int n = processes.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (processes.get(j).getArrivalTime() < processes.get(minIndex).getArrivalTime()
                        || (processes.get(j).getArrivalTime() == processes.get(minIndex).getArrivalTime()
                        && processes.get(j).getBurstTime() < processes.get(minIndex).getBurstTime())) {
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
        sortBasedOnArrivalAndBurstTime();

       // ProcessVisualization visualization = new ProcessVisualization(processes);
        double currentTime = 0; // Initialize current time to 0

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        List<Process> executedProcesses = new ArrayList<>();

        while (!processes.isEmpty()) {
            Process currentProcess = processes.remove(0);
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            double startTime = currentTime;

//            for (int i = 0; i < currentProcess.getBurstTime(); i++) {
//                visualization.animateExecution();
//                try {
//                    Thread.sleep(500); // Adjust the sleep duration for visualization speed
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

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

            currentTime += context_switch_cost;

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

            // Sort arrived processes based on burst time
            arrivedNotExecuted.sort(Comparator.comparingDouble(Process::getBurstTime));

            if (!arrivedNotExecuted.isEmpty()) {
                Process nextProcess = arrivedNotExecuted.get(0);
                processes.remove(nextProcess);
                processes.add(0, nextProcess);
            }
        }

        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
