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

package info.sequitur.util;

import info.sequitur.algorithm.NonTerminal;
import info.sequitur.algorithm.Rule;
import info.sequitur.algorithm.Sequitur;
import info.sequitur.algorithm.Symbol;

import java.util.ArrayList;
import java.util.List;

public class SequiturUtil
{

	public static Rule run(String input)
	{
		Sequitur sequitur = new Sequitur();
		sequitur.process(input);
		return sequitur.getFirstRule();
	}

	public static String buildRuleTable(Sequitur sequitur,
			NamingStrategy strategy)
	{
		List<Rule> rules = new ArrayList<Rule>(sequitur.getNumRules());
		int processedRules = 0;
		StringBuilder text = new StringBuilder();

		text.append("Usage\tRule\n");
		rules.add(sequitur.getFirstRule());
		while (processedRules < rules.size()) {
			Rule currentRule = rules.get(processedRules);
			appendRow(text, rules, currentRule, processedRules, strategy);
			processedRules++;
		}
		return text.toString();
	}

	private static void appendRow(StringBuilder text, List<Rule> rules,
			Rule currentRule, int processedRules, NamingStrategy strategy)
	{
		text.append(" ");
		text.append(currentRule.count);
		text.append("\t");
		text.append(name(currentRule.number, strategy));
		text.append(" -> ");
		for (Symbol sym = currentRule.first(); (!sym.isGuard()); sym = sym
				.getNext()) {
			if (sym.isNonTerminal()) {
				Rule referedTo = ((NonTerminal) sym).getRule();
				if ((rules.size() <= referedTo.index)
						|| (rules.get(referedTo.index) != referedTo)) {
					referedTo.index = rules.size();
					rules.add(referedTo);
				}
				text.append("[");
				text.append(name(referedTo.number, strategy));
				text.append("]");
			} else {
				int value = sym.getValue();
				if (value == ' ') {
					text.append('_');
				} else {
					if (value == '\n') {
						text.append("\\n");
					} else {
						text.append((char) value);
					}
				}
			}
			text.append(' ');
		}
		text.append('\n');
	}

	private static String name(int index, NamingStrategy strategy)
	{
		switch (strategy) {
		default:
		case USE_R_PLUS_NUMBER:
			return "R" + index;
		case USE_LETTERS: {
			int offset = index % 26;
			char c = (char) ('A' + offset);
			if (index < 26) {
				return "" + c;
			}
			return c + "" + (index / 26);
		}
		case USE_LETTERS_START_WITH_S: {
			if (index == 0) {
				return "S";
			}
			index -= 1;
			int offset = index % 25;
			char c;
			if (offset < 18) {
				c = (char) ('A' + offset);
			} else {
				c = (char) ('A' + offset + 1);
			}
			if (index < 25) {
				return "" + c;
			}
			return c + "" + (index / 25);
		}
		}
	}
}
