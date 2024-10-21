import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderRepositoryTest {
    private static HibernateUtil hibernateUtil;
    private static SessionFactory sessionFactory;
    private static OrderRepository orderRepository;
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
        orderRepository = new OrderRepository(sessionFactory);
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
    void createsOrder(){
        order2 = orderRepository.createOrder("Gucci", List.of(item1));
        String result = order2.toString();
        assertEquals(2, orderRepository.all().size());
        assertEquals(String.format("Order placed by: Gucci on %s", LocalDate.now()), result);
    }
    @Test
    void createsOrderAndAddsItems(){
        order3 = orderRepository.createOrder("Gucci", List.of(item1, item2, item3));
        var result = order3.getItems().size();
        assertEquals(3, result);

    }
    @Test
    void returnsEmptyListIfNoOrdersAreCreated() {
        session.getTransaction().begin();
        session.remove(order1);

        session.getTransaction().commit();
        var result = orderRepository.all();
        assertEquals(List.of(), result);
    }

    @Test
    void throwsAnErrorIfCreatingAnOrderWithEmptyCustomerName() {
        RuntimeException r = assertThrows(RuntimeException.class, ()->orderRepository.createOrder("",List.of(item1)));
        assertEquals("Customer's name cannot be empty!", r.getMessage());
    }
    @Test
    void throwsAnErrorIfCreatingAnOrderWithEmptyItems() {
        RuntimeException r = assertThrows(RuntimeException.class, ()->orderRepository.createOrder("Gucci",List.of()));
        assertEquals("Unable to create an empty order!", r.getMessage());
    }
    @Test
    void returnsAllOrders() {
        var result = orderRepository.all().size();
        assertEquals(1, result);
    }

    @Test
    void returnsMessageIfThereAreNoOrders() {
        session.getTransaction().begin();
        session.remove(order1);
        session.getTransaction().commit();
        var result = orderRepository.displayAll();
        assertEquals("No active orders!", result);
    }

    @Test
    void displaysAllOrdersAsString() {
        var result = orderRepository.displayAll();
        assertEquals("Order placed by: First Customer on 2024-10-05\n", result);
    }
}