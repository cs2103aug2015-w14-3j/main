package ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import logic.command.Command;
import parser.CommandProcessor;
import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import util.StringUtil;
import util.TimeUtil;

public class UIController implements Initializable {

	// Message string constants
	private static final String MSG_CMD_WELCOME = "Welcome! Loading your stuffs";
	private static final String MSG_PENDING_HELLO = "Hello %s,";
	private static final String MSG_EMPTY = "";
	private static final String MSG_EMPTY_TABLE = "Nothing here";
	private static final String MSG_COUNT_OVERDUE = "Overdue [ %s ]";
	private static final String MSG_COUNT_PENDING = "Pending [ %s ]";
	private static final String MSG_COUNT_DONE = "Done [ %s ]";

	// Utility string constants
	private static final String EMPTY_STRING = "";
	private static final String VAR_TABLE_STRING_ID = "id";
	private static final String VAR_TABLE_STRING_TASK = "task";
	private static final String VAR_TABLE_STRING_SDATE = "sDate";

	// CSS node constants
	private static final String CSS_PRIORITY_HIGH = "highPriority";
	private static final String CSS_PRIORITY_NORMAL = "normalPriority";
	private static final String CSS_PRIORITY_LOW = "lowPriority";
	private static final String CSS_FLAG_NULL = "undone";
	private static final String CSS_FLAG_DONE = "done";
	private static final String CSS_OVERDUE = "overdue";
	private static final String CSS_CURRENT = "current";

	// FXML constants
	@FXML private Label pendingMsg;
	@FXML private Label timeDateMsg;
	@FXML private Label cmdMsg;
	@FXML private Label syntaxMsg;
	@FXML private Label tableFloatHeader;
	@FXML private Label tableTimedHeader;
	@FXML private Label overdueCount;
	@FXML private Label pendingCount;
	@FXML private Label doneCount;
	@FXML private TableColumn<UITask, Integer> idTimed;
	@FXML private TableColumn<UITask, String> taskTimed;
	@FXML private TableColumn<UITask, String> sDate;
	@FXML private TableColumn<UITask, Integer> idFloat;
	@FXML private TableColumn<UITask, String> taskFloat;
	@FXML private TableView<UITask> tableTimed;
	@FXML private TableView<UITask> tableFloat;
	@FXML private TextField input;
	@FXML private AnchorPane anchor;

	private static UI uI;
	private static UIController uIController;
	private List<Task> _floatingTaskList;
	private List<Task> _nonFloatingTaskList;
	private ArrayList<String> inputBuffer = new ArrayList<>();
	ObservableList<UITask> dataTimed = FXCollections.observableArrayList();
	ObservableList<UITask> dataFloat = FXCollections.observableArrayList();

	private Queue<String> masterQ;
	private Stack<String> upStack;
	private Stack<String> downStack;

	public UIController() {
		masterQ = new LinkedList<String>();
		upStack = new Stack<String>();
		downStack = new Stack<String>();
	}

	static UIController getUIController() {
		uIController = UI.getController();
		return uIController;
	}

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		// Label fields
		cmdMsg.setText(MSG_CMD_WELCOME);
		String username = System.getProperty("user.name");

		pendingMsg.setText(String.format(MSG_PENDING_HELLO, username));

		//TODO unimplemented label field
		timeDateMsg.setText(EMPTY_STRING);
		overdueCount.setText(EMPTY_STRING);
		pendingCount.setText(EMPTY_STRING);
		doneCount.setText(EMPTY_STRING);

