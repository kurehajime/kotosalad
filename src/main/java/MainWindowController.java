/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 *
 * @author gabill
 */
public class MainWindowController implements Initializable {

    @FXML
    private TextArea text_in;
    @FXML
    private TextArea text_out;
    @FXML
    private TextField text_col;
    @FXML
    private TextField text_row;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String s =text_in.getText();
        int row =isNumber(text_row.getText())? Integer.parseInt(text_row.getText()) :3;
        int col =isNumber(text_col.getText())? Integer.parseInt(text_col.getText()) :35;
        String output =Hamsaradkun.salad(s,row,col);
        text_out.setText(output);

        //クリップボードにもコピー
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(output);
        clipboard.setContent(content);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String input="";
        try {
            InputStream is=getClass().getClassLoader().getResourceAsStream("sample.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while((line = br.readLine()) != null){
                input+=line+"\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        text_in.setText(input);

    }
    public boolean isNumber(String val) {
        String regex = "\\A[-]?[0-9]+\\z";
        Pattern p = Pattern.compile(regex);
        Matcher m1 = p.matcher(val);
        return m1.find();
    }

}