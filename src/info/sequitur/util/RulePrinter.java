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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulePrinter
{
	private StringBuilder text;
	private List<Rule> rules;
	private Map<Rule, Integer> ruleToIndex = new HashMap<Rule, Integer>();

	private NamingStrategy strategy;
	private NamingOrder order;

	public RulePrinter(Sequitur sequitur, NamingStrategy strategy,
			NamingOrder order)
	{
		this.strategy = strategy;
		this.order = order;
		rules = new ArrayList<Rule>(sequitur.getNumRules());
		int processedRules = 0;
		text = new StringBuilder();

		text.append("Usage\tRule\n");
		rules.add(sequitur.getFirstRule());
		ruleToIndex.put(sequitur.getFirstRule(), 0);
		while (processedRules < rules.size()) {
			Rule currentRule = rules.get(processedRules);
			traverse(currentRule, processedRules);
			processedRules++;
		}

		if (order == NamingOrder.BY_CREATION) {
			Collections.sort(rules, new Comparator<Rule>() {

				@Override
				public int compare(Rule r1, Rule r2)
				{
					return r1.number - r2.number;
				}
			});
		}

		for (int i = 0; i < rules.size() - 1; i++) {
			Rule rule = rules.get(i);
			appendRow(text, rule);
			text.append('\n');
		}
		appendRow(text, rules.get(rules.size() - 1));
	}

	private void traverse(Rule currentRule, int processedRules)
	{
		for (Symbol sym = currentRule.first(); (!sym.isGuard()); sym = sym
				.getNext()) {
			if (sym.isNonTerminal()) {
				Rule referedTo = ((NonTerminal) sym).getRule();
				Integer indexOrNull = ruleToIndex.get(referedTo);
				int index = indexOrNull == null ? 0 : indexOrNull;
				if ((rules.size() <= index) || (rules.get(index) != referedTo)) {
					ruleToIndex.put(referedTo, rules.size());
					rules.add(referedTo);
				}
			}
		}
	}

	private void appendRow(StringBuilder text, Rule currentRule)
	{
		text.append(" ");
		text.append(currentRule.count);
		text.append("\t");
		text.append(getName(currentRule));
		text.append(" -> ");
		for (Symbol sym = currentRule.first(); (!sym.isGuard()); sym = sym
				.getNext()) {
			if (sym.isNonTerminal()) {
				Rule referedTo = ((NonTerminal) sym).getRule();
				text.append("[");
				text.append(getName(referedTo));
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
	}

	public String getName(Rule rule)
	{
		int index;
		switch (order) {
		default:
		case BY_CREATION:
			index = rule.number;
			break;
		case BY_USAGE:
			index = ruleToIndex.get(rule);
			break;
		}
		switch (strategy) {
		default:
		case USE_NUMBER:
			return "" + index;
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

	public String getText()
	{
		return text.toString();
	}
}
