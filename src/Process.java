import java.awt.*;
import java.util.*;

class Process {

    private String name;
    private double arrivalTime;
    private double originalBurstTime;
    private double burstTime;
    private int priorityNum;
    private double turnaround;
    private double waitTime;
    private double finishTime;

    int Quantum;
    double remainingQuantum;
    private boolean preemptive;
    private int random;
    private Color color;
    double agFactor ;
    int completionTime;




    //constructor
    public Process(String name, double arrivalTime, double burstTime , double originalBurstTime , int priorityNum, Color color) {
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
    public void setPreemptive(boolean preemptive) {this.preemptive = false;}
    public void setColor(
            Color color){this.color=color;}
    public Color getColor(){
        return color;
    }

    public void setQuantumTime(int quantum) {
        this.Quantum = quantum;
        this.remainingQuantum = quantum;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getAgFactor(){
        return agFactor;
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
    public int getQuantumTime() {
        return Quantum;
    }
    public double getRemainingtime(){return remainingQuantum;}
    public void incrementPrioritybyaging() {

        priorityNum=1;
    }
    public boolean isPreemptive() {
        return preemptive;
    }
    // Method to update burst time based on execution
    public void execute() {
        if (burstTime > 0) {
            burstTime--;
            remainingQuantum--;
        }
    }

    // Method to check if the process still has work to do
    public boolean hasJobToDo() {
        return burstTime > 0;
    }

    // Update isPreemptive method based on scheduler logic
    public boolean Preemptive()
    {
        return preemptive && burstTime > 0;
    }


    public boolean getPreemptive() {
        return preemptive ;
    }

    public double getRemainingQuantum() {
        return  remainingQuantum ;
    }

    public int getCompletionTime() {
        return completionTime;
    }
    public void setCompletionTime(int completionTime)
    {
        this.completionTime=completionTime;
    }
}