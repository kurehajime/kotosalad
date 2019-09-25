import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import com.atilika.kuromoji.ipadic.Tokenizer
import java.util.function.BinaryOperator


/**
 *
 * @author gabill
 */
class Hamsaradkun : Application() {

    @Throws(Exception::class)
    override fun start(stage: Stage) {
        tokenizer = Tokenizer()
        val root = FXMLLoader.load<Parent>(javaClass.getResource("MainWindow.fxml"))

        val scene = Scene(root)

        stage.scene = scene
        stage.title = "Ham salad kun"
        stage.show()
    }

    companion object {
        internal var tokenizer: Tokenizer? = null

        fun makeDict(input: String): Map<String, List<String>> {
            //正規化
            val lines = Arrays.asList(*input
                .replace("[.?!？！。]".toRegex(), "\n")
                .replace("[「」【】()]".toRegex(), "")
                .split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            )

            //分かち書き
            val wakati = lines.parallelStream().map { line ->
                var newLine = ""
                newLine = tokenizer!!.tokenize(line).stream()
                    .map { token ->

                        "|" + token.surface
                    }
                    .reduce(newLine, BinaryOperator<String> { obj, str -> obj + str })
                "_BOS_$newLine"
            }.map { newLine -> "$newLine|_EOS_|" }
                .reduce("", BinaryOperator<String> { obj, str -> obj + str })

            //辞書作成
            val wakatiList = Arrays.asList(
                *wakati.replace(
                    "\n".toRegex(),
                    "_EOS_"
                ).split("[|]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            )
            val dict = HashMap<String, MutableList<String>>()
            for (i in wakatiList.indices) {
                if (i == wakatiList.size - 1 || "_EOS_" == wakatiList[i]) {
                    continue
                }
                val now = wakatiList[i]
                val next = wakatiList[i + 1]
                if (!dict.containsKey(now)) {
                    dict[now] = ArrayList()
                }
                //辞書に追加。
                dict[now]?.add(next)

                //「、」は文頭としても扱う。
                if ((now == "," || now == ".")) {
                    dict["_BOS_"]?.add(next)
                }
            }
            return dict
        }

        fun shuffle(dict: Map<String, List<String>>): String {
            var rtnStr = ""
            var now = dict["_BOS_"]?.get(Math.floor(Math.random() * dict["_BOS_"]?.size!!).toInt())
            while ("_EOS_" != now) {
                rtnStr += now
                now = dict[now]?.get(Math.floor(Math.random() * dict[now]?.size!!).toInt())
            }
            rtnStr = rtnStr.replace("_BOS_".toRegex(), "").replace("_BOS_".toRegex(), "_EOS_")

            return rtnStr
        }

        fun salad(input: String, row: Int, col: Int): String {
            var rtnStr = ""
            val dict = Hamsaradkun.makeDict(input)
            for (r in 0 until row) {
                var line1 = ""
                for (i in 0..41) {
                    val line2 = Hamsaradkun.shuffle(dict)
                    if (Math.abs(line1.length - col) > Math.abs(line2.length - col)) {
                        line1 = line2
                    }
                }
                rtnStr += line1 + "\n"
            }
            return rtnStr
        }
    }

}