import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessInputGUI {
    private JFrame frame;
    private List<Process> processes;
    private List<Color> processColors; // New list to store colors
    private DefaultTableModel tableModel;
    private int selectedAlgorithm;
    private String schedulingOutput;

    public ProcessInputGUI() {
        frame = new JFrame("Process Information");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        processes = new ArrayList<>();
        processColors = new ArrayList<>(); // Initialize the color list

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        panel.add(new JLabel("Number of Processes: "));
        JTextField numProcessesField = new JTextField();
        panel.add(numProcessesField);

        panel.add(new JLabel("Time Quantum for Round Robin: "));
        JTextField timeQuantumField = new JTextField();
        panel.add(timeQuantumField);

        panel.add(new JLabel("Context Switch Cost for SJF: "));
        JTextField contextSwitchCostField = new JTextField();


        panel.add(contextSwitchCostField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            int numProcesses = Integer.parseInt(numProcessesField.getText());
            int timeQuantum = Integer.parseInt(timeQuantumField.getText());
            int contextSwitchCost = Integer.parseInt(contextSwitchCostField.getText());

            for (int i = 0; i < numProcesses; i++) {
                String name = JOptionPane.showInputDialog("Enter Process Name: ");
                double arrivalTime = Double.parseDouble(JOptionPane.showInputDialog("Enter Arrival Time: "));
                double burstTime = Double.parseDouble(JOptionPane.showInputDialog("Enter Burst Time: "));
                int priorityNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority Num: "));
                String colorString = JOptionPane.showInputDialog("Enter Color (e.g., RED, GREEN, BLUE): ");
                Color color = getColorFromString(colorString); // Convert string color to Color object

                processes.add(new Process(name, arrivalTime, burstTime, 0, priorityNum, 0, colorString));
                processColors.add(color); // Store the color separately
            }

            // Create the table and display it
            JTable table = displayTable();

            // Execute the selected algorithm based on the choice stored in selectedAlgorithm
            switch (selectedAlgorithm) {
                case 1: // SJF
                    SJF sjfScheduler = new SJF(contextSwitchCost);
                    sjfScheduler.setProcesses(new ArrayList<>(processes));
                    sjfScheduler.schedule();

                    break;
                case 2: // SRTF
                    // Code for SRTF and other algorithms
                    break;
                case 3: // Priority
                    // Code for Priority algorithm
                    break;
                case 4: // AG
                    AG agScheduler = new AG(timeQuantum);
                    agScheduler.setProcesses(new ArrayList<>(processes));
                    agScheduler.schedule();
                   // schedulingOutput = "AG Scheduling Output:\n" + agScheduler.getOutput();
                    break;
                default:
                    schedulingOutput = "Invalid choice. No algorithm executed.";
                    break;
            }

            // Create split pane and display scheduling output and the table
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(new JScrollPane(new JTextArea(schedulingOutput)));
            splitPane.setBottomComponent(new JScrollPane(table));

            JFrame outputFrame = new JFrame("Scheduling Output and Process Details");
            outputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            outputFrame.setSize(800, 600);

            outputFrame.add(splitPane);
            outputFrame.setVisible(true);

            frame.dispose(); // Close the input frame
        });
        panel.add(submitButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu scheduleMenu = new JMenu("Schedule");
        JMenuItem sjfItem = new JMenuItem("SJF");
        JMenuItem srtfItem = new JMenuItem("SRTF");
        JMenuItem priorityItem = new JMenuItem("Priority");
        JMenuItem agItem = new JMenuItem("AG");

        sjfItem.addActionListener(e -> selectedAlgorithm = 1);
        srtfItem.addActionListener(e -> selectedAlgorithm = 2);
        priorityItem.addActionListener(e -> selectedAlgorithm = 3);
        agItem.addActionListener(e -> selectedAlgorithm = 4);

        scheduleMenu.add(sjfItem);
        scheduleMenu.add(srtfItem);
        scheduleMenu.add(priorityItem);
        scheduleMenu.add(agItem);
        menuBar.add(scheduleMenu);

        frame.setJMenuBar(menuBar);

        frame.add(panel);
        frame.setVisible(true);
    }

    private JTable displayTable() {
        JFrame tableFrame = new JFrame("Process Details");
        tableFrame.setSize(600, 400);
        tableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Process Name");
        tableModel.addColumn("Arrival Time");
        tableModel.addColumn("Burst Time");
        tableModel.addColumn("Priority Num");
        tableModel.addColumn("Color");

        JTable table = new JTable(tableModel) {
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 4) {
                    return new ColorRenderer();
                }
                return super.getCellRenderer(row, column);
            }
        };

        JScrollPane scrollPane = new JScrollPane(table);

        for (int i = 0; i < processes.size(); i++) {
            tableModel.addRow(new Object[]{
                    processes.get(i).getName(),
                    processes.get(i).getArrivalTime(),
                    processes.get(i).getBurstTime(),
                    processes.get(i).getPriority(),
                    processColors.get(i) // Retrieve color from the separate list
            });
        }

        panel.add(scrollPane, BorderLayout.CENTER);

        tableFrame.add(panel);
        tableFrame.setVisible(true);

        return table;
    }

    private Color getColorFromString(String colorString) {
        switch (colorString.trim().toUpperCase()) {
            case "RED":
                return Color.RED;
            case "GREEN":
                return Color.GREEN;
            case "BLUE":
                return Color.BLUE;
            // Add more color options as needed
            default:
                try {
                    // Try to parse the color from the string representation (e.g., "#RRGGBB")
                    return Color.decode(colorString.trim());
                } catch (NumberFormatException e) {
                    // If unable to parse, return a default color (e.g., black)
                    return Color.BLACK;
                }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProcessInputGUI::new);
    }

    static class ColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            comp.setBackground((Color) value);
            return comp;
        }
    }
}
