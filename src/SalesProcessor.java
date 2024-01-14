import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.util.Map;

public class SalesProcessor {
    private List<Sale> salesData;
    private List<Customer> customersData;
    private List<Product> productsData;
    public  int topCount;
    public  int fintTotalAmunt;
    ExecutorService executor;

    public SalesProcessor(List<Sale> salesData, List<Customer> customersData, List<Product> productsData) {
        this.salesData = salesData;
        this.customersData = customersData;
        this.productsData = productsData;
        executor = Executors.newFixedThreadPool(16);

    }

    public double calculateTotalSales() {
        return salesData.stream()
                .mapToDouble(Sale::getSaleAmount)
                .sum();
    }

    public List<Product> findTopPopularProducts() {

        Map<Integer, Long> productCounts = salesData.stream()
                .distinct() // Удаление дубликатов
                .collect(Collectors.groupingBy(Sale::getProductId, Collectors.counting()));
         productCounts.entrySet().forEach(entry -> {
            // Выводим ключ (ID продукта) и значение (количество продаж)
            System.out.println("Product ID: " + entry.getKey() + ", Sales Count: " + entry.getValue());
        });

                return productCounts.entrySet().stream()
                // Отфильтровать продукты с минимальным количеством продаж
                .filter(entry -> entry.getValue() > 0)
                // Отсортировать записи по значению (количеству продаж) в порядке убывания3
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                // Ограничить поток указанным количеством записей (topCount)
                .limit(topCount)
                // Для каждой записи в потоке выполнить следующее:
                .map(entry -> productsData.stream()
                        // Фильтровать список продуктов, оставить только тот, чей ID совпадает с ключом записи (ID продукта)
                        .filter(product -> product.getProductId() == entry.getKey())
                        // Найти первый подходящий продукт или вернуть null, если нет соответствий
                        .findFirst().orElse(null))
                // Отфильтровать все null значения (продукты, не найденные в списке продуктов)
                .filter(Objects::nonNull)
                // Собрать результат в список List<Product>
                .collect(Collectors.toList());




    }


    public List<Product> findTopUnpopularProducts() {
        // Получение уникальных продуктовых ID с количеством их продаж
        Map<Integer, Long> productCounts = salesData.stream()
                .distinct() // Удаление дубликатов
                .collect(Collectors.groupingBy(Sale::getProductId, Collectors.counting()));


            return productCounts.entrySet().stream()
                            // Отфильтровать продукты с минимальным количеством продаж
                            .filter(entry -> entry.getValue() > 0)
                            // Отсортировать записи по значению (количеству продаж) в порядке возрастания
                            .sorted(Map.Entry.comparingByValue())
                            // Ограничить поток указанным количеством записей (topCount)
                            .limit(topCount)
                            // Для каждой записи в потоке выполнить следующее:
                            .map(entry -> productsData.stream()
                                    // Фильтровать список продуктов, оставить только тот, чей ID совпадает с ключом записи (ID продукта)
                                    .filter(product -> product.getProductId() == entry.getKey())
                                    // Найти первый подходящий продукт или вернуть null, если нет соответствий
                                    .findFirst().orElse(null))
                            // Отфильтровать все null значения (продукты, не найденные в списке продуктов)
                            .filter(Objects::nonNull)
                            // Собрать результат в список List<Product>
                            .collect(Collectors.toList());


    }

    public List<Customer> findCustomersByTotalAmount(double amount) {
        Map<Integer, Double> customerTotalAmount = salesData.stream()
                .collect(Collectors.groupingBy(Sale::getCustomerId, Collectors.summingDouble(Sale::getSaleAmount)));

        return customerTotalAmount.entrySet().stream()
                .filter(entry -> entry.getValue() > amount)
                .map(entry -> customersData.stream()
                        .filter(customer -> customer.getCustomerId() == entry.getKey())
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());



    }


    public Map<String, Object> analyzeSalesTrends() {
        Map<String, Object> salesTrends = new HashMap<>();


            // Анализ тенденций продаж
            double totalSales = calculateTotalSales();

            List<Product> topFivePopularProducts = findTopPopularProducts();
            List<Product> topFiveUnpopularProducts = findTopUnpopularProducts();

            List<Customer> highValueCustomers = findCustomersByTotalAmount(fintTotalAmunt); // пример суммы

            salesTrends.put("TotalSales", totalSales);
            salesTrends.put("TopFivePopularProducts", topFivePopularProducts);
            salesTrends.put("TopFiveUnpopularProducts", topFiveUnpopularProducts);
            salesTrends.put("HighValueCustomers", highValueCustomers);



        return salesTrends;
    }


    public void generateReports(Map<String, Object> data) {
        // Генерация отчетов на основе переданных данных
        try {

            FileWriter writer = new FileWriter("sales_report.txt");

            // Запись общей суммы продаж в отчет
            writer.write("Общий объем продаж: " + data.get("TotalSales") + "\n\n");

            // Запись популярных товаров в отчет
            writer.write("Топ популярных продуктов:\n");
            for (Product product : (List<Product>) data.get("TopFivePopularProducts")) {
                writer.write(product.getProductName() + "\n");
            }
            writer.write("\n");

            // Запись непопулярных товаров в отчет
            writer.write("Топ непопулярных продуктов: \n");
            for (Product product : (List<Product>) data.get("TopFiveUnpopularProducts")) {
                writer.write(product.getProductName() + "\n");
            }
            writer.write("\n");

            // Запись информации о покупателях с высокими покупками
            writer.write("Транжиры: \n");
            for (Customer customer : (List<Customer>) data.get("HighValueCustomers")) {
                writer.write(customer.getCustomerName() + " - " + customer.getCustomerEmail() + "\n");
            }

            writer.close();
            System.out.println("Отчеты сгенерированы успешно.");
        } catch (IOException e) {
            System.out.println("Отчеты об ошибках, генерирующие отчеты: " + e.getMessage());
        }
    }
}