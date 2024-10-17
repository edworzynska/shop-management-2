import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.List;

public class OrderRepository {
    private SessionFactory sessionFactory;
    private Order order;

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

    public Order createOrder(String name){

        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        order = new Order();
        order.setCustomerName(name);
        order.setDateOfOrder(LocalDate.now());
        session.persist(order);
        session.getTransaction().commit();
        session.close();

        return order;
    }
}
