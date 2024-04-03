import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDictionary {

    private Map<String, ArrayList<String>> dictionaryData;
    private final SimpleLogger log;

    public ServerDictionary(String filePath, SimpleLogger log) {
        this.log = log;

        Gson gson = new Gson();
        try {
            log.updateLog("INFO: Loading dictionary from file...");
            this.dictionaryData = gson.fromJson(new FileReader(filePath), ConcurrentHashMap.class);
            log.updateLog("INFO: Dictionary loaded from file '" + filePath + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkMessage searchWord(String word) {
        String wordUpper = word.toUpperCase();
        if (dictionaryData.containsKey(wordUpper)) {
            log.updateLog("INFO: Word found '" + wordUpper + "'");
            return new NetworkMessage(Status.SUCCESS_WORD_FOUND, dictionaryData.get(wordUpper).toArray(new String[0]));
        } else {
            log.updateLog("INFO: Word not found '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

    public NetworkMessage addWord(String word, String meaning) {
        String wordUpper = word.toUpperCase();
        if (!dictionaryData.containsKey(wordUpper)) {
            ArrayList<String> meanings = new ArrayList<>();
            meanings.add(meaning);
            dictionaryData.putIfAbsent(wordUpper,meanings);
            log.updateLog("INFO: Word added '" + wordUpper + "'");
            return new NetworkMessage(Status.SUCCESS_WORD_ADDED, meanings.toArray(new String[0]));
        } else {
            log.updateLog("INFO: Word already exists '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_WORD_EXISTS);
        }
    }

    public NetworkMessage removeWord(String word) {
        String wordUpper = word.toUpperCase();
        if (dictionaryData.containsKey(wordUpper)) {
            dictionaryData.remove(wordUpper);
            log.updateLog("INFO: Word removed '" + wordUpper + "'");
            return new NetworkMessage(Status.SUCCESS_WORD_REMOVED);
        } else {
            log.updateLog("INFO: Word not found '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

    public NetworkMessage updateWord(String word, String meaning) {
        String wordUpper = word.toUpperCase();
        if (dictionaryData.containsKey(wordUpper)) {
            dictionaryData.get(wordUpper).add(meaning);
            log.updateLog("INFO: Definitions added for word '" + wordUpper + "'");
            return new NetworkMessage(Status.SUCCESS_WORD_UPDATED, dictionaryData.get(wordUpper).toArray(new String[0]));
        } else {
            log.updateLog("INFO: Word not found '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

}
