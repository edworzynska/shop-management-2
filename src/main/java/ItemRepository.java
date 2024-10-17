import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;

public class ItemRepository {
    private SessionFactory sessionFactory;
    private Item item;

    public ItemRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public List<Item> all() {
        List<Item> allItems = null;
        try (Session session = sessionFactory.openSession()) {
            allItems = session.createQuery("from Item", Item.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return allItems;
    }

    public Item createItem(String name, Double unitPrice, Long quantity){

        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        item = new Item();
        item.setName(name);
        item.setUnitPrice(unitPrice);
        item.setQuantity(quantity);
        session.persist(item);
        session.getTransaction().commit();
        session.close();

        return item;
    }
}
