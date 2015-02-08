import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class ParallelSentencePanelListener implements MouseListener, MouseMotionListener, KeyListener, FocusListener {

	private long startTime = -1;
	
	private long elapsedMilliseconds() {
		long now = System.currentTimeMillis();
		long elapsed = now - startTime;
		return elapsed;
	}
	
	private void log(ComponentEvent e) {
		if (startTime < 0) {
			startTime = System.currentTimeMillis();
		}
		
		StringBuilder str = new StringBuilder();
		
		str.append(elapsedMilliseconds());
		str.append('\t');
		
		switch(e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			str.append("MOUSE_PRESSED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_RELEASED:
			str.append("MOUSE_RELEASED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_CLICKED:
			str.append("MOUSE_CLICKED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_ENTERED:
			str.append("MOUSE_ENTERED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_EXITED:
			str.append("MOUSE_EXITED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_MOVED:
			str.append("MOUSE_MOVED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_DRAGGED:
			str.append("MOUSE_DRAGGED");
			str.append('\t');
			break;
		case MouseEvent.MOUSE_WHEEL:
			str.append("MOUSE_WHEEL");
			str.append('\t');
			break;
		case KeyEvent.KEY_PRESSED:
			str.append("KEY_PRESSED");
			str.append('\t');
			str.append(KeyEvent.getKeyText(((KeyEvent)e).getKeyCode()));
			break;
		case KeyEvent.KEY_RELEASED:
			str.append("KEY_RELEASED");
			str.append('\t');
			str.append(KeyEvent.getKeyText(((KeyEvent)e).getKeyCode()));
			break;
		case KeyEvent.KEY_TYPED:
			str.append("KEY_TYPED");
			str.append('\t');
			char c = ((KeyEvent)e).getKeyChar();
			if (c=='\t') {
				str.append("TAB");
			} else {
				str.append(c);
			}	
			break;
		case FocusEvent.FOCUS_GAINED:
			str.append("FOCUS_GAINED");
			str.append('\t');
			break;
		case FocusEvent.FOCUS_LOST:
			str.append("FOCUS_LOST");
			str.append('\t');
			break;
		default:
			str.append("UNKNOWN");
		}
		
		str.append('\t');
		 
		str.append(e.getSource().toString());
		
		System.err.println(str.toString());
		//System.err.println(elapsedMilliseconds() + "\t" + e.getSource().toString() + "\t" + e.paramString());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		log(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		log(e);		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		log(e);		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		log(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		log(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		log(e);		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		log(e);		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		log(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		log(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		log(e);
	}

	@Override
	public void focusGained(FocusEvent e) {
		log(e);		
	}

	@Override
	public void focusLost(FocusEvent e) {
		log(e);
	}

}
