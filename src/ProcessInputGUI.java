import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Random;


class Process {

    private String name;
    private double arrivalTime;
    private double originalBurstTime;
    private double burstTime;
    private int priorityNum;
    private double turnaround;
    private double waitTime;

    int Quantum;
    double remainingQuantum;
    private boolean preemptive;
    private int random;
    private Color color;
    double agFactor ;
    int completionTime;




    //constructor
    public Process(String name, double arrivalTime, double burstTime , double originalBurstTime , int priorityNum, Color color, int random) {
        this.name=name;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.originalBurstTime=burstTime;
        this.priorityNum=priorityNum;

        this.random=random;
        this.color=color;

    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setRandom(int random){
        this.random=random;
    }


    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(double burstTime) {
        this.burstTime = burstTime;
    }

    public void setOriginalBurstTime(double originalBurstTime) {this.originalBurstTime = originalBurstTime;}

    public void setPriority(int priorityNum) {
        this.priorityNum = priorityNum;
    }
    public void setTurnaround(double turnaround) {this.turnaround = turnaround;}

    public void setWaitTime(double waitTime) {this.waitTime = waitTime;}

    public void setPreemptive(boolean preemptive) {this.preemptive = false;}

    public Color getColor(){
        return color;
    }

    public void setQuantumTime(int quantum) {
        this.Quantum = quantum;
        this.remainingQuantum = quantum;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getAgFactor(){
        return agFactor;
    }

    public int getRandom(){return random;}


    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getBurstTime() {
        return burstTime;
    }
    public double getOriginalBurstTime() {return originalBurstTime;}

    public int getPriority() {
        return priorityNum;
    }

    public double getTurnaround() {return turnaround;}

    public double getWaitTime() {return waitTime;}

    public int getQuantumTime() {
        return Quantum;
    }


    // Method to update burst time based on execution
    public void execute() {
        if (burstTime > 0) {
            burstTime--;
        }
    }

    // Method to check if the process still has work to do
    public boolean hasJobToDo() {
        return burstTime > 0;
    }


    public boolean getPreemptive() {
        return preemptive ;
    }

    public double getRemainingQuantum() {
        return  remainingQuantum ;
    }
    public void decreaseRemainingQuantum() {
        remainingQuantum--;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    public void setCompletionTime(int completionTime)
    {
        this.completionTime=completionTime;
    }
}
 interface Ischeduler {
    public void schedule();
}


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


 class SRTF implements Ischeduler {
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
 class SJF implements Ischeduler {
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

                    g.setColor(process.getColor()); // Use the process color for the rectangle
                    g.fillRect(startX, startY, (int) (endX - startX), rectHeight); // Draw the rectangle

                    startX = (int) endX +context_switch_cost; // Move the starting X position for the next rectangle
                }
            }
        };

        visualizationFrame.add(visualizationPanel);
        visualizationFrame.setVisible(true);
    }
}



class AG implements Ischeduler {
    private List<Process> processes;
    private int quantum;
    private int currentTime = 0;

