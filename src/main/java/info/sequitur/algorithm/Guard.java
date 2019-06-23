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

public class Guard extends Symbol
{

	Rule r;

	Guard(Sequitur sequitur, Rule rule)
	{
		super(sequitur);
		r = rule;
		value = 0;
		p = this;
		n = this;
	}

	@Override
	public void cleanUp()
	{
		sequitur.join(p, n);
	}

	@Override
	public boolean isGuard()
	{
		return true;
	}

	@Override
	public void deleteDigram()
	{
		// Do nothing
	}

	@Override
	public boolean check()
	{
		return false;
	}

}
