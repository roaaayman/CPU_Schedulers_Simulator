import java.awt.*;

public class Process {

     String name;
     int arrivalTime;
     double originalBurstTime;
     double burstTime;
     double remainingburstTime;
     int priorityNum;
     int starttime;
     double turnaround;
     int endtime;

     double waitTime;
     double remainingtime;
     boolean preemptive;
     int random;
     double Quantum;
     String color;



    //constructor
    public Process(String name, int arrivalTime, double burstTime , double originalBurstTime , int priorityNum, int random, String color,double Quantum) {
        this.name=name;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.originalBurstTime=burstTime;
        this.priorityNum=priorityNum;
        this.remainingburstTime=burstTime;
        this.Quantum=Quantum;
        this.random=random;
        this.color=color;
        this.preemptive=false;

    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setRandom(int random){
        this.random=random;
    }

    public void setQuantum(double Quantum){
        this.Quantum=Quantum;
    }

    public double getQuantum(){return Quantum;}
    public void setArrivalTime(int arrivalTime) {
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

    public void setPreemptive(boolean preemptive) {
        this.preemptive = false;
    }
public void setColor(String color){this.color=color;}




    // Getters
    public String getName() {
        return name;
    }

    public int getRandom(){return random;}


    public int getArrivalTime() {
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
    public double getRemainingtime(){return remainingtime;}

    public void incrementPrioritybyaging() {

        priorityNum=1;
    }

    public boolean isPreemptive() {
        return preemptive;
    }
    public String getcolor()
    {
        return color;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getstarttime()
    {
        return starttime;
    }
    public int getEndtime()
    {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }
}

