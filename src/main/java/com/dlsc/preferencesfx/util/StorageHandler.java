package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_CATEGORY;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_DIVIDER_POSITION;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_POS_X;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_POS_Y;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_WIDTH;
import static com.dlsc.preferencesfx.PreferencesFx.DIVIDER_POSITION;
import static com.dlsc.preferencesfx.PreferencesFx.SELECTED_CATEGORY;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_POS_X;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_POS_Y;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_WIDTH;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.Group;
import com.dlsc.preferencesfx.Setting;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;

public class StorageHandler {

  private Preferences preferences;

  public StorageHandler(Class<?> saveClass) {
    preferences = Preferences.userNodeForPackage(saveClass);
  }

  /**
   * Stores the last selected category in TreeView.
   *
   * @param categoryId the category id to be stored
   */
  public void putSelectedCategory(int categoryId) {
    preferences.putInt(SELECTED_CATEGORY, categoryId);
  }

  /**
   * Gets the last selected category in TreeView.
   *
   * @return the index of the selected category. 0 if none is found
   */
  public int getSelectedCategory() {
    return preferences.getInt(SELECTED_CATEGORY, DEFAULT_CATEGORY);
  }

  /**
   * Stores the given divider position of the MasterDetailPane.
   *
   * @param dividerPosition the divider position to be stored
   */
  public void putDividerPosition(double dividerPosition) {
    preferences.putDouble(DIVIDER_POSITION, dividerPosition);
  }

  /**
   * Gets the stored divider position of the MasterDetailPane.
   *
   * @return the double value of the divider position. 0.2 if none is found
   */
  public double getDividerPosition() {
    return preferences.getDouble(DIVIDER_POSITION, DEFAULT_DIVIDER_POSITION);
  }

  /**
   * Stores the window width of the PreferencesDialog.
   *
   * @param windowWidth the width of the window to be stored
   */
  public void putWindowWidth(double windowWidth) {
    preferences.putDouble(WINDOW_WIDTH, windowWidth);
  }

  /**
   * Searches for the window width of the PreferencesDialog.
   *
   * @return the double value of the window width. 1000 if none is found
   */
  public double getWindowWidth() {
    return preferences.getDouble(WINDOW_WIDTH, DEFAULT_PREFERENCES_WIDTH);
  }

  /**
   * Stores the window height of the PreferencesDialog.
   *
   * @param windowHeight the height of the window to be stored
   */
  public void putWindowHeight(double windowHeight) {
    preferences.putDouble(WINDOW_HEIGHT, windowHeight);
  }

  /**
   * Searches for the window height of the PreferencesDialog.
   *
   * @return the double value of the window height. 700 if none is found
   */
  public double getWindowHeight() {
    return preferences.getDouble(WINDOW_HEIGHT, DEFAULT_PREFERENCES_HEIGHT);
  }

  /**
   * Stores the position of the PreferencesDialog in horizontal orientation.
   *
   * @param windowPosX the double value of the window position in horizontal orientation
   */
  public void putWindowPosX(double windowPosX) {
    preferences.putDouble(WINDOW_POS_X, windowPosX);
  }

  /**
   * Searches for the horizontal window position.
   *
   * @return the double value of the horizontal window position
   */
  public double getWindowPosX() {
    return preferences.getDouble(WINDOW_POS_X, DEFAULT_PREFERENCES_POS_X);
  }

  /**
   * Stores the position of the PreferencesDialog in vertical orientation.
   *
   * @param windowPosY the double value of the window position in vertical orientation
   */
  public void putWindowPosY(double windowPosY) {
    preferences.putDouble(WINDOW_POS_Y, windowPosY);
  }

  /**
   * Searches for the vertical window position.
   *
   * @return the double value of the vertical window position
   */
  public double getWindowPosY() {
    return preferences.getDouble(WINDOW_POS_Y, DEFAULT_PREFERENCES_POS_Y);
  }

  private List<Category> reworkedCategories = FXCollections.observableArrayList();

  public List<Category> setupPreferenceProperties(List<Category> categories) {
    categories.forEach(category -> reworkedCategories.add(getRecursiveCategory(category)));
    return reworkedCategories;
  }

  private Category getRecursiveCategory(Category category) {
    String breadCrumbString = "";
    breadCrumbString += category.getDescription();
    Category newCategory = Category.of(category.getDescription());

    for (Group group : category.getGroups()) {
      breadCrumbString += group.getDescription();
      Group newGroup = Group.of(group.getDescription());
      newCategory.getGroups().add(newGroup);

      for (Setting setting : group.getSettings()) {
        breadCrumbString += setting.getDescription();
        newGroup.getSettings().add(Setting.of(setting.getDescription(), preferences.get(breadCrumbString, setting.valueProperty())));
      }
    }

    if (!Objects.equals(category.getChildren(), null)) {
      category.getChildren().forEach(category1 ->
          newCategory.subCategories(getRecursiveCategory(category1))
      );
    }
    return newCategory;
  }
}
