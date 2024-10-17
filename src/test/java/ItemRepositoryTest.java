
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        session.getTransaction().commit();
    }

    @Test
    void returnsAll() {
        var result = itemRepository.all().size();
        assertEquals(4, result);
    }

    @Test
    void createsItem() {
        Item item5 = itemRepository.createItem("tomatoes", 1.20, 500L);
        var size = itemRepository.all().size();
        assertEquals(5, size);
        var str = item5.toString();
        assertEquals("tomatoes, quantity: 500, unit price: 1.2GBP", str);
    }
}