import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class ParallelSentencePanel extends JPanel {
	
	private class WordLabel extends JLabel {
		
		private final Provenance provenance;
		private final int wordNumber;
		
		public WordLabel(Provenance provenance, String label, int wordNumber, PostEditor postEditor) {
			super(label);
			this.provenance = provenance;
			this.wordNumber = wordNumber;
			if (postEditor != null) {
				this.addMouseListener(postEditor.listener);
				this.addMouseMotionListener(postEditor.listener);
				this.addKeyListener(postEditor.listener);
			}
		}
		
		public String toString() {
			//return ""+this.provenance+" word " + this.wordNumber + " of parallel sentence " + ParallelSentencePanel.this.sentenceNumber + " of document " + ParallelSentencePanel.this.documentNumber;
			return formatString(this.provenance, this.wordNumber, this.getText());
		}
	}
	
	private class EditField extends JTextField {
		public EditField(PostEditor postEditor) {
			super();
			if (postEditor != null) {
				this.addMouseListener(postEditor.listener);
				this.addMouseMotionListener(postEditor.listener);
				this.addKeyListener(postEditor.listener);
				this.addFocusListener(postEditor.listener);
			}
		}
		 
		public String toString() {
			return formatString(Provenance.Field, this.getCaretPosition(), this.getText());
		}
	}
	
	public String formatString(Provenance provenance) {
		return formatString(provenance,null,"");
	}
	
	public String formatString(Provenance provenance, Integer wordNumber, String text) {
		return "" + this.documentNumber + "\t" + this.sentenceNumber + "\t" + provenance.toString() + "\t" + (wordNumber==null ? "" : wordNumber) + "\t" + text;
	}
	
	private final List<WordLabel> sourceWords;
	private final List<WordLabel> targetWords;
	private final List<WordAlignment> wordAlignments;
	
	private final JPanel sourcePanel;
	private final JPanel targetPanel;
	
	private final EditField editableArea;
	
	final int sentenceNumber;
	final int documentNumber;
	
	public String toString() {
		return formatString(Provenance.Panel);
	}
	
	public ParallelSentencePanel(final ParallelSentence parallelSentence, final PostEditor postEditor) {
		if (postEditor != null) {
			this.addMouseListener(postEditor.listener);
			this.addMouseMotionListener(postEditor.listener);
			this.addKeyListener(postEditor.listener);
			this.documentNumber = postEditor.getDocumentNumber();
		} else {
			this.documentNumber = -1;
		}
		if (parallelSentence != null) {
			this.sentenceNumber = parallelSentence.sentenceNumber;
		} else {
			this.sentenceNumber = -1;
		}
		this.sourceWords = new ArrayList<WordLabel>();
		this.targetWords = new ArrayList<WordLabel>();
		this.wordAlignments = new ArrayList<WordAlignment>();
		
		this.sourcePanel = new JPanel();
		this.targetPanel = new JPanel();
		
		this.editableArea = new EditField(postEditor);

		for (int index=0; index<parallelSentence.sourceWords.length; index+=1) {
			this.sourceWords.add(new WordLabel(Provenance.Source, parallelSentence.sourceWords[index], index, postEditor));
		}

//		StringBuilder textToEdit = new StringBuilder();
		for (int index=0; index<parallelSentence.targetWords.length; index+=1) {
			this.targetWords.add(new WordLabel(Provenance.Target, parallelSentence.targetWords[index], index, postEditor));
//			textToEdit.append(word);
//			textToEdit.append(" ");
		}
		String previouslyEditedText = parallelSentence.getEditedTranslation();
//		if (previouslyEditedText==null || previouslyEditedText.isEmpty()) {
//			this.editableArea.setText(textToEdit.toString().trim());
//		} else {
			this.editableArea.setText(previouslyEditedText);
//		}
//		this.editableArea.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				parallelSentence.setEditedTranslation(editableArea.getText());
//			}
//			
//		});
//		
			this.editableArea.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					parallelSentence.setEditedTranslation(editableArea.getText());
				}

			});
			
			this.editableArea.addKeyListener(new KeyAdapter(){

				@Override
				public void keyPressed(KeyEvent arg0) {
					parallelSentence.setEditedTranslation(editableArea.getText());
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					parallelSentence.setEditedTranslation(editableArea.getText());
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					parallelSentence.setEditedTranslation(editableArea.getText());
				}
				
				
			});
			
		this.editableArea.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				parallelSentence.setEditedTranslation(editableArea.getText());
				postEditor.setSentenceNumber(parallelSentence.sentenceNumber);
//				ParallelSentencePanel.this.scrollRectToVisible(ParallelSentencePanel.this.getBounds());
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				parallelSentence.setEditedTranslation(editableArea.getText());
			}

			
		});
		
		for (WordAlignment a : parallelSentence.alignmentPoints) {
			this.wordAlignments.add(a);
		} 	

		for (JLabel sourceWord : this.sourceWords) {
			this.sourcePanel.add(sourceWord);
		}

		for (JLabel targetWord : this.targetWords) {
			this.targetPanel.add(targetWord);
		}
		
		this.sourcePanel.setBackground(Color.WHITE);
		this.targetPanel.setBackground(Color.WHITE);

		this.sourcePanel.setMaximumSize(this.sourcePanel.getMinimumSize());
		this.targetPanel.setMaximumSize(this.targetPanel.getMinimumSize());
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(this.sourcePanel);
		this.add(Box.createRigidArea(new Dimension(0,20)));
		this.add(this.targetPanel);
		this.add(Box.createRigidArea(new Dimension(0,10)));
		this.add(this.editableArea);
		this.add(Box.createRigidArea(new Dimension(0,15)));
		this.setMaximumSize(this.getMinimumSize());
	}
	
	
	
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (WordAlignment a : wordAlignments) {
			
//			System.err.println("Document " + documentNumber + ", sentence " + sentenceNumber + ", drawing alignment from source word " + a.sourceIndex + " to target word " + a.targetIndex);

			JLabel source = sourceWords.get(a.sourceIndex);
			JLabel target = targetWords.get(a.targetIndex);

			Rectangle sourceBounds = source.getBounds();
			Rectangle targetBounds = target.getBounds();

			Point sourcePoint = sourcePanel.getLocation();
			Point targetPoint = targetPanel.getLocation();

			g.drawLine(
					sourcePoint.x + sourceBounds.x + sourceBounds.width/2, 
					sourcePoint.y + sourceBounds.y + sourceBounds.height, 
					targetPoint.x + targetBounds.x + targetBounds.width/2, 
					targetPoint.y + targetBounds.y
			);

//			g.drawLine(sourceBounds.x + sourceBounds.width/2, 
//					sourceBounds.y + sourceBounds.height, 
//					targetBounds.x + targetBounds.width/2, 
//					targetBounds.y);

			
		}
	}
	
}
