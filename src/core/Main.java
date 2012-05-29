package core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import view.View;
import model.Model;
import controller.Controller;
import controller.WordEvent;

/**
 * Main class responsible for launching the whole application. Creates instances of Model, View and Controller. Controller is in new thread
 * @author Jakub Borowski
 */

public class Main
{
	/**
	 * main function
	 * @param args		runnable arguments
	 */
	public static void main(String[] args)
	{
		final int QUEUE_MAX = 1024;
		final BlockingQueue<WordEvent> eventQueue = new LinkedBlockingQueue<WordEvent>(QUEUE_MAX);
		Model model = new Model();
		View view = new View(eventQueue, model);
		final Controller controller = new Controller(eventQueue, model, view);
		final Thread controllerThread = new Thread() {
			public void run(){
				controller.start();
			}
		};
		controllerThread.run();
	}
}
