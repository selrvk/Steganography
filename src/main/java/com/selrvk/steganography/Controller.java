package com.selrvk.steganography;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.event.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Controller {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView imgView;
    private TextField inputField;

    private File file;

    public void uploadImg(ActionEvent event){

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );

        File chosenFile = fileChooser.showOpenDialog(stage);
        this.file = chosenFile;

        if(chosenFile != null){

            Image myImage = new Image(chosenFile.toURI().toString());
            imgView.setImage(myImage);
            readImage(chosenFile);
        }

    }

    public void readImage(File file){

        try{

            BufferedImage img = ImageIO.read(file);
            int w = img.getWidth();
            int h = img.getHeight();

            for(int y = 0 ; y < h ; y++) {

                for (int x = 0; x < w; x++) {

                    int pixel = img.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;

                    if (x == 0 && y == 0) {
                        System.out.println("Top left pixel: R= " + red + "  G= " + green + "  B= " + blue + "  Alpha= " + alpha);
                    }
                }
            }

        } catch (IOException e){


            e.printStackTrace();

        }
    }
    public void encode(){


        System.out.println("Encode pressed");

    }

    public String convertToBinary(String string){

        return "uwu";
    }

    public void decode(){

        System.out.println("Decode pressed");

    }

}
