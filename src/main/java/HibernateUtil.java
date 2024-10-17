import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    public HibernateUtil() {

    }
    final StandardServiceRegistry registry =
            new StandardServiceRegistryBuilder()
                    .build();

    public SessionFactory startSession() {
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Item.class)
                    .addAnnotatedClass(Order.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return sessionFactory;
    }
}