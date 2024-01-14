import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SaleDataReader {
    private File salesFile;
    private File customersFile;
    private File productsFile;


    public SaleDataReader(File salesFile, File customersFile, File productsFile) {
        this.salesFile = salesFile;
        this.customersFile = customersFile;
        this.productsFile = productsFile;


    }

    public List<Sale> readSalesData() throws IOException {
        List<Sale> salesData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(salesFile))) {
            String line;
            br.readLine(); // Skip header if present

            while ((line = br.readLine()) != null) {
                String[] saleInfo = line.split(";");
                if (saleInfo.length == 5) {
                    int saleId = Integer.parseInt(saleInfo[0]);
                    LocalDateTime saleDateTime = LocalDateTime.parse(saleInfo[1], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                    int customerId = Integer.parseInt(saleInfo[2]);
                    int productId = Integer.parseInt(saleInfo[3]);
                    int saleAmount = Integer.parseInt(saleInfo[4]);
                    Sale sale = new Sale(saleId, saleDateTime, customerId, productId,saleAmount );
                    salesData.add(sale);
                }
            }
        }

        return salesData;
    }

    public List<Customer> readCustomersData() throws IOException {
        List<Customer> customersData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(customersFile))) {
            String line;
            br.readLine(); // Skip header if present

            while ((line = br.readLine()) != null) {
                String[] customerInfo = line.split(";");
                if (customerInfo.length == 3) {
                    int customerID = Integer.parseInt(customerInfo[0]);
                    String customerName = customerInfo[1];
                    String customerEmail = customerInfo[2];
                    Customer customer = new Customer(customerID, customerName, customerEmail);
                    customersData.add(customer);
                }
            }
        }

        return customersData;
    }

    public List<Product> readProductsData() throws IOException {
        List<Product> productsData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(productsFile))) {
            String line;
            br.readLine(); // Skip header if present

            while ((line = br.readLine()) != null) {
                String[] productInfo = line.split(";");
                if (productInfo.length == 3) {
                    int productID = Integer.parseInt(productInfo[0]);
                    String productName = productInfo[1];
                    String productCategory = productInfo[2];
                    Product product = new Product(productID, productName, productCategory);
                    productsData.add(product);
                }
            }
        }

        return productsData;
    }

}
