import javax.swing.*;
import java.awt.*;
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
        visualizeAlgorithmOutput(executedProcesses);
    }
    public void visualizeAlgorithmOutput(List<Process> processes) {
        JFrame visualizationFrame = new JFrame("Process Visualization");
        visualizationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        visualizationFrame.setSize(800, 400);

        JPanel visualizationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int startX = 50; // Initial X position for drawing rectangles
                int startY = 50; // Initial Y position for drawing rectangles
                int rectWidth = 50; // Width of each rectangle
                int rectHeight = 30; // Height of each rectangle

                for (Process process : processes) {
                    double duration = process.getBurstTime(); // Calculate or fetch the duration of each process
                    double endX = startX + duration * 10 ; // Calculate the end X position based on duration

                    g.setColor(process.getcolor()); // Use the process color for the rectangle
                    g.fillRect(startX, startY, (int) (endX - startX), rectHeight); // Draw the rectangle

                    startX = (int) endX +context_switch_cost; // Move the starting X position for the next rectangle
                }
            }
        };

        visualizationFrame.add(visualizationPanel);
        visualizationFrame.setVisible(true);
    }
}
