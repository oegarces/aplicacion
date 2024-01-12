package co.com.bancolombia.consumer.helper;

import co.com.bancolombia.consumer.ProductDTO;
import co.com.bancolombia.model.product.Category;
import co.com.bancolombia.model.product.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductHelper {
    public static Product getProduct(ProductDTO productDTO){
        return Product.builder()
                .id(productDTO.getId())
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .images(productDTO.getImages())
                .category(getCategory(productDTO.getCategory()))
                .build();
    }
    public static Category getCategory(ProductDTO.CategoryDTO categoryDTO){
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .build();
    }
}
