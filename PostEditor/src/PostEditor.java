import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;

//import org.apache.batik.swing.JSVGCanvas;

public class PostEditor {

	private final String postEditedPath;
	
//	private final JFrame svgFrame;
//	private final JFrame sourceTextFrame;
//	private final JFrame targetTextFrame;
	private final JFrame parallelSentenceFrame;

	private final List<TranslatedDocument> translatedDocuments;

//	private final JSVGCanvas svgCanvas;


	@SuppressWarnings("unused")
	private Integer sentenceNumber = 0;
	private Integer documentNumber;

//	private final JTextArea uneditableSourceTextArea;
//	private final JScrollPane uneditableSourceTextAreaScollPane;
//	
//	private final JTextArea uneditableTargetTextArea;
//	private final JScrollPane uneditableTargetTextAreaScollPane;
	
	private final JMenuBar menuBar;
	private final JMenu fileMenu;
//	private final JMenuItem nextSentence;
//	private final JMenuItem previousSentence;
	private final JMenuItem nextDocument;
	private final JMenuItem previousDocument;
	private final JMenuItem saveDocument;
	private final JMenuItem gotoDocument;
//	private final JMenuItem displayParse;
	
	private final JPanel parallelSentencesPanel;
	
//	private final Map<Integer,List<ParallelSentencePanel>> parallelSentencePanels;
//	
	@SuppressWarnings("serial")
	public PostEditor(String postEditedPath, String sourcePath, String targetPath, String alignmentsPath, /*String svgListPath,*/ int startingDocumentNumber) throws FileNotFoundException {

		this.translatedDocuments = TranslatedDocument.collateTranslatedDocuments(postEditedPath, sourcePath, targetPath, alignmentsPath /*, svgListPath*/);
//		System.err.println(this.translatedDocuments.size() + " documents");
		
		this.postEditedPath = postEditedPath;
		
		this.documentNumber = startingDocumentNumber - 1;
//		this.svgFrame = new JFrame("Dependency Parse");
//		this.sourceTextFrame = new JFrame("Source Text");
//		this.targetTextFrame = new JFrame("Machine Translations");
		this.parallelSentenceFrame = new JFrame("Aligned Translations");
		
		this.parallelSentenceFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				saveDocument();
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				saveDocument();
				System.exit(0);
			}
			
		});
		
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
//		this.nextSentence = new JMenuItem("Next Sentence");
//		this.previousSentence = new JMenuItem("Previous Sentence");
		this.saveDocument = new JMenuItem("Save");
		this.nextDocument = new JMenuItem("Next Document");
		this.previousDocument = new JMenuItem("Previous Document");
		this.gotoDocument = new JMenuItem("Go to Document...");
//		this.displayParse = new JMenuItem("Display Parse");
//		this.next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
//		this.previous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
//		this.nextSentence.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		this.previousSentence.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		this.nextDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK));
//		this.previousDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK));
		this.nextDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.previousDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.saveDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.gotoDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		this.displayParse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		this.fileMenu.add(nextSentence);
//		this.fileMenu.add(previousSentence);
		this.fileMenu.add(nextDocument);
		this.fileMenu.add(previousDocument);
		this.fileMenu.add(saveDocument);
		this.fileMenu.add(gotoDocument);
//		this.fileMenu.add(displayParse);
		this.menuBar.add(fileMenu);

		final ActionListener menuListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				Object eventSource = event.getSource();
				
