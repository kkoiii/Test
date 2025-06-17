package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {
    private String menuCode;
    private String name;
    private double price;
    private List<String> ingredients;

    public Menu(String menuCode, String name, double price, List<String> ingredients) {
        this.menuCode = menuCode;
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>(ingredients);
    }

    // Getters
    public String getMenuCode() { return menuCode; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public List<String> getIngredients() { return new ArrayList<>(ingredients); }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    
    // Methods to manage ingredients
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s | %-30s | %,d VND\n", menuCode, name, (int)price));
        sb.append("Ingredients:\n");
        for (String ingredient : ingredients) {
            sb.append("+ ").append(ingredient).append("\n");
        }
        return sb.toString();
    }
} 