import java.util.Scanner;
import java.util.concurrent.*;

public class UserInterface {


    private static Scanner scanner = new Scanner(System.in);

    public static void start(SalesProcessor processor) {
        System.out.println("Добро пожаловать!");
        boolean running = true;
        // Создаем пул потоков с фиксированным количеством потоков (в данном случае, 5 потоков)
        ExecutorService executor = Executors.newFixedThreadPool(5);

        int sizeTop = 4;
        int findAmunt = 100;


        while (running) {
            displayMenu();
            String userInput = getUserInput();

            switch (userInput.toLowerCase()) {
                case "1":
                    // Логика для выбора параметров обработки данных
                    System.out.println("Операция 1 выбрана.");
                    System.out.println("Размер рейтинга: "+ sizeTop+"\nПокупатели купившие свыше чем: " +findAmunt);
                    break;
                case "2":
                    // Логика для изменения параметров обработки данных
                    System.out.println("Операция 2 выбрана.");
                    System.out.println("Размер рейтинга: ");
                    sizeTop = Integer.parseInt(getUserInput());
                    System.out.println("\nПокупатели, купившие свыше чем: ");
                    findAmunt = Integer.parseInt(getUserInput());
                    break;
                case "3":
                // Логика для выполнения анализа данных и генерации отчетов
                    System.out.println("Операция 3 выбрана.");
                    int finalSizeTop = sizeTop;
                    int finalFindAmunt = findAmunt;
                    Future<?> future = executor.submit(() -> {
                        processor.topCount = finalSizeTop;
                        processor.fintTotalAmunt = finalFindAmunt;
                        processor.generateReports(processor.analyzeSalesTrends());
                    });

                    try {
                        future.get(2, TimeUnit.SECONDS); // Ожидание выполнения задачи не более 2 секунд
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        future.cancel(true); // Отмена задачи при таймауте
                        System.out.println("Таймаут выполнения операции.");
                    }


                    break;
                case "q":
                    running = false;
                    break;
                default:
                    System.out.println("Неизвесная опция. Попробуйте ещё раз!");
                    break;
            }
        }
        executor.shutdown();
        System.out.println("Выход из программы, прощайте!");
    }

    public static void displayMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Просмотр параметров обработки");
        System.out.println("2. Изменение параметров обработки");
        System.out.println("3. Анализ данных и создание отчетов");
        System.out.println("Q. Выход");
        System.out.print("Выберете операцию: ");
    }

    public static String getUserInput() {
        return scanner.nextLine();
    }
}