    public AG(int quantum) {
        this.quantum = quantum;
        this.processes = new ArrayList<>();
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public static int randomFactor() {
        Random random = new Random();
        return random.nextInt(21); // Generates a number between 0 and 20
    }

    private double calculateAGFactor(Process process) {
        double arrivalTime = process.getArrivalTime();
        double burstTime = process.getBurstTime();
        double priority = process.getPriority();

        int randomValue = randomFactor();
        double AGFactor;

        if (randomValue < 10) {
            AGFactor = randomValue + arrivalTime + burstTime;
        } else if (randomValue > 10) {
            AGFactor = 10 + arrivalTime + burstTime;
        } else {
            AGFactor = priority + arrivalTime + burstTime;
        }
        process.agFactor = AGFactor;
        return AGFactor;
    }

    private void scenario_1(Process runningProcess, List<Process> readyQueue) {

        double totalQuantum = 0;
        for (Process process : readyQueue) {
            totalQuantum += process.getQuantumTime();
        }

        double meanOfQuantum = totalQuantum / readyQueue.size();

        int newQuantum = runningProcess.getQuantumTime() + (int) Math.ceil(0.1 * (meanOfQuantum));
        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
        runningProcess.setQuantumTime(newQuantum);
        readyQueue.add(runningProcess);
        runningProcess = null;
    }

    private void scenario_2(Process runningProcess, List<Process> readyQueue) {

        double newQuantum = runningProcess.getRemainingQuantum() + runningProcess.getQuantumTime();
        runningProcess.setQuantumTime((int) newQuantum);
        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
        readyQueue.add(runningProcess);
        runningProcess = null;
    }

    private void scenario_3(Process runningProcess, List<Process> dieList, int time) {
        runningProcess.setQuantumTime(0);
        int completionTime = time;
        runningProcess.setCompletionTime(completionTime);
        System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
        dieList.add(runningProcess);
    }


    public void sortReadyQueue(List<Process> readyQueue) {
        for (int i = 0; i < readyQueue.size() - 1; i++) {
            for (int j = 0; j < readyQueue.size() - i - 1; j++) {
                Process process1 = readyQueue.get(j);
                Process process2 = readyQueue.get(j + 1);

                if (process1.getAgFactor() > process2.getAgFactor()) {
                    // Swap processes if they are out of order
                    readyQueue.set(j, process2);
                    readyQueue.set(j + 1, process1);
                }
            }
        }
    }

    @Override
    public void schedule() {

        List<Process> readyQueue = new ArrayList<>(processes); // Make a copy of processes to act as the ready queue
        List<Process> dieList = new ArrayList<>(); // List to hold finished processes
        Process runningProcess = null; // Track the currently running process
        int time = 0;


        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            p.setQuantumTime(quantum);

        }
        while (!readyQueue.isEmpty()) {
            for (int i = 0; i < readyQueue.size(); i++) {
                Process p = readyQueue.get(i);
                calculateAGFactor(p);
            }

            sortReadyQueue(readyQueue);
            System.out.println("Current Time: " + time);

            for (int i = 0; i < readyQueue.size(); i++) {
                Process p = readyQueue.get(i);
                if (p.getArrivalTime() <= time) {
                    runningProcess = readyQueue.remove(i);
                    runningProcess.setPreemptive(false);
                    break;
                }
            }


            if (!runningProcess.getPreemptive()) {
                int halfQuantum = (int) Math.ceil(runningProcess.getRemainingQuantum() * 0.5);
                for (int i = 0; i < halfQuantum; i++) {
                    System.out.println("Time " + time + ":" + runningProcess.getName() + " is running");

                    runningProcess.decreaseRemainingQuantum();
                    runningProcess.execute();
                    time++;
                }

                runningProcess.setPreemptive(true);
                System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getRemainingQuantum());
            }

            while (runningProcess.hasJobToDo()) {
                if (readyQueue.isEmpty()) {
                    break;
                }

                Process smallProcess = readyQueue.get(0);
                double smallestAGFactor = calculateAGFactor(smallProcess);

                for (Process process : readyQueue) {
                    double agFactor = calculateAGFactor(process);
                    process.agFactor = agFactor;
                    if (agFactor < smallestAGFactor && process.getArrivalTime() >= time) {
                        smallestAGFactor = agFactor;
                        smallProcess = process;
                    }
                }

                if (smallProcess == null || calculateAGFactor(smallProcess) > calculateAGFactor(runningProcess)) {
                    System.out.println("Time " + time + ":" + runningProcess.getName() + " is running");
                    time++;
                    runningProcess.decreaseRemainingQuantum();
                    runningProcess.execute();
                    System.out.println("Updated Quantum for " + runningProcess.getName() + ": " + runningProcess.getQuantumTime());
                } else {
                    break;
                }
            }

            if (runningProcess.hasJobToDo()) {
                if (runningProcess.getRemainingQuantum() == 0) {
                    scenario_1(runningProcess, readyQueue);
                } else {
                    // Scenario ii: Process didn't use all its quantum time based on another process conversion
                    scenario_2(runningProcess, readyQueue);
                }
            } else if (!(runningProcess.hasJobToDo())) {
                // Scenario iii: Process finished its job
                scenario_3(runningProcess, dieList, time);
            }
        }
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (int i = 0; i < dieList.size(); i++) {
            Process process = dieList.get(i);
            double turnaroundTime = process.getCompletionTime() - process.getArrivalTime();
            double waitingTime = turnaroundTime - process.getBurstTime();

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            System.out.println("Process " + process.getName() + " - Waiting Time: " + waitingTime + ", Turnaround Time: " + turnaroundTime);
        }

