import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SRTF implements Ischeduler {
    private List<Process> processes;
    private static final double WaitTime_threshold = 10; // Maximum waiting time threshold
    private JFrame frame;

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    @Override
    public void schedule() {
        List<Process> executedProcesses = new ArrayList<>();
        List<Process> currentShortestProcesses = new ArrayList<>(); // Track the current list of shortest processes
        double currentTime = 0;
        int completed = 0;

        while (completed != processes.size()) {
            double minBurst = Double.MAX_VALUE;
            int shortestIndex = -1;

            for (int i = 0; i < processes.size(); i++) {
                Process process = processes.get(i);
                if (!executedProcesses.contains(process) && process.getArrivalTime() <= currentTime
                        && process.getBurstTime() < minBurst) {
                    shortestIndex = i;
                    minBurst = process.getBurstTime();
                }
            }

            if (shortestIndex == -1) {
                currentTime++;
                continue;
            }

            Process shortest = processes.get(shortestIndex);
            currentShortestProcesses.add(shortest); // Add the current shortest process to the list

            // Check and handle processes exceeding the wait time threshold
            for (Process process : processes) {
                if (!executedProcesses.contains(process) && process != shortest
                        && process.getArrivalTime() <= currentTime) {
                    process.setWaitTime(process.getWaitTime() + 1);

                    if (process.getWaitTime() >= WaitTime_threshold) {
                        processes.remove(process);
                        processes.add(0, process);
                        System.out.println("**Pushing Process to Front : " + process.getName() + " at time " + currentTime);

                        if (shortest.getBurstTime() > 0) {
                            System.out.println("**Preempting Process: " + shortest.getName() + " at time " + currentTime);
                            shortest = processes.get(0);
                            currentShortestProcesses.add(shortest); // Add the preempted process to the list
                            break;
                        }
                    }
                }
            }
            double startTime = currentTime;
            System.out.println("Executing Process: " + shortest.getName() + " from time " + startTime);

            shortest.setBurstTime(shortest.getBurstTime() - 1);
            currentTime++;

            if (shortest.getBurstTime() == 0) {
                executedProcesses.add(shortest);
                completed++;

                double finishTime = currentTime;
                double turnaroundTime = finishTime - shortest.getArrivalTime();
                double waitingTime = turnaroundTime - shortest.getOriginalBurstTime();

                shortest.setWaitTime(waitingTime);
                shortest.setTurnaround(turnaroundTime);
                System.out.println("--------------------------------");
                System.out.println("Time Details for Process " + shortest.getName() + " :  \n");
                System.out.println("Finish Time for Process " + shortest.getName() + ": " + finishTime);
                System.out.println("Waiting Time for Process " + shortest.getName() + ": " + waitingTime);
                System.out.println("Turnaround Time for Process " + shortest.getName() + ": " + turnaroundTime);
                System.out.println("--------------------------------");
            }

            // Update the visualization with the current list of shortest processes

        }

        // Calculate averages
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
        System.out.println("Visualizing...");
        visualizeAlgorithmOutput(currentShortestProcesses);



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
                int rectHeight = 30; // Height of each rectangle

                for (Process process : processes) {
                    double duration = process.getOriginalBurstTime() - process.getBurstTime(); // Calculate the actual duration
                    double endX = startX + duration * 10; // Calculate the end X position based on duration

                    g.setColor(process.getColor()); // Use the process color for the rectangle
                    g.fillRect(startX, startY, (int) (duration * 10), rectHeight); // Draw the rectangle

                    startX = (int) endX; // Move the starting X position for the next rectangle
                }
            }
        };

        visualizationFrame.add(visualizationPanel);
        visualizationFrame.setVisible(true);
    }
}