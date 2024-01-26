package org.example;

public class Main {
    public static void main(String[] args) {
        Product product1 = new Product("3829172", "Кровать", "белый", 15600, 30);
        Product product2 = new Product("3829173", "Диван",  25600, 7);
        Product product3 = new Product("3829174", "Шкаф", "красный", 7800, 9);

        DatabaseManagement.addProducts(product1, product2, product3);
        DatabaseManagement.printProductsList();
        DatabaseManagement.printOrderProducts(5);
        DatabaseManagement.addOrder("Николай Иванов", "(921)051-29-33", null, "Главный пр. 21-21",
                product1, product1, product1, product2, product3, product3);
    }
}