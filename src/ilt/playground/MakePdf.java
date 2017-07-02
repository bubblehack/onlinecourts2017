package ilt.playground;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class MakePdf {

	public static void main(String[] args) {
		go(null, null);
	}
	
	public MakePdf() {
	}
	
	public static void go(DocumentTemplate template, Map<Variable, String> answers) {
		(new MakePdf()).makePdf(template, answers);
	}
	
	int top = 700;
	int left = 100;
	PDPageContentStream contents;
	PDFont font = PDType1Font.HELVETICA_BOLD;
	PDFont font2 = PDType1Font.HELVETICA;
	float spaceWidth;
	float maxWidth = 30000;

	public void makePdf(DocumentTemplate template, Map<Variable, String> answers) {
		try {
			spaceWidth = font.getStringWidth(" ");
			String filename = "/Users/rsta/Desktop/result.pdf";

			try (PDDocument doc = new PDDocument()) {
				// a valid PDF document requires at least one page
				PDPage page = new PDPage();

				int top = 700;
				doc.addPage(page);
				contents = new PDPageContentStream(doc, page);
				write("IN THE ONLINE MONEY CLAIMS COURT");
				nextLine();
				write(String.format("BETWEEN %s (Claimant) AND %s (Defendant)", "todo", "alsotodo"));
				nextLine();
				writeOffset("------------------------------------", 220);
				nextLine();
				writeOffset("PARTICULARS OF CLAIM", 220);
				nextLine();
				writeOffset("------------------------------------", 220);
				nextLine();
				List<String> clauses = template.resolveClauses(answers, false);
				for (String s : clauses) {
					writeWrapped(s);
				}
				contents.close();
				doc.save(filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void write(String message) throws Exception {
		writeOffset(message, 100);
	}
	
	void writeSoft(String message) throws Exception {
		contents.beginText();
		contents.setFont(font, 12);
		contents.newLineAtOffset(100, top);
		contents.showText(message);
		contents.endText();
	}
	
	void writeOffset(String message, int offset) throws Exception {
		contents.beginText();
		contents.setFont(font, 12);
		contents.newLineAtOffset(offset, top);
		contents.showText(message);
		contents.endText();
	}
	
	void writeWrapped(String message) throws Exception {
		Scanner sc = new Scanner(message);
		StringBuilder sb = new StringBuilder();
		float width = 0;
		while (sc.hasNext()) {
			String nextString = sc.next();
			width += font2.getStringWidth(nextString);
			if (width > maxWidth) {
				write(sb.toString());
				smallLine();
				width = 0;
				sb = new StringBuilder();
			}
			sb.append(nextString);
			sb.append(" ");
			width += spaceWidth;
		}
		if (sb.length() > 0) {
			write(sb.toString());
			nextLine();
		}
		sc.close();
	}

	void nextLine() {
		top -= 30;
	}
	
	void smallLine() {
		top -= 20;
	}
}
