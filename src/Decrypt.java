import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

class Decrypt {
    private int key;

    public Decrypt(int key) {
        this.key = key;
    }

    public void decryptFile(String filePath) {
        String encryptedText = readEncryptedText(filePath);
        String decryptedText = decrypt(encryptedText);
        saveDecryptedText(filePath, decryptedText);
    }

    private String readEncryptedText(String filePath) {
        StringBuilder text = new StringBuilder();
        try {
            File inputFile = new File(filePath);
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
                if (scanner.hasNextLine()) {
                    text.append("\n");
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        return text.toString();
    }

    private String decrypt(String encryptedText) {
        StringBuilder decryptedText = new StringBuilder();

        int i = 0;
        while (i < encryptedText.length()) {
            char c = encryptedText.charAt(i);

            if (Character.isLetter(c)) {
                boolean isUpperCase = Character.isUpperCase(c);
                c = Character.toLowerCase(c);
                char decryptedChar = (char) (((c - 'a') * getMultiplicativeInverse(key, 26) % 26) + 'a');
                if (isUpperCase) {
                    decryptedChar = Character.toUpperCase(decryptedChar);
                }
                decryptedText.append(decryptedChar);
            } else {
                if (i + 1 < encryptedText.length()) {
                    String specialCharacter = encryptedText.substring(i, i + 2);
                    String replacement = handleChangedGermanSpecialCharacter(specialCharacter);
                    if (!replacement.isEmpty()) {
                        decryptedText.append(replacement);
                        i++;
                    } else {
                        decryptedText.append(c);
                    }
                } else {
                    decryptedText.append(c);
                }
            }

            i++;
        }

        return decryptedText.toString();
    }

    private int getMultiplicativeInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
    }

    private String handleChangedGermanSpecialCharacter(String specialCharacter) {
        return switch (specialCharacter) {
            case "ae" -> "ä";
            case "oe" -> "ö";
            case "ue" -> "ü";
            case "ss" -> "ß";
            default -> "";
        };
    }
    public static int findKey(String encryptedText) {
        int[] letterFrequencies = countOccurrences(encryptedText.toLowerCase());

        int maxFrequency = 0;
        char mostFrequentCharacter = 'a';

        for (char c = 'a'; c <= 'z'; c++) {
            int frequency = letterFrequencies[c - 'a'];
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mostFrequentCharacter = c;
            }
        }

        int commonCharacterIndex = mostFrequentCharacter - 'a';
        String languageCommonCharacters = "enisratdhulcgmobwfkzpvyxq";
        int maxOccurrences = 0;
        int foundKey = 0;

        Random random = new Random();

        for (int i = 0; i < languageCommonCharacters.length(); i++) {
            int languageCommonCharacterIndex = languageCommonCharacters.charAt(i) - 'a';
            for (int key : new int[]{3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25}) {
                int decryptedCharacterIndex = (languageCommonCharacterIndex * key) % 26;
                char decryptedCharacter = (char) (decryptedCharacterIndex + 'a');
                int occurrences = letterFrequencies[decryptedCharacter - 'a'];

                if (occurrences > maxOccurrences) {
                    maxOccurrences = occurrences;
                    foundKey = key;
                } else if (occurrences == maxOccurrences && random.nextBoolean()) {
                    foundKey = key;
                }

                if (decryptedCharacterIndex == commonCharacterIndex) {
                    return foundKey;
                }
            }
        }

        return foundKey;
    }

    private static int[] countOccurrences(String text) {
        int[] letterFrequencies = new int[26];
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                letterFrequencies[c - 'a']++;
            }
        }
        return letterFrequencies;
    }
    private void saveDecryptedText(String decryptedText, String filePath) {
        String directory = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        String outputFilePath = directory + "decrypted_" + fileName;

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            writer.write(decryptedText);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

    }


}
