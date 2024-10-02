package com.store.oncommerce_web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String title;
    @Setter
    private BigDecimal price;
    @Setter
    private String description;
    @Setter
    private String image;
    @Setter
    private String category;
    @Setter
    private BigDecimal discount;

    @Setter
    private String colorImages; // '{"#FF5733": "/images/product1_red.jpg", "#33FF57": "/images/product1_green.jpg"}'

    // Métodos para trabajar con JSON
    public Map<String, String> getColorImagesAsMap() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (colorImages == null || colorImages.isEmpty()) {
                return new HashMap<>(); // Devuelve un mapa vacío en lugar de null
            }
            return mapper.readValue(colorImages, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>(); // En caso de error, también devuelve un mapa vacío
        }
    }


    public void setColorImagesFromMap(Map<String, String> colorImagesMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.colorImages = mapper.writeValueAsString(colorImagesMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Embedded
    private Rating rating;



    public BigDecimal getDiscountedPrice() {
        if (this.discount != null && this.discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = this.price
                    .multiply(this.discount)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            return this.price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
        }
        return this.price.setScale(2, RoundingMode.HALF_UP); // Retorna el precio original si no hay descuento
    }

    public BigDecimal getPrice() {
        return this.price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiscount() {
        return (this.discount != null) ? this.discount : BigDecimal.ZERO;
    }

    @Embeddable
    @Data
    public static class Rating implements Serializable{
        private Double rate;
        private Integer count;
    }



}

