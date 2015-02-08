package util;

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;


public class EventLogger implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, FocusListener {

	private final Writer writer;
	
	public EventLogger(String fileName) throws UnsupportedEncodingException, IOException {
		
		final FileOutputStream output = new FileOutputStream(fileName);

		if (fileName.endsWith(".gz")) {
			writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8");
		} else if (fileName.endsWith(".zip")) {
			writer = new OutputStreamWriter(new ZipOutputStream(output), "UTF-8");
		} else {
			writer = new OutputStreamWriter(output, "UTF-8");
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private long startTime = -1;
	
	private long elapsedMilliseconds() {
		
		if (startTime < 0) {
			startTime = System.currentTimeMillis();
		}
		
		long now = System.currentTimeMillis();
		long elapsed = now - startTime;
		return elapsed;
	}
	
	private void log(ComponentEvent e) {
		
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
		
//		
//		str.append('\t');
//		str.append("XXX");
//		
		str.append('\t');
		str.append(e.paramString().replaceFirst("\t.*", ""));
//
//		str.append('\t');
//		str.append("YYY");
		
		str.append('\t');	 
		str.append(e.getSource().toString());
		
		str.append('\n');
		
		try {
			writer.append(str.toString());
		} catch (IOException exception) {
			exception.printStackTrace();
			System.err.println(str.toString());
		}

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
	public void mouseWheelMoved(MouseWheelEvent e) {
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
