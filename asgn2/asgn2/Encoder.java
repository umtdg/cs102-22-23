package asgn2;

import java.io.*;
import java.util.*;

public interface Encoder {
    public HashMap<String, Integer> encode(
        Scanner in,
        FileWriter mapOut,
        File fileIn
    ) throws IOException, RuntimeException;
}
