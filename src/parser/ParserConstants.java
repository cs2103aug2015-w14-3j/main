//@@author A0125496X
/**
 * Keeps a list of user input constants to be resolved by both LanguageProcessor and Command
 *
 * @author amoshydra
 */
package parser;

public class ParserConstants {
		public static final String DELIMITER_TOKEN = "-";

		public static final String TASK_SPECIFIER_TASKNAME = DELIMITER_TOKEN + "name";
		public static final String TASK_SPECIFIER_STARTTIME = DELIMITER_TOKEN + "sd";
		public static final String TASK_SPECIFIER_ENDTIME = DELIMITER_TOKEN + "ed";
		public static final String TASK_SPECIFIER_PRIORITY = DELIMITER_TOKEN + "p";

		public static final String TASK_FILTER_ALL = DELIMITER_TOKEN + "all";
		public static final String TASK_FILTER_DONE = DELIMITER_TOKEN + "done";
		public static final String TASK_FILTER_FLOATING = DELIMITER_TOKEN + "floating";

		public static final String TASK_SETTING_NEWFILE = DELIMITER_TOKEN + "nf";
		public static final String TASK_SETTING_OPENFILE = DELIMITER_TOKEN + "of";
		
		public static final String TASK_MARK_UNMARK = DELIMITER_TOKEN + "um";
		
		public static final String[] TASK_PRIORITY_HIGH = {"high","h"};
		public static final String[] TASK_PRIORITY_NORM = {"norm","normal","n"};
		public static final String[] TASK_PRIORITY_LOW = {"low","l"};
}
