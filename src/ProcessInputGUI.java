import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessInputGUI extends JFrame {
    private List<Process> processes;
    private DefaultTableModel tableModel;
    private SJF sjfScheduler;

    public ProcessInputGUI() {
        processes = new ArrayList<>();
        tableModel = new DefaultTableModel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField numProcessesField = new JTextField();
        JTextField timeQuantumField = new JTextField();
        JTextField contextSwitchCostField = new JTextField();
        JTextField typeofschedule = new JTextField();


        inputPanel.add(new JLabel("Number of Processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(new JLabel("Time Quantum for Round Robin:"));
        inputPanel.add(timeQuantumField);
        inputPanel.add(new JLabel("Context Switch Cost for SJF:"));
        inputPanel.add(contextSwitchCostField);
        inputPanel.add(new JLabel("type of schedule"));
        inputPanel.add(typeofschedule);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            int numProcesses = Integer.parseInt(numProcessesField.getText());
            // Get other values like time quantum and context switch cost similarly

            for (int i = 0; i < numProcesses; i++) {
                String name = JOptionPane.showInputDialog("Enter Process Name:");
                double arrivalTime = Double.parseDouble(JOptionPane.showInputDialog("Enter Arrival Time:"));
                double burstTime = Double.parseDouble(JOptionPane.showInputDialog("Enter Burst Time:"));
                int priorityNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority Num:"));

                // Use JColorChooser to allow users to select a color from the screen
                Color processColor = JColorChooser.showDialog(mainPanel, "Choose Color", Color.BLACK);

                processes.add(new Process(name, arrivalTime, burstTime, 0, priorityNum, 0, processColor));
            }

            displayProcessInformation();
            // Get the input from typeofschedule field
            String scheduleType = typeofschedule.getText();
            // Use switch-case based on the scheduleType
            switch (scheduleType) {
                case "SJF":
                    // Execute SJF visually after entering data
                    sjfScheduler = new SJF(Integer.parseInt(contextSwitchCostField.getText()));
                    sjfScheduler.setProcesses(processes);
                    sjfScheduler.schedule();
                    break;
//                case "SRTF":
//                    // Execute SRTF visually after entering data
//                    SRTF srtfScheduler = new SRTF(Integer.parseInt(contextSwitchCostField.getText()));
//                    srtfScheduler.setProcesses(processes);
//                    srtfScheduler.schedule();
//                    break;
                  case "Priority":
                      // Execute Priority Scheduler
                      prioirtySchedling priorityScheduler = new prioirtySchedling();
                      priorityScheduler.setProcesses(new ArrayList<>(processes));

                      System.out.println("\nExecuting Priority Scheduling Algorithm: \n");
                      priorityScheduler.schedule();
                      break;

//                case "AG":
//                    // Execute Agile scheduling visually after entering data
//                    AG agileScheduler = new AG();
//                    agileScheduler.setProcesses(processes);
//                    agileScheduler.schedule();
//                    break;
                default:
                    // Handle other cases or provide an error message for invalid input
                    break;
            }


        });

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        tableModel.addColumn("Name");
        tableModel.addColumn("Arrival Time");
        tableModel.addColumn("Burst Time");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Color");

        JTable processTable = new JTable(tableModel);
        processTable.getColumnModel().getColumn(4).setCellRenderer(new ColorRenderer()); // Set renderer for Color column
        JScrollPane tableScrollPane = new JScrollPane(processTable);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setTitle("Process Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displayProcessInformation() {
        for (Process process : processes) {
            String name = process.getName();
            double arrivalTime = process.getArrivalTime();
            double burstTime = process.getBurstTime();
            int priority = process.getPriority();
            Color color = process.getColor();

            tableModel.addRow(new Object[]{
                    name,
                    arrivalTime,
                    burstTime,
                    priority,
                    color
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProcessInputGUI::new);
    }

    // Custom cell renderer to display Color as a colored square in the table
    class ColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Color) {
                JLabel colorLabel = new JLabel();
                colorLabel.setOpaque(true);
                colorLabel.setBackground((Color) value);
                return colorLabel;
            }
            return cellComponent;
        }
    }
}