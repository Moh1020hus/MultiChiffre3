import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class Encrypt {
    private final int key;

    public Encrypt(int key) {
        this.key = key;
    }

    public void encryptFile(String filePath) {
        String plainText = readPlainText(filePath);
        String encryptedText = encrypt(plainText);
        saveEncryptedText(filePath, encryptedText);
    }

    private String readPlainText(String filePath) {
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

    private String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        text = text.toLowerCase();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                char encryptedChar = (char) (((c - 'a') * key % 26) + 'a');
                encryptedText.append(encryptedChar);

            } else if (c == 'ä' || c == 'ö' || c == 'ü' || c == 'ß') {
                encryptedText.append(handleGermanSpecialCharacter(c));
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    private String handleGermanSpecialCharacter(char c) {
        return switch (c) {
            case 'ä' -> "ae";
            case 'ö' -> "oe";
            case 'ü' -> "ue";
            case 'ß' -> "ss";
            default -> "";
        };
    }

    private void saveEncryptedText(String filePath, String encryptedText) {
        try {
            Path inputPath = Paths.get(filePath);
            String directory = inputPath.getParent().toString();
            String encryptedFilePath = directory + File.separator + "encrypted.txt";
            File encryptedFile = new File(encryptedFilePath);
            FileWriter writer = new FileWriter(encryptedFile);
            writer.write(encryptedText);
            writer.close();
            System.out.println("File encrypted successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing the encrypted file.");
            e.printStackTrace();
        }
    }
}
