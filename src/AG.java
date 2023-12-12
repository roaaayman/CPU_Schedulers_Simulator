import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;
    private double quantum;
    private boolean isPreemptive = false;
    private int starttime;
    private int finishtime;
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

    private double AGFactor(Process process) {
        double arrivalTime = process.getArrivalTime();
        double burstTime = process.getBurstTime();
        double priority = process.getPriority();

        int randomValue = process.getRandom();
        double AGFactor;

        if (randomValue < 10) {
            AGFactor = randomValue + arrivalTime + burstTime;
        } else if (randomValue > 10) {
            AGFactor = 10 + arrivalTime + burstTime;
        } else {
            AGFactor = priority + arrivalTime + burstTime;
        }
        return AGFactor;
    }

    @Override
    public void schedule() {
        List<Process> readyQueue = new ArrayList<>();
        Iterator<Process> iterator = processes.iterator();

        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (process.getArrivalTime() <= currentTime) {
                readyQueue.add(process);
                iterator.remove(); // Remove safely using iterator
            }
        }


        if(!readyQueue.isEmpty() ) {
            Process currentExecution = readyQueue.get(0);
            double remaining = currentExecution.getBurstTime()-quantum;
            double limit= Math.ceil(0.5*quantum);
            if(remaining>limit)
            {
                currentExecution.setBurstTime(remaining);
                currentTime+=limit;
                if (!isPreemptive && currentTime - currentExecution.getstarttime() >= limit) {
                    isPreemptive=false;
                }


            }
            else
            {
                currentExecution.setBurstTime(0);
                currentExecution.setPreemptive(true);
                if (!isPreemptive && currentTime - currentExecution.getstarttime() >= limit) {
                    isPreemptive = true;
                }
                currentExecution.setEndtime(currentTime);

            }
        }
        if(isPreemptive && !readyQueue.isEmpty())
        {
            Process currentExecution = readyQueue.get(0);
            for(Process p:readyQueue)
            {
                double agFactorCurrent = AGFactor(currentExecution);
                double agFactorNew = AGFactor(p);
                if (agFactorNew < agFactorCurrent || (agFactorNew == agFactorCurrent && p.getArrivalTime() < currentExecution.getArrivalTime())) {
                    readyQueue.add(currentExecution);
                    readyQueue.remove(p);
                    currentExecution = p;
                }
            }
            //scenario 1
            if(currentExecution.getBurstTime()>0)
            {
                double limit=Math.ceil(0.1*quantum);
                readyQueue.add(currentExecution);
                currentExecution.setBurstTime(0);
                currentExecution.setEndtime(currentTime);
                isPreemptive=true;
                currentExecution.setQuantum(currentExecution.getQuantum()+limit);
            }
            //scenario 2
            if(currentExecution.getBurstTime()>0)
            {
                double remaining=currentExecution.getBurstTime()-quantum;
                readyQueue.add(currentExecution);
                currentExecution.setBurstTime(0);
                currentExecution.setEndtime(currentTime);
                isPreemptive=true;
                currentExecution.setQuantum(currentExecution.getQuantum()+remaining);
            }
            if(currentExecution.getBurstTime()==0)
            {
                readyQueue.remove(currentExecution);
                currentExecution.setEndtime(currentTime);
                currentExecution.setQuantum(0);
            }
            currentExecution.setStarttime(currentTime);
        }

    }




}