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

package info.sequitur;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;

public class Sequitur extends JPanel
{

	private static final long serialVersionUID = 1L;

	private JTextArea text;
	private JTextArea rules;
	private JButton submit;
	private JLabel dataLabel;
	private JLabel rulesLabel;
	private Font f1 = new Font("TimesRoman", Font.BOLD, 18);
	private Font f2 = new Font("TimesRoman", Font.PLAIN, 12);

	public Sequitur()
	{
		String defaultText = new String(
				"pease porridge hot,\npease porridge cold,\npease porridge in the pot,\nnine days old.\n\nsome like it hot,\nsome like it cold,\nsome like it in the pot,\nnine days old.\n");

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		dataLabel = new JLabel("Data");
		dataLabel.setFont(f1);

		text = new JTextArea(9, 70);
		text.setFont(f2);

		rulesLabel = new JLabel("Grammar");
		rulesLabel.setFont(f1);

		rules = new JTextArea(9, 70);
		rules.setEditable(false);
		rules.setFont(f2);

		submit = new JButton("Run sequitur");
		submit.setFont(f1);

		JScrollPane scrollText = new JScrollPane(text);
		JScrollPane scrollRules = new JScrollPane(rules);

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(dataLabel, c);

		c.gridy++;
		c.weighty = 1.0;
		add(scrollText, c);

		c.gridy++;
		c.weighty = 0.0;
		add(rulesLabel, c);

		c.gridy++;
		c.weighty = 1.0;
		add(scrollRules, c);

		c.gridy++;
		c.weighty = 0.0;
		add(submit, c);

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateGrammer();
			}
		});

		text.getDocument().addDocumentListener(new SimpleDocumentListener() {

			@Override
			public void someUpdate(DocumentEvent e)
			{
				updateGrammer();
			}
		});

		text.setText(defaultText);
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
