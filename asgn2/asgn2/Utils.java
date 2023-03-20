package asgn2;

import java.io.*;
import java.util.*;

import javax.swing.plaf.basic.BasicListUI.ListSelectionHandler;

public class Utils {
/**
     * Close a {@link Closeable} object if it is not null. Catch any {@link IOException} and
     * print the error message to {@link System#err}.
     *
     * @param <T> Closeable type
     * @param closeable Closeable object
     */
    public static <T extends Closeable> void close(T closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                System.err.printf("'%s': failed to close\n", closeable.toString());
            }
        }
    }

    /**
     * Read frequencies of words from {@link Scanner} and sort them
     * in the following order:
     *   1. Most frequent word has the highest priority,
     *   2. If frequencies are the same, longer word has the priority.
     *   3. If lengths are the same, alphabetical order is used.
     *
     * @param in Scanner to read from
     * @return Return a sorted set of word frequencies
     */
    public static SortedSet<Map.Entry<String, Integer>> getWordFreqs(Scanner in) {
        // read file and get word frequencies
        final Map<String, Integer> wordFreq = new HashMap<>();
        Utils.forEachWord(in, new WordHandler() {
            @Override
            public void handleWord(String word) {
                wordFreq.merge(word, 1, Integer::sum);
            }
        }, null, null, null);

        SortedSet<Map.Entry<String, Integer>> sortedFreq = new TreeSet<>(
            new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                    // first compare the frequencies
                    int res = e2.getValue() - e1.getValue();

                    // if frequencies are the same, compare the lengths
                    if (res == 0) {
                        res = e2.getKey().length() - e2.getKey().length();
                    }

                    // if lengths are the same, compare the words (alphabetical order)
                    if (res == 0) {
                        res = e1.getKey().compareTo(e2.getKey());
                    }

                    return res != 0 ? res : 1;
                }
            }
        );
        sortedFreq.addAll(wordFreq.entrySet());

        // clear the original map
        wordFreq.clear();

        return sortedFreq;
    }

    /**
     * Create a decoding map from a {@link Scanner}.
     *
     * @param in Filename of the decoding map
     */
    public static HashMap<String, String> getDecodingMap(Scanner in) {
        final HashMap<String, String> decodingMap = new HashMap<>();
        forEachLine(in, new LineHandler() {
            public void handleLine(String line) {
                String[] parts = line.split(":");
                if (parts.length != 2) { return; }

                String encoding = parts[0].strip();
                String word = parts[1].strip();

                if (!decodingMap.containsKey(encoding)) {
                    decodingMap.put(encoding, word);
                }
            }
        }, null);

        return decodingMap;
    }

    /**
     * Strip the string and check if it is empty or only contains whitespaces
     *
     * @param str
     * @return Returns true if the string is empty or only contains whitespaces
     */
    public static boolean isWhiteSpaceOnly(String str) {
        str.strip();
        return str.isBlank() || str.isEmpty();
    }

    /**
     * Iterate through each line in a {@link Scanner} and call the defaultHandler.
     *
     * If the nextLineHandler is not null, the nextLineHandler will be called when
     * there is a next line.
     *
     * @param in Scanner to read from
     * @param defaultHandler Handler for each line
     * @param nextLineHandler Handler to call if there is a next line. Can be null.
     */
    public static void forEachLine(
        Scanner in,
        LineHandler defaultHandler,
        LineHandler nextLineHandler
    ) {
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (Utils.isWhiteSpaceOnly(line)) continue;

            defaultHandler.handleLine(line);

            if (nextLineHandler != null && in.hasNextLine()) {
                nextLineHandler.handleLine(line);
            }
        }
    }

    /**
     * Iterate through each word in a {@link Scanner} and call the defaultHandler.
     *
     * If the firstWordHandler is not null, the first word of each line will be
     * passed to the firstWordHandler.
     *
     * If the lastWordHandler is not null, the last word of each line will be
     * passed to the lastWordHandler.
     *
     * @param in Scanner to read from
     * @param defaultHandler Handler for each word
     * @param firstWordHandler Handler for the first word of each line. Can be null.
     * @param lastWordHandler Handler for the last word of each line. Can be null.
     * @param nextLineHandler Handler to call if there is a next line. Can be null.
     */
    public static void forEachWord(
        Scanner in,
        WordHandler defaultHandler,
        WordHandler firstWordHandler,
        WordHandler lastWordHandler,
        LineHandler nextLineHandler
    )
    {
        forEachLine(in, new LineHandler() {
            public void handleLine(String line) {
                String[] lineWords = line.split(" ");

                int startI = 0;
                int endI = lineWords.length;

                // call the firstWordHandler if it is not null
                if (firstWordHandler != null && !Utils.isWhiteSpaceOnly(lineWords[0])) {
                    firstWordHandler.handleWord(lineWords[0]);
                    startI++;
                }

                // call the lastWordHandler if it is not null
                if (lastWordHandler != null && !Utils.isWhiteSpaceOnly(lineWords[lineWords.length - 1])) {
                    lastWordHandler.handleWord(lineWords[lineWords.length - 1]);
                    endI--;
                }

                // call the defaultHandler for each word
                for (int i = startI; i < endI; i++) {
                    if (Utils.isWhiteSpaceOnly(lineWords[i])) continue;

                    defaultHandler.handleWord(lineWords[i]);
                }
            }
        }, nextLineHandler);
    }
}
