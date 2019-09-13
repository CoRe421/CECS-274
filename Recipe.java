/**
 * A class that contains the information for a recipe, including methods that can be called from other programs.
 */
public class Recipe {
    private String mTitle;
    private String mAuthor;
    private int mPrepTime;
    private int mCookingTime;
    private String[] mIngredients;
    private String[] mSteps;
    private String mDescription;

    /**
     * Constructs a new Recipe with the given parameters.
     * @param title The title of the new recipe.
     * @param author The author of the recipe.
     * @param prepTime The amount of time to prep the new recipe.
     * @param cookingTime The amount of time to cook the new recipe.
     * @param ingredients The array of ingredients for the new recipe.
     * @param steps The array of steps for the new recipe.
     * @param description The description of the recipe.
     */
    public Recipe(String title, String author, int prepTime, int cookingTime, String[] ingredients, String[] steps, String description) {
        mTitle = title;
        mAuthor = author;
        mPrepTime = prepTime;
        mCookingTime = cookingTime;
        mIngredients = ingredients;
        mSteps = steps;
        mDescription = description;
    }

    /**
     * Gets the title of the recipe.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Gets the author of the recipe.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Gets the prep time of the recipe.
     */
    public int getPrepTime() {
        return mPrepTime;
    }

    /**
     * Gets the cooking time of the recipe.
     */
    public int getCookingTime() {
        return mCookingTime;
    }

    /**
     * Gets the array of ingredients for the recipe.
     */
    public String[] getIngredients() {
        return mIngredients;
    }

    /**
     * Gets the array of steps for the recipe.
     */
    public String[] getSteps() {
        return mSteps;
    }

    /**
     * Gets the description of the recipe.
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Changes the title of the recipe.
     * @param newTitle The new title of the recipe.
     */
    public void setTitle(String newTitle) {
        mTitle = newTitle;
    }

    /**
     * Changes the author of the recipe.
     * @param newAuthor The new author of the recipe.
     */
    public void setAuthor(String newAuthor) {
        mAuthor = newAuthor;
    }

    /**
     * Returns a description of the recipe, including all variables.
     */
    public String getDetails() {
        String prepMinutePlurality = " minutes";
        String cookMinutePlurality = " minutes";
        if (mPrepTime == 1) {
            prepMinutePlurality = " minute";
        }
        if (mCookingTime == 1) {
            cookMinutePlurality = " minute";
        }
        String fullRecipe = mTitle + "\n" + mDescription + "\n" + "by " + mAuthor + "\n" + mPrepTime + prepMinutePlurality +
                " prep time, " + mCookingTime + cookMinutePlurality + " cook time\n" + "Ingredients:\n";
        for(int i = 0; i < mIngredients.length; i++) {
            fullRecipe += "* " + mIngredients[i] + "\n";
        }
        fullRecipe += "Steps:\n";
        for(int i = 0; i < mSteps.length; i++) {
            fullRecipe += (i + 1) + ". " + mSteps[i] + "\n";
        }
        return fullRecipe;
    }

    /**
     * Returns a small concise description of the recipe.
     */
    public String toString() {
        String pluralMinutes = " minutes";
        if (mPrepTime + mCookingTime == 1) {
            pluralMinutes = " minute";
        }
        return mTitle + " - " + mDescription + " - " + (mPrepTime + mCookingTime) + pluralMinutes + " time";
    }

    /**
     * Returns the sum of the cooking time and prep time for the recipe.
     */
    public int getTotalTime() {
        return mCookingTime + mPrepTime;
    }

    /**
     * Returns the length of the mSteps array.
     */
    public int getNumberOfSteps() {
        return mSteps.length;
    }

    /**
     * Creates a string in list form of the ingredients of the recipe.
     * @return The string that includes all of the elements in the array of ingredients.
     */
    public String getShoppingList() {
        String shoppingList = "";
        for(int i = 0; i < mIngredients.length; i++) {
            if (i + 1 == mIngredients.length) {
                shoppingList += mIngredients[i];
            }
            else {
                shoppingList += mIngredients[i] + ", ";
            }
        }
        return shoppingList;
    }

    /**
     * Determines if the list of ingredients contains the specified ingredient.
     * @param ingredient The ingredient that you wish to find in the recipe.
     * @return Returns true only if the ingredient parameter was found somewhere in the ingredients array.
     */
    public boolean usesIngredient(String ingredient) {
        for(int i = 0; i < mIngredients.length; i++) {
            if (mIngredients[i].toLowerCase().contains(ingredient.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
