import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void createsEmptyOrder(){
        order2 = orderRepository.createOrder("Gucci");
        String result = order2.toString();
        assertEquals(2, orderRepository.all().size());
        assertEquals("Order placed by: Gucci on 2024-10-17", result);
    }
    @Test
    void createsOrderAndAddsItems(){
        order3 = orderRepository.createOrder("Gucci");
        session.getTransaction().begin();
        order3.getItems().add(item1);
        order3.getItems().add(item2);
        order3.getItems().add(item3);
        session.merge(order3);
        session.getTransaction().commit();
        var result = order3.getItems().size();
        assertEquals(3, result);

    }
    @Test
    void returnsAllOrders() {
        var result = orderRepository.all().size();
        assertEquals(1, result);
    }
}