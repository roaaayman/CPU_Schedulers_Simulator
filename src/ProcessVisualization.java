import java.awt.*;
import java.util.List;

public class ProcessVisualization extends Frame {
    private List<Process> processes;

    public ProcessVisualization(List<Process> processes) {
        this.processes = processes;
        setSize(800, 600);
        setVisible(true);
        setTitle("Process Execution Visualization");
        initializeVisualization();
    }

    private void initializeVisualization() {
        // Use AWT Graphics to visualize the execution order of processes
        // Draw rectangles or shapes to represent processes and show their execution details
        // Use process data like arrival time, burst time, and colors to display process information
        Graphics graphics = getGraphics();

        int y = 50;
        for (Process process : processes) {
            int x = 50;
            int width = (int) (process.getBurstTime() * 10); // Adjust the width based on burst time

            graphics.setColor(Color.BLACK);
            graphics.drawString("Process: " + process.getName() + " Arr: " + process.getArrivalTime(), 10, y);

            // Remove the line below to avoid setting the default color to blue
            // graphics.setColor(Color.BLUE);

            graphics.fillRect(x, y - 10, width, 20); // Drawing rectangles representing processes

            y += 30; // Adjust y position for the next process
        }

        animateExecution();
    }


    public void animateExecution() {
        Graphics graphics = getGraphics();
        int y = 50;
        double currentTime = 0; // Initialize the current time

        for (Process process : processes) {
            int x = 50;
            int width = (int) (process.getBurstTime() * 10); // Adjust the width based on burst time
            Color processColor = getColorFromString(process.getColor());

            // Draw a rectangle for the process with different colors to show execution steps
            for (int i = 0; i < width; i++) {
                graphics.setColor(processColor);
                graphics.fillRect(x, y - 10, i, 20); // Animation step by step

                try {
                    Thread.sleep(100); // Pause to visualize each step
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Update y position for the next process
            y += 30 + 10; // Separation between processes

            // Calculate the finish time of the current process
            double finishTime = currentTime + process.getBurstTime();
            double context_switch_cost= 1;

            // Consider context switch time for the next process
            currentTime = finishTime + context_switch_cost;

            // Adjust the time for the next process based on the current process's burst time and context switch time
            try {
                Thread.sleep((long) (context_switch_cost+process.getBurstTime())); // Delay for context switch time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    private Color getColorFromString(String color) {
        // Convert a string representation of color to a Color object
        switch (color.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }
}
