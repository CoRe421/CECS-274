import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A program that reads a file with a certain number of recipes, and creates an interface for the user to interact with in order to display or search through the recipes.
 */
public class RecipeProgram {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RecipeBook newRecipes = getFile(input);
        int choice = 0;
        while (choice != 7) {
            System.out.println("What would you like to do?");
            printMenu();
            System.out.println("Please enter a slection:");
            choice = input.nextInt();
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
        System.out.println("7. Exit");
    }

    /**
     * Prints out first the number of recipes in the list, and then each of the recipes in the list on seperate lines.
     * @param newRecipes The list of recipes that is printed out.
     */
    public static void showRecipes(RecipeBook newRecipes) {
        System.out.println("There are " + newRecipes.getRecipeCount() + " recipes");
        for (int i = 0; i < newRecipes.getRecipeCount(); i++) {
            System.out.println((i + 1) + ". " + newRecipes.getRecipe(i));
        }
        System.out.println();
    }

    /**
     * Prints the details of a specific recipe.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void showDetails(RecipeBook newRecipes, Scanner input) {
        System.out.println("What recipe would you like the details for?: ");
        int recipeNumber = input.nextInt();
        try {
            System.out.println(newRecipes.getRecipe(recipeNumber - 1).getDetails());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("There seems to be a problem, that recipe doesn't exist. \n");
        }
    }

    /**
     * Adds a recipe to the list of recipes.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void addRecipe(RecipeBook newRecipes, Scanner input) {
        System.out.println("Please enter the name of a recipe file: ");
        input.nextLine();
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
            System.out.println("Where would you like to add the recipe? (enter -1 to add it to the end of the list)");
            int userIndex = input.nextInt();
            if (userIndex == -1) {
                newRecipes.addLast(userRecipe);
            } else {
                newRecipes.insert(userIndex, userRecipe);
            }
            System.out.println(userRecipe.getTitle() + " - was added to list \n");
        }
        catch (FileNotFoundException ex) {
            System.out.println("Sorry, that file was not found \n");
        }
    }

    /**
     * Removes a recipe from the list of recipes.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void removeRecipe(RecipeBook newRecipes, Scanner input) {
        System.out.println("Enter a recipe name:");
        input.nextLine();
        String userTitle = input.nextLine();
        int titleIndex = newRecipes.findRecipeTitle(userTitle);
        if (titleIndex != -1) {
            System.out.println(newRecipes.getRecipe(titleIndex).getTitle() + " was removed from the recipe book. \n");
            newRecipes.removeAt(titleIndex);
        }
        else {
            System.out.println("No recipe was found in the list with that title. \n");
        }
    }

    /**
     * Searches for a specific ingredient in the list of recipes.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void searchIngredient(RecipeBook newRecipes, Scanner input) {
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

    /**
     * Saves the list of recipes to a new file.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user of the program.
     */
    public static void saveBook(RecipeBook newRecipes, Scanner input) {
        System.out.println("Enter a file name:");
        input.nextLine();
        String userFile = input.nextLine();
        File newRecipeFile = new File(userFile);
        try {
            PrintWriter output = new PrintWriter(new FileWriter(newRecipeFile));
            output.write(Integer.toString(newRecipes.getRecipeCount()));
            output.println();
            for (int i = 0; i < newRecipes.getRecipeCount(); i++) {
                String ingredientString = "";
                String stepString = "";
                String[] recipeIngredients = newRecipes.getRecipe(i).getIngredients();
                String[] recipeSteps = newRecipes.getRecipe(i).getSteps();
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
                output.println(newRecipes.getRecipe(i).getTitle() + "|" + newRecipes.getRecipe(i).getAuthor() + "|" + newRecipes.getRecipe(i).getDescription() + "|" + newRecipes.getRecipe(i).getPrepTime() + "|" + newRecipes.getRecipe(i).getCookingTime() + "|" + ingredientString + "|" + stepString);
            }
            output.close();
            System.out.println("Saved to " + userFile + "! \n");
        }
        catch (IOException ex) {
            System.out.println("Oops, that file doesn't exist \n");
        }
    }

    /**
     * Asks the user for the file that contains the list of recipes that will be used in the program.
     * @param input The object used to get input from the user of the program.
     */
    public static RecipeBook getFile(Scanner input) {
        System.out.println("Please enter the name of a recipe file: ");
        try {
            String fileName = input.nextLine(); //input.nextLine();
            RecipeBook newRecipes = new RecipeBook(fileName);
            return newRecipes;
        } catch (NoSuchElementException ex) {
            System.out.println("That file seems to not be formatted correctly");
            return null;
        }
    }

    /**
     * Gets the choice from the user of the program, and calls the corresponding method.
     * @param newRecipes The list of recipes that is being used.
     * @param input The object used to get input from the user.
     * @param choice The menu option that the user of the program selects.
     */
    public static void menuChoice(RecipeBook newRecipes, Scanner input, int choice) {
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
    }
}