//				if (eventSource==nextSentence) {
//					if (sentenceNumber+1 < translatedDocuments.get(documentNumber).size()) {
//						sentenceNumber += 1;
//						updateSentence();
//					}
//				} else if (eventSource==previousSentence) {
//					if (sentenceNumber >= 1) {
//						sentenceNumber -= 1;
//						updateSentence();
//					}
//				} else 
				if (eventSource==nextDocument) {
					if (documentNumber+1 < translatedDocuments.size()) {
//						System.err.println("Next Document");
						documentNumber += 1;
						sentenceNumber = 0;
						updateDocument(true);
						updateSentence();
					}
//					else {
//						System.err.println("Won't do Next Document");
//					}
				} else if (eventSource==previousDocument) {
					if (documentNumber >= 1) {
//						System.err.println("Previous Document");
						documentNumber -= 1;
						sentenceNumber = translatedDocuments.get(documentNumber).size()-1;
						updateDocument(true);
						updateSentence();
					} 
//					else {
////						System.err.println("Won't do Previous Document");
//					}
				} else if (eventSource==saveDocument) {
					boolean success = saveDocument();
					if (success) {
						JOptionPane.showMessageDialog(parallelSentenceFrame, "Save complete!");
					} else {
						JOptionPane.showMessageDialog(parallelSentenceFrame, "A problem occurred while saving", "Error saving", JOptionPane.ERROR_MESSAGE);
					}
				} else if (eventSource==gotoDocument) {
					String value = JOptionPane.showInputDialog("Enter document number:");
					try {
						int i = Integer.valueOf(value) - 1;
						if (i >= 0 && i < translatedDocuments.size()) {
							documentNumber = Integer.valueOf(value) - 1;
							updateDocument(true);
						} else {
							JOptionPane.showMessageDialog(parallelSentenceFrame, "\"" + value + "\" is not a valid document number.\nValid numbers are [1-"+translatedDocuments.size()+"]", "Invalid number", JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(parallelSentenceFrame, "\"" + value + "\" is not a valid document number.\nValid numbers are [1-"+translatedDocuments.size()+"]", "Invalid number", JOptionPane.ERROR_MESSAGE);
					}
				} /* else if (eventSource==displayParse) {
					svgCanvas.setURI(getSVGPath()); 
					svgFrame.pack();
					Dimension svgFrameDimension = svgFrame.getPreferredSize();
					svgFrameDimension.height = svgFrameDimension.height*2;
					svgFrame.setExtendedState(svgFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
					svgFrameDimension.width = svgFrame.getSize().width;
					svgFrame.setSize(svgFrameDimension);
					svgFrame.setVisible(true);
				} */
			
			}
			
		};
//		this.nextSentence.addActionListener(menuListener);
//		this.previousSentence.addActionListener(menuListener);
		this.nextDocument.addActionListener(menuListener);
		this.previousDocument.addActionListener(menuListener);
		this.saveDocument.addActionListener(menuListener);
		this.gotoDocument.addActionListener(menuListener);
//		this.displayParse.addActionListener(menuListener);
		
		this.parallelSentenceFrame.setJMenuBar(this.menuBar);
		
//		this.svgCanvas = new JSVGCanvas();
		
		

//		this.parallelSentencePanels = new HashMap<Integer,List<ParallelSentencePanel>>();
//		for (int i=0, n=translatedDocuments.size(); i<n; i+=1) {
//			parallelSentencePanels.put(i, new ArrayList<ParallelSentencePanel>());
//		}
		
//		this.svgFrame.getContentPane().add(svgCanvas);
//		this.svgFrame.pack();
//		Dimension svgFrameDimension = this.svgFrame.getPreferredSize();
//		svgFrameDimension.height = svgFrameDimension.height*2;
//		this.svgFrame.setExtendedState(this.svgFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//		svgFrameDimension.width = this.svgFrame.getSize().width;
//		this.svgFrame.setSize(svgFrameDimension);
//		this.svgFrame.setVisible(true);
//		
//		this.uneditableSourceTextArea = new JTextArea();
//		this.uneditableSourceTextArea.setColumns(50);
//		this.uneditableSourceTextAreaScollPane = new JScrollPane(uneditableSourceTextArea);
//		this.uneditableSourceTextAreaScollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		this.uneditableSourceTextAreaScollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		this.sourceTextFrame.getContentPane().add(uneditableSourceTextAreaScollPane);
//		this.sourceTextFrame.pack();
//		this.sourceTextFrame.setVisible(true);
//
//		this.uneditableTargetTextArea = new JTextArea();
//		this.uneditableTargetTextArea.setColumns(50);
//		this.uneditableTargetTextAreaScollPane = new JScrollPane(uneditableTargetTextArea);
//		this.uneditableTargetTextAreaScollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		this.uneditableTargetTextAreaScollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		this.targetTextFrame.getContentPane().add(uneditableTargetTextAreaScollPane);
//		this.targetTextFrame.pack();
//		this.targetTextFrame.setVisible(true);
		


