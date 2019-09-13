import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.lang.Math;
/**
 * A class that contains an array of Recipe objects, including methods that can be called upon from another program.
 */
public class RecipeBook {
    private Recipe[] mRecipeBook;
    private int mCount;
    /**
     * Takes a file given by the user and reads every line of the file, creating Recipe objects and putting them into an array.
     * @param fileName The name of the file containing the recipes, must be an existing file that is formatted correctly.
     */
    public RecipeBook(String fileName) {
        File textFile = new File(fileName);
        try {
            Scanner input = new Scanner(textFile);
            mCount = input.nextInt();
            int length = (int)Math.round(Math.pow(2,Math.ceil(Math.log((double)mCount)/Math.log(2))));
            mRecipeBook = new Recipe[length];
            input.nextLine();
            for (int i = 0; i < mCount; i++) {
                String line = input.nextLine();
                String[] recipeArray = line.split("\\|");
                String prepTimeS = recipeArray[3];
                String cookTimeS = recipeArray[4];
                int prepTime = Integer.parseInt(prepTimeS);
                int cookTime = Integer.parseInt(cookTimeS);
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
        return mCount;
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
        for(int i = startingIndex; i < mCount; i++) {
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

    private void resize() {
        Recipe[] temp = new Recipe[2*mRecipeBook.length];
        for (int i = 0; i < mRecipeBook.length; i++) {
            temp[i] = mRecipeBook[i];
        }
        mRecipeBook = temp;
    }

    /**
     * Adds a value to the end of the list, resizing if necessary.
     * @param v The value that is added to the list.
     */
    public void addLast(Recipe v) {
        if (mCount == mRecipeBook.length) {
            resize();
        }
        mRecipeBook[mCount++] = v;
    }

    /**
     * Adds a value to a specified index of the list, moving other items over and/or resizing if necessary.
     * @param index The index that the value is placed in the list.
     * @param v The value that is added to the list.
     */
    public void insert(int index, Recipe v) {
        if (mCount == mRecipeBook.length) {
            resize();
        }
        shiftRight(index);
        mRecipeBook[index] = v;
    }

    /**
     * Removes the value at a specified index in the list.
     * @param index The index at which the value is removed.
     */
    public void removeAt(int index){
        if (index >= mRecipeBook.length) {
            throw new IndexOutOfBoundsException("There is no recipe at that index");
        }
        shiftLeft(index);
    }

    /**
     * Searches through the list of recipe titles for the specified title, returning the index if it is found.
     * @param title The recipe title that is being searched for in the list of recipes.
     * @return The index of the found recipe, or -1 if no recipe is found in the list with the specified title.
     */
    public int findRecipeTitle(String title) {
        for (int i = 0; i < mCount; i++) {
            if(title.equals(mRecipeBook[i].getTitle())) {
                return i;
            }
        }
        return -1;
    }

    private void shiftRight(int index) {
        if (mCount == mRecipeBook.length) {
            resize();
        }
        Recipe[] temp = new Recipe[mRecipeBook.length];
        for(int i = 0; i <= index; i++) {
            temp[i] = mRecipeBook[i];
        }
        for(int i = index; i < mCount; i++) {
            temp[i + 1] = mRecipeBook[i];
        }
        mCount++;
        mRecipeBook = temp;
    }

    private void shiftLeft(int index) {
        Recipe[] temp = new Recipe[mRecipeBook.length];
        for(int i = 0; i < index; i++) {
            temp[i] = mRecipeBook[i];
        }
        for(int i = index; i < mCount - 1; i++) {
            temp[i] = mRecipeBook[i + 1];
        }
        mCount--;
        mRecipeBook = temp;
    }
}