		// Table
		idTimed.setCellValueFactory(new PropertyValueFactory<UITask, Integer>(VAR_TABLE_STRING_ID));
		taskTimed.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));
		sDate.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_SDATE));
		tableTimed.setRowFactory(new Callback<TableView<UITask>, TableRow<UITask>>() {
			@Override
			public TableRow<UITask> call(TableView<UITask> tableView) {
				final TableRow<UITask> row = new TableRow<UITask>() {

					@Override
					protected void updateItem(UITask task, boolean empty) {
						super.updateItem(task, empty);
						updateRowColour(this);
					}
				};
				return row;
			}
		});

		idFloat.setCellValueFactory(new PropertyValueFactory<UITask, Integer>(VAR_TABLE_STRING_ID));
		taskFloat.setCellValueFactory(new PropertyValueFactory<UITask, String>(VAR_TABLE_STRING_TASK));
		tableFloat.setRowFactory(new Callback<TableView<UITask>, TableRow<UITask>>() {

			@Override
			public TableRow<UITask> call(TableView<UITask> tableView) {
				final TableRow<UITask> row = new TableRow<UITask>() {
					@Override
					protected void updateItem(UITask task, boolean empty) {
						super.updateItem(task, empty);
						updateRowColour(this);
					}
				};
				return row;
			}
		});

		tableTimed.setPlaceholder(new Label(MSG_EMPTY_TABLE));
		tableFloat.setPlaceholder(new Label(MSG_EMPTY_TABLE));

		tableTimed.setFocusTraversable(false);
		tableFloat.setFocusTraversable(false);

		// Command help tips listener
		input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				processSyntaxMessage(oldValue, newValue);
			}
		});
	}

	private void updateRowColour(TableRow<UITask> tableRow) {
		if (!tableRow.isEmpty()) {

			UITask uITask = tableRow.getItem();
			PRIORITY_TYPE priorityCheck = uITask.getPriority();
			FLAG_TYPE flagCheck = uITask.getFlag();

			// Clear all custom css element before adding new style to it
			removeAllCSSElement(tableRow);

			// Check flag
			if (flagCheck == FLAG_TYPE.DONE) {
				tableRow.getStyleClass().add(CSS_FLAG_DONE);
			} else {
				if (priorityCheck == PRIORITY_TYPE.HIGH) {
					tableRow.getStyleClass().add(CSS_PRIORITY_HIGH);
				} else if (priorityCheck == PRIORITY_TYPE.LOW) {
					tableRow.getStyleClass().add(CSS_PRIORITY_LOW);
				}
			}

			// Check overdue
			long endTime = uITask.getEndTime();
			boolean isOverdue = TimeUtil.isBeforeNow(endTime) && endTime != Task.DATE_NULL;
			if (isOverdue) {
				tableRow.getStyleClass().add(CSS_OVERDUE);
			}
		} else {
			removeAllCSSElement(tableRow);
		}
	}

	void removeAllCSSElement(TableRow<UITask> tableRow) {
		tableRow.getStyleClass().remove(CSS_PRIORITY_HIGH);
		tableRow.getStyleClass().remove(CSS_PRIORITY_LOW);
		tableRow.getStyleClass().remove(CSS_FLAG_DONE);
		tableRow.getStyleClass().remove(CSS_OVERDUE);
	}

	// Create UI
	static void createUI() {
		if (uI == null) {
			uI = new UI();

			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(uI.getClass());
				}
			}.start();

			while (!uI.isInitialised()) {
				try {
					Thread.sleep(0);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	String getInput() {
		synchronized (inputBuffer) {
			// wait for input from field
			while (inputBuffer.isEmpty()) {
				try {
					inputBuffer.wait();
				} catch (InterruptedException e) {}
				//TODO check for unimplemented method
			}

			return inputBuffer.remove(0);
		}
	}

	void setInput(String str) {
		input.setText(str);
	}

	void clearInput() {
		input.clear();
	}

	void setOutputMsg(String a) {
		// TODO temporary fix for illegalState

		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	cmdMsg.setText(a);
		    }
		});
	}

	void setTimeDateMsg(String str) {
		timeDateMsg.setText(str);
	}

	void setSyntaxMsg(String str) {
		syntaxMsg.setText(str);
	}

	void setDoneCount(int count) {
		String str = String.format(MSG_COUNT_DONE, count);
		doneCount.setText(str);
	}

	void setPendingCount(int count) {
		String str = String.format(MSG_COUNT_PENDING, count);
		pendingCount.setText(str);
	}

	void setOverdueCount(int count) {
		String str = String.format(MSG_COUNT_OVERDUE, count);
		overdueCount.setText(str);
	}

	void showUIHelpOverlay() {
		uI.showUIHelpOverlayStage();
	}

	void hideUIHelpOverlay() {
		uI.hideUIHelpOverlayStage();
	}

	private void generateTable() {

		dataTimed.clear();
		dataFloat.clear();

		for (Task t : _nonFloatingTaskList) {
			dataTimed.add(new UITask(t));
		}

		for (Task t : _floatingTaskList) {
			dataFloat.add(new UITask(t));
		}

		tableTimed.setItems(dataTimed);
		tableFloat.setItems(dataFloat);
	}

	void seperateTaskList(List<Task> taskList) {

		_nonFloatingTaskList = new ArrayList<Task>();
		_floatingTaskList = new ArrayList<Task>();

		// Iterate through list and remove all floating tasks
		for (int i = 0; i < taskList.size();) {
			if (isFloating(taskList.get(i))) {
				_floatingTaskList.add(taskList.remove(i));
			} else {
				i++;
			}
		}
		// Remaining tasks are all non-floating
		_nonFloatingTaskList = taskList;

		Collections.sort(_nonFloatingTaskList, new taskCollections.comparators.EndTimeComparator());
		Collections.sort(_floatingTaskList, new taskCollections.comparators.PriorityComparator());
		// Collections.sort(_floatingTaskList);

		generateTable();
	}

	private boolean isFloating(Task task) {
		if (task.getEndTime() == 0) {
			return true;
		} else {
			return false;
		}
	}

	// Event methods
	public void enterPressed() {

		synchronized (inputBuffer) {
			String in = input.getText().trim();
			masterQ.add(in);

			//can put the following into a method
			resetStacks();
			// end method


			inputBuffer.add(in);
			inputBuffer.notify();
		}
		clearInput();
	}

	private void resetStacks() {
		Queue<String> toStack = new LinkedList<String>(masterQ);

		upStack.clear();
		downStack.clear();

		while(!toStack.isEmpty()) {
			upStack.push(toStack.poll());
		}
	}

	public void showHistory(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.UP)) {
			if (!upStack.isEmpty()) {
				String history = upStack.pop();
				downStack.push(history);
				setInput(history);
			}
		} else if (ke.getCode().equals(KeyCode.DOWN)) {
			if (!downStack.isEmpty()) {
				String history = downStack.pop();
				upStack.push(history);
				setInput(history);
			} else {
				setInput("");
			}
		}
	}

	private void processSyntaxMessage(String oldValue, String newValue) {
		String oldCommand = EMPTY_STRING;
		String newCommand = EMPTY_STRING;

		if (!oldValue.isEmpty() && oldValue != null) {
			oldCommand = StringUtil.getFirstWord(oldValue);
		}
		if (!newValue.isEmpty() && newValue != null) {
			newCommand = StringUtil.getFirstWord(newValue);
		}

		if (newCommand == null) {
			return;
		}

		if (newCommand.equals(oldCommand)) {
			return;
		} else {
			if (isValidCmd(newCommand)) {
				CommandProcessor cp = parser.CommandProcessor.getInstance();
				Command commandType = cp.getCmdType(newCommand);
				String syntaxMessage = newCommand + " " + commandType.getHelpInfo();
				setSyntaxMsg(syntaxMessage);
			} else {
				setSyntaxMsg(MSG_EMPTY);
			}
		}
	}

	private boolean isValidCmd(String input) {
		return CommandProcessor.getInstance().getEffectiveCmd(input) == null ? false : true;
	}

}
