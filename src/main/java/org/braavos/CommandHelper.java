package org.braavos;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.google.common.io.Resources;

/**
 * Helper class for processing the commands.
 */
public class CommandHelper {

    private static final String CMD_PREFIX = "ccwc";
    private static final String BYTE_COUNT_CMD = "-c";
    private static final String LINE_COUNT_CMD = "-l";
    private static final String WORD_COUNT_CMD = "-w";
    private static final String CHAR_COUNT_CMD = "-m";

    public void processInput(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length > 1 && Arrays.asList(tokens).contains(CMD_PREFIX)) {
            if (tokens[0].matches("^" + CMD_PREFIX)) {
                processFile(tokens);
            } else {
                processStandardInput(input);
            }
        } else {
            System.out.println("Could not find a valid ccwc command. Please try again.");
        }
    }

    // -------------------------------------------- PRIVATE METHODS --------------------------------------------------
    private void processFile(String[] tokens) {
        switch (tokens.length) {
            case 2 -> processDefaultCommand(tokens[1], null);
            case 3 -> processIndividualCommand(tokens[1], tokens[2], null);
            default -> System.out.println("Cannot process command. Please try again");
        }
    }

    private void processStandardInput(String input) {
        int pipeIndex = input.lastIndexOf("|");
        String content = input.substring(0, pipeIndex).trim();
        String[] commands = input.substring(pipeIndex + 1).trim().split(" ");
        switch (commands.length) {
            case 1 -> processDefaultCommand(null, content);
            case 2 -> processIndividualCommand(commands[1], null, content);
            default -> System.out.println("Cannot process command. Please try again");
        }
    }

    private void processDefaultCommand(String fileName, String fileContent) {
        fileName = fileName == null ? "" : fileName;
        fileContent = fileContent == null ? getFileContent(fileName) : fileContent;
        if (!fileContent.isEmpty()) {
            long lineCount = getLineCount(fileContent);
            long wordCount = getWordCount(fileContent);
            long byteCount = getByteCount(fileContent);
            System.out.printf("%,d %,d %,d %s", lineCount, wordCount, byteCount, fileName);
        }
    }

    private void processIndividualCommand(String command, String fileName, String fileContent) {
        fileName = fileName == null ? "" : fileName;
        fileContent = fileContent == null ? getFileContent(fileName) : fileContent;
        if (!fileContent.isEmpty()) {
            switch (command) {
                case BYTE_COUNT_CMD -> System.out.printf("%d %s", getByteCount(fileContent), fileName);
                case LINE_COUNT_CMD -> System.out.printf("%d %s", getLineCount(fileContent), fileName);
                case WORD_COUNT_CMD -> System.out.printf("%d %s", getWordCount(fileContent), fileName);
                case CHAR_COUNT_CMD -> System.out.printf("%d %s", getCharCount(fileContent), fileName);
                default -> System.out.println("Unsupported command. Please try again.");
            }
        }
    }

    public long getByteCount(String content) {
        return content.getBytes().length;
    }

    public int getLineCount(String content) {
        int lines = 0;
        try (LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(content))) {
            lineNumberReader.skip(Long.MAX_VALUE);
            lines = lineNumberReader.getLineNumber();
        } catch (IOException e) {
            System.out.println("Failed to get the number of lines.");
        }
        return lines;
    }

    public int getWordCount(String content) {
        return Pattern.compile("\\s+").split(content).length;
    }

    public int getCharCount(String content) {
        return content.length();
    }

    private String getFileContent(String fileName) {
        String fileContent = "";
            try {
                String filePath = Resources.getResource(fileName).getFile();
                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    fileContent = Files.readString(file.toPath());
                }
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Failed to read file " + fileName);
            }
        return fileContent;
    }
}
