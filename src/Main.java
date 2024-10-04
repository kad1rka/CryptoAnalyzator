
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class CaesarCipher {


    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public void encrypt(String inputFile, String outputFile, int key) {
        String text = readFile(inputFile);
        String encryptedText = processText(text, key, true);
        writeFile(outputFile, encryptedText);
    }

    public void decrypt(String inputFile, String outputFile, int key) {
        String text = readFile(inputFile);
        String decryptedText = processText(text, key, false);
        writeFile(outputFile, decryptedText);
    }

    public void bruteForce(String inputFile, String outputFile) {
        String text = readFile(inputFile);
        StringBuilder decryptedText = new StringBuilder();
        for (int key = 1; key < ALPHABET.length(); key++) {
            decryptedText.append("*******************************************\n\n" +
                    "Key: " + key + " "
                    + processText(text, key, false) +
                    "\n\n");
        }
        writeFile(outputFile, decryptedText.toString());
    }

    public void statisticalAnalysis(String inputFile, String outputFile) {
        String text = readFile(inputFile);
        StringBuilder decryptedText = new StringBuilder();
        Map<Character, Integer> frequencyMap = new HashMap<>();

        // посчитал количество вхождений каждой буквы
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char lowerChar = Character.toLowerCase(c);
                frequencyMap.put(lowerChar, frequencyMap.getOrDefault(lowerChar, 0) + 1);
            }
        }

        // поиск максимально встречающейся буквы в зашифрованном тексте
        char mostFrequentChar = ' ';
        int maxFrequency = 0;

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }
        int key = (ALPHABET.indexOf(mostFrequentChar) - ALPHABET.indexOf('e') + ALPHABET.length()) % ALPHABET.length(); // е - первая самая встречающаяся буква
        decryptedText.append("*******************************************\n\n" + "Letter: " + "'e' " + processText(text, key, false) + "\n\n");

        int secondKey = (ALPHABET.indexOf(mostFrequentChar) - ALPHABET.indexOf('t') + ALPHABET.length()) % ALPHABET.length(); // t - первая самая встречающаяся буква
        decryptedText.append("*******************************************\n\n" + "Letter: " + "'t' " + processText(text, secondKey, false) + "\n\n");

        writeFile(outputFile, decryptedText.toString());
    }



    private String processText(String text, int key, boolean encrypt) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                int newIndex;
                if (encrypt) {
                    newIndex = (index + key) % ALPHABET.length();
                } else {
                    newIndex = (index - key + ALPHABET.length()) % ALPHABET.length();
                }
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }


    private String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return content.toString();
    }

    private void writeFile(String filePath, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        CaesarCipher cipher = new CaesarCipher();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Выберите действие:");
            System.out.println("1. Шифрование");
            System.out.println("2. Расшифровка с ключом");
            System.out.println("3. Brute force");
            System.out.println("4. Статистический анализ");
            System.out.println("0. Выход");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите имя входного файла: ");
                    String inputFileEncrypt = scanner.nextLine();
                    System.out.print("Введите имя выходного файла: ");
                    String outputFileEncrypt = scanner.nextLine();
                    System.out.print("Введите ключ: ");
                    int encryptKey = scanner.nextInt();
                    cipher.encrypt(inputFileEncrypt, outputFileEncrypt, encryptKey);
                    break;

                case 2:
                    System.out.print("Введите имя входного файла: ");
                    String inputFileDecrypt = scanner.nextLine();
                    System.out.print("Введите имя выходного файла: ");
                    String outputFileDecrypt = scanner.nextLine();
                    System.out.print("Введите ключ: ");
                    int decryptKey = scanner.nextInt();
                    cipher.decrypt(inputFileDecrypt, outputFileDecrypt, decryptKey);
                    break;

                case 3:
                    System.out.print("Введите имя входного файла: ");
                    String inputFileBruteForce = scanner.nextLine();
                    System.out.print("Введите имя выходного файла: ");
                    String outputFileBruteForce = scanner.nextLine();
                    cipher.bruteForce(inputFileBruteForce, outputFileBruteForce);
                    break;

                case 4:
                    System.out.print("Введите имя входного файла: ");
                    String inputFileAnalysis = scanner.nextLine();
                    System.out.print("Введите имя выходного файла: ");
                    String outputFileAnalysis = scanner.nextLine();
                    cipher.statisticalAnalysis(inputFileAnalysis, outputFileAnalysis);
                    break;

                case 0:
                    System.out.println("Выход из программы.");
                    break;

                default:
                    System.out.println("\nНеверный выбор. Пожалуйста, попробуйте снова.\n");
            }
        } while (choice != 0);

        scanner.close();
    }
}