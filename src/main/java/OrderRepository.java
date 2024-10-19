import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

import java.util.List;

public class OrderRepository {
    private SessionFactory sessionFactory;
    private Order order;
    private Session session;

    public OrderRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public List<Order> all() {
        List<Order> allOrders = null;
        try (Session session = sessionFactory.openSession()) {
            allOrders = session.createQuery("from Order", Order.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return allOrders;
    }
//    private void addToCart(List<Item> items){
//        session.getTransaction().begin();
//        for (Item item : items){
//            order.getItems().add(item);
//        }
//        session.merge(order);
//        session.getTransaction().commit();
//    }

    public Order createOrder(String name, List<Item> items){

        session = sessionFactory.openSession();
        session.getTransaction().begin();
        order = new Order();
        order.setCustomerName(name);
        order.setDateOfOrder(LocalDate.now());

        order.setItems(items);

        session.merge(order);
        session.getTransaction().commit();
        session.close();

        return order;
    }
    public String displayAll(){
        StringBuilder str = new StringBuilder();
        for (Order order : all()){
            str.append(order.toString()).append("\n");
        }
        return str.isEmpty()? "No active orders!" : str.toString();
    }
}
