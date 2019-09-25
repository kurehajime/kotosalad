import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.atilika.kuromoji.ipadic.Tokenizer;


/**
 *
 * @author gabill
 */
public class Hamsaradkun extends Application {
    static Tokenizer tokenizer=null;

    @Override
    public void start(Stage stage) throws Exception {
        tokenizer= new Tokenizer();
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Ham salad kun");
        stage.show();
    }

    public static Map<String,List<String>> makeDict(String input){
        //正規化
        List<String> lines= Arrays.asList(input
                .replaceAll("[.?!？！。]", "\n")
                .replaceAll("[「」【】()]", "")
                .split("\n"));

        //分かち書き
        String wakati= lines.parallelStream().map((line) -> {
            String newLine="";
            newLine =tokenizer.tokenize(line).stream()
                    .map((token) -> {

                        return "|"+token.getSurface();}

                    )
                    .reduce(newLine, String::concat);
            return "_BOS_"+newLine;
        }).map((newLine) -> newLine+"|_EOS_|")
                .reduce("", String::concat);

        //辞書作成
        List<String> wakatiList=Arrays.asList(wakati.replaceAll("\n", "_EOS_").split("[|]"));
        Map<String,List<String>> dict=new HashMap<>();
        for(int i=0;i<wakatiList.size();i++){
            if(i==wakatiList.size()-1||"_EOS_".equals(wakatiList.get(i))){
                continue;
            }
            String now=wakatiList.get(i);
            String next=wakatiList.get(i+1);
            if(!dict.containsKey(now)){
                dict.put(now,new ArrayList<>());
            }
            //辞書に追加。
            dict.get(now).add(next);

            //「、」は文頭としても扱う。
            if(now.equals(",")||now.equals(".")){
                dict.get("_BOS_").add(next);
            }
        }
        return dict;
    }
    public static  String shuffle(Map<String,List<String>>  dict){
        String rtnStr="";
        String now=dict.get("_BOS_").get((int) Math.floor(Math.random()*dict.get("_BOS_").size()));
        while(!"_EOS_".equals(now)){
            rtnStr+=now;
            now=dict.get(now).get((int) Math.floor(Math.random()*dict.get(now).size()));
        }
        rtnStr=rtnStr.replaceAll("_BOS_", "").replaceAll("_BOS_", "_EOS_");

        return rtnStr;
    }
    public static String salad(String input,int row,int col){
        String rtnStr="";
        Map<String,List<String>> dict=Hamsaradkun.makeDict(input);
        for(int r=0;r<row;r++){
            String line1="";
            for(int i=0;i<42;i++){
                String line2=Hamsaradkun.shuffle(dict);
                if(Math.abs(line1.length()-col)>Math.abs(line2.length()-col)){
                    line1=line2;
                }
            }
            rtnStr+=line1+"\n";
        }
        return rtnStr;
    }

}