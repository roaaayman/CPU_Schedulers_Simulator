import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;
    private double quantum;
    private int currentTime = 0;

    public AG(double quantum) {
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
        List<Process>readyQueue=new ArrayList<>();
        List<Process>dieList=new ArrayList<>();
        List<Process>runningQueue=new ArrayList<>();
        Process currentProcess=null;
        int currentTime=0;
        while (!processes.isEmpty() || !readyQueue.isEmpty() || currentProcess!=null)
        {
            Iterator<Process> iterator = processes.iterator();
            while (iterator.hasNext()) {
                Process p = iterator.next();
                if (p.getArrivalTime() == currentTime) {
                    readyQueue.add(p);
                    iterator.remove();  // Use iterator to remove the element safely
                }
            }

            if(currentProcess!=null)
            {

                currentProcess.setStarttime(currentTime);
                System.out.println("Time " + currentTime + ": Process " + currentProcess.getName() + " is running.");
                currentProcess.Quantum--;
                currentProcess.remainingburstTime=currentProcess.burstTime--;

                    if(!currentProcess.preemptive&&currentProcess.Quantum==Math.ceil(0.5* currentProcess.Quantum)) {
                        currentProcess.preemptive = true;
                        Process processWithSmallerAG = findProcessWithSmallerAG(readyQueue, AGFactor(currentProcess));
                        if (processWithSmallerAG != null) {
                            readyQueue.add(currentProcess);
                            runningQueue.remove(currentProcess);
                            currentProcess = processWithSmallerAG;
                            runningQueue.add(currentProcess);
                        }
                        //scenario 2
                        if(currentProcess.Quantum>0 && currentProcess.remainingburstTime>0)
                        {
                            readyQueue.add(currentProcess);
                            runningQueue.remove(currentProcess);
                            double unusedQuantum=currentProcess.Quantum;
                            currentProcess.Quantum+=unusedQuantum;

                        }
                        else if (currentProcess.Quantum == 0) {
                            //scenario 1
                            if(currentProcess.remainingburstTime>0)
                            {
                                readyQueue.add(currentProcess);
                                runningQueue.remove(currentProcess);
                                int sum=0;
                                for(Process p:processes)
                                {
                                    sum+=p.Quantum;
                                }
                                int mean=sum/ processes.size();
                                currentProcess.Quantum+=Math.ceil(0.1*mean);
                            }
                            //scenario 3
                            else {
                                dieList.add(currentProcess);
                                runningQueue.remove(currentProcess);
                            }

                        }


                    }
            }
            else {
                if(!readyQueue.isEmpty())
                {
                    currentProcess=readyQueue.remove(0);
                    runningQueue.add(currentProcess);
                }
            }
            currentTime++;
        }
    }
    private Process findProcessWithSmallerAG(List<Process> readyQueue, double currentAGFactor) {
        for (Process process : readyQueue) {
            if (AGFactor(process) < currentAGFactor) {
                return process;
            }
        }
        return null;
    }
}