/*
 This class is part of a Java port of Craig Nevill-Manning's Sequitur algorithm.
 Copyright (C) 2014 Sebastian Kuerten

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package info.sequitur.ui.presentation;

import info.sequitur.algorithm.DebugCallback;
import info.sequitur.algorithm.NonTerminal;
import info.sequitur.algorithm.Rule;
import info.sequitur.algorithm.Sequitur;
import info.sequitur.algorithm.Symbol;
import info.sequitur.ui.presentation.state.SimpleState;
import info.sequitur.ui.presentation.state.State;
import info.sequitur.util.NamingOrder;
import info.sequitur.util.NamingStrategy;
import info.sequitur.util.RulePrinter;

import java.util.ArrayList;
import java.util.List;

public class Model implements DebugCallback
{

	private String input;

	private int length;

	private int currentChar = 0;

	private int currentNumberOfSteps = 0;
	private int currentStep = 0;

	private Sequitur sequitur;

	private List<List<State>> actions = new ArrayList<List<State>>();

	private List<State> currentAction;

	private List<ModelChangeListener> listeners = new ArrayList<ModelChangeListener>();

	public Model(String input)
	{
		this.input = input;
		length = input.length();

		for (int i = 0; i < length; i++) {
			actions.add(new ArrayList<State>());
		}

		sequitur = new Sequitur(this);
		currentAction = actions.get(0);
		pushSimple("Init");
		for (int i = 0; i < length; i++) {
			currentAction = actions.get(i);
			sequitur.process(input.charAt(i));
		}

		// System.out.println("sizes:");
		// for (int i = 0; i < length; i++) {
		// System.out.println(i + ": " + actions.get(i).size());
		// }

		currentChar = 0;
		currentStep = 0;
		currentAction = actions.get(0);
		currentNumberOfSteps = currentAction.size();
	}

	public int getLength()
	{
		return length;
	}

	public void addModelChangedListener(ModelChangeListener listener)
	{
		listeners.add(listener);
	}

	public void removeModelChangedListener(ModelChangeListener listener)
	{
		listeners.remove(listener);
	}

	private void triggerModelChanged()
	{
		for (ModelChangeListener listener : listeners) {
			listener.modelChanged();
		}
	}

	private void pushSimple(String message)
	{
		currentAction.add(createSimpleState(message));
	}

	private State createSimpleState(String message)
	{
		RulePrinter printer = createPrinter();
		String table = printer.getTable();
		String digrams = printer.getDigramList();
		State state = new SimpleState(message, table, digrams);
		return state;
	}

	private RulePrinter createPrinter()
	{
		return new RulePrinter(sequitur,
				NamingStrategy.USE_LETTERS_START_WITH_S,
				NamingOrder.BY_CREATION, "Verwendung\tProduktion\n");
	}

	@Override
	public void lookForDigram(Symbol symbol)
	{
		RulePrinter printer = createPrinter();
		pushSimple("Suche Bigramm: " + printer.getSymbol(symbol)
				+ printer.getSymbol(symbol.getNext()));
	}

	@Override
	public void digramNotFound()
	{
		pushSimple("Nicht gefunden");
	}

	@Override
	public void preCreateRule()
	{
		pushSimple("Erstelle Produktion");
	}

	@Override
	public void preReuseRule(Rule rule)
	{
		pushSimple("Verwende bestehende Produktion wieder");
	}

	@Override
	public void reuseRule(Rule rule)
	{
		pushSimple("Bestehende Produktion wiederverwendet");
	}

	@Override
	public void createRule(Rule rule)
	{
		RulePrinter printer = createPrinter();
		pushSimple("Erstelle Produktion: " + printer.getProduction(rule));
	}

	@Override
	public void preUnderusedRule(NonTerminal nonTerminal)
	{
		RulePrinter printer = createPrinter();
		pushSimple("Eliminiere unterverwendete Produktion: "
				+ printer.getProduction(nonTerminal.getRule()));
	}

	@Override
	public void underusedRule(NonTerminal nonTerminal)
	{
		RulePrinter printer = createPrinter();
		pushSimple("Unterverwendete Produktion eliminiert: "
				+ printer.getName(nonTerminal.getRule()));
	}

	public Sequitur getCurrentSequitur()
	{
		Sequitur sequitur = new Sequitur();
		sequitur.process(input.substring(0, currentChar));
		return sequitur;
	}

	public int getCurrentChar()
	{
		return currentChar;
	}

	public void setCurrentChar(int currentChar)
	{
		this.currentChar = currentChar;
		this.currentStep = 0;
		currentAction = actions.get(currentChar);
		currentNumberOfSteps = currentAction.size();
		triggerModelChanged();
	}

	public int getCurrentStep()
	{
		return currentStep;
	}

	public void setCurrentStep(int currentStep)
	{
		this.currentStep = currentStep;
		triggerModelChanged();
	}

	public int getCurrentNumberOfSteps()
	{
		return currentNumberOfSteps;
	}

	public State getCurrentState()
	{
		System.out.println(currentChar + "," + currentStep);
		return currentAction.get(currentStep);
	}
}
