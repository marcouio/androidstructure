package com.molinari.androidstructure.command;

public interface ICommand {

	/**
	 * Metodo implementato dai singoli comandi per eseguire la specifica
	 * operazione
	 * 
	 * @return true se il comando è andato a buon fine
	 */
	public boolean execute();

	/**
	 * Metodo implementato dai singoli comandi per eseguire l'undo della
	 * specifica operazione
	 * 
	 * @return true se il comando è andato a buon fine
	 */
	public boolean unExecute();

	/**
	 * Metodo implementato all'interno di AbstractCommand, richiama l'unExecute
	 * del comando
	 * 
	 * @return
	 */
	public boolean undoCommand();

	/**
	 * Metodo implementato all'interno di AbstractCommand, richiama l'execute
	 * del comando
	 * 
	 * @return
	 */
	public boolean doCommand();

}
