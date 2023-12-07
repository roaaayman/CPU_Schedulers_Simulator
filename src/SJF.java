import java.util.List;

public class SJF implements Ischeduler {
    private List<Process> processes;

    int context_switch_cost;
    public SJF(int contextSwitchCost) {
         context_switch_cost=contextSwitchCost;
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
        double currentTime = 0; // Initialize current time to 0

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;



        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            double startTime = currentTime;
            System.out.println("Executing Process: " + process.getName() + " from time " + startTime);

            currentTime += process.getBurstTime();
            double finishTime = currentTime;
            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);
            double waitingTime = startTime - process.getArrivalTime();
            double turnaroundTime = finishTime - process.getArrivalTime();
            System.out.println("Waiting Time for " + process.getName() + ": " + waitingTime);
            System.out.println("Turnaround Time for " + process.getName() + ": " + turnaroundTime);

            // Consider context switch cost for the next process
            currentTime += context_switch_cost;


            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
        }
        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnaroundTime = totalTurnaroundTime / processes.size();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

}