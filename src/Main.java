import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File customersFile = new File( "customers.csv"); // Путь к файлу с данными о покупателях
        File productsFile = new File( "products.csv"); // Путь к файлу с данными о товарах
        File salesFile = new File( "sales.csv"); // Путь к файлу с данными о продажах

        try {
            SaleDataReader reader = new SaleDataReader(salesFile, customersFile, productsFile);
            List<Sale> salesData = reader.readSalesData();
            List<Customer> customersData = reader.readCustomersData();
            List<Product> productsData = reader.readProductsData();
            System.out.println("Загружено: Продаж – " +salesData.size() + ", Покупателей – "+ customersData.size()
                    +", Продуктов – "+ productsData.size());
            SalesProcessor processor = new SalesProcessor(salesData, customersData, productsData);

            UserInterface.start(processor); // Запуск пользовательского интерфейса

        } catch (IOException e) {
            System.out.println("Error reading data files: " + e.getMessage());
        }
    }
}