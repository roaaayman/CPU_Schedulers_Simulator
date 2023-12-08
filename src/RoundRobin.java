import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class RoundRobin implements Ischeduler {
    private List<Process> processes;
    //private Queue<Process> ReadyQueue ;//to put the process that finished its quantum time to then end of the ready queue
    private int q;
    public RoundRobin(int TimeQuantum)
    {
        q=TimeQuantum;
    }
    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
    public void sortonarrivialtime()
    {
        processes.sort(Comparator.comparingDouble(Process::getArrivalTime));


    }

    @Override
    public void schedule() {
        sortonarrivialtime();
        int Currenttime=0;




        }





    }
}
