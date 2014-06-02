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

public abstract class Symbol
{

	static final int numTerminals = 100000;

	static final int prime = 2265539;

	protected Sequitur sequitur;

	int value;
	Symbol p, n;

	public Symbol(Sequitur sequitur)
	{
		this.sequitur = sequitur;
	}

	public int getValue()
	{
		return value;
	}

	public Symbol getPrevious()
	{
		return p;
	}

	public Symbol getNext()
	{
		return n;
	}

	/**
	 * Abstract method: cleans up for symbol deletion.
	 */

	public abstract void cleanUp();

	/**
	 * Inserts a symbol after this one.
	 */

	public void insertAfter(Symbol toInsert)
	{
		sequitur.join(toInsert, n);
		sequitur.join(this, toInsert);
	}

	/**
	 * Removes the digram from the hash table. Overwritten in sub class guard.
	 */

	public void deleteDigram()
	{
		Symbol dummy;

		if (n.isGuard()) {
			return;
		}
		dummy = sequitur.getDigrams().get(this);

		// Only delete digram if its exactly
		// the stored one.

		if (dummy == this) {
			sequitur.getDigrams().remove(this);
		}
	}

	/**
	 * Returns true if this is the guard symbol. Overwritten in subclass guard.
	 */

	public boolean isGuard()
	{
		return false;
	}

	/**
	 * Returns true if this is a non-terminal. Overwritten in subclass
	 * nonTerminal.
	 */

	public boolean isNonTerminal()
	{
		return false;
	}

	/**
	 * Checks a new digram. If it appears elsewhere, deals with it by calling
	 * match(), otherwise inserts it into the hash table. Overwritten in
	 * subclass guard.
	 */

	public boolean check()
	{
		Symbol found;

		if (n.isGuard())
			return false;
		if (!sequitur.getDigrams().containsKey(this)) {
			found = sequitur.getDigrams().put(this, this);
			return false;
		}
		found = sequitur.getDigrams().get(this);
		if (found.n != this)
			match(this, found);
		return true;
	}

	/**
	 * Replace a digram with a non-terminal.
	 */

	public void substitute(Rule r)
	{
		cleanUp();
		n.cleanUp();
		p.insertAfter(new NonTerminal(sequitur, r));
		if (!p.check())
			p.n.check();
	}

	/**
	 * Deal with a matching digram.
	 */

	public void match(Symbol newD, Symbol matching)
	{
		Rule r;

		if (matching.p.isGuard() && matching.n.n.isGuard()) {
			// reuse an existing rule

			r = ((Guard) matching.p).r;
			newD.substitute(r);
		} else {
			// create a new rule

			r = new Rule(sequitur);
			try {
				Symbol first = (Symbol) newD.clone();
				Symbol second = (Symbol) newD.n.clone();
				r.guard.n = first;
				first.p = r.guard;
				first.n = second;
				second.p = first;
				second.n = r.guard;
				r.guard.p = second;

				matching.substitute(r);
				newD.substitute(r);

				// Bug fix (21.8.2012): moved the following line
				// to occur after substitutions (see sequitur_simple.cc)

				sequitur.getDigrams().put(first, first);
			} catch (CloneNotSupportedException c) {
				c.printStackTrace();
			}
		}

		// Check for an underused rule.

		if (r.first().isNonTerminal()
				&& (((NonTerminal) r.first()).r.count == 1)) {
			((NonTerminal) r.first()).expand();
		}
	}

	@Override
	public String toString()
	{
		if (n == null) {
			return "Symbol(" + value + "," + n + ")";
		}
		return "Symbol(" + value + "," + n.value + ")";
	}

	/**
	 * Produce the hashcode for a digram.
	 */

	@Override
	public int hashCode()
	{
		// Values in linear combination with two
		// prime numbers.

		long code = ((21599 * (long) value) + (20507 * (long) n.value));
		code = code % prime;
		return (int) code;
	}

	/**
	 * Test if two digrams are equal. WARNING: don't use to compare two symbols.
	 */

	@Override
	public boolean equals(Object obj)
	{
		return ((value == ((Symbol) obj).value) && (n.value == ((Symbol) obj).n.value));
	}
}
