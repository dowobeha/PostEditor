public class ParallelSentence {

	public final String[] sourceWords;
	public final String[] targetWords; 
	public final WordAlignment[] alignmentPoints;
//	public final String svgPath;
	public final int sentenceNumber;
	private String editedTranslation;
	
	public ParallelSentence(String editedTranslation, String[] sourceWords, String[] targetWords, String[] alignmentPoints, int sentenceNumber) {
		this.sourceWords = sourceWords;
		this.targetWords = targetWords;
//		this.svgPath = svgPath;
		this.sentenceNumber = sentenceNumber;
		this.alignmentPoints = new WordAlignment[alignmentPoints.length];
		for (int i=0, n=alignmentPoints.length; i<n; i+=1) {
			String[] parts = alignmentPoints[i].split("-");
				this.alignmentPoints[i] = new WordAlignment(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
			
		}
//		StringBuilder textToEdit = new StringBuilder();
//		for (String word : this.targetWords) {
//			textToEdit.append(word);
//			textToEdit.append(" ");
//		}
//		this.editedTranslation = textToEdit.toString();
		this.editedTranslation = editedTranslation;
		
	}
	
	public ParallelSentence setEditedTranslation(String editedTranslation) {
		this.editedTranslation = editedTranslation;
		//System.err.println();
		return this;
	}
	
	public String getEditedTranslation() {
		return this.editedTranslation;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (String sourceWord : sourceWords) {
			s.append(sourceWord);
			s.append(' ');
		}
		
		s.append('\n');
		
		for (String targetWord : targetWords) {
			s.append(targetWord);
			s.append(' ');
		}
		
		s.append('\n');
		
		s.append(editedTranslation);
		
		s.append('\n');
		
		return s.toString();
	}
}
