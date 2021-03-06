//@@author A0126394B
package taskCollections;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A task object used to store task name, time and different attributes.
 *
 * @author amoshydra
 */
public class Task implements Comparable<Task> {
	private static int taskNumber = 0;

	/**
	 * Field value constant for flag attribute.
	 */
	public enum FLAG_TYPE {
		NULL(0), DONE(1);

		private final int value;

		private FLAG_TYPE(int value) {
			this.value = value;
		}

		int getValue() {
			return value;
		}

		private static final Map<Integer, FLAG_TYPE> lookup = new HashMap<Integer, FLAG_TYPE>();

		static {
			for (FLAG_TYPE f : EnumSet.allOf(FLAG_TYPE.class))
				lookup.put(f.getValue(), f);
		}

		static FLAG_TYPE get(int value) {
			return lookup.get(value);
		}
	};

	/**
	 * Field value constant for priority attribute.
	 */
	public enum PRIORITY_TYPE {
		HIGH(0), NORMAL(1), LOW(2);

		private final int value;

		private PRIORITY_TYPE(int value) {
			this.value = value;
		}

		int getValue() {
			return value;
		}

		private static final Map<Integer, PRIORITY_TYPE> lookup = new HashMap<Integer, PRIORITY_TYPE>();

		static {
			for (PRIORITY_TYPE p : EnumSet.allOf(PRIORITY_TYPE.class))
				lookup.put(p.getValue(), p);
		}

		static PRIORITY_TYPE get(int value) {
			return lookup.get(value);
		}
	};

	/**
	 * Field value for start time or end time attribute indicating an empty
	 * date.
	 */
	public final static int DATE_NULL = 0;
	public final static int DATE_START = 1;
	private final static String EMPTY_STRING = "";
	private final static String TO_STRING_DELIMETER = "|";
	private final static String TO_STRING_START = "";
	private final static String TO_STRING_END = "";

	private int id;
	private String name;
	private String description;
	private long startTime;
	private long endTime;
	private FLAG_TYPE flag;
	private PRIORITY_TYPE priority;

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time, flag and priority as the argument. An
	 * internal ID will be used to differentiate duplicated value.
	 *
	 * @param name
	 *            the name of the newly constructed {@code Task}
	 * @param startTime
	 *            the starting time of the newly constructed {@code Task} in
	 *            UNIX format
	 * @param endTime
	 *            the ending time of the newly constructed {@code Task} in UNIX
	 *            format
	 * @param flag
	 *            the given flag field; Task marked as done is specified with
	 *            this flag.
	 * @param priority
	 *            the given priority field;
	 *
	 */
	public Task(String name, long startTime, long endTime, FLAG_TYPE flag, PRIORITY_TYPE priority) {
		this(name, EMPTY_STRING, startTime, endTime, flag, priority);
	}

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time, flag and priority as the argument. The
	 * ID will be specified through fileProcessor only. Do not use this to add
	 * new task from user.
	 *
	 * @param id
	 *            the index number used to identify this task.
	 * @param name
	 *            the name of the newly constructed {@code Task}
	 * @param startTime
	 *            the starting time of the newly constructed {@code Task} in
	 *            UNIX format
	 * @param endTime
	 *            the ending time of the newly constructed {@code Task} in UNIX
	 *            format
	 * @param flag
	 *            the given flag field; Task marked as done is specified with
	 *            this flag.
	 * @param priority
	 *            the given priority field;
	 *
	 */
	public Task(int id, String name, long startTime, long endTime, FLAG_TYPE flag, PRIORITY_TYPE priority) {
		this(id, name, EMPTY_STRING, startTime, endTime, flag, priority);
	}

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time, flag and priority as the argument. The
	 * ID will be specified through fileProcessor only. Do not use this to add
	 * new task from user.
	 *
	 * @param id
	 *            the index number used to identify this task.
	 * @param name
	 *            the name of the newly constructed {@code Task}
	 * @param description
	 *            the description of the newly constructed {@code Task}
	 * @param startTime
	 *            the starting time of the newly constructed {@code Task} in
	 *            UNIX format
	 * @param endTime
	 *            the ending time of the newly constructed {@code Task} in UNIX
	 *            format
	 * @param flag
	 *            the given flag field; Task marked as done is specified with
	 *            this flag.
	 * @param priority
	 *            the given priority field;
	 *
	 * @deprecated Description field is deprecated. Use the original constructor
	 *             without description instead.
	 *
	 */
	public Task(int id, String name, String description, long startTime, long endTime, FLAG_TYPE flag,
			PRIORITY_TYPE priority) {

		this.id = id;
		if (id >= taskNumber) {
			taskNumber = id;
			taskNumber++;
		}

		this.name = name;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
		this.priority = priority;
	}

