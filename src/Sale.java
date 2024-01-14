import java.time.LocalDateTime;

public class Sale {
    private int saleId;
    private LocalDateTime saleDateTime;
    private int customerId;
    private int productId;
    private int saleAmount;
    public Sale(int saleId, LocalDateTime saleDate,
                int customerId, int productId, int _saleAmount){

        this.saleId = saleId;
        this.saleDateTime = saleDate;
        this.customerId = customerId;
        this.productId = productId;
        this.saleAmount = _saleAmount;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public void setSaleDateTime(LocalDateTime saleDateTime) {
        this.saleDateTime = saleDateTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    // Геттеры и сеттеры для атрибутов
    // ...
}
