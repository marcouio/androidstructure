package com.molinari.androidstructure.command;


public abstract class AbstractCommand implements ICommand {

	public boolean doCommand() {
		if (execute()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean undoCommand() {
		if (unExecute()) {
			return true;
		} else {
			return false;
		}
	}

	public abstract boolean execute();

	public abstract boolean unExecute();

}
