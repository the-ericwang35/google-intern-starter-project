package com.example.foodsupplier.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FoodSupplierDAO {

    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static final int VENDOR_ONE = 1;
    private static final int VENDOR_TWO = 2;
    private static final int VENDOR_THREE = 3;

    private Map<String, List<Integer>> ingredientToVendorsMap;

    public FoodSupplierDAO() {
        setupVendorMap();
    }

    public String getVendors(String ingredient) {
        List<Integer> vendors = ingredientToVendorsMap.getOrDefault(ingredient, Collections.emptyList());
        int[] vendorsArray = listToArray(vendors);
        return IntStream.of(vendorsArray)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(","));
    }

    private void setupVendorMap() {
        ingredientToVendorsMap = new HashMap<>();
        List<Integer> appleVendors = Arrays.asList(VENDOR_ONE, VENDOR_TWO, VENDOR_THREE);
        ingredientToVendorsMap.put(APPLE, appleVendors);
        List<Integer> bananaVendors = Arrays.asList(VENDOR_ONE, VENDOR_THREE);
        ingredientToVendorsMap.put(BANANA, bananaVendors);
    }

    private int[] listToArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
