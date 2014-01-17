package com.molinari.androidstructure.command;

import java.util.ArrayList;

public class MacroCommand extends AbstractCommand {
	
	ArrayList<AbstractCommand> listaComandiInterna = new ArrayList<AbstractCommand>();
	private int indiceCorrente = 0;
	
	public void add(final AbstractCommand comando) {
		listaComandiInterna.add(comando);
	}
	
	public void add(final int index, final AbstractCommand comando) {
		listaComandiInterna.add(index, comando);
	}
	
	public void remove(final int index) {
		listaComandiInterna.remove(index);
	}
	
	public void remove(final AbstractCommand comando){
		listaComandiInterna.remove(comando);
	}
	
	@Override
	public boolean execute() {
		boolean ok = true;
		for (ICommand comando : listaComandiInterna) {
			if(!comando.execute()){
				ok = false;
				break;
			}else{
				indiceCorrente++;
			}
		}
		return ok;
	}

	@Override
	public boolean unExecute() {
		boolean ok = true;
		for (ICommand comando : listaComandiInterna) {
			if(!comando.unExecute()){
				ok = false;
				break;
			}else{
				indiceCorrente--;
			}
		}
		return ok;
	}

	public ArrayList<AbstractCommand> getListaComandiInterna() {
		return listaComandiInterna;
	}
	
	public int getIndiceCorrente() {
		return indiceCorrente;
	}
}
