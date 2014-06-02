package info.sequitur;

/*
 This class is part of a Java port of Craig Nevill-Manning's Sequitur algorithm.
 Copyright (C) 1997 Eibe Frank

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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Sequitur extends JPanel
{

	private static final long serialVersionUID = 1L;

	JTextArea text;
	JTextArea rules;
	JButton submit;
	JPanel dataPanel;
	JPanel rulesPanel;
	JPanel buttonPanel;
	JPanel label1Panel;
	JPanel label2Panel;
	JLabel dataLabel;
	JLabel rulesLabel;
	Font f1 = new Font("TimesRoman", Font.BOLD, 18);
	Font f2 = new Font("TimesRoman", Font.PLAIN, 12);

	public Sequitur()
	{
		String defaultText = new String(
				"pease porridge hot,\npease porridge cold,\npease porridge in the pot,\nnine days old.\n\nsome like it hot,\nsome like it cold,\nsome like it in the pot,\nnine days old.\n");

		setLayout(new FlowLayout());
		dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		add(dataPanel);
		label1Panel = new JPanel();
		label1Panel.setLayout(new FlowLayout());
		dataPanel.add("North", label1Panel);
		dataLabel = new JLabel("Data");
		dataLabel.setFont(f1);
		label1Panel.add(dataLabel);
		text = new JTextArea(9, 70);
		text.setFont(f2);
		text.setText(defaultText);
		dataPanel.add("South", text);
		rulesPanel = new JPanel();
		rulesPanel.setLayout(new BorderLayout());
		add(rulesPanel);
		label2Panel = new JPanel();
		label2Panel.setLayout(new FlowLayout());
		rulesPanel.add("North", label2Panel);
		rulesLabel = new JLabel("Grammar");
		rulesLabel.setFont(f1);
		label2Panel.add(rulesLabel);
		rules = new JTextArea(9, 70);
		rules.setEditable(false);
		rules.setFont(f2);
		rulesPanel.add("South", rules);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		add(buttonPanel);
		submit = new JButton("Run sequitur");
		submit.setFont(f1);
		buttonPanel.add(submit);
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateGrammer();
			}
		});
	}

	protected void updateGrammer()
	{
		submit.setEnabled(false);
		text.setEditable(false);
		runSequitur();
		submit.setEnabled(true);
		text.setEditable(true);
	}

	public void runSequitur()
	{
		Rule firstRule = new Rule();
		int i;

		// Reset number of rules and Hashtable.

		Rule.numRules = 0;
		Symbol.theDigrams.clear();
		for (i = 0; i < text.getText().length(); i++) {
			firstRule.last()
					.insertAfter(new Terminal(text.getText().charAt(i)));
			firstRule.last().p.check();
		}
		rules.setText(firstRule.getRules());
	}

}