//		final JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// Create a panel and add the button, status label and the SVG canvas.
		
//		for (int i=0, n=translatedDocuments.size(); i<n; i+=1) {
//			List<ParallelSentencePanel> list = parallelSentencePanels.get(i);
//			for (ParallelSentence parallelSentence : translatedDocuments.get(i)) {
//				list.add(new ParallelSentencePanel(parallelSentence));
//			}
//		}
		
		this.parallelSentencesPanel = new ParallelSentencesPanel();
//		this.parallelSentencesPanel = new JPanel();
		this.parallelSentencesPanel.setLayout(new BoxLayout(this.parallelSentencesPanel, BoxLayout.PAGE_AXIS));
		JScrollPane parallelSentencesScrollPane = new JScrollPane(parallelSentencesPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		parallelSentencesScrollPane.setLayout(new ScrollPaneLayout(){
			public void layoutContainer(Container parent) {
				super.layoutContainer(parent);
				Component view = viewport.getView();
				if (view != null) {
					Dimension viewPortSize = viewport.getSize();
					Dimension viewSize = view.getSize();
					
					if ((viewPortSize.width > viewSize.width) || (viewPortSize.height > viewSize.height)) {
						int spaceX = (viewPortSize.width - viewSize.width) / 2;
						
						if (spaceX < 0) spaceX = 0;
						
						viewport.setLocation(spaceX, 1);
						viewport.setSize(viewPortSize.width - spaceX, viewPortSize.height);
					}
				}
			}
		});
//		System.err.println(parallelSentencesPanel.getBackground().toString());
//		System.err.println(parallelSentencesScrollPane.getViewport().getBackground());
		parallelSentencesScrollPane.getViewport().setBackground(parallelSentencesPanel.getBackground());
		parallelSentencesScrollPane.setBackground(parallelSentencesPanel.getBackground());
//		System.err.println(parallelSentencesScrollPane.getViewport().getBackground());
		this.parallelSentenceFrame.setContentPane(parallelSentencesScrollPane);
		this.parallelSentenceFrame.pack();
		this.parallelSentenceFrame.setExtendedState(this.parallelSentenceFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.parallelSentenceFrame.setVisible(true);
		
		this.updateDocument(false);
		this.updateSentence();
//		System.err.println(parallelSentencesScrollPane.getViewport().getBackground());
		
	} 

	void setSentenceNumber(int sentenceNumber) {
		this.sentenceNumber = sentenceNumber;
	}

	private boolean saveDocument() {
		boolean success = true;
//		System.err.println("Writing out to " + postEditedPath);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(postEditedPath), "UTF-8"));
			out.write("=================================");
			out.newLine();
			out.write("=================================");
			out.newLine();
			for (TranslatedDocument document : this.translatedDocuments) {
				for (ParallelSentence parallelSentence : document) {
					out.write(parallelSentence.getEditedTranslation());
					out.newLine();
				}
				out.write("=================================");
				out.newLine();
				out.write("=================================");
				out.newLine();
			}
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		} finally {
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					success = false;
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}
	
	private void updateDocument(boolean writePostEdits) {
		
//		
//		translatedDocuments.get(documentNumber).populateUneditableSourceTextArea(uneditableSourceTextArea);
//		uneditableSourceTextAreaScollPane.setSize(uneditableSourceTextAreaScollPane.getPreferredSize());
//		uneditableSourceTextAreaScollPane.setMinimumSize(uneditableSourceTextAreaScollPane.getPreferredSize());
//		uneditableSourceTextAreaScollPane.setMaximumSize(uneditableSourceTextAreaScollPane.getPreferredSize());
//
//		translatedDocuments.get(documentNumber).populateUneditableTargetTextArea(uneditableTargetTextArea);
//		uneditableTargetTextAreaScollPane.setSize(uneditableTargetTextAreaScollPane.getPreferredSize());
//		uneditableTargetTextAreaScollPane.setMinimumSize(uneditableTargetTextAreaScollPane.getPreferredSize());
//		uneditableTargetTextAreaScollPane.setMaximumSize(uneditableTargetTextAreaScollPane.getPreferredSize());

//		this.parallelSentencesPanel.requestFocus();
		this.parallelSentenceFrame.setVisible(false);
		this.parallelSentenceFrame.setTitle("Document " + (documentNumber+1) + " of " + translatedDocuments.size());
		this.parallelSentencesPanel.removeAll();
//		System.err.println(documentNumber + " (size=="+ translatedDocuments.size()+"\"");
		for (ParallelSentence parallelSentence : this.translatedDocuments.get(documentNumber)) {
//			final ParallelSentencePanel panel = new ParallelSentencePanel(this.translatedDocuments.get(documentNumber).get(sentenceNumber));
			final ParallelSentencePanel parallelSentencePanel = new ParallelSentencePanel(parallelSentence, this);
			this.parallelSentencesPanel.add(parallelSentencePanel);			
		}
//		this.parallelSentencesPanel.setMaximumSize(this.parallelSentenceFrame.getMaximumSize());
//		this.parallelSentencesPanel.setSize(this.parallelSentenceFrame.getMaximumSize());
		this.parallelSentenceFrame.setVisible(true);

		if (writePostEdits) {
			saveDocument();
		} 
//		else {
//			System.err.println("Not writing out to " + postEditedPath);
//		}
		
//		this.parallelSentenceFrame.pack();
		
		
	}
	

	private void updateSentence() {

//		svgCanvas.setURI(this.getSVGPath());   	

	}


/*
	private String getSVGPath() {
		return "file:" + this.translatedDocuments.get(documentNumber).get(sentenceNumber).svgPath;
	}
*/	
	public static void main(String[] args) throws FileNotFoundException {

		if (args.length==0) {
			JOptionPane.showMessageDialog(null,
    			"Please click OK, then select your data file");
    		
    		JFileChooser fileChooser = new JFileChooser();
    		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    		System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
  				new PostEditor(fileChooser.getSelectedFile().getAbsolutePath(), "data.ru", "data.en", "data.alignments", 1);
			}
    		
			
		
		} else if (args.length==5) {
			
			new PostEditor(args[0], args[1], args[2], args[3], 1);
			
		} else if (args.length==6) {
			
			new PostEditor(args[0], args[1], args[2], args[3], Integer.valueOf(args[5]));
			
		} else {

			System.err.println("Usage: PostEditor postEdit.txt source.txt mtOutput.txt alignments.txt (startingDocumentNumber)");
			
//			final String postEditedPath = "/exp1/active/lane/2014-02-25_wmt14_postediting/newstest2014-ruen-src.ru.tokenized.translations.with_breaks.concatenated_transliterations.postedited";
//			final String sourcePath = "/exp1/active/lane/2014-02-25_wmt14_postediting/newstest2014-ruen-src.ru.tokenized";
//			final String targetPath = "/exp1/active/lane/2014-02-25_wmt14_postediting/newstest2014-ruen-src.ru.tokenized.translations.with_breaks.concatenated_transliterations";
//			final String alignmentsPath = "/exp1/active/lane/2014-02-25_wmt14_postediting/newstest2014-ruen-src.ru.tokenized.alignments";
//			final String svgListPath = "/exp1/active/lane/2014-02-25_wmt14_postediting/newstest2014-ruen-src.ru.tokenized.split/newstest2014-ruen-src.ru.tokenized.translations.svg_list";
//
//			new PostEditor(postEditedPath, sourcePath, targetPath, alignmentsPath, svgListPath, 0);
//			
		}
	}

}
