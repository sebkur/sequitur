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

package info.sequitur.decode;

import info.sequitur.algorithm.NonTerminal;
import info.sequitur.algorithm.Rule;
import info.sequitur.algorithm.Sequitur;
import info.sequitur.algorithm.Symbol;

public class Decoder
{
	public static String decode(Sequitur sequitur)
	{
		StringBuilder text = new StringBuilder();

		Rule firstRule = sequitur.getFirstRule();

		decode(text, firstRule);

		return text.toString();
	}

	private static void decode(StringBuilder text, Rule rule)
	{
		for (Symbol s = rule.first(); (!s.isGuard()); s = s.getNext()) {
			decode(text, s);
		}
	}

	private static void decode(StringBuilder text, Symbol symbol)
	{
		if (symbol.isNonTerminal()) {
			NonTerminal nt = (NonTerminal) symbol;
			Rule rule = nt.getRule();
			decode(text, rule);
		} else {
			text.append((char) symbol.getValue());
		}
	}
}
