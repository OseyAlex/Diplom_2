package praktikumservices.stellar_burgers.scripts;

import io.qameta.allure.Step;
import praktikumservices.stellar_burgers.entities.IngredientsData;
import praktikumservices.stellar_burgers.helpers.OrdersHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GetIngredientsData {
    private HashMap<String, List> orderHash;
    private List<String> ingredientsList;

    @Step("Получить пустой список ингредиентов")
    public HashMap<String, List> getEmptyOrderHash() {
        ingredientsList = new ArrayList<>();
        orderHash = new HashMap<>();
        orderHash.put("ingredients", ingredientsList);
        return orderHash;
    }

    @Step("Получить список ингредиентов с неправильным хэш")
    public HashMap<String, List> getOrderHashWithWrongHash() {
        ingredientsList = new ArrayList<>();
        orderHash = new HashMap<>();
        ingredientsList.add("wrongHash");
        orderHash.put("ingredients", ingredientsList);
        return orderHash;
    }

    @Step("Получить OrderHash с N случайных ингридиентов")
    public HashMap<String, List> getOrderHashWithIngredients(int numberOfIngredients) {
        OrdersHelper orderHelper = new OrdersHelper();
        IngredientsData ingredientsData = orderHelper.getIngredients();
        ingredientsList = new ArrayList<>();
        orderHash = new HashMap<>();
        Random rnd = new Random();
        int maxIngredientsQuantity = ingredientsData.getData().size();
        for (int i = 1; i <= numberOfIngredients; i++) {
            ingredientsList.add(ingredientsData.getData().get(rnd.nextInt(maxIngredientsQuantity)).getId());
        }
        orderHash.put("ingredients", ingredientsList);
        return orderHash;
    }
}
