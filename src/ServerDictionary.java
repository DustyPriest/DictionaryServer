import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDictionary {

    private Map<String, ArrayList<String>> dictionaryData;
    private final String filePath;

    public ServerDictionary(String filePath) {

        this.filePath = filePath;

        Gson gson = new Gson();
//        FileReader file;
        try {
//            file = new FileReader(filePath);
            this.dictionaryData = gson.fromJson(new FileReader(filePath), ConcurrentHashMap.class);
//            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkMessage searchWord(String word) {
        if (dictionaryData.containsKey(word)) {
            return new NetworkMessage(Status.SUCCESS_WORD_FOUND, dictionaryData.get(word).toArray(new String[0]));
        } else {
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

    public NetworkMessage addWord(String word, String meaning) {
        if (!dictionaryData.containsKey(word)) {
            ArrayList<String> meanings = new ArrayList<>();
            meanings.add(meaning);
            dictionaryData.putIfAbsent(word,meanings);
            return new NetworkMessage(Status.SUCCESS_WORD_ADDED);
        } else {
            return new NetworkMessage(Status.FAILURE_WORD_EXISTS);
        }
    }

    public NetworkMessage removeWord(String word) {
        if (dictionaryData.containsKey(word)) {
            dictionaryData.remove(word);
            return new NetworkMessage(Status.SUCCESS_WORD_REMOVED);
        } else {
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

    public NetworkMessage updateWord(String word, String meaning) {
        if (dictionaryData.containsKey(word)) {
            dictionaryData.get(word).add(meaning);
            return new NetworkMessage(Status.SUCCESS_WORD_UPDATED);
        } else {
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

}
