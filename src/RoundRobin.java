//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class RoundRobin implements Ischeduler {
//    private List<Process> processes;
//    private int q;
//
//    public RoundRobin(int TimeQuantum) {
//        q = TimeQuantum;
//    }
//
//    public void setProcesses(List<Process> processes) {
//        this.processes = processes;
//    }
//
//    public void sortOnArrivalTime() {
//        processes.sort(Comparator.comparingDouble(Process::getArrivalTime));
//    }
//
//    public boolean allProcessesCompleted() {
//        for (Process process : processes) {
//            if (process.getRemainingtime() > 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void schedule() {
//        // Existing code here
//
//        int Timer = 0;
//        int ContextSwitch = 0;
//        Process CPU = null;
//        List<Process> ReadyQueue = new ArrayList<>();
//        List<Process> EndProcesses = new ArrayList<>();
//        int Counter = 0;
//
//        while (!ReadyQueue.isEmpty() || !processes.isEmpty() || CPU != null) {
//            //add to ReadyQueue
//            for (int i = 0; i < processes.size(); i++) {
//                if (processes.get(i).getArrivalTime() == Timer) {
//                    ReadyQueue.add(processes.remove(i));
//                }
//            }
//
//            // Add to CPU
//            if (CPU == null) {
//                CPU = ReadyQueue.remove(0);
//            }
//
//            Counter++;
//            CPU.setServiceTime(CPU.getServiceTime() + 1);
//
//            if (CPU.getBurstTime() == CPU.getServiceTime()) {
//                // done
//                CPU.setCompletionTime(Timer); // Completion Time is set
//                EndProcesses.add(CPU);
//                CPU = null;
//                ContextSwitch++;
//                Counter = 0;
//            } else if (Counter == q) {
//                // Exceeds TimeQuantum
//                ReadyQueue.add(CPU);
//                CPU = null;
//                ContextSwitch++;
//                Counter = 0;
//            }
//            Timer++; // Real Time
//            // Update your metrics and print as needed after this point.
//        }
//    }
//}
