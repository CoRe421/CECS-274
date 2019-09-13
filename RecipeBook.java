import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
/**
 * A class that contains an array of Recipe objects, including methods that can be called upon from another program.
 */
public class RecipeBook {
    private Recipe[] mRecipeBook;

    /**
     * Takes a file given by the user and reads every line of the file, creating Recipe objects and putting them into an array.
     * @param fileName The name of the file containing the recipes, must be an existing file that is formatted correctly.
     */
    public RecipeBook(String fileName) {
        File textFile = new File(fileName);
        try {
            Scanner input = new Scanner(textFile);
            mRecipeBook = new Recipe[input.nextInt()];
            input.nextLine();
            for (int i = 0; i < mRecipeBook.length; i++) {
                String line = input.nextLine();
                String[] recipeArray = line.split("\\|");
                int prepTime = Integer.parseInt(recipeArray[3]);
                int cookTime = Integer.parseInt(recipeArray[4]);
                String[] recipeIngredients = recipeArray[5].split("@");
                String[] recipeSteps = recipeArray[6].split("@");
                Recipe newRecipe = new Recipe(recipeArray[0], recipeArray[1], prepTime, cookTime, recipeIngredients, recipeSteps, recipeArray[2]);
                mRecipeBook[i] = newRecipe;
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("oops");
        }
    }

    /**
     * Gets the amount of recipes in the array.
     */
    public int getRecipeCount() {
        return mRecipeBook.length;
    }

    /**
     * Gets the recipe at a certain index.
     * @param index The index at which the recipe is in the array.
     */
    public Recipe getRecipe(int index) {
        if (index > mRecipeBook.length) {
            throw new ArrayIndexOutOfBoundsException("That index does not exist");
        }
        return mRecipeBook[index];
    }

    /**
     * Finds the recipes that include a certain ingredient, starting at either a certain index or from the beginning.
     * @param ingredient The ingredient that is being searched for.
     * @param startingIndex The index at which the user wants to start looking for the ingredient.
     * @return The index of the first recipe that contain the ingredient starting from the user's index, or -1 if no Recipe uses the ingredient.
     */
    public int findIngredient(String ingredient, int startingIndex) {
        System.out.println("test");
        for(int i = startingIndex; i < mRecipeBook.length; i++) {
            if(mRecipeBook[i].usesIngredient(ingredient)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the recipes that include a certain ingredient, starting at 0.
     * @param ingredient The ingredient that is being searched for.
     * @return The index of all the recipes that contain the ingredient.
     */
    public int findIngredient(String ingredient) {
        return findIngredient(ingredient, 0);
    }
}
