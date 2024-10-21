package com.selrvk.steganography;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.event.*;
import javafx.stage.FileChooser;
import java.io.File;

public class Controller {
    @FXML

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ImageView imgView;

    public void uploadImg(ActionEvent event){

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );
        File chosenFile = fileChooser.showOpenDialog(stage);

        if(chosenFile != null){

            Image myImage = new Image(getClass().getResourceAsStream("selrvklogo.png"));
            imgView.setImage(myImage);
        }

    }

    public void encode(){

        System.out.println("Encode pressed");

    }

    public void decode(){

        System.out.println("Decode pressed");

    }

}
