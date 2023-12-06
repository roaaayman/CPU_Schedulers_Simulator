import java.util.List;

public class SJF implements Ischeduler {
    private List<Process> processes;

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

        double totalwaitingtime = 0;
        double totalturnaroundtime=0;
        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            double startTime = currentTime;
            System.out.println("Executing Process: " + process.getName() + " from time " + startTime);

            currentTime += process.getBurstTime();
            double finishTime = currentTime;
            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);
            double waitingtime = finishTime-process.getBurstTime()-process.getArrivalTime();
            double turnaroundtime = process.getBurstTime() + waitingtime;
            System.out.println("WAITING TIME " + process.getName() +  waitingtime);
            System.out.println("turn around time " + process.getName() +  turnaroundtime);


            totalwaitingtime += waitingtime;
            totalturnaroundtime += turnaroundtime;
        }
        double averagewaitingtime = totalwaitingtime / processes.size();
        double averageturnaroundtime= totalturnaroundtime / processes.size();
        System.out.println("Average Waiting Time: " + averagewaitingtime);
        System.out.println("Average turnaround Time: " + averageturnaroundtime);


    }
}