import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RecipeSet {
    private class Node {
        private Recipe mData;
        private Node mLeft;
        private Node mRight;
        private Node mParent;
        private Node(Recipe r) {
            mData = r;
        }
        private void setLeft(Node n) {
            mLeft = n;
        }
        private void setRight(Node n) {
            mRight = n;
        }
        private void setParent(Node n) {
            mParent = n;
        }
    }
    private Node mRoot;
    private int mCount;

    /**
     * Takes a file given by the user and reads every line of the file, creating Recipe objects and putting them into an array.
     * @param fileName The name of the file containing the recipes, must be an existing file that is formatted correctly.
     */
    public RecipeSet(String fileName) {
        File textFile = new File(fileName);
        try {
            Scanner input = new Scanner(textFile);
            int newCount = input.nextInt();
            input.nextLine();
            for (int i = 0; i < newCount; i++) {
                String line = input.nextLine();
                String[] recipeArray = line.split("\\|");
                String prepTimeS = recipeArray[3];
                String cookTimeS = recipeArray[4];
                int prepTime = Integer.parseInt(prepTimeS);
                int cookTime = Integer.parseInt(cookTimeS);
                String[] recipeIngredients = recipeArray[5].split("@");
                String[] recipeSteps = recipeArray[6].split("@");
                Recipe newRecipe = new Recipe(recipeArray[0], recipeArray[1], prepTime, cookTime, recipeIngredients, recipeSteps, recipeArray[2]);
                add(newRecipe);
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("oops");
        }
    }

    /**
     * Gets the amount of recipes in the array
     */
    public int getCount() {
        return mCount;
    }

    /**
     * Finds whether or not there is a recipe with the given name.
     * @param recipeName The title of the recipe that is being looked for.
     * @return A boolean describing whether or not a recipe with that title was found.
     */
    public Recipe findRecipe(String recipeName) {
        Node recipeNode = find(recipeName, mRoot);
        if (recipeNode != null) {
            return recipeNode.mData;
        }
        return null;
    }

    private Node find(String recipeName, Node n) {
        if (n == null) {
            return null;
        }
        if (n.mData.getTitle().equals(recipeName)) {
            return n;
        }
        if (n.mData.getTitle().compareTo(recipeName) < 0) {
            return find(recipeName, n.mRight);
        }
        return find(recipeName, n.mLeft);
    }


    /**
     * Adds the given recipe.
     * @param recipe The recipe being added.
     */
    public void add(Recipe recipe) {
        if (findRecipe(recipe.getTitle()) != null) {
            return;
        }
        else if (mCount == 0) {
            Node newNode = new Node(recipe);
            mRoot = newNode;
            mCount++;
        }
        else {
            add(recipe, mRoot);
        }
    }

    private void add(Recipe recipe, Node n) {
        if (n.mData.getTitle().compareTo(recipe.getTitle()) < 0 && n.mRight == null) {
            Node newNode = new Node(recipe);
            n.setRight(newNode);
            newNode.setParent(n);
            mCount++;
        }
        else if (n.mData.getTitle().compareTo(recipe.getTitle()) > 0 && n.mLeft == null) {
            Node newNode = new Node(recipe);
            n.setLeft(newNode);
            newNode.setParent(n);
            mCount++;
        }
        else {
            if (n.mData.getTitle().compareTo(recipe.getTitle()) < 0) {
                add(recipe, n.mRight);
            }
            else if (n.mData.getTitle().compareTo(recipe.getTitle()) > 0) {
                add(recipe, n.mLeft);
            }
        }
    }

    /**
     * Gets the height of the tree.
     */
    public int getHeight() {
        return getHeight(mRoot);
    }

    private int getHeight(Node n) {
        if (n == null) {
            return 0;
        }
        int leftHeight = getHeight(n.mLeft);
        int rightHeight = getHeight(n.mRight);
        if (leftHeight > rightHeight) {
            return leftHeight + 1;
        }
        return rightHeight + 1;
    }

    /**
     * Removes the recipe with the given title from the tree.
     * @param recipeName The title of the recipe being removed.
     */
    public void remove(String recipeName) {
        Node foundNode = find(recipeName, mRoot);
        if (foundNode == null) {
            System.out.println("That recipe wasn't found.");
        }
        else {
            if (foundNode.mLeft == null && foundNode.mRight == null) {
                if (foundNode == mRoot) {
                    mRoot = null;
                }
                else if (foundNode == foundNode.mParent.mLeft) {
                    foundNode.mParent.mLeft = null;
                }
                else {
                    foundNode.mParent.mRight = null;
                }

                mCount--;
            }
            else if (foundNode.mLeft == null || foundNode.mRight == null) {
                if (foundNode.mLeft != null) {
                    if (foundNode == mRoot) {
                        mRoot = foundNode.mLeft;
                    }
                    else {
                        foundNode.mParent.setRight(foundNode.mLeft);
                        foundNode.mLeft.setParent(foundNode.mParent);
                    }
                }
                else {
                    if (foundNode == mRoot) {
                        mRoot = foundNode.mRight;
                    }
                    else {
                        foundNode.mParent.setRight(foundNode.mRight);
                        foundNode.mRight.setParent(foundNode.mParent);
                    }
                }
                mCount--;
            }
            else {
                Node largestNode = findLargestNode(foundNode.mLeft);
                remove(largestNode.mData.getTitle());
                foundNode.mData = largestNode.mData;
            }
        }
    }

    private Node findLargestNode(Node n) {
        if (n.mRight == null) {
            return n;
        }
        return findLargestNode(n.mRight);
    }

    /**
     * Finds the recipes in the tree with the given ingredient.
     * @param ingredient The ingredient being searched for.
     * @return A RecipeBook that contains all of the recipes that include the given ingredient.
     */
    public RecipeBook findIngredient(String ingredient) {
        RecipeBook ingredientRecipes = new RecipeBook();
        ingredientRecipes = inOrderTraverse(mRoot, ingredientRecipes, ingredient);
        return ingredientRecipes;
    }

    private RecipeBook inOrderTraverse(Node n, RecipeBook ingredientList, String ingredient) {
        if (n.mLeft != null) {
            inOrderTraverse(n.mLeft, ingredientList, ingredient);
        }
        if (n.mData.usesIngredient(ingredient)) {
            ingredientList.addLast(n.mData);
        }
        if (n.mRight != null) {
            inOrderTraverse(n.mRight, ingredientList, ingredient);
        }
        return ingredientList;
    }

    /**
     * Creates a RecipeBook will all of the recipes inside of it.
     */
    public RecipeBook getAllRecipes() {
        if (mCount == 0) {
            return null;
        }
        RecipeBook allRecipes = new RecipeBook();
        return preTraverse(mRoot, allRecipes);
    }

    private RecipeBook preTraverse(Node n, RecipeBook ingredientList) {
        ingredientList.addLast(n.mData);
        if (n.mLeft != null) {
            preTraverse(n.mLeft, ingredientList);
        }
        if (n.mRight != null) {
            preTraverse(n.mRight, ingredientList);
        }
        return ingredientList;
    }
}
