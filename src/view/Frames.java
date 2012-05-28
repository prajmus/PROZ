package view;

/**
 * Interface for different views
 * @author Jakub Borowski
 *
 */

public interface Frames{
	/**
	 * Prepares and displays top menu
	 */
	public abstract void prepareMenu();
	/**
	 * Prepares and displays the rest of the window
	 */
	public abstract void prepareFrame();
}
