package com.shopjava.shopjava_forestage_backend.unit.factory;

import com.github.javafaker.Faker;
import com.shopjava.shopjava_forestage_backend.model.Product;

import java.util.Locale;

public class ProductFactory {
    public static Product createDefault() {
        Faker faker = new Faker(Locale.forLanguageTag("zh-TW"));

        Product product = new Product();
        product.setName(faker.commerce().productName());
        product.setPrice(faker.number().numberBetween(100, 10000));
        product.setSku(faker.regexify("SKU-[A-Z]{3}-[0-9]{3}"));
        product.setRoute(faker.internet().slug());
        product.setDescription(faker.lorem().paragraph());
        product.setQuantity(faker.number().numberBetween(0, 1000));
        product.setStatus(true);
        product.setFirstPhoto(faker.file().fileName() + ".jpg");
        return product;
    }
}
