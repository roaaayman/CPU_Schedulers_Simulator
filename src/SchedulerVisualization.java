//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.stage.Stage;
//
//public class SchedulerVisualization extends Application {
//
//    @Override
//    public void start(Stage stage) {
//        // Creating axes
//        NumberAxis xAxis = new NumberAxis();
//        NumberAxis yAxis = new NumberAxis();
//
//        // Creating a line chart
//        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("Scheduler Visualization");
//
//        // Creating a series for the chart
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("Processes");
//
//        // Simulated data - replace this with your scheduling logic
//        series.getData().add(new XYChart.Data<>(1, 5));
//        series.getData().add(new XYChart.Data<>(2, 10));
//        series.getData().add(new XYChart.Data<>(3, 15));
//        series.getData().add(new XYChart.Data<>(4, 8));
//
//        // Adding series to the chart
//        lineChart.getData().add(series);
//
//        // Creating a scene and setting it to the stage
//        Scene scene = new Scene(lineChart, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
