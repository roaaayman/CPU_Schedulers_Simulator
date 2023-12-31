import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//
//public class PriorityScheduling implements Ischeduler {
//    private List<Process> processes;
//    private List<Process> executedProcesses;
//    private List<Process> waitingProcesses;
//    int agingThreshold = 5;
//
//    public void setProcesses(List<Process> processes) {
//        this.processes = processes;
//    }
//
//    @Override
//    public void schedule() {
//        // Sort the processes based on Arrival time
//        processes.sort(Comparator.comparingDouble(Process::getArrivalTime));
//
//        // Then sort based on priority within each arrival time group
//        processes.sort((p1, p2) -> {
//            if (p1.getArrivalTime() == p2.getArrivalTime()) {
//                return Double.compare(p1.getPriority(), p2.getPriority());
//            }
//            return 0;
//        });
//
//        executedProcesses = new ArrayList<>();
//        waitingProcesses = new ArrayList<>();
//
//        double totalWaitingTime = 0;
//        double totalTurnaroundTime = 0;
//        double currentTime = 0;
//
//        for (Process process : new ArrayList<>(processes)) {
//            if (currentTime < process.getArrivalTime()) {
//                currentTime = process.getArrivalTime();
//            }
//
//            double startTime = currentTime;
//            System.out.println("Executing Process: " + process.getName() + " from time " + startTime);
//
//            currentTime += process.getBurstTime();
//            double finishTime = currentTime;
//            System.out.println("--------------------------------");
//            System.out.println("Time Details for Process " + process.getName() + " :  \n");
//
//            System.out.println("Finish Time for Process " + process.getName() + ": " + finishTime);
//
//            double turnaroundTime = finishTime - process.getArrivalTime();
//            double waitingTime = turnaroundTime - process.getBurstTime();
//            System.out.println("Waiting Time for Process " + process.getName() + ": " + waitingTime);
//            System.out.println("Turnaround Time for Process " + process.getName() + ": " + turnaroundTime);
//            System.out.println("--------------------------------");
//
//            totalWaitingTime += waitingTime;
//            totalTurnaroundTime += turnaroundTime;
//
//            executedProcesses.add(process);
////            //aging apply
////            for (Process p : processes) {
////                if (!executedProcesses.contains(p) && (currentTime - p.getArrivalTime() >= agingThreshold)) {
////                    double scaleFactor = 0.1; // Adjust this scaling factor as needed
////                    int newPriority = (int) (p.getPriority() *scaleFactor);
////
////                    // Set the new priority for the current process 'p'
////                    p.setPriority(newPriority);
////                }
////            }
//
//
//
//
//
//            // Find waiting processes
//            waitingProcesses.clear();
//            for (Process p : processes) {
//                if (!executedProcesses.contains(p)) {
//                    waitingProcesses.add(p);
//                }
//            }
//            // Check for the processes that arrived before the finish time of the current process
//            List<Process> arrivedProcesses = new ArrayList<>();
//            for (Process p : waitingProcesses) {
//                if (p.getArrivalTime() < finishTime) {
//                    arrivedProcesses.add(p);
//                }
//            }
//
//            arrivedProcesses.sort(Comparator.comparingInt(Process::getPriority));
//            if (!arrivedProcesses.isEmpty()) {
//                Process nextProcess = arrivedProcesses.get(0);
//                processes.remove(nextProcess);
//                processes.add(0, nextProcess);
//            }
//
//
//
//
//        System.out.println("Executed Processes:");
//            for (Process p : executedProcesses) {
//                System.out.println("[Name: " + p.getName() + ", Arrival Time: " + p.getArrivalTime() +
//                        ", Burst Time: " + p.getBurstTime() + ", Priority: " + p.getPriority() + "].");
//
//            }
//            System.out.println("         ***********************         ");
//
//            System.out.println("Waiting Processes:");
//            for (Process p : waitingProcesses) {
//                System.out.println("[Name: " + p.getName() + ", Arrival Time: " + p.getArrivalTime() +
//                        ", Burst Time: " + p.getBurstTime() + ", Priority: " + p.getPriority() + "].");
//
//            }
//
//        }
//
//        double averageWaitingTime = totalWaitingTime / processes.size();
//        double averageTurnaroundTime = totalTurnaroundTime / processes.size();
//        System.out.println("Average Waiting Time: " + averageWaitingTime);
//        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
//    }
//}
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

