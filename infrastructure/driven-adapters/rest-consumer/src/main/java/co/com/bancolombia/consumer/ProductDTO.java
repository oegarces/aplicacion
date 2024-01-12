package co.com.bancolombia.consumer;


    import lombok.Builder;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.List;

@Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
public class ProductDTO {

    private Integer id;
    private String title;
    private double price;
    private String description;
    private List<String> images;
    private CategoryDTO category;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class CategoryDTO{
        private Integer id;
        private String name;

    }

}