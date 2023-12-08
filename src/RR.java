import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RR implements Ischeduler {
    private List<Process> processes;
    private int timeQuantum;

    public RR(int timeQuantum) {
        this.processes = new ArrayList<>();
        this.timeQuantum = timeQuantum;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    @Override
    public void schedule() {
        Queue<Process> readyQueue = new LinkedList<>(processes);
        List<Process> executedProcesses = new ArrayList<>();
        double currentTime = 0;

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            double startTime = currentTime;
            System.out.println("Executing Process: " + currentProcess.getName() + " from time " + startTime);

            if (currentProcess.getBurstTime() > timeQuantum) {
                currentProcess.setBurstTime(currentProcess.getBurstTime() - timeQuantum);
                currentTime += timeQuantum;
                readyQueue.add(currentProcess);
                System.out.println("Process " + currentProcess.getName() + " preempted at time " + currentTime);
            } else {
                currentTime += currentProcess.getBurstTime();
                double finishTime = currentTime;

                double turnaroundTime = finishTime - currentProcess.getArrivalTime();
                double waitingTime = turnaroundTime - currentProcess.getOriginalBurstTime();

                currentProcess.setWaitTime(waitingTime);
                currentProcess.setTurnaround(turnaroundTime);
                System.out.println("--------------------------------");
                System.out.println("Time Details for Process " + currentProcess.getName() + " :  \n");
                System.out.println("Finish Time for Process " + currentProcess.getName() + ": " + finishTime);
                System.out.println("Waiting Time for Process " + currentProcess.getName() + ": " + waitingTime);
                System.out.println("Turnaround Time for Process " + currentProcess.getName() + ": " + turnaroundTime);
                System.out.println("--------------------------------");

                executedProcesses.add(currentProcess);
            }
        }

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : executedProcesses) {
            totalWaitingTime += process.getWaitTime();
            totalTurnaroundTime += process.getTurnaround();
        }

        double averageWaitingTime = totalWaitingTime / executedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / executedProcesses.size();

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
