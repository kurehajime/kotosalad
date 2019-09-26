/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.ResourceBundle
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern

/**
 *
 * @author gabill
 */
class MainWindowController : Initializable {

    @FXML
    private var text_in: TextArea? = null
    @FXML
    private var text_out: TextArea? = null
    @FXML
    private var text_col: TextField? = null
    @FXML
    private var text_row: TextField? = null

    @FXML
    private fun handleButtonAction(event: ActionEvent) {
        val s = text_in!!.text
        val row = if (isNumber(text_row!!.text)) Integer.parseInt(text_row!!.text) else 1
        val col = if (isNumber(text_col!!.text)) Integer.parseInt(text_col!!.text) else 35
        val output = Hamsaradkun.salad(s, row, col)
        text_out!!.text = output

        //クリップボードにもコピー
        val clipboard = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(output)
        clipboard.setContent(content)
    }

    override fun initialize(url: URL, rb: ResourceBundle?) {
        var input = ""
        try {
            val `is` = javaClass.classLoader.getResourceAsStream("sample.txt")
            val br = BufferedReader(InputStreamReader(`is`!!, "UTF-8"))
            var line: String?

            do  {
                line = br.readLine()
                if(line == null){
                    break
                }
                input += line + "\n"
            }while(true)
        } catch (ex: IOException) {
            Logger.getLogger(MainWindowController::class.java.name).log(Level.SEVERE, null, ex)
        }

        text_in!!.text = input

    }

    fun isNumber(`val`: String): Boolean {
        val regex = "\\A[-]?[0-9]+\\z"
        val p = Pattern.compile(regex)
        val m1 = p.matcher(`val`)
        return m1.find()
    }

}