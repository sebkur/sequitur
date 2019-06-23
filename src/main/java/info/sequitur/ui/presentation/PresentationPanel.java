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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

public class PresentationPanel extends JPanel implements ModelChangeListener
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
		input = new JTextField("dabcdbcabcdbcbbcc");
		input.setFont(input.getFont().deriveFont(20f));
		state = new StatePanel();

		bgUni = new ButtonGroup();
		bgChar = new ButtonGroup();
		bgStep = new ButtonGroup();

		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel pUni = new JPanel(new FlowLayout());
		pUni.setBorder(BorderFactory.createTitledBorder("Schritt"));
		pUni.add(bgUni.getFirst());
		pUni.add(bgUni.getPrevious());
		pUni.add(bgUni.getNext());
		pUni.add(bgUni.getLast());
		panelButtons.add(pUni);

		JPanel pChar = new JPanel(new FlowLayout());
		pChar.setBorder(BorderFactory.createTitledBorder("Zeichen"));
		pChar.add(bgChar.getFirst());
		pChar.add(bgChar.getPrevious());
		pChar.add(bgChar.getNext());
		pChar.add(bgChar.getLast());
		panelButtons.add(pChar);

		JPanel pStep = new JPanel(new FlowLayout());
		pStep.setBorder(BorderFactory.createTitledBorder("Zwischenschritt"));
		pStep.add(bgStep.getFirst());
		pStep.add(bgStep.getPrevious());
		pStep.add(bgStep.getNext());
		pStep.add(bgStep.getLast());
		panelButtons.add(pStep);

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

		bgUni.getFirst().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				firstUni();
			}
		});
		bgUni.getPrevious().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				prevUni();
			}
		});
		bgUni.getNext().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextUni();
			}
		});
		bgUni.getLast().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				lastUni();
			}
		});

		bgChar.getFirst().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				firstChar();
			}
		});
		bgChar.getPrevious().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				prevChar();
			}
		});
		bgChar.getNext().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextChar();
			}
		});
		bgChar.getLast().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				lastChar();
			}
		});

		bgStep.getFirst().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				firstStep();
			}
		});
		bgStep.getPrevious().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				prevStep();
			}
		});
		bgStep.getNext().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextStep();
			}
		});
		bgStep.getLast().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				lastStep();
			}
		});

		inputChanged();

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if (e.getID() == KeyEvent.KEY_RELEASED) {
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						nextUni();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						prevUni();
					}
				}
				return false;
			}
		});
	}

	protected void inputChanged()
	{
		String text = input.getText();
		model = new Model(text);
		state.setModel(model);
		model.addModelChangedListener(this);
		modelChanged();
	}

	protected void firstUni()
	{
		firstChar();
	}

	protected void prevUni()
	{
		if (!prevStep()) {
			if (model.getCurrentChar() > 0) {
				prevChar();
				lastStep();
			}
		}
	}

	protected void nextUni()
	{
		if (!nextStep()) {
			nextChar();
		}
	}

	protected void lastUni()
	{
		lastChar();
		lastStep();
	}

	protected void firstChar()
	{
		if (model.getCurrentChar() > 0) {
			model.setCurrentChar(0);
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

	protected void lastChar()
	{
		if (model.getCurrentChar() < model.getLength() - 1) {
			model.setCurrentChar(model.getLength() - 1);
		}
	}

	protected void firstStep()
	{
		if (model.getCurrentStep() > 0) {
			model.setCurrentStep(0);
		}
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

	protected void lastStep()
	{
		if (model.getCurrentStep() < model.getCurrentNumberOfSteps() - 1) {
			model.setCurrentStep(model.getCurrentNumberOfSteps() - 1);
		}
	}

	@Override
	public void modelChanged()
	{
		int cc = model.getCurrentChar();
		int cs = model.getCurrentStep();

		bgChar.getFirst().setEnabled(cc > 0);
		bgChar.getPrevious().setEnabled(cc > 0);
		bgChar.getNext().setEnabled(cc < model.getLength() - 1);
		bgChar.getLast().setEnabled(cc < model.getLength() - 1);
		bgStep.getFirst().setEnabled(cs > 0);
		bgStep.getPrevious().setEnabled(cs > 0);
		bgStep.getNext().setEnabled(cs < model.getCurrentNumberOfSteps() - 1);
		bgStep.getLast().setEnabled(cs < model.getCurrentNumberOfSteps() - 1);
		bgUni.getFirst().setEnabled(cc > 0 || cs > 0);
		bgUni.getPrevious().setEnabled(cc > 0 || cs > 0);
		bgUni.getNext().setEnabled(
				cc < model.getLength() - 1
						|| cs < model.getCurrentNumberOfSteps() - 1);
		bgUni.getLast().setEnabled(
				cc < model.getLength() - 1
						|| cs < model.getCurrentNumberOfSteps() - 1);
	}
}
