import java.util.*;

public class AG implements Ischeduler {
    private List<Process> processes;
    private List<Process> ready;

    private double quantum;
    private int currentTime = 0;
    Process tempProcess = null;
    Process currentProcess = null;
    int completeProcesses=0; //no of completed Processes
    double avgW=0,avgT=0;
    int smallest=0;
    int timer=0;
    int casee = 0; //to decide which scenario
    int min= 0;  //

    public AG(double quantum) {
        this.quantum = quantum;
        this.processes = new ArrayList<>();
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
        for(int i=0;i<processes.size();i++)
        {
            processes.get(i).remainingburstTime=processes.get(i).burstTime;
            processes.get(i).remainingquantum=processes.get(i).Quantum;

        }
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

    }
    public void printQuarter()
    {
        System.out.print("\nQuantum(");
        for (int i = 0; i < processes.size(); i++) {
            if (i != 0)
                System.out.print(',');
            System.out.print(processes.get(i).getQuantum());
        }
        System.out.print(")-> ceil(25%) = (");
        for (int i = 0; i < processes.size(); i++) {
            if (i != 0)
                System.out.print(',');
            if(currentProcess.getName().equals(processes.get(i).getName())) {
                System.out.print((int) Math.ceil(currentProcess.getQuantum() / 2.0));
            }
            else{
                System.out.print(processes.get(i).getQuantum());
            }
        }
        System.out.print(')');
    }
    public void checkArrival() //to check the arrival of the process
    {
        while (smallest < processes.size() && processes.get(smallest).getArrivalTime() <= timer) {
            ready.add(processes.get(smallest));
            smallest++;
        }
        if(ready.isEmpty())
        {
            timer++;
        }

    }

    public void calculations() //to calculate the waiting time and turn around time
    {
        for(int i=0;i<processes.size();i++)
        {
            processes.get(i).turnaround=processes.get(i).endtime-processes.get(i).arrivalTime;
            avgT+=processes.get(i).turnaround;
            processes.get(i).waitTime=processes.get(i).turnaround-processes.get(i).burstTime;
            avgW+=processes.get(i).waitTime;
        }
    }
    public void print() //print the info the process
    {

        System.out.println("\n");
        System.out.println("\npid  arrival  burst  completion  turn   waiting    quantum  randomnumber AGFactor ");
        for(int i=0;i<processes.size();i++)
        {

            System.out.println(processes.get(i).getName()+" \t "+processes.get(i).getArrivalTime()+" \t"+processes.get(i).getBurstTime()+" \t"+processes.get(i).getEndtime()+
                    " \t  "+processes.get(i).getTurnaround()+"  \t "+processes.get(i).getWaitTime()+"\t  "+processes.get(i).getQuantum()+"\t  "+processes.get(i).getRandom()+"\t  "+AGFactor(processes.get(i)));
        }
        System.out.println("\nAverage waiting time: "+ (avgW/processes.size()));     // printing average waiting time.
        System.out.println("Average turnaround time:"+(avgT/processes.size()));


    }
    public void preemptive()
    {
        if (currentProcess != null) {
            // Check if the process has used up 50% of its quantum time
            if (currentProcess.Quantum == Math.ceil(0.5 * currentProcess.getQuantum())) {
                // Convert to preemptive AG
                currentProcess.setPreemptive(true);

                // Find a process with a smaller AG-Factor
                Process processWithSmallerAG = findProcessWithSmallerAG(readyQueue, AGFactor(currentProcess));

            }
        }
    }
    public void scenario_1() //if a running process used all of its quantum time and still have the job to do
    {
        int sum=0;
        for(Process p:processes)
        {
            sum+=p.Quantum;
        }
        int mean=sum/ processes.size();
        currentProcess.Quantum+=Math.ceil(0.1*mean);
        currentProcess.remainingquantum+=Math.ceil(0.1*mean);
        tempProcess = currentProcess;
        ready.add(currentProcess);
        currentProcess = null;
        casee=0;

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

    }
}