import com.nutrition.inventory.entity.FoodItem;
import com.nutrition.inventory.repository.FoodItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = com.nutrition.inventory.FoodBackendServiceApplication.class)
class FoodItemRepositoryTest {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Test
    void testReadFoodItems() {
        List<FoodItem> items = foodItemRepository.findAll();

        assertNotNull(items);

        System.out.println("Total food items: " + items.size());

        for (FoodItem item : items) {
            System.out.println(item.getName());
        }
    }
}