/*
 This class is part of a Java port of Craig Nevill-Manning's Sequitur algorithm.
 Copyright (C) 1997 Eibe Frank
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

package info.sequitur.algorithm;

import java.util.Hashtable;

public class Sequitur
{

	public static Rule run(String input)
	{
		Sequitur sequitur = new Sequitur();
		return sequitur.reallyRun(input);
	}

	// The total number of rules.
	private int numRules = 0;
	// The hash table for the digrams
	private Hashtable<Symbol, Symbol> theDigrams = new Hashtable<Symbol, Symbol>(
			Symbol.prime);

	private Rule reallyRun(String input)
	{
		Rule firstRule = new Rule(this);

		// Reset number of rules and Hashtable.

		numRules = 0;
		theDigrams.clear();
		for (int i = 0; i < input.length(); i++) {
			firstRule.last().insertAfter(new Terminal(this, input.charAt(i)));
			firstRule.last().p.check();
		}
		return firstRule;
	}

	/**
	 * Links two symbols together, removing any old digram from the hash table.
	 */

	public void join(Symbol left, Symbol right)
	{
		if (left.n != null) {
			left.deleteDigram();

			// Bug fix (21.8.2012): included two if statements, adapted from
			// sequitur_simple.cc, to deal with triples

			if ((right.p != null) && (right.n != null)
					&& right.value == right.p.value
					&& right.value == right.n.value) {
				theDigrams.put(right, right);
			}

			if ((left.p != null) && (left.n != null)
					&& left.value == left.n.value && left.value == left.p.value) {
				theDigrams.put(left.p, left.p);
			}
		}

		left.n = right;
		right.p = left;
	}

	public int getNumRules()
	{
		return numRules;
	}

	public void setNumRules(int numRules)
	{
		this.numRules = numRules;
	}

	public Hashtable<Symbol, Symbol> getDigrams()
	{
		return theDigrams;
	}

}
