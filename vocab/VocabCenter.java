package vocab;

import menu.Menu;
import menu.MenuInput;

//-----------------------------------------------------
//Assignment 2
//Written by: Boni, Junior (40287501), 
//Talar Mustafa (40284214)
//
//-----------------------------------------------------

/**
 * Driver
 */
public class VocabCenter {
	private static Menu currentMenu;

	public static void main(String[] args) {
		currentMenu = Menu.configMenu();
		int option = 0;
		do {
			currentMenu.displayMenu();
			System.out.print("Enter Your Choice: ");
			option = MenuInput.promptOption(9);
			currentMenu.execute(option);
		} while (option != 0);
	}
}
