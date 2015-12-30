package questionTypes;

import java.util.ArrayList;

/**
 * A "question" that just provides the student with information about another question
 * or the test in general
 * @author Mark Wiggans
 */
public class Information extends MultipleChoiceQuestion {
	public Information(String question){
		super(new ArrayList<String>());
	}
}
