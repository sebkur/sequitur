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

import java.util.ArrayList;
import java.util.List;

public class Rule
{
	private Sequitur sequitur;

	// Guard symbol to mark beginning
	// and end of rule.

	public Guard guard;

	// Counter keeps track of how many
	// times the rule is used in the
	// grammar.

	public int count;

	// The rule's number.
	// Used for identification of
	// non-terminals.

	public int number;

	// Index used for printing.

	public int index;

	Rule(Sequitur sequitur)
	{
		this.sequitur = sequitur;
		number = sequitur.getNextRuleId();
		guard = new Guard(sequitur, this);
		count = 0;
		index = 0;
	}

	public Symbol first()
	{
		return guard.n;
	}

	public Symbol last()
	{
		return guard.p;
	}

	public String getRules()
	{
		List<Rule> rules = new ArrayList<Rule>(sequitur.getNumRules());
		Rule currentRule;
		Rule referedTo;
		Symbol sym;
		int index;
		int processedRules = 0;
		StringBuffer text = new StringBuffer();

		text.append("Usage\tRule\n");
		rules.add(this);
		while (processedRules < rules.size()) {
			currentRule = rules.get(processedRules);
			text.append(" ");
			text.append(currentRule.count);
			text.append("\tR");
			text.append(processedRules);
			text.append(" -> ");
			for (sym = currentRule.first(); (!sym.isGuard()); sym = sym.n) {
				if (sym.isNonTerminal()) {
					referedTo = ((NonTerminal) sym).r;
					if ((rules.size() > referedTo.index)
							&& (rules.get(referedTo.index) == referedTo)) {
						index = referedTo.index;
					} else {
						index = rules.size();
						referedTo.index = index;
						rules.add(referedTo);
					}
					text.append('R');
					text.append(index);
				} else {
					if (sym.value == ' ') {
						text.append('_');
					} else {
						if (sym.value == '\n') {
							text.append("\\n");
						} else {
							text.append((char) sym.value);
						}
					}
				}
				text.append(' ');
			}
			text.append('\n');
			processedRules++;
		}
		return new String(text);
	}
}
