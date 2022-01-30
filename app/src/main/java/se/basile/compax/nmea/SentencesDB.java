package se.basile.compax.nmea;

import java.util.HashMap;
import java.util.Map;

public class SentencesDB {
    private static final Map<String, SentenceAbstr> sentences = new HashMap<String, SentenceAbstr>();

    public SentenceAbstr addSentence(String type, SentenceAbstr sentence){
        sentences.put(type, sentence);
        return sentence;
    }

    public void parse(String line) {
        SentenceAbstr sentence = null;

        if(line.startsWith("$")) {
            String nmea = line.substring(1);
            String[] tokens = nmea.split(",");
            String type = tokens[0];

            if(sentences.containsKey(type)) {
                sentence = sentences.get(type);
                sentence.parse(tokens);
            }
        }
        return;
    }
}
