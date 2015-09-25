/**
 * @author amoshydra
 * 
 */
package tds;
import java.util.List;
import java.util.TreeSet;

import tds.comparators.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import tds.TaskAttributeConstants;

public class TaskTree implements TaskCollection<Task> {
	
	private static final int TASK_NAME_TREE 		= TaskAttributeConstants.NAME;
	private static final int TASK_START_TIME_TREE 	= TaskAttributeConstants.START_TIME;
	private static final int TASK_END_TIME_TREE 	= TaskAttributeConstants.END_TIME;
	private static final int TASK_FLAG_TREE 		= TaskAttributeConstants.FLAG;
	private static final int TASK_PRIORITY_TREE 	= TaskAttributeConstants.PRIORITY;
	private static final int SIZE_OF_TASK_TREES = 5;
	
	private ArrayList <TreeSet<Task>> taskTrees;
	private int taskTreeSize;
	
	public TaskTree() {
		taskTreeSize = 0;
		taskTrees = new ArrayList<TreeSet<Task>>(SIZE_OF_TASK_TREES);
		
		taskTrees.add(TASK_NAME_TREE, new TreeSet<Task>(new NameComparator()));
		taskTrees.add(TASK_START_TIME_TREE, new TreeSet<Task>(new StartTimeComparator()));
		taskTrees.add(TASK_END_TIME_TREE, new TreeSet<Task>(new EndTimeComparator()));
		taskTrees.add(TASK_FLAG_TREE, new TreeSet<Task>(new FlagComparator()));
		taskTrees.add(TASK_PRIORITY_TREE, new TreeSet<Task>(new PriorityComparator()));
	}
	
	public TaskTree(Collection<Task> collection) {
		this();
		for (TreeSet<Task> tree : taskTrees) {
			tree.addAll(collection);
		}
	}

	@Override
	public void add(Task task) {
		for (TreeSet<Task> tree : taskTrees) {
			tree.add(task);
		}		
		increaseTaskListSize();
	}

	@Override
	public Task remove(Task task) {
		Task temp = task;
		for (TreeSet<Task> tree : taskTrees) {
			tree.remove(task);
		}
		decreaseTaskListSize();
		return temp;
	}

	@Override
	public Task replace(Task taskOld, Task taskNew) {
		Task temp = taskOld;
		remove(taskOld);
		add(taskNew);
		return temp;
	}

	@Override
	public List<Task> searchName(String searchTerm) {
		ArrayList<Task> resultList = new ArrayList<Task>(taskTreeSize);
		
		boolean isCaseInsensitive = isLowercase(searchTerm);
		String checkString;
		
		for (Task task : taskTrees.get(TASK_NAME_TREE)) {
			
			checkString = task.getName();
			if (isCaseInsensitive) {
				checkString = checkString.toLowerCase();
			}
			
			if (checkString.contains(searchTerm)) {
				resultList.add(task);
			}
		}
		return resultList;
	}

	private boolean isLowercase(String text) {
		int textLength = text.length();
		char charInText;
		
		for (int i = 0; i < textLength; i++) {
			charInText = text.charAt(i);
			if (charInText >= 'A' && charInText <= 'Z') {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Task> queryStartTime(long startTimeUpperBound, long startTimeLowerBound) {
		return query(taskTrees.get(TASK_START_TIME_TREE), startTimeUpperBound, startTimeLowerBound);
	}

	@Override
	public List<Task> queryEndTime(long endTimeUpperBound, long endTimeLowerBound) {
		return query(taskTrees.get(TASK_END_TIME_TREE), endTimeUpperBound, endTimeLowerBound);
	}

	public List<Task> query(TreeSet<Task> taskTree, long longLowerBound, long longUpperBound) {
		Task lowerBound = new Task("lower",longLowerBound,longLowerBound,0,0);
		Task upperBound = new Task("upper",longUpperBound,longUpperBound,0,0);
		
		taskTree.floor(lowerBound);
		taskTree.ceiling(upperBound);
		
		return new ArrayList<Task>(taskTree.subSet(lowerBound, upperBound));
	}
	
	@Override
	public List<Task> searchFlag(int flagSearch) {
		return search(taskTrees.get(TASK_FLAG_TREE), flagSearch);
	}

	@Override
	public List<Task> searchPriority(int prioritySearch) {
		return search(taskTrees.get(TASK_PRIORITY_TREE), prioritySearch);
	}
	
	public List<Task> search(TreeSet<Task> taskTree, int integerSearch) {
		Task lowerBound = new Task("lower",0,0,integerSearch,integerSearch);
		Task upperBound = new Task("upper",0,0,integerSearch+1,integerSearch+1);
		
		taskTree.floor(lowerBound);
		taskTree.ceiling(upperBound);
		
		return new ArrayList<Task>(taskTree.subSet(lowerBound, upperBound));
	}
	
	@Override
	public List<Task> getSortedList(Comparator<Task> comparator) {
		return null;
	}
	
	public List<Task> getSortedList(TreeSet<Task> taskTree) {
		ArrayList<Task> resultList = new ArrayList<Task>(taskTreeSize);
		for (Task task : taskTree) {
			resultList.add(task);
		}
		return resultList;
	}
	
	@Override
	public int size() {
		return taskTreeSize;
	}

	private void increaseTaskListSize() {
		taskTreeSize += 1;
	}
	private void decreaseTaskListSize() {
		taskTreeSize -= 1;
	}
}
