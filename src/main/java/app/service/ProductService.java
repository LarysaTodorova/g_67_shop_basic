package app.service;

import app.domain.Product;
import app.exception.ProductNotFoundException;
import app.exception.ProductSaveException;
import app.exception.ProductUpdateException;
import app.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private static ProductService instance;
    private final ProductRepository repository = new ProductRepository();

    private ProductService() {

    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    // Сохранить продукт в базе данных (при сохранении продукт автоматически считается активным).
    public Product save(Product product) {
        if (product == null) {
            throw new ProductSaveException("Product can't be null");
        }

        String title = product.getTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new ProductSaveException("Products title can't be null");
        }

        if (product.getPrice() < 0) {
            throw new ProductSaveException("Products price can't be negative");
        }

        product.setActive(true);
        return repository.save(product);
    }

    // Вернуть все продукты из базы данных (активные).
    public List<Product> getAllActiveProducts() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .toList();
    }

    // Вернуть один продукт из базы данных по его идентификатору (если он активен).
    public Product getActiveProductById(Long id) {
        Product product = repository.findById(id);

        if (product == null || !product.isActive()) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    // Изменить один продукт в базе данных по его идентификатору.
    public void update(Long id, double newPrice) {
        if (newPrice < 0) {
            throw new ProductUpdateException("Product price can't be negative");
        }
        repository.update(id, newPrice);
    }

    //  Удалить продукт из базы данных по его идентификатору.
    public void deleteById(Long id) {
        Product product = getActiveProductById(id);
        product.setActive(false);
    }

    // Удалить продукт из базы данных по его наименованию.
    public void deleteByTitle(String title) {
        getAllActiveProducts()
                .stream()
                .filter(product -> product.getTitle().equals(title))
                .forEach(product -> product.setActive(false));
    }

    // Восстановить удалённый продукт в базе данных по его идентификатору.
    public void restoreById(Long id) {
        Product product = repository.findById(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        product.setActive(true);
    }

    // Вернуть общее количество продуктов в базе данных (активных).
    public int getActiveProductsCount() {
        return getAllActiveProducts().size();
    }

    // Вернуть суммарную стоимость всех продуктов в базе данных (активных).
    public double getActiveProductsTotalCost() {
        // 1 Способ. Цикл
//        double sum = 0.0;
//        for (Product product : getAllActiveProducts()) {
//            sum += product.getPrice();
//        }
//          return sum;

        // 2 способ. Стрим
        return getAllActiveProducts()
                .stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    // Вернуть среднюю стоимость продукта в базе данных (из активных).
    public double getActiveProductsAveragePrice() {
        //  1 Способ с использованием предыдущих методов
//        int activeProductsCount = getActiveProductsCount();
//        if (activeProductsCount == 0) {
//            return 0;
//        }
//
//        return getActiveProductsTotalCost() / activeProductsCount;

        // 2 Способ. Стрим
        return getAllActiveProducts()
                .stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);
    }
}
