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
import info.sequitur.decode.Decoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunTest
{
	public static void main(String[] args)
	{
		if (args.length < 3) {
			System.out.println("usage: " + RunTest.class.getSimpleName()
					+ " <# different chars> <max length> <runs>");
			System.exit(1);
		}

		int cs = Integer.parseInt(args[0]);
		int ml = Integer.parseInt(args[1]);
		int runs = Integer.parseInt(args[2]);

		List<Character> chars = new ArrayList<>();
		for (int i = 0; i < cs; i++) {
			char c = (char) ('a' + i);
			chars.add(c);
		}

		Random random = new Random();

		for (int run = 0; run < runs; run++) {
			int len = random.nextInt(ml - 1) + 1;
			String input = buildSequence(random, chars, len);
			Sequitur sequitur = new Sequitur();
			sequitur.process(input);
			String output = Decoder.decode(sequitur);
			if (!input.equals(output)) {
				System.out.println((input.equals(output)) + " " + input);
			}
		}

	}

	private static String buildSequence(Random random, List<Character> chars,
			int len)
	{
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int k = random.nextInt(chars.size());
			strb.append(chars.get(k));
		}
		return strb.toString();
	}
}
