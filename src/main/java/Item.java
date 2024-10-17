import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Entity
@Table(name="Items")
public class Item {

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Getter
    @ManyToMany(mappedBy="items")
    private Set<Order> orders = new HashSet<>();

    @Getter
    @Setter
    @Column(name="name")
    private String name;

    @Getter
    @Setter
    @Column(name="unit_price")
    private Double unitPrice;

    @Getter
    @Setter
    @Column(name="quantity")
    private Long quantity;

    public Item() {}

    @Override
    public String toString(){
        return getName() + ", quantity: " + getQuantity() +
                ", unit price: " + getUnitPrice() + "GBP";
    }

}
