import java.util.List;
import java.util.Random;

public class AG implements Ischeduler {
    private double AGFactor;
    private List<Process> processes;

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public static int randomFactor() {
        Random random = new Random();
        return random.nextInt(21); // Generates a number between 0 and 20
    }

    public void setAGFactor() {
        for (Process process : processes)
        {
            double arrivalTime = process.getArrivalTime();
            double burstTime = process.getBurstTime();
            double prioritynum= process.getPriority();
            if (randomFactor() < 10) {
                AGFactor = randomFactor() + arrivalTime + burstTime;
            }
            if (randomFactor() >10) {
                AGFactor = 10 + arrivalTime + burstTime;
            }
            if (randomFactor() == 10) {
                AGFactor = prioritynum + arrivalTime + burstTime;
            }

        }
    }

    @Override
    public void schedule() {
        // Implement your scheduling logic here
    }
}
