import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Item> items = new ArrayList<>();

    @Getter
    @Column(name = "customer_name")
    private String customerName;

    @Getter
    @Setter
    @Column(name = "date_of_order")
    private LocalDate dateOfOrder;

    public void setItems(List<Item> items) {
        this.items = items;
        if (items.isEmpty()) throw new RuntimeException("Unable to create an empty order!");
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        if (customerName.isEmpty() || customerName.isBlank()) throw new RuntimeException("Customer's name cannot be empty!");
    }

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
