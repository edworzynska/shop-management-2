import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private Main mainApp;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void displaysAllItems() {
        when(itemRepository.displayAll()).thenReturn("a string with contents");
        Scanner scanner = new Scanner("1\n0\n");
        mainApp = new Main(itemRepository, orderRepository, scanner);
        mainApp.run();
        verify(itemRepository, times(1)).displayAll();
    }

    @Test
    void addsItemToStock() {
        Scanner scanner = new Scanner("2\nbiscuits\n6,80\n100\n0\n");
        mainApp = new Main(itemRepository, orderRepository, scanner);
        mainApp.run();

        ArgumentCaptor<String> name = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> price = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Long> quantity = ArgumentCaptor.forClass(Long.class);

        verify(itemRepository, times(1)).createItem(name.capture(), price.capture(), quantity.capture());

        assertEquals("biscuits", name.getValue());
        assertEquals(6.80, price.getValue());
        assertEquals(100L, quantity.getValue());
    }

}