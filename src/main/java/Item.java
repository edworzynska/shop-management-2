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
    @Column(name="name")
    private String name;

    public void setName(String name) {
        this.name = name;
        if (name.isEmpty() || name.isBlank()) throw new RuntimeException("Product's name cannot be empty!");
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        if (unitPrice <= 0.0) throw new RuntimeException("Price cannot be less or equal to 0!");
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
        if (quantity <= 0) throw new RuntimeException("Quantity cannot be less or equal to 0!");
    }

    @Getter
    @Column(name="unit_price")
    private Double unitPrice;

    @Getter
    @Column(name="quantity")
    private Long quantity;

    public Item() {}

    @Override
    public String toString(){
        return getName() + ", quantity: " + getQuantity() +
                ", unit price: " + getUnitPrice() + "GBP";
    }

}
