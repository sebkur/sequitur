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

package info.sequitur.ui;

import info.sequitur.algorithm.Sequitur;
import info.sequitur.util.NamingStrategy;
import info.sequitur.util.SequiturUtil;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;

public class InteractiveSequiturPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private JTextArea text;
	private JTextArea rules;
	private JLabel dataLabel;
	private JLabel rulesLabel;
	private Font f1 = new Font("TimesRoman", Font.BOLD, 18);
	private Font f2 = new Font("TimesRoman", Font.PLAIN, 12);

	public InteractiveSequiturPanel()
	{
		String defaultText = new String(
				"pease porridge hot,\npease porridge cold,\npease porridge in the pot,\nnine days old.\n\nsome like it hot,\nsome like it cold,\nsome like it in the pot,\nnine days old.\n");

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		dataLabel = new JLabel("Data");
		dataLabel.setFont(f1);

		text = new JTextArea();
		text.setFont(f2);

		rulesLabel = new JLabel("Grammar");
		rulesLabel.setFont(f1);

		rules = new JTextArea();
		rules.setEditable(false);
		rules.setFont(f2);

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
		text.setEditable(false);
		runSequitur();
		text.setEditable(true);
	}

	public void runSequitur()
	{
		Sequitur sequitur = new Sequitur();
		sequitur.process(text.getText());
		rules.setText(SequiturUtil.buildRuleTable(sequitur,
				NamingStrategy.USE_LETTERS_START_WITH_S));
	}

}
