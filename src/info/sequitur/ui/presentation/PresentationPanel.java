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

import info.sequitur.ui.SimpleDocumentListener;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

public class PresentationPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private JTextField input;
	private Model model;

	private StatePanel state;

	private ButtonGroup bgUni;
	private ButtonGroup bgChar;
	private ButtonGroup bgStep;

	public PresentationPanel()
	{
		input = new JTextField("abcdbcabcdbc");
		input.setFont(input.getFont().deriveFont(20f));
		state = new StatePanel();

		bgUni = new ButtonGroup();
		bgChar = new ButtonGroup();
		bgStep = new ButtonGroup();

		JPanel panelButtons = new JPanel(new FlowLayout());
		panelButtons.add(bgUni.getPrevious());
		panelButtons.add(bgUni.getNext());
		panelButtons.add(bgChar.getPrevious());
		panelButtons.add(bgChar.getNext());
		panelButtons.add(bgStep.getPrevious());
		panelButtons.add(bgStep.getNext());

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;

		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.0;
		add(input, c);

		c.gridy++;
		add(panelButtons, c);

		c.gridy++;
		c.weighty = 1.0;
		add(state, c);

		input.getDocument().addDocumentListener(new SimpleDocumentListener() {

			@Override
			public void someUpdate(DocumentEvent e)
			{
				inputChanged();
			}
		});

		bgUni.getNext().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextUni();
			}
		});
		bgUni.getPrevious().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				prevUni();
			}
		});

		bgChar.getNext().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextChar();
			}
		});
		bgChar.getPrevious().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				prevChar();
			}
		});

		bgStep.getNext().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextStep();
			}
		});
		bgStep.getPrevious().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				prevStep();
			}
		});

		inputChanged();
	}

	protected void inputChanged()
	{
		String text = input.getText();
		model = new Model(text);
		state.setModel(model);
	}

	protected void prevUni()
	{
		if (!prevStep()) {
			prevChar();
		}
	}

	protected void nextUni()
	{
		if (!nextStep()) {
			nextChar();
		}
	}

	protected boolean prevChar()
	{
		if (model.getCurrentChar() > 0) {
			model.setCurrentChar(model.getCurrentChar() - 1);
			return true;
		}
		return false;
	}

	protected boolean nextChar()
	{
		if (model.getCurrentChar() < model.getLength() - 1) {
			model.setCurrentChar(model.getCurrentChar() + 1);
			return true;
		}
		return false;
	}

	protected boolean prevStep()
	{
		if (model.getCurrentStep() > 0) {
			model.setCurrentStep(model.getCurrentStep() - 1);
			return true;
		}
		return false;
	}

	protected boolean nextStep()
	{
		if (model.getCurrentStep() < model.getCurrentNumberOfSteps() - 1) {
			model.setCurrentStep(model.getCurrentStep() + 1);
			return true;
		}
		return false;
	}
}
