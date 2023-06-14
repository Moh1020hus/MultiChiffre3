import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();

        System.out.print("Enter 'e' to encrypt or 'd' to decrypt: ");
        char operation = scanner.nextLine().charAt(0);

        if (operation == 'e') {
            System.out.print("Enter '1' to encrypt with key=3 or '2' to encrypt with a random key: ");
            int keyOption = scanner.nextInt();

            if (keyOption == 1) {
                Encrypt encrypter = new Encrypt(3);
                encrypter.encryptFile(filePath);
                System.out.println("File encrypted successfully with key=3.");
            } else if (keyOption == 2) {
                int randomKey = generateRandomKey();
                Encrypt encrypter = new Encrypt(randomKey);
                encrypter.encryptFile(filePath);
                System.out.println("File encrypted successfully with random key: " + randomKey);
            } else {
                System.out.println("Invalid key option.");
            }
        } else if (operation == 'd') {
            System.out.print("Enter '1' to decrypt with key=3 or '2' to find the key: ");
            int keyOption = scanner.nextInt();

            if (keyOption == 1) {
                Decrypt decrypter = new Decrypt(3);
                decrypter.decryptFile(filePath);
                System.out.println("File decrypted successfully with key=3.");
            } else if (keyOption == 2) {
                int foundKey = Decrypt.findKey(filePath);
                if (foundKey != -1) {
                    Decrypt decrypter = new Decrypt(foundKey);
                    decrypter.decryptFile(filePath);
                    System.out.println("Found key: " + foundKey);
                    System.out.println("File decrypted successfully with the found key.");
                } else {
                    System.out.println("Could not find the key. Decryption unsuccessful.");
                }
            } else {
                System.out.println("Invalid key option.");
            }
        } else {
            System.out.println("Invalid operation.");
        }

        scanner.close();
    }

    private static int generateRandomKey() {
        int[] keys = { 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25 };
        int randomIndex = (int) (Math.random() * keys.length);
        return keys[randomIndex];
    }
}
