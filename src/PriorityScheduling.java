
        import javax.swing.*;
        import java.awt.*;
        import java.util.ArrayList;
        import java.util.Collections;
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
            // Aging: Decrease priority of the process with the most waiting time
            if (!arrivedNotExecuted.isEmpty()) {
                Process maxWaitingProcess = Collections.max(arrivedNotExecuted, Comparator.comparingDouble(Process::getWaitTime));
                int newPriority = (int)  (maxWaitingProcess.getPriority() * 0.1); // Example: reducing priority by 10%
                maxWaitingProcess.setPriority(newPriority);

                // Sort arrived processes based on priority
                arrivedNotExecuted.sort(Comparator.comparingInt(Process::getPriority));


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

                    g.setColor(process.getColor()); // Use the process color for the rectangle
                    g.fillRect(startX, startY, (int) (endX - startX), rectHeight); // Draw the rectangle

                    startX = (int) endX ; // Move the starting X position for the next rectangle
                }
            }
        };

        visualizationFrame.add(visualizationPanel);
        visualizationFrame.setVisible(true);
    }
}
