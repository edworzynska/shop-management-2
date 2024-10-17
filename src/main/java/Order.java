import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Getter
@EqualsAndHashCode
@Entity
@Table(name="Orders")
public class Order {

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Orders_Items",
            joinColumns = {@JoinColumn(name="order_id")},
            inverseJoinColumns = {@JoinColumn(name="item_id")}
    )
    private Set<Item> items = new HashSet<>();

    @Getter
    @Setter
    @Column(name = "customer_name")
    private String customerName;

    @Getter
    @Setter
    @Column(name = "date_of_order")
    private LocalDate dateOfOrder;

    public Order() {}

    public Order(String customerName, LocalDate dateOfOrder) {
        this.customerName = customerName;
        this.dateOfOrder = dateOfOrder;
    }
    @Override
    public String toString(){
        return "Order placed by: " + getCustomerName() + " on " + getDateOfOrder();
    }
}
