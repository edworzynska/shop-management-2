import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HibernateUtil hibernateUtil = new HibernateUtil();
        SessionFactory sessionFactory = hibernateUtil.startSession();
        ItemRepository itemRepository = new ItemRepository(sessionFactory);
        OrderRepository orderRepository = new OrderRepository(sessionFactory);
        Scanner scanner = new Scanner(System.in);
        int selected;

        System.out.println("===>   Welcome to ShopManager!   <===\n");

        do{
            System.out.println("Please select the action:\n" +
                    "1 = show the items in stock\n" +
                    "2 = add an item to the stock\n" +
                    "3 = list all orders\n" +
                    "4 = create a new order\n" +
                    "0 = exit");
            selected = scanner.nextInt();

            switch (selected) {
                case 1:
                    System.out.println(itemRepository.displayAll());
                    break;
                case 2:
                    System.out.println("name: ");
                    String itemName = scanner.next();
                    System.out.println("price: ");
                    Double price = scanner.nextDouble();
                    System.out.println("quantity: ");
                    Long quantity = scanner.nextLong();
                    itemRepository.createItem(itemName, price, quantity);
                    break;
                case 3:
                    List<Order> all = orderRepository.all();
                    for (Order order : all){
                        System.out.println(order.toString() + "\nordered items: ");
                        List<Item> itemsInOrder = itemRepository.itemsInOrder(order);
                        for (Item item : itemsInOrder){
                            System.out.println(item.toString());
                        }
                    }
                    break;
                case 4:
                    System.out.println("customer's name: ");
                    String customerName = scanner.next();
                    System.out.println("Please enter the item names for the order, one at a time. Type 'done' when finished:");
                    List<String> itemsToAdd = new ArrayList<>();

                    while (true) {
                        String inputItem = scanner.next();
                        if (inputItem.equalsIgnoreCase("done")) {
                            break;
                        }
                        itemsToAdd.add(inputItem);
                    }
                    List<Item> itemList = itemRepository.findByNameAndReturnList(itemsToAdd);

                    if (itemList.isEmpty()) {
                        System.out.println("No valid items found. Order not created.");
                    } else {
                        orderRepository.createOrder(customerName, itemList);
                        System.out.println("Order created successfully!");
                    }
                    break;
            }
        } while (selected != 0);
    }
}