	/**
	 * Initializes a newly created {@code Task} object so that it store the
	 * name, starting time, ending time, flag and priority as the argument. An
	 * internal ID will be used to differentiate duplicated value.
	 *
	 * @param name
	 *            the name of the newly constructed {@code Task}
	 * @param description
	 *            the description of the newly constructed {@code Task}
	 * @param startTime
	 *            the starting time of the newly constructed {@code Task} in
	 *            UNIX format
	 * @param endTime
	 *            the ending time of the newly constructed {@code Task} in UNIX
	 *            format
	 * @param flag
	 *            the given flag field; Task marked as done is specified with
	 *            this flag.
	 * @param priority
	 *            the given priority field;
	 *
	 * @deprecated Description field will be deprecated. Use the original
	 *             constructor without description instead.
	 *
	 */
	public Task(String name, String description, long startTime, long endTime, FLAG_TYPE flag, PRIORITY_TYPE priority) {
		this.id = taskNumber++;
		this.name = name;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
		this.priority = priority;
	}

	static Task getVirtualTask() {
		Task temp = new Task(taskNumber, EMPTY_STRING, EMPTY_STRING, DATE_NULL, DATE_NULL, FLAG_TYPE.NULL,
				PRIORITY_TYPE.NORMAL);
		taskNumber--;
		return temp;
	}

	/**
	 * Returns the id of this task in {@code int}.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of this task in {@code String}.
	 *
	 * @return the name of this task.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of this task in {@code String}.
	 *
	 * @return the description of this task.
	 * @deprecated Description field is deprecated.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the start time of this task in {@code long}.
	 *
	 * @return the start time of this task.
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Returns the end time of this task in {@code long}.
	 *
	 * @return the end time of this task.
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * Returns the flag of this task in {@code int}.
	 *
	 * @return the flag of this task.
	 */
	public FLAG_TYPE getFlag() {
		return flag;
	}

	/**
	 * Returns the priority of this task in {@code int}.
	 *
	 * @return the priority of this task.
	 */
	public PRIORITY_TYPE getPriority() {
		return priority;
	}

	/**
	 * Change the id of this task.
	 *
	 * @param id
	 *            the new id for the task.
	 */
	public void setId(int id) {
		this.id = id;

	}

	/**
	 * Change the name of this task.
	 *
	 * @param name
	 *            the new name for the task.
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Change the description of this task.
	 *
	 * @param description
	 *            the new description for the task.
	 * @deprecated Description field is deprecated.
	 */
	void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Change the start time of this task.
	 *
	 * @param startTime
	 *            the new start time for the task.
	 */
	void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Change the end time of this task.
	 *
	 * @param endTime
	 *            the new end time for the task.
	 */
	void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * Change the flag of this task.
	 *
	 * @param flag
	 *            the new flag for the task.
	 */
	void setFlag(FLAG_TYPE flag) {
		this.flag = flag;
	}

	/**
	 * Change the priority of this task.
	 *
	 * @param priority
	 *            the new priority for the task.
	 */
	void setPriority(PRIORITY_TYPE priority) {
		this.priority = priority;
	}

	/**
	 * @return a hash code value for this object enumerated using the ID of this
	 *         {@code Task}.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * Determines whether or not two task are equal. The two tasks are equal if
	 * the values name, start time, end time, flag and priority are equal.
	 *
	 * @return {@code true} if the object to be compared is an instance of Task
	 *         and has the same attributes; false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Task))
			return false;
		if (obj == this)
			return true;

		Task rhs = (Task) obj;
		return (name.equals(rhs.name)) && (startTime == rhs.startTime) && (endTime == rhs.endTime) && (flag == rhs.flag)
				&& (priority == rhs.priority);
	}

	/**
	 * Compares this {@code Task} instance with another based on the order they
	 * are created.
	 *
	 * @return the value 0 if this {@code Task} is equal to the argument
	 *         {@code Task}; a value less than 0 if this {@code Task} is created
	 *         earlier than the argument {@code Task}; and a value greater than
	 *         0 if this {@code Task} is created later than the argument
	 *         {@code Task}.
	 */
	@Override
	public int compareTo(Task rhs) {
		return this.compareIdTo(rhs);
	}

	/**
	 * Compares this {@code Task} instance with another to generate an array of
	 * check bits.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return a boolean array with all element as {@code true} if the
	 *         {@code Task} is identical to the other; A difference in
	 *         attributes between two task will cause a boolean in the array to
	 *         become false.
	 */
	public boolean[] getAttributesDiff(Task rhs) {
		boolean[] checkBits = new boolean[Attributes.NUM_OF_ATTRIBUTES];

		checkBits[Attributes.TYPE.NAME.getValue()] = (this.name.equals(rhs.name));
		checkBits[Attributes.TYPE.START_TIME.getValue()] = (this.startTime - rhs.startTime == 0);
		checkBits[Attributes.TYPE.END_TIME.getValue()] = (this.endTime - rhs.endTime == 0);
		checkBits[Attributes.TYPE.FLAG.getValue()] = (this.flag.value - rhs.flag.value == 0);
		checkBits[Attributes.TYPE.PRIORITY.getValue()] = (this.priority.value - rhs.priority.value == 0);
		checkBits[Attributes.TYPE.ID.getValue()] = (this.compareIdTo(rhs) == 0);

		return checkBits;
	}

