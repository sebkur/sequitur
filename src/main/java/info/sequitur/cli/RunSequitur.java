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

import info.sequitur.algorithm.Sequitur;
import info.sequitur.util.NamingOrder;
import info.sequitur.util.NamingStrategy;
import info.sequitur.util.RulePrinter;

public class RunSequitur
{
	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println(
					"usage: " + RunSequitur.class.getSimpleName() + " <input>");
			System.exit(1);
		}

		String input = args[0];

		Sequitur sequitur = new Sequitur();
		sequitur.process(input);
		RulePrinter printer = new RulePrinter(sequitur,
				NamingStrategy.USE_LETTERS_START_WITH_S,
				NamingOrder.BY_CREATION);
		System.out.println(printer.getTable());
	}
}
