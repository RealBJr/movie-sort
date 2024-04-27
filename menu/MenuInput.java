package menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class MenuInput {
	private static Scanner sc = new Scanner(System.in);

	public static String promptText() {
		return sc.next() + sc.nextLine();
	}

	/**
	 * Choose among the max options. Enforce valid input
	 * 
	 * @param maxOption, max number that the user can input
	 * @return
	 */
	public static int promptOption(int maxOption) {
		int chosen = 0;
		try {
			chosen = sc.nextInt();
			if (chosen < 0 || chosen > maxOption) {
				throw new InputMismatchException();
			}
		} catch (InputMismatchException e) {
			System.out.print("Please input a number between 0 and " + maxOption + ": ");
			sc.nextLine(); // flush the line
			return promptOption(maxOption);
		}
		return chosen;
	}

	/**
	 * Get the first character of the response
	 * 
	 * @param options
	 */
	public static char promptOption() {
		char chosen = sc.next().charAt(0);
		sc.nextLine();
		return chosen;
	}
}
