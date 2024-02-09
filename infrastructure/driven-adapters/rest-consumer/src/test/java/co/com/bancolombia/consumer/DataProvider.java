package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.config.AdapterProperties;
import co.com.bancolombia.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static AdapterProperties getAdapterProperties(){

        AdapterProperties adapterProperties = new AdapterProperties();
        adapterProperties.setGetBooks("/products");
        AdapterProperties.Configuration configuration = new AdapterProperties.Configuration();
        configuration.setTimeout(40000L);
        adapterProperties.setConfiguration(configuration);
        return  adapterProperties;
    }

    public static AdapterProperties getAdapterPropertiesTiemout(){
        AdapterProperties adapterProperties = new AdapterProperties();
        adapterProperties.setGetBooks("/products");
        AdapterProperties.Configuration configuration = new AdapterProperties.Configuration();
        configuration.setTimeout(1L);
        adapterProperties.setConfiguration(configuration);
        return  adapterProperties;
    }

    public  static List<ProductDTO> getProductsDTO(){
        List<ProductDTO> products = new ArrayList<ProductDTO>();
        products.add(getProductDTO(1,"tv"));
        products.add(getProductDTO(2, "pc"));
         return products;
    }

    public static ProductDTO getProductDTO(Integer id, String title) {

        return ProductDTO.builder()
                .id(id)
                .title(title)
                .build();
    }
}
