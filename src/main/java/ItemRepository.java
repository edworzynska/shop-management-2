import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.ArrayList;
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
            throw new RuntimeException(e.getMessage());
        }
        return allItems;
    }
    public String displayAll(){
        StringBuilder str = new StringBuilder();
        for (Item item : all()){
            str.append(item.toString()).append("\n");
        }
        return str.isEmpty()? "Your stock is empty!" : str.toString();
    }

    public Item createItem(String name, Double unitPrice, Long quantity){

        try(Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            item = new Item();
            item.setName(name);
            item.setUnitPrice(unitPrice);
            item.setQuantity(quantity);
            session.persist(item);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return item;
    }

    public List<Item> findByNameAndReturnList(List<String> itemsToAdd){
        List<Item> itemList = new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            for (String str : itemsToAdd) {
                Item item = session.createQuery("from Item where name = :name", Item.class)
                        .setParameter("name", str)
                        .getSingleResult();
                itemList.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return itemList;
    }
    public List<Item> itemsInOrder(Order order){
        List<Item> itemsInOrder;

        try (Session session = sessionFactory.openSession()) {
            itemsInOrder = session.createQuery("from Item i join i.orders o where o.id = :id", Item.class)
                    .setParameter("id", order.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return itemsInOrder;

    }
}
