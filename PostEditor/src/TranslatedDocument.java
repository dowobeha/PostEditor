import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;


public class TranslatedDocument implements Iterable<ParallelSentence> {

	private final List<ParallelSentence> parallelSentences;
	
	private TranslatedDocument(List<ParallelSentence> parallelSentences) {
		this.parallelSentences = parallelSentences;
	}
	
	public int size() {
		return parallelSentences.size();
	}
	
	public ParallelSentence get(int index) {
		return parallelSentences.get(index);
	}
	
	public void populateUneditableSourceTextArea(JTextArea textArea) {
		textArea.setText(null);
		
		StringBuilder s = new StringBuilder();
		for (ParallelSentence p : parallelSentences) {
			for (String word : p.sourceWords) {
				s.append(word);
				s.append(" ");
			}
			s.append(System.getProperty("line.separator"));
		}
		
		textArea.setText(s.toString());
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setRows(this.size());
	}

	public void populateUneditableTargetTextArea(JTextArea textArea) {
		textArea.setText(null);
		
		StringBuilder s = new StringBuilder();
		for (ParallelSentence p : parallelSentences) {
			for (String word : p.targetWords) {
				s.append(word);
				s.append(" ");
			}
			s.append(System.getProperty("line.separator"));
		}
		
		textArea.setText(s.toString());
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setRows(this.size());
	}
	
	public static List<TranslatedDocument> collateTranslatedDocuments(String postEditedPath, String sourcePath, String targetPath, String alignmentsPath) {
		
		List<TranslatedDocument> list = new ArrayList<TranslatedDocument>();
		
		List<ParallelSentence> parallelSentences = new ArrayList<ParallelSentence>();
//		boolean flag = true;

		Scanner postEditScanner = null;
		Scanner sourceScanner = null;
		Scanner targetScanner = null;
		Scanner alignmentsScanner = null;

		try {
			postEditScanner = new Scanner (new File(postEditedPath), "UTF-8");
			sourceScanner = new Scanner (new File(sourcePath), "UTF-8");
			targetScanner = new Scanner (new File(targetPath), "UTF-8");
			alignmentsScanner = new Scanner (new File(alignmentsPath), "UTF-8");
			
//			Scanner svgListScanner = new Scanner(new File(svgListPath), "UTF-8");
			
			while (postEditScanner.hasNextLine() && sourceScanner.hasNextLine() && targetScanner.hasNextLine() && alignmentsScanner.hasNextLine() ) {
				
				String postEdit = postEditScanner.nextLine(); //System.err.println(postEdit);
				String sourceLine = sourceScanner.nextLine();
				String targetLine = targetScanner.nextLine();
				String alignments = alignmentsScanner.nextLine();
//				String svgPath = svgListScanner.nextLine();
				
				String[] sourceParts = sourceLine.split("\\s+");
				String[] targetParts = targetLine.split("\\s+");
				String[] alignmentParts = alignments.split("\\s+");
				
//				System.err.println(valid(sourceParts) + " " + valid(targetParts) + " " + valid(alignmentParts));
				
				if (valid(sourceParts) && valid(targetParts) /*&& valid(alignmentParts)*/) {
					if (!valid(alignmentParts)) { alignmentParts=new String[0]; }
					ParallelSentence parallelSentence = new ParallelSentence(postEdit, sourceParts, targetParts, alignmentParts, parallelSentences.size());
//					parallelSentence.setEditedTranslation(postEdit);
//					if (flag) {
//						System.err.println(parallelSentence);
//						System.err.println();
//						flag = false;
//					}
//					System.exit(-1);
					parallelSentences.add(parallelSentence);
				} else {
					if (! parallelSentences.isEmpty()) {
						list.add(new TranslatedDocument(parallelSentences));
						parallelSentences = new ArrayList<ParallelSentence>();
					}
				}
				
			}
			
//			System.err.println(postEditScanner.hasNextLine() + " " + sourceScanner.hasNextLine() + " " + targetScanner.hasNextLine() + " " + alignmentsScanner.hasNextLine() + " " + svgListScanner.hasNextLine());
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postEditScanner != null) {
				postEditScanner.close();
			}
			if (sourceScanner != null) {
				sourceScanner.close();
			}
			if (targetScanner != null) {
				targetScanner.close();
			}
			if (alignmentsScanner != null) {
				alignmentsScanner.close();
			}
		}
			
		return list;
	}
	
    private static boolean valid(String[] line) {
    	if (line==null) {
    		return false;
    	} else if (line.length==0) {
    		return false;
    	} else if ( line.length==1 && (line[0].equals("=================================") || line[0].equals("")) ) {
    		return false;
    	} else {
    		return true;
    	}
    }

	@Override
	public Iterator<ParallelSentence> iterator() {
		return parallelSentences.iterator();
	}
	
	
}
