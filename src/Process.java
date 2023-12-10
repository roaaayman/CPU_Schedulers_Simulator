public class Process {

    private String name;
    private String color;
    private double arrivalTime;
    private double originalBurstTime;
    private double burstTime;
    private int priorityNum;
    private double turnaround;
    private double waitTime;
    private double remainingtime;
    private boolean preemptive;



    //constructor
    public Process(String name, double arrivalTime, double burstTime , double originalBurstTime , int priorityNum, String color) {
        this.name=name;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.originalBurstTime=burstTime;
        this.priorityNum=priorityNum;
        this.color=color;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
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

    public void setPreemptive(boolean preemptive) {
        this.preemptive = preemptive;
    }




    // Getters
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

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
    public double getRemainingtime(){return remainingtime;}

    public void incrementPrioritybyaging() {

        priorityNum=1;
    }

    public boolean isPreemptive() {
        return preemptive;
    }


}

