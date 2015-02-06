import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Scrollable;


@SuppressWarnings("serial")
public class ParallelSentencesPanel extends JPanel implements Scrollable {

	private final Dimension singleSentenceDimension;
	private final Dimension maxDimension;
	
	public ParallelSentencesPanel() {
		ParallelSentence dummy = new ParallelSentence("foo", new String[]{ "foo" }, new String[]{ "bar" }, new String[]{ "0-0" }, 0);
		singleSentenceDimension = new ParallelSentencePanel(dummy,null).getPreferredSize();
		
		JFrame dummyFrame = new JFrame();
		dummyFrame.setExtendedState(dummyFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		maxDimension = dummyFrame.getMaximumSize();
//		System.err.println(maxDimension);
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.maxDimension;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return this.singleSentenceDimension.height * 5;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return this.singleSentenceDimension.height;
	}

}
