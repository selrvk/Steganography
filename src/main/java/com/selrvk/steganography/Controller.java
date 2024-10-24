package com.selrvk.steganography;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private ImageView imgView;
    @FXML
    private TextField inputField;
    @FXML
    private TextField outputField;

    private final int maxInputLength = 50;
    private BufferedImage img;
    private File file;

    public void initialize(){

            inputField.textProperty().addListener((observable, oldValue, newValue) -> {

                if(newValue.length() > maxInputLength){

                    inputField.setText(oldValue);
                    printError("Too long");
                }
            });

    }

    public void uploadImg(ActionEvent event){

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(

                new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg)", "*.png", "*.jpg")
        );

        File chosenFile = fileChooser.showOpenDialog(stage);

        if(chosenFile != null){

            try {

                Image myImage = new Image(chosenFile.toURI().toString());
                imgView.setImage(myImage);
                setFile(chosenFile);

                BufferedImage img = ImageIO.read(getFile());
                setImage(img);

            } catch (Exception e){

                e.printStackTrace();

            }
        }

    }

    public void encode(){

        int binIndex = 0;

        if(inputField.getText().length() >= 8 && img != null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Set File Name");
            alert.setHeaderText("Enter file name: ");

            TextField fileNameInput = new TextField();
            fileNameInput.setPromptText(" ");

            GridPane gridPane = new GridPane();
            gridPane.add(fileNameInput, 1, 1);

            alert.getDialogPane().setContent(gridPane);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                String fileName = fileNameInput.getText();

                try{

                    String bin = convertToBinary(inputField.getText());
                    int w = img.getWidth();
                    int h = img.getHeight();

                    outerLoop:
                    for(int y = 0 ; y < h ; y++) {
                        for (int x = 0; x < w; x++) {

                            if(binIndex >= bin.length()){
                                break outerLoop;
                            }

                            int pixel = img.getRGB(x, y);
                            int red = (pixel >> 16) & 0xff;
                            int green = (pixel >> 8) & 0xff;
                            int blue = pixel & 0xff;

                            blue = (blue & 0xFE) | (bin.charAt(binIndex) - '0');
                            binIndex++;

                            int newPixel = (0xff << 24) | (red<<16) | (green << 8) | blue;
                            img.setRGB(x,y,newPixel);
                        }
                    }

                    exportImage(img, fileName);
                    inputField.setText(" ");

                } catch (Exception e){

                    e.printStackTrace();
                }
            }
        } else if (inputField.getText().length() < 8 && !inputField.getText().isEmpty()){

            printError("Too short");

        } else if (img == null){

            printError("No image");

        } else {

            printError("Empty field");
        }
    }

    public String convertToBinary(String string){

        StringBuilder bin = new StringBuilder();

        for(char c : string.toCharArray()){

            bin.append(String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0"));
        }

        return bin.toString();
    }

    public void exportImage(BufferedImage image, String fileName){

        String path = System.getProperty("user.home") + "/Desktop/" + fileName + ".png";

        try {

            File exportLoc = new File(path);
            ImageIO.write(image, "png", exportLoc);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void decode(){

        if(img != null){

            try{

                StringBuilder bin = new StringBuilder();
                int w = img.getWidth();
                int h = img.getHeight();

                outerLoop:
                for(int y = 0 ; y < h ; y++) {
                    for (int x = 0; x < w; x++) {

                        if(bin.length() >= 400){
                            break outerLoop;
                        }

                        int pixel = img.getRGB(x, y);
                        int blue = pixel & 0xff;

                        bin.append((blue & 1) == 0 ? '0' : '1');
                    }
                }

                outputField.setText(convertToText(bin.toString()));

            } catch (Exception e){

                e.printStackTrace();
            }
        } else {

            printError("No image");
        }
    }

    public String convertToText(String string){

        StringBuilder data = new StringBuilder();
        for(int i = 0 ; i < string.length() ; i += 8) {

            String sByte =  string.substring(i, i + 8);
            int cCode = Integer.parseInt(sByte, 2);

            if(cCode >= 32 && cCode <= 126){

                data.append((char) cCode);

            } else {

                break;
            }
        }

        return data.toString();
    }

    public void printError(String sError){

        if(sError.equals("Too long")){

            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Input too long!");
            error.showAndWait();

        } else if(sError.equals("Too short")){

            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Input too short!");
            error.showAndWait();

        } else if(sError.equals("Empty field")){

            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Empty field!");
            error.showAndWait();

        } else if(sError.equals("No image")){

            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Please upload an image!");
            error.showAndWait();
        }
    }

    public void setFile(File file){

        this.file = file;
    }

    public File getFile(){

        return this.file;
    }

    public void setImage(BufferedImage img){

        this.img = img;
    }   

    public BufferedImage getImage(){

        return this.img;
    }

}
