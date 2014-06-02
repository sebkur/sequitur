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

package info.sequitur.ui.presentation;

import info.sequitur.ui.presentation.state.SimpleState;
import info.sequitur.ui.presentation.state.State;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class StatePanel extends JPanel implements ModelChangeListener
{

	private static final long serialVersionUID = 1L;

	private JTextField fieldMessage;
	private JTextArea areaGrammer;
	private JTextArea areaDigrams;
	private JTextArea areaOldGrammer;
	private JTextArea areaOldDigrams;

	private Model model;

	public StatePanel()
	{
		float fontSize = 20;

		fieldMessage = new JTextField();
		areaGrammer = new JTextArea();
		areaDigrams = new JTextArea();
		areaOldGrammer = new JTextArea();
		areaOldDigrams = new JTextArea();

		fieldMessage.setFont(fieldMessage.getFont().deriveFont(fontSize));
		areaGrammer.setFont(areaGrammer.getFont().deriveFont(fontSize));
		areaDigrams.setFont(areaDigrams.getFont().deriveFont(fontSize));
		areaOldGrammer.setFont(areaOldGrammer.getFont().deriveFont(fontSize));
		areaOldDigrams.setFont(areaOldDigrams.getFont().deriveFont(fontSize));

		areaGrammer.setEditable(false);
		areaDigrams.setEditable(false);

		areaOldGrammer.setEditable(false);
		areaOldDigrams.setEditable(false);

		areaGrammer.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		areaDigrams.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		areaOldGrammer.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		areaOldDigrams.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));

		areaOldGrammer.setBackground(new Color(0xDDDDDD));
		areaOldDigrams.setBackground(new Color(0xDDDDDD));

		JScrollPane scrollGrammer = new JScrollPane(areaGrammer);
		JScrollPane scrollDigrams = new JScrollPane(areaDigrams);
		JScrollPane scrollOldGrammer = new JScrollPane(areaOldGrammer);
		JScrollPane scrollOldDigrams = new JScrollPane(areaOldDigrams);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;

		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.0;
		add(fieldMessage, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1.0;
		add(scrollGrammer, c);

		c.gridx = 1;
		c.gridy = 1;
		add(scrollDigrams, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 1.0;
		add(scrollOldGrammer, c);

		c.gridx = 1;
		c.gridy = 2;
		add(scrollOldDigrams, c);
	}

	public void setModel(Model model)
	{
		this.model = model;
		model.addModelChangedListener(this);
		modelChanged();
	}

	@Override
	public void modelChanged()
	{
		if (model.getLength() == 0) {
			fieldMessage.setText("");
			areaGrammer.setText("");
			areaDigrams.setText("");
			return;
		}
		State state = model.getCurrentState();
		State oldState = model.getPreviousState();

		if (state instanceof SimpleState) {
			SimpleState simple = (SimpleState) state;
			fieldMessage.setText(simple.message);
		}

		update(areaGrammer, areaDigrams, state);
		update(areaOldGrammer, areaOldDigrams, oldState);
	}

	private void update(JTextArea areaGrammer, JTextArea areaDigrams,
			State state)
	{
		if (state == null) {
			areaGrammer.setText("");
			areaDigrams.setText("");
			return;
		}
		areaGrammer.setText(state.table);
		areaDigrams.setText("Bigramm-Index\n" + state.digrams);
	}
}