        double averageWaitingTime = (double) totalWaitingTime / dieList.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / dieList.size();

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        String processStatus;
        if (runningProcess.getBurstTime() == 0) {
            processStatus = runningProcess.getName();
        } else {
            processStatus = "None";
        }
        System.out.println("Running Process: " + processStatus);            visualizeAlgorithmOutput(dieList);

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
                    double duration = process.getCompletionTime() - process.getArrivalTime(); // Calculate the actual duration
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



public class ProcessInputGUI extends JFrame {
    private List<Process> processes;
    private DefaultTableModel tableModel;
    private SJF sjfScheduler;

    public ProcessInputGUI() {
        processes = new ArrayList<>();
        tableModel = new DefaultTableModel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField numProcessesField = new JTextField();
        JTextField timeQuantumField = new JTextField();
        JTextField contextSwitchCostField = new JTextField();
        JTextField typeofschedule = new JTextField();


        inputPanel.add(new JLabel("Number of Processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(new JLabel("Time Quantum for Round Robin:"));
        inputPanel.add(timeQuantumField);
        inputPanel.add(new JLabel("Context Switch Cost for SJF:"));
        inputPanel.add(contextSwitchCostField);
        inputPanel.add(new JLabel("type of schedule"));
        inputPanel.add(typeofschedule);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            int numProcesses = Integer.parseInt(numProcessesField.getText());
            // Get other values like time quantum and context switch cost similarly

            for (int i = 0; i < numProcesses; i++) {
                String name = JOptionPane.showInputDialog("Enter Process Name:");
                double arrivalTime = Double.parseDouble(JOptionPane.showInputDialog("Enter Arrival Time:"));
                double burstTime = Double.parseDouble(JOptionPane.showInputDialog("Enter Burst Time:"));
                int priorityNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority Num:"));

                // Use JColorChooser to allow users to select a color from the screen
                Color processColor = JColorChooser.showDialog(mainPanel, "Choose Color", Color.BLACK);

                processes.add(new Process(name, arrivalTime, burstTime, 0, priorityNum, processColor,0));
            }

            displayProcessInformation();
            // Get the input from typeofschedule field
            String scheduleType = typeofschedule.getText();
            // Use switch-case based on the scheduleType
            switch (scheduleType) {
                case "sjf":
                    // Execute SJF visually after entering data
                    sjfScheduler = new SJF(Integer.parseInt(contextSwitchCostField.getText()));
                    sjfScheduler.setProcesses(processes);
                    sjfScheduler.schedule();
                    break;
                case "srtf":
                    // Execute SRTF visually after entering data
                    SRTF srtfScheduler = new SRTF();
                    srtfScheduler.setProcesses(processes);
                    srtfScheduler.schedule();
                    break;
                case "Priority":
                    // Execute Priority Scheduler
                    prioirtySchedling priorityScheduler = new prioirtySchedling();
                    priorityScheduler.setProcesses(new ArrayList<>(processes));

                    System.out.println("\nExecuting Priority Scheduling Algorithm: \n");
                    priorityScheduler.schedule();
                    break;

                case "AG":
                // Execute AG scheduling visually after entering data
                    AG agileScheduler = new AG(Integer.parseInt(timeQuantumField.getText()));
                    agileScheduler.setProcesses(processes);
                    agileScheduler.schedule();
                    break;
                default:
                    // Handle other cases or provide an error message for invalid input
                    break;
            }


        });

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        tableModel.addColumn("Name");
        tableModel.addColumn("Arrival Time");
        tableModel.addColumn("Burst Time");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Color");

        JTable processTable = new JTable(tableModel);
        processTable.getColumnModel().getColumn(4).setCellRenderer(new ColorRenderer()); // Set renderer for Color column
        JScrollPane tableScrollPane = new JScrollPane(processTable);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setTitle("Process Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displayProcessInformation() {
        for (Process process : processes) {
            String name = process.getName();
            double arrivalTime = process.getArrivalTime();
            double burstTime = process.getBurstTime();
            int priority = process.getPriority();
            Color color = process.getColor();

            tableModel.addRow(new Object[]{
                    name,
                    arrivalTime,
                    burstTime,
                    priority,
                    color
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProcessInputGUI::new);
    }

    // Custom cell renderer to display Color as a colored square in the table
    class ColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Color) {
                JLabel colorLabel = new JLabel();
                colorLabel.setOpaque(true);
                colorLabel.setBackground((Color) value);
                return colorLabel;
            }
            return cellComponent;
        }
    }
}