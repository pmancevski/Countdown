import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.Timer;

public class CountDownController {

    Timer timer = new Timer();

    @FXML
    private TextField hourTextField;

    @FXML
    private TextField minuteTextField;

    @FXML
    private TextField secondsTextField;

    @FXML
    private TextField enterHourTextField;

    @FXML
    private Button enterValueButton;

    @FXML
    void enterHourButton(ActionEvent event) {
        int hour = Integer.parseInt(enterHourTextField.getText());
        if (minuteTextField.getText().trim().isEmpty()){
            hour--;
            minuteTextField.setText("60");
        }
        if (secondsTextField.getText().trim().isEmpty()){
            int minutes = Integer.parseInt(minuteTextField.getText());
            minutes--;
            secondsTextField.setText("60");
            minuteTextField.setText(String.format("%d", minutes));
        }

        if (hourTextField.getText().trim().isEmpty()){
            hourTextField.setText(String.format("%d", hour));
        }
        else {
            int h = Integer.parseInt(hourTextField.getText());
            hourTextField.setText(String.format("%d", h+hour));
        }
    }

    @FXML
    void loadTextAction(ActionEvent event) {
        try (Scanner input = new Scanner(Paths.get("Hour.txt"))){
            hourTextField.setText(input.next());
        }
        catch (IOException | NoSuchElementException | IllegalStateException e){
            e.printStackTrace();
        }

        try (Scanner input = new Scanner(Paths.get("Minute.txt"))){
            minuteTextField.setText(input.next());
        }
        catch (IOException | NoSuchElementException | IllegalStateException e){
            e.printStackTrace();
        }

        try (Scanner input = new Scanner(Paths.get("Second.txt"))){
            secondsTextField.setText(input.next());
        }
        catch (IOException | NoSuchElementException | IllegalStateException e){
            e.printStackTrace();
        }
    }

    @FXML
    void saveTextAction(ActionEvent event) {
        try (Formatter hour = new Formatter("Hour.txt")){
            hour.format("%s", hourTextField.getText());
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e){
            e.printStackTrace();
        }

        try (Formatter minute = new Formatter("Minute.txt")){
            minute.format("%s", minuteTextField.getText());
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e){
            e.printStackTrace();
        }

        try (Formatter second = new Formatter("Second.txt")){
            second.format("%s", secondsTextField.getText());
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e){
            e.printStackTrace();
        }
    }

    @FXML
    void startPressAction(ActionEvent event) {
        if (!secondsTextField.getText().trim().isEmpty()){
            timer.scheduleAtFixedRate(new TimerTask() {
                int sec = Integer.parseInt(secondsTextField.getText());
                int min = Integer.parseInt(minuteTextField.getText());
                int hour = Integer.parseInt(hourTextField.getText());
                @Override
                public void run() {
                    secondsTextField.setText(String.format("%d", sec--));
                    if ( hour > 0 || min > 0 || sec > 0){
                        if (min > 0){
                            if (sec < 0){
                                sec = 59;
                                minuteTextField.setText(String.format("%d", --min));
                            }
                        }
                        else {
                            if (sec < 0){
                                hourTextField.setText(String.format("%d", --hour));
                                min = 60;
                                minuteTextField.setText(String.format("%d", --min));
                                sec = 60;
                                secondsTextField.setText(String.format("%d", sec));
                            }
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,
                                "Countdown finished, restart the program & " +
                                        "enter hours");
                        timer.cancel();
                    }
                }
            }, 0, 1000);
        }
    }

    @FXML
    void stopPressAction(ActionEvent event) {
        timer.cancel();
    }

}
