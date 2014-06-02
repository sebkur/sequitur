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

package info.sequitur.cli;

import info.sequitur.algorithm.DebugCallback;
import info.sequitur.algorithm.NonTerminal;
import info.sequitur.algorithm.Rule;
import info.sequitur.algorithm.Sequitur;
import info.sequitur.algorithm.Symbol;
import info.sequitur.util.NamingOrder;
import info.sequitur.util.NamingStrategy;
import info.sequitur.util.RulePrinter;

public class RunSequiturStepwise implements DebugCallback
{
	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println("usage: "
					+ RunSequiturStepwise.class.getSimpleName() + " <input>");
			System.exit(1);
		}

		String input = args[0];

		RunSequiturStepwise stepwise = new RunSequiturStepwise();
		stepwise.execute(input);
	}

	private Sequitur sequitur;

	public RunSequiturStepwise()
	{
		sequitur = new Sequitur(this);
	}

	String headline = "********************************************************************************";

	private void execute(String input)
	{
		StringBuilder soFar = new StringBuilder();

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			soFar.append(c);

			RulePrinter printer = createPrinter();
			System.out.println(headline);
			System.out.println(printer.getTable());

			System.out.println(headline);
			System.out.println("Next character: '" + c + "', processed: '"
					+ soFar + "'");
			System.out.println(headline);
			System.out.println("* "
					+ printer.getProduction(sequitur.getFirstRule()) + c);

			sequitur.process(c);
		}
	}

	private RulePrinter createPrinter()
	{
		RulePrinter printer = new RulePrinter(sequitur,
				NamingStrategy.USE_LETTERS_START_WITH_S,
				NamingOrder.BY_CREATION);
		return printer;
	}

	@Override
	public void preReuseRule(Rule rule)
	{
		RulePrinter printer = createPrinter();
		System.out.println(" > reuse: " + printer.getProduction(rule));
	}

	@Override
	public void reuseRule(Rule rule)
	{
		RulePrinter printer = createPrinter();
		System.out.println();
		System.out.println(printer.getTable());
		System.out.println();
	}

	@Override
	public void preCreateRule()
	{
		// ignore
	}

	@Override
	public void createRule(Rule rule)
	{
		RulePrinter printer = createPrinter();

		System.out.println(" > Create production: "
				+ printer.getProduction(rule));
		System.out.println();
		System.out.println(printer.getTable());
		System.out.println();
	}

	@Override
	public void preUnderusedRule(NonTerminal nonTerminal)
	{
		// ignore
	}

	@Override
	public void underusedRule(NonTerminal nonTerminal)
	{
		RulePrinter printer = createPrinter();

		System.out.println("* Underused production: "
				+ printer.getName(nonTerminal.getRule()));
		System.out.println();

		System.out.println(printer.getTable());
	}

	@Override
	public void lookForDigram(Symbol symbol)
	{
		RulePrinter printer = createPrinter();
		System.out.println("* look for digram: " + printer.getSymbol(symbol)
				+ printer.getSymbol(symbol.getNext()));
	}

	@Override
	public void digramNotFound()
	{
		System.out.println(" > Digram not found");
	}

}
