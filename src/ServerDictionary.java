// ServerDictionary
// Loads dictionary from JSON & maintains dictionary data in thread safe memory

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDictionary {

    private static ServerDictionary instance;
    private final String filePath;
    private Map<String, ArrayList<String>> dictionaryData;
    private final SimpleLogger log;
    private int changeCounter = 0;

    private ServerDictionary(String filePath, SimpleLogger log) throws FileNotFoundException {
        this.filePath = filePath;
        this.log = log;

        Gson gson = new Gson();
        log.updateLog("INFO: Loading dictionary from file...");
        this.dictionaryData = gson.fromJson(new FileReader(filePath), ConcurrentHashMap.class);
        if (dictionaryData == null) {
            dictionaryData = new ConcurrentHashMap<>();
        } else {
            dictionaryToUpper();
        }
        log.updateLog("INFO: Dictionary loaded from file '" + filePath + "'");

    }

    public static ServerDictionary setInstance(String filePath, SimpleLogger log) throws FileNotFoundException {
        if (instance == null) {
            instance = new ServerDictionary(filePath, log);
            return instance;
        }
        throw new IllegalStateException("ServerDictionary instance already exists");
    }

    public static ServerDictionary getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ServerDictionary instance not created");
        }
        return instance;
    }

    public NetworkMessage searchWord(String word) {
        String wordUpper = word.toUpperCase();
        if (dictionaryData.containsKey(wordUpper)) {
            log.updateLog("INFO: Word found '" + wordUpper + "'");
            incrementChangeCounter();
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
            incrementChangeCounter();
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
            incrementChangeCounter();
            return new NetworkMessage(Status.SUCCESS_WORD_REMOVED);
        } else {
            log.updateLog("INFO: Word not found '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

    public NetworkMessage updateWord(String word, String meaning) {
        String wordUpper = word.toUpperCase();
        if (dictionaryData.containsKey(wordUpper) && !meaningExists(wordUpper, meaning)) {
            dictionaryData.get(wordUpper).add(meaning);
            log.updateLog("INFO: Definitions added for word '" + wordUpper + "'");
            incrementChangeCounter();
            return new NetworkMessage(Status.SUCCESS_WORD_UPDATED, dictionaryData.get(wordUpper).toArray(new String[0]));
        } else if (!meaningExists(wordUpper, meaning)) {
            log.updateLog("INFO: Definition already exists for '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_DEFINITION_EXISTS);
        } else {
            log.updateLog("INFO: Word not found '" + wordUpper + "'");
            return new NetworkMessage(Status.FAILURE_NOT_FOUND);
        }
    }

    private boolean meaningExists(String wordUpper, String meaning) {
        for (String def : dictionaryData.get(wordUpper)) {
            if (def.equalsIgnoreCase(meaning)) {
                return true;
            }
        }
        return false;
    }

    public void saveDictionaryToFile() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(dictionaryData, writer);
            log.updateLog("INFO: Dictionary saved to file '" + filePath + "'");
        } catch (IOException e) {
            log.updateLog("ERROR: Dictionary save failed");
            e.printStackTrace();
        }
    }

    private void incrementChangeCounter() {
        changeCounter++;
        if (changeCounter >= 10) {
            saveDictionaryToFile();
            changeCounter = 0;
        }
    }

    public int getChangeCounter() {
        return changeCounter;
    }

    private void dictionaryToUpper() {
        ConcurrentHashMap<String, ArrayList<String>> newDictionary = new ConcurrentHashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : dictionaryData.entrySet()) {
            newDictionary.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        dictionaryData = newDictionary;
    }

}
