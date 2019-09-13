import java.util.Scanner;
/**
 * A program that reads a file with a certain number of recipes, and creates an interface for the user to interact with in order to display or search through the recipes.
 */
public class RecipeProgram {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the name of a recipe file: ");
        String fileName = input.nextLine();
        RecipeBook newRecipes = new RecipeBook(fileName);
        int choice = 0;
        while (choice != 4) {
            System.out.println("What would you like to do?");
            System.out.println("1. Show all recipes");
            System.out.println("2. Show recipe details");
            System.out.println("3. Search for ingredient");
            System.out.println("4. Exit");
            choice = input.nextInt();
            if (choice == 1) {
                System.out.println("There are " + newRecipes.getRecipeCount() + " recipes");
                for(int i = 0; i < newRecipes.getRecipeCount(); i++) {
                    System.out.println((i + 1) + ". " + newRecipes.getRecipe(i));
                }
                System.out.println();
            }
            else if (choice == 2) {
                System.out.println("What recipe would you like the details for?: ");
                int recipeNumber = input.nextInt();
                try {
                    System.out.println(newRecipes.getRecipe(recipeNumber - 1).getDetails());
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("There seems to be a problem, that recipe doesn't exist. \n");
                }
            }
            else if (choice == 3) {
                System.out.println("What ingredient would you like to search for?");
                input.nextLine();
                String ingredient = input.nextLine();
                boolean wasFound = false;
                int foundIndex = newRecipes.findIngredient(ingredient);
                while (foundIndex >= 0) {
                    System.out.println((foundIndex + 1) + ". " + newRecipes.getRecipe(foundIndex));
                    wasFound = true;
                    foundIndex = newRecipes.findIngredient(ingredient, foundIndex + 1);
                }
                if (wasFound == false) {
                    System.out.println("No recipes contained that ingredient");
                }
                System.out.println();
            }
        }
        System.out.println("See ya!");
    }
}
