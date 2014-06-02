package info.sequitur;

import javax.swing.JFrame;

public class RunSequitur
{
	public static void main(String[] args)
	{
		Sequitur sequitur = new Sequitur();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(sequitur);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
