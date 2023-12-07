public class Process {
    private String name;
    private String color;
    private double arrivalTime;
    private double originalBurstTime;
    private double burstTime;
    private int priorityNum;
    private double turnaround;
    private double waitTime;

    public Process() {
    }

    //constructor
    public Process(String name, double arrivalTime, double burstTime , double originalBurstTime , int priorityNum) {
        this.name=name;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.originalBurstTime=burstTime;
        this.priorityNum=priorityNum;
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

    public void incrementPrioritybyaging() {

        priorityNum++;
    }
    public void decrementPrioritybyaging() {

        priorityNum--;
    }
}

