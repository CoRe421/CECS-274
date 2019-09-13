import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A program that reads a file with a certain number of recipes, and creates an interface for the user to interact with in order to display or search through the recipes.
 */
public class RecipeProgram {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RecipeSet newRecipes = getFile(input);
        int choice = 0;
        while (choice != 8) {
            System.out.println("What would you like to do?");
            printMenu();
            System.out.println("Please enter a slection:");
            choice = input.nextInt();
            input.nextLine();
            menuChoice(newRecipes, input, choice);
        }
        System.out.println("See ya!");
    }

    /**
     * Prints the menu
     */
    public static void printMenu() {
        System.out.println("1. Show all recipes");
        System.out.println("2. Show recipe details");
        System.out.println("3. Add recipe");
        System.out.println("4. Remove Recipe");
        System.out.println("5. Search for ingredient");
        System.out.println("6. Save recipe book");
        System.out.println("7. Print tree efficiency.");
        System.out.println("8. Exit");
    }

    /**
     * Prints out first the number of recipes in the list, and then each of the recipes in the list on seperate lines.
     * @param newRecipes The tree of recipes that is printed out.
     */
    public static void showRecipes(RecipeSet newRecipes) {
        if (newRecipes.getCount() > 1) {
            System.out.println("There are " + newRecipes.getCount() + " recipes");
            RecipeBook allRecipes = newRecipes.getAllRecipes();
            for (int i = 0; i < allRecipes.getRecipeCount(); i++) {
                System.out.println((i + 1) + ". " + allRecipes.getRecipe(i));
            }
        }
        else if (newRecipes.getCount() == 1) {
            System.out.println("There is 1 recipe");
            RecipeBook allRecipes = newRecipes.getAllRecipes();
            for (int i = 0; i < allRecipes.getRecipeCount(); i++) {
                System.out.println((i + 1) + ". " + allRecipes.getRecipe(i));
            }
        }
        else {
            System.out.println("There are 0 recipes");
        }
        System.out.println();
    }

    /**
     * Prints the details of a specific recipe.
     * @param newRecipes The tree of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void showDetails(RecipeSet newRecipes, Scanner input) {
        System.out.println("What recipe would you like the details for?: ");
        String recipeName = input.nextLine();
        System.out.println();
        Recipe foundRecipe = newRecipes.findRecipe(recipeName);
        if (foundRecipe != null) {
            System.out.println(foundRecipe.getDetails());
        }
        else {
            System.out.println("The recipe with that title was not found.");
        }
    }

    /**
     * Adds a recipe to the list of recipes.
     * @param newRecipes The tree of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void addRecipe(RecipeSet newRecipes, Scanner input) {
        System.out.println("Please enter the name of a recipe file: ");
        File newRecipeFile = new File(input.nextLine()); //input.nextLine()
        try {
            Scanner fileInput = new Scanner(newRecipeFile);
            String line = fileInput.nextLine();
            String[] recipeArray = line.split("\\|");
            int prepTime = Integer.parseInt(recipeArray[3]);
            int cookTime = Integer.parseInt(recipeArray[4]);
            String[] recipeIngredients = recipeArray[5].split("@");
            String[] recipeSteps = recipeArray[6].split("@");
            Recipe userRecipe = new Recipe(recipeArray[0], recipeArray[1], prepTime, cookTime, recipeIngredients, recipeSteps, recipeArray[2]);
            newRecipes.add(userRecipe);
            System.out.println(userRecipe.getTitle() + " - was added to list \n");
        }
        catch (FileNotFoundException ex) {
            System.out.println("Sorry, that file was not found \n");
        }
    }

    /**
     * Removes a recipe from the list of recipes.
     * @param newRecipes The tree of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void removeRecipe(RecipeSet newRecipes, Scanner input) {
        System.out.println("Enter a recipe name:");
        String userTitle = input.nextLine();
        Recipe titleFound = newRecipes.findRecipe(userTitle);
        if (titleFound != null) {
            newRecipes.remove(userTitle);
            System.out.println(userTitle + " was removed from the recipe book. \n");
        }
        else {
            System.out.println("No recipe was found in the list with that title. \n");
        }
    }

    /**
     * Searches for a specific ingredient in the list of recipes.
     * @param newRecipes The tree of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void searchIngredient(RecipeSet newRecipes, Scanner input) {
        System.out.println("What ingredient would you like to search for?");
        String ingredient = input.nextLine();
        RecipeBook ingredientList = newRecipes.findIngredient(ingredient);
        if (ingredientList.getRecipeCount() > 0) {
            for (int i = 0; i < ingredientList.getRecipeCount(); i++) {
                System.out.println((i + 1) + ". " + ingredientList.getRecipe(i));
            }
        }
        else {
            System.out.println("No recipes contained that ingredient");
        }
        System.out.println();
    }

    /**
     * Saves the list of recipes to a new file.
     * @param newRecipes The tree of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void saveBook(RecipeSet newRecipes, Scanner input) {
        System.out.println("Enter a file name:");
        String userFile = input.nextLine();
        File newRecipeFile = new File(userFile);
        try {
            PrintWriter output = new PrintWriter(new FileWriter(newRecipeFile));
            output.write(Integer.toString(newRecipes.getCount()));
            output.println();
            RecipeBook allRecipes = newRecipes.getAllRecipes();
            for (int i = 0; i < allRecipes.getRecipeCount(); i++) {
                String ingredientString = "";
                String stepString = "";
                String[] recipeIngredients = allRecipes.getRecipe(i).getIngredients();
                String[] recipeSteps = allRecipes.getRecipe(i).getSteps();
                for (int j = 0; j < recipeIngredients.length; j++) {
                    if (j == recipeIngredients.length - 1) {
                        ingredientString += recipeIngredients[j];
                        break;
                    }
                    ingredientString += recipeIngredients[j] + "@";
                }
                for (int j = 0; j < recipeSteps.length; j++) {
                    if (j == recipeSteps.length - 1) {
                        stepString += recipeSteps[j];
                        break;
                    }
                    stepString += recipeSteps[j] + "@";
                }
                output.println(allRecipes.getRecipe(i).getTitle() + "|" + allRecipes.getRecipe(i).getAuthor() + "|" + allRecipes.getRecipe(i).getDescription() + "|" + allRecipes.getRecipe(i).getPrepTime() + "|" + allRecipes.getRecipe(i).getCookingTime() + "|" + ingredientString + "|" + stepString);
            }
            output.close();
            System.out.println("Saved to " + userFile + "! \n");
        }
        catch (IOException ex) {
            System.out.println("Oops, that file doesn't exist \n");
        }
    }

    /**
     * Calculates the tree-efficiency of the RecipeSet.
     * @param newRecipes The tree of recipes that is being used.
     */
    public static void getEfficiency(RecipeSet newRecipes) {
        System.out.println("Efficiency: " + (double)newRecipes.getCount() / (Math.pow(2, newRecipes.getHeight()) - 1) + "\n");
    }

    /**
     * Asks the user for the file that contains the list of recipes that will be used in the program.
     * @param input The object used to get input from the user of the program.
     */
    public static RecipeSet getFile(Scanner input) {
        System.out.println("Please enter the name of a recipe file: ");
        try {
            String fileName = input.nextLine(); //input.nextLine();
            RecipeSet newRecipes = new RecipeSet(fileName);
            return newRecipes;
        }
        catch (NoSuchElementException ex) {
            System.out.println("It seems like that file is not formatted correctly");
            return null;
        }
    }

    /**
     * Gets the choice from the user of the program, and calls the corresponding method.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user.
     * @param choice The menu option that the user of the program selects.
     */
    public static void menuChoice(RecipeSet newRecipes, Scanner input, int choice) {
        if (choice == 1) {
            showRecipes(newRecipes);
        }
        else if (choice == 2) {
            showDetails(newRecipes, input);
        }
        else if (choice == 3) {
            addRecipe(newRecipes, input);
        }
        else if (choice == 4) {
            removeRecipe(newRecipes, input);
        }
        else if (choice == 5) {
            searchIngredient(newRecipes, input);
        }
        else if (choice == 6) {
            saveBook(newRecipes, input);
        }
        else if (choice == 7) {
            getEfficiency(newRecipes);
        }
    }
}
