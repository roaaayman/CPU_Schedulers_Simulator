import java.awt.*;

public class Process {

    private String name;
    private double arrivalTime;
    private double originalBurstTime;
    private double burstTime;
    private int priorityNum;
    private double turnaround;
    private double waitTime;
    private double finishTime;

    private double remainingtime;
    private boolean preemptive;
    private int random;
    private Color color;
    private int starttime;
    private int endtime;
    private double waitingTime=0;



    //constructor
    public Process(String name, double arrivalTime, double burstTime , double originalBurstTime , int priorityNum, int random, Color color) {
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

    public void setFinishTime(double finishTime) {this.finishTime = finishTime;}
    public void setPreemptive(boolean preemptive) {
        this.preemptive = false;
    }
public void setColor(Color color){this.color=color;}




    // Getters
    public String getName() {
        return name;
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

    public double getFinishTime() {return finishTime;}

    public double getRemainingtime(){return remainingtime;}

    public void incrementPrioritybyaging() {

        priorityNum=1;
    }

    public boolean isPreemptive() {
        return preemptive;
    }
    public Color getcolor()
    {
        return color;
    }
    public void incrementWaitingTime() {
        this.waitingTime++;
    }


}

