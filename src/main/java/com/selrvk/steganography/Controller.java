package com.selrvk.steganography;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller {
    @FXML
    private ImageView imgView;
    @FXML
    private TextField inputField;

    private Scene scene;
    private Parent root;

    private File file;

    public void uploadImg(ActionEvent event){

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );

        File chosenFile = fileChooser.showOpenDialog(stage);
        setFile(chosenFile);

        if(chosenFile != null){

            Image myImage = new Image(chosenFile.toURI().toString());
            imgView.setImage(myImage);
        }

    }

    public void encode(){

        int binIndex = 0;

        try{

            String bin = convertToBinary(inputField.getText());
            BufferedImage img = ImageIO.read(getFile());
            int w = img.getWidth();
            int h = img.getHeight();

            outerLoop:
            for(int y = 0 ; y < h ; y++) {

                for (int x = 0; x < w; x++) {

                    if(binIndex >= bin.length()){
                        break outerLoop;
                    }

                    int pixel = img.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;

                    blue = (blue & 0xFE) | (bin.charAt(binIndex) - '0');
                    binIndex++;

                    int newPixel = (0xff << 24) | (red<<16) | (green << 8) | blue;
                    img.setRGB(x,y,newPixel);

                    exportImage(img);
                }
            }

        } catch (Exception e){

            e.printStackTrace();

        }
    }

    public String convertToBinary(String string){

        StringBuilder bin = new StringBuilder();
        for(char c : string.toCharArray()){

            bin.append(String.format("%8s", Integer.toBinaryString(c)).replaceAll(" ", "0"));
        }

        return bin.toString();
    }

    public void exportImage(BufferedImage image){

        try {

            File exportLoc = new File("src/testuwu.png");
            ImageIO.write(image, "png", exportLoc);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void decode(){

        StringBuilder bin = new StringBuilder();

        try{

            BufferedImage img = ImageIO.read(getFile());
            int w = img.getWidth();
            int h = img.getHeight();

            outerLoop:
            for(int y = 0 ; y < h ; y++) {

                for (int x = 0; x < w; x++) {

                    if(bin.length() >= 48){
                        break outerLoop;
                    }

                    int pixel = img.getRGB(x, y);

                    int blue = pixel & 0xff;
                    
                    bin.append(blue & 1);

                }
            }

        } catch (Exception e){

            e.printStackTrace();

        }


    }

    public void setFile(File file){

        this.file = file;

    }

    public File getFile(){

        return this.file;
    }

}
