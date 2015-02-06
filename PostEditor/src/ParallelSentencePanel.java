import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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

	private final List<JLabel> sourceWords;
	private final List<JLabel> targetWords;
	private final List<WordAlignment> wordAlignments;
	
	private final JPanel sourcePanel;
	private final JPanel targetPanel;
	
	private final JTextField editableArea;
	
	public ParallelSentencePanel(final ParallelSentence parallelSentence, final PostEditor postEditor) {
		
		this.sourceWords = new ArrayList<JLabel>();
		this.targetWords = new ArrayList<JLabel>();
		this.wordAlignments = new ArrayList<WordAlignment>();
		
		this.sourcePanel = new JPanel();
		this.targetPanel = new JPanel();
		
		this.editableArea = new JTextField();

		for (String word : parallelSentence.sourceWords) {
			this.sourceWords.add(new JLabel(word));
		}

//		StringBuilder textToEdit = new StringBuilder();
		for (String word : parallelSentence.targetWords) {
			this.targetWords.add(new JLabel(word));
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
