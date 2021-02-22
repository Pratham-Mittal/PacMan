
public class StringHelper {
    public static int countLines(String str){
        String[] line = str.split("\r\n|\r|\n");
        return  line.length;
    }
}
