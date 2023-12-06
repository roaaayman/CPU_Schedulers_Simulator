public class Process {
    private String name;
    private String color;
    private double arrivalTime;
    private double burstTime;
    private int priorityNum;
    private double turnaround;
    private double waittime;

    public Process() {
    }

    public Process(String name, double arrivalTime, double burstTime,int priorityNum) {
        this.name=name;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
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

    public void setPriorityNum(int priorityNum) {
        this.priorityNum = priorityNum;
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

    public int getPriority() {
        return priorityNum;
    }
}