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

public class NonTerminal extends Symbol implements Cloneable
{

	Rule r;

	NonTerminal(Sequitur sequitur, Rule rule)
	{
		super(sequitur);
		r = rule;
		r.count++;
		value = numTerminals + r.number;
		p = null;
		n = null;
	}

	public Rule getRule()
	{
		return r;
	}

	/**
	 * Extra cloning method necessary so that count in the corresponding rule is
	 * increased.
	 */

	@Override
	protected Object clone()
	{
		NonTerminal sym = new NonTerminal(sequitur, r);

		sym.p = p;
		sym.n = n;
		return sym;
	}

	@Override
	public void cleanUp()
	{
		sequitur.join(p, n);
		deleteDigram();
		r.count--;
	}

	@Override
	public boolean isNonTerminal()
	{
		return true;
	}

	/**
	 * This symbol is the last reference to its rule. The contents of the rule
	 * are substituted in its place.
	 */

	public void expand()
	{
		sequitur.emitPreUnderusedRule(this);

		// I think this one is missing in the original implementation!
		deleteDigram();

		sequitur.join(p, r.first());
		sequitur.join(r.last(), n);

		// Bug fix (21.8.2012): digram consisting of the last element of
		// the inserted rule and the first element after the inserted rule
		// must be put into the hash table (Simon Schwarzer)

		sequitur.emitInsertDigram(r.last());
		sequitur.getDigrams().put(r.last(), r.last());

		// Necessary so that garbage collector
		// can delete rule and guard.

		r.guard.r = null;
		r.guard = null;

		sequitur.emitUnderusedRule(this);
	}

}
