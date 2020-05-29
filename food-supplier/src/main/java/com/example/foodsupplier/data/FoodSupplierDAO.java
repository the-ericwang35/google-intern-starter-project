package com.example.foodsupplier.data;

import com.example.foodsupplier.utils.StackdriverUtils;
import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsRecorder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.foodsupplier.utils.StackdriverUtils.NUM_SUPPLIERS;

public class FoodSupplierDAO {

    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static final String HAIRCUT = "haircut";
    private static final int VENDOR_ONE = 1;
    private static final int VENDOR_TWO = 2;
    private static final int VENDOR_THREE = 3;

    private static final StatsRecorder statsRecorder = Stats.getStatsRecorder();

    private Map<String, List<Integer>> ingredientToVendorsMap;

    public FoodSupplierDAO() {
        setupVendorMap();
        StackdriverUtils.setupSupplierRecorder();
    }

    public String getVendors(String ingredient) throws InterruptedException {
        List<Integer> vendors = ingredientToVendorsMap.getOrDefault(ingredient, Collections.emptyList());
        int[] vendorsArray = listToArray(vendors);
        statsRecorder.newMeasureMap().put(NUM_SUPPLIERS, vendorsArray.length).record();

        // Sleep for random time to simulate query time.
        Thread.sleep((long)(Math.random() * 1000));
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
        List<Integer> haircutVendors = Collections.singletonList(VENDOR_THREE);
        ingredientToVendorsMap.put(HAIRCUT, haircutVendors);
    }

    private int[] listToArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