	/**
	 * Compares the name of this {@code Task} instance with another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the name of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this name is
	 *         lexicographically less than the argument name; a value greater
	 *         than 0 if this name is lexicographically greater than the
	 *         argument name.
	 */
	public int compareNameTo(Task rhs) {
		int result = this.name.compareTo(rhs.name);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the starting time of this {@code Task} instance with another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the starting time of this {@code Task} is equal to
	 *         the argument {@code Task}; a value less than 0 if this starting
	 *         time is numerically less than the argument starting time; a value
	 *         greater than 0 this starting time is numerically greater than the
	 *         argument starting time.
	 */
	public int compareStartTimeTo(Task rhs) {
		Long startTimeLongThis = new Long(this.startTime);
		Long startTimeLongRhs = new Long(rhs.startTime);
		int result = startTimeLongThis.compareTo(startTimeLongRhs);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the ending time of this {@code Task} instance with another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the ending time of this {@code Task} is equal to
	 *         the argument {@code Task}; a value less than 0 if this ending
	 *         time is numerically less than the argument ending time; a value
	 *         greater than 0 this ending time is numerically greater than the
	 *         argument ending time.
	 */
	public int compareEndTimeTo(Task rhs) {
		Long endTimeLongThis = new Long(this.endTime);
		Long endTimeLongRhs = new Long(rhs.endTime);
		int result = endTimeLongThis.compareTo(endTimeLongRhs);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the starting and ending time of this {@code Task} instance with
	 * another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the time of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this time is
	 *         numerically less than the argument time; a value greater than 0
	 *         this time is numerically greater than the argument time.
	 */
	public int compareTimeTo(Task rhs) {

		Long thisTime = this.getStartTime();
		if (thisTime == Task.DATE_NULL) {
			thisTime = this.getEndTime();
		}

		Long rhsTime = rhs.getStartTime();
		if (rhsTime == Task.DATE_NULL) {
			rhsTime = rhs.getEndTime();
		}

		int result = thisTime.compareTo(rhsTime);
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the flag of this {@code Task} instance with another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the flag of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this flag time is
	 *         numerically less than the argument flag; a value greater than 0
	 *         this flag is numerically greater than the argument flag.
	 */
	public int compareFlagTo(Task rhs) {
		int result = this.flag.value - rhs.flag.value;
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the priority of this {@code Task} instance with another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the priority of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this priority time
	 *         is numerically less than the argument priority; a value greater
	 *         than 0 this priority is numerically greater than the argument
	 *         priority.
	 */
	public int comparePriorityTo(Task rhs) {
		int result = this.priority.value - rhs.priority.value;
		return handleDuplicatedAttributes(this, rhs, result);
	}

	/**
	 * Compares the ID of this {@code Task} instance with another.
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 *
	 * @return the value 0 if the ID of this {@code Task} is equal to the
	 *         argument {@code Task}; a value less than 0 if this ID is
	 *         numerically less than the argument ID; a value greater than 0
	 *         this ID is numerically greater than the argument ID.
	 */
	public int compareIdTo(Task rhs) {
		return (this.id - rhs.id);
	}

	/**
	 * Allow comparator to differentiate two duplicated attributes via its ID of
	 * creation
	 *
	 * @param rhs
	 *            a {@code Task} to be compared with this {@code Task}
	 * @param result
	 *            result obtained from previous comparison
	 *
	 * @return the original result if it is already different. Otherwise, the
	 *         difference in ID is returned
	 */
	private static int handleDuplicatedAttributes(Task lhs, Task rhs, int result) {
		if (result == 0) {
			return lhs.compareIdTo(rhs);
		} else {
			return result;
		}
	}

	/**
	 * Allow Task class ID to be reset to a desired ID. This method is intended
	 * for internal testing only.
	 *
	 * @param resetCountId
	 *            ID to be reset to
	 */
	public static void resetTaskClassId(int resetCountId) {
		taskNumber = resetCountId;
	}

	/**
	 * Allow Task class ID to be reset to its starting value, 0. This method is
	 * intended for internal testing only.
	 *
	 */
	public static void resetTaskClassId() {
		resetTaskClassId(0);
	}

	/**
	 * Represent this {@code Task} into a {@code String} format
	 *
	 * @return a string representation of this task in the format such as:
	 *
	 *         <pre>
	 *         name | startTime | endTime | flag | priority
	 *         </pre>
	 */
	@Override
	public String toString() {
		return TO_STRING_START + id + TO_STRING_DELIMETER + name + TO_STRING_DELIMETER + startTime + TO_STRING_DELIMETER
				+ endTime + TO_STRING_DELIMETER + flag + TO_STRING_DELIMETER + priority + TO_STRING_END;
	}
}