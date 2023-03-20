import java.io.*;
import java.util.*;

import asgn2.*;

public class Main {
    /**
     * Encode the input file and write the encoded file using the encoder
     * to the output file and the encoding map to the map file.
     *
     * @param inputFilename Input file, to be encoded
     * @param outFilename Output file, where to output the encoded text
     * @param mapFilename Encoding map file, to output the encoding map for decoding
     * @param encoder Encoder to use. Returns the encoding map.
     */
    public static void encode(
        String inputFilename,
        String outFilename,
        String mapFilename,
        Encoder encoder
    ) {
        File fileIn = new File(inputFilename);
        Scanner in = null;
        FileWriter out = null;
        FileWriter mapOut = null;

        try {
            // Get the encoding map
            in = new Scanner(fileIn);
            mapOut = new FileWriter(mapFilename);
            HashMap<String, Integer> words = encoder.encode(in, mapOut, fileIn);
            Utils.close(mapOut);

            // reset the scanner
            Utils.close(in);
            in = new Scanner(fileIn);

            // Open the output file
            out = new FileWriter(outFilename);
            final FileWriter innerOut = out;

            // Create handlers for the forEachWord method
            WordHandler firstWordHandler = new WordHandler() {
                public void handleWord(String word) {
                    Integer encoding = words.get(word);
                    if (encoding == null) {
                        throw new RuntimeException(
                            String.format("'%s' is not in the encoding map", word)
                        );
                    }

                    try {
                        innerOut.write(String.format("%d", words.get(word)));
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            };
            WordHandler defaultHandler = new WordHandler() {
                public void handleWord(String word) {
                    Integer encoding = words.get(word);
                    if (encoding == null) {
                        throw new RuntimeException(
                            String.format("'%s' is not in the encoding map", word)
                        );
                    }

                    try {
                        innerOut.write(String.format(" %d", words.get(word)));
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            };
            LineHandler nextLineHandler = new LineHandler() {
                public void handleLine(String line) {
                    try {
                        innerOut.write("\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            };

            // Encode the file
            Utils.forEachWord(in, defaultHandler, firstWordHandler, null, nextLineHandler);
        } catch (RuntimeException e) {
            System.err.printf("Runtime Error: %s\n", e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.printf("IO Error: %s\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("IO Error: %s\n", e.getMessage());
        } finally {
            Utils.close(in);
            Utils.close(out);
            Utils.close(mapOut);
        }
    }

    public static HashMap<String, Integer> encodev1(
        Scanner in,
        FileWriter mapOut,
        File fileIn
    ) throws IOException, RuntimeException {
        // encode on the fly
        int wordCount = 0;
        HashMap<String, Integer> words = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();

            // skip empty lines or lines with only spaces
            if (line.isBlank() || line.isEmpty()) continue;

            for (String word : line.split(" ")) {
                if (!words.containsKey(word)) {
                    words.put(word, wordCount);
                    mapOut.write(String.format("%d: %s\n", wordCount, word));

                    wordCount++;
                }
            }
        }

        return words;
    }

    public static HashMap<String, Integer> encodev2(
        Scanner in,
        FileWriter mapOut,
        File fileIn
    ) throws IOException, RuntimeException {
        // read in all words sorted by their frequencies
        SortedSet<Map.Entry<String, Integer>> wordFreqs = Utils.getWordFreqs(in);

        // create the encoding map
        int wordCount = 0;
        HashMap<String, Integer> words = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wordFreqs) {
            String word = entry.getKey();
            if (!words.containsKey(word)) {
                words.put(word, wordCount);
                mapOut.write(String.format("%d: %s\n", wordCount, word));

                wordCount++;
            }
        }

        // clear the word frequencies
        wordFreqs.clear();

        return words;
    }

    public static void decode(
        String inputFilename,
        String outFilename,
        String mapFilename
    ) {
        Scanner in = null;
        Scanner mapIn = null;
        FileWriter out = null;

        try {
            // Create the decoding map
            mapIn = new Scanner(new File(mapFilename));
            HashMap<String, String> decodingMap = Utils.getDecodingMap(mapIn);
            Utils.close(mapIn);

            // Decode the file
            in = new Scanner(new File(inputFilename));
            out = new FileWriter(outFilename);
            final FileWriter innerOut = out;

            WordHandler firstWordHandler = new WordHandler() {
                public void handleWord(String encoded) {
                    String word = decodingMap.get(encoded);
                    if (word == null) {
                        throw new RuntimeException(
                            String.format("'%s' is not in the decoding map", encoded)
                        );
                    }

                    try {
                        innerOut.write(word);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            };
            WordHandler defaultHandler = new WordHandler() {
                public void handleWord(String encoded) {
                    String word = decodingMap.get(encoded);
                    if (word == null) {
                        throw new RuntimeException(
                            String.format("'%s' is not in the decoding map", encoded)
                        );
                    }

                    try {
                        innerOut.write(String.format(" %s", word));
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            };
            LineHandler nextLineHandler = new LineHandler() {
                public void handleLine(String line) {
                    try {
                        innerOut.write("\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            };

            Utils.forEachWord(in, defaultHandler, firstWordHandler, null, nextLineHandler);
        } catch (FileNotFoundException e) {
            System.err.printf("IO Error: %s\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("IO Error: %s\n", e.getMessage());
        } catch (NumberFormatException e) {
            System.err.printf("Erro: %s\n", e.getMessage());
        } finally {
            Utils.close(in);
            Utils.close(mapIn);
            Utils.close(out);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <encode|decode> [v1|v2]");
            return;
        }

        String op = args[0];
        String strategy = "v1";
        if (args.length >= 2) strategy = args[1];

        if (op.equals("encode")) {
            if (strategy.equals("v2")) {
                encode("sample_input/input.txt", "encoded.txt", "map.txt", Main::encodev2);
            } else {
                encode("sample_input/input.txt", "encoded.txt", "map.txt", Main::encodev1);
            }
        } else if (op.equals("decode")) {
            decode("encoded.txt", "decoded.txt", "map.txt");
        }
    }
}