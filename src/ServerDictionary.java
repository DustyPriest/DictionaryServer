import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDictionary {

    private Map<String, ArrayList<String>> dictionaryData;
    private String filePath;

    public ServerDictionary(String filePath) {

        this.filePath = filePath;

        Gson gson = new Gson();
        try {
            this.dictionaryData = gson.fromJson(new FileReader(filePath), ConcurrentHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String[] searchWord(String word) {
        if (dictionaryData.containsKey(word)) {
            return dictionaryData.get(word).toArray(new String[0]);
        } else {
            return new String[]{"Word not found"};
        }
    }

}
