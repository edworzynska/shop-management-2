
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemRepositoryTest {
    private static HibernateUtil hibernateUtil;
    private static SessionFactory sessionFactory;
    private static ItemRepository itemRepository;
    private static Session session;

    private Item item1;
    private static Item item2;
    private static Item item3;
    private static Item item4;

    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    void setUp() {
        hibernateUtil = new HibernateUtil();
        sessionFactory = hibernateUtil.startSession();
        itemRepository = new ItemRepository(sessionFactory);
        session = sessionFactory.openSession();
        session.getTransaction().begin();

        item1 = new Item();
        item1.setName("milk");
        item1.setUnitPrice(2.0);
        item1.setQuantity(50L);
        session.persist(item1);

        item2 = new Item();
        item2.setName("butter");
        item2.setUnitPrice(5.0);
        item2.setQuantity(20L);
        session.persist(item2);

        item3 = new Item();
        item3.setName("egg");
        item3.setUnitPrice(0.2);
        item3.setQuantity(200L);
        session.persist(item3);

        item4 = new Item();
        item4.setName("bread");
        item4.setUnitPrice(3.5);
        item4.setQuantity(100L);
        session.persist(item4);

        order1 = new Order();
        order1.setCustomerName("First Customer");
        order1.setDateOfOrder(LocalDate.parse("2024-10-05"));
        order1.getItems().add(item4);
        order1.getItems().add(item2);
        session.persist(order1);

        session.getTransaction().commit();
    }

    @Test
    void returnsAll() {
        var result = itemRepository.all().size();
        assertEquals(4, result);
    }

    @Test
    void returnsEmptyListIfNoItemsAreAdded() {
        session.getTransaction().begin();
        session.remove(order1);
        session.remove(item1);
        session.remove(item2);
        session.remove(item3);
        session.remove(item4);
        session.getTransaction().commit();
        var result = itemRepository.all();
        assertEquals(List.of(), result);
    }

    @Test
    void createsItem() {
        Item item5 = itemRepository.createItem("tomatoes", 1.20, 500L);
        var size = itemRepository.all().size();
        assertEquals(5, size);
        var str = item5.toString();
        assertEquals("tomatoes, quantity: 500, unit price: 1.2GBP", str);
    }


    @Test
    void throwsAnErrorWhileCreatingItemWithInvalidName() {
        RuntimeException r = assertThrows(RuntimeException.class, ()->itemRepository.createItem("", 5.0, 10L));
        assertEquals("Product's name cannot be empty!", r.getMessage());
    }
    @Test
    void throwsAnErrorWhileCreatingItemWithInvalidPrice() {
        RuntimeException r = assertThrows(RuntimeException.class, ()->itemRepository.createItem("doo", 0.0, 10L));
        assertEquals("Price cannot be less or equal to 0!", r.getMessage());
    }
    @Test
    void throwsAnErrorWhileCreatingItemWithInvalidQuantity() {
        RuntimeException r = assertThrows(RuntimeException.class, ()->itemRepository.createItem("doo", 5.0, -5L));
        assertEquals("Quantity cannot be less or equal to 0!", r.getMessage());
    }

    @Test
    void displaysAll() {
        var result = itemRepository.displayAll();
        assertEquals("milk, quantity: 50, unit price: 2.0GBP\n" +
                "butter, quantity: 20, unit price: 5.0GBP\n" +
                "egg, quantity: 200, unit price: 0.2GBP\n" +
                "bread, quantity: 100, unit price: 3.5GBP\n", result);
    }

    @Test
    void returnsMessageIfDisplayingEmptyListOfItems() {
        session.getTransaction().begin();
        session.remove(order1);
        session.remove(item1);
        session.remove(item2);
        session.remove(item3);
        session.remove(item4);
        session.getTransaction().commit();
        var result = itemRepository.displayAll();
        assertEquals("Your stock is empty!", result);
    }

    @Test
    void returnsListOfItemsInOrder(){
        var result = itemRepository.itemsInOrder(order1);
        var resultSize = result.size();
        assertEquals(2, resultSize);
        var resultStr = "";
        for (Item item : result){
            resultStr += item.toString();
        }
        assertEquals("butter, quantity: 20, unit price: 5.0GBPbread, quantity: 100, unit price: 3.5GBP", resultStr);
    }
}