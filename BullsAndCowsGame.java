
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

    public class BullsAndCowsGame {
        private static final int CODE_LENGTH = 4; // Длина загадываемой строки
        private static final String SAVE_FILE = "game_log.txt"; // Файл для сохранения результатов игры
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // Формат даты и времени

        private Random random;
        private List<Integer> secretCode;
        private int gameNumber;
        private int attempts;
        private boolean gameWon;

        public BullsAndCowsGame() {
            random = new Random();
            secretCode = generateSecretCode();
            gameNumber = 0;
            attempts = 0;
            gameWon = false;
        }

        private List<Integer> generateSecretCode() {
            List<Integer> code = new ArrayList<>();
            while (code.size() < CODE_LENGTH) {
                int digit = random.nextInt(10);
                if (!code.contains(digit)) {
                    code.add(digit);
                }
            }
            return code;
        }

        public void playGame() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Добро пожаловать в Быки и Коровы!");

            while (!gameWon) {
                gameNumber++;
                attempts = 0;
                gameWon = false;
                System.out.println("Игра " + gameNumber + ":");
                System.out.println("Я сгенерировал секрет " + CODE_LENGTH + "-digit number. Сможете угадать");

                while (!gameWon) {
                    System.out.print("Введите сове предложение");
                    String guess = scanner.nextLine();
                    if (guess.length() != CODE_LENGTH) {
                        System.out.println("Недопустимая длинна. Please enter a " + CODE_LENGTH + "-digit number.");
                        continue;
                    }

                    attempts++;
                    int bulls = 0;
                    int cows = 0;
                    List<Integer> guessDigits = new ArrayList<>();
                    for (char ch : guess.toCharArray()) {
                        int digit = Character.getNumericValue(ch);
                        guessDigits.add(digit);
                    }

                    for (int i = 0; i < CODE_LENGTH; i++) {
                        int secretDigit = secretCode.get(i);
                        int guessDigit = guessDigits.get(i);
                        if (secretDigit == guessDigit) {
                            bulls++;
                        } else if (secretCode.contains(guessDigit)) {
                            cows++;
                        }
                    }

                    if (bulls == CODE_LENGTH) {
                        gameWon = true;
                        System.out.println("Поздравляем вы угадали " + attempts + " попытки .");
                    } else {
                        System.out.println("Быки: " + bulls + ", Коровы: " + cows);
                    }
                }

                saveGameLog(gameNumber, attempts);
                System.out.print("Хотите снова сыграть ? (да/нет): ");
                String playAgain = scanner.nextLine().toLowerCase();
                if (!playAgain.equals("да")) {
                    break;
                }
            }

            System.out.println("Спсибо что играите в Быки и Коровы ");
        }

        private void saveGameLog(int gameNumber, int attempts) {
            try {
                FileWriter writer = new FileWriter(SAVE_FILE, true);
                LocalDateTime currentTime = LocalDateTime.now();
                String timestamp = currentTime.format(DATE_FORMATTER);
                String logEntry = "Игра  " + gameNumber + ": " + attempts + " попытка - " + timestamp + "\n";
                writer.write(logEntry);
                writer.close();
            } catch (IOException e) {
                System.out.println("Не удалось сохранить игру: " + e.getMessage());
            }
        }

        public static void main(String[] args) {
            BullsAndCowsGame game = new BullsAndCowsGame();
            game.playGame();
        }
    }

}
