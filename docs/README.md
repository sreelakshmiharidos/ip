---

# **Gilu Task Manager - User Guide**

*A simple, efficient task manager with CLI and GUI support.*

---

## **Introduction**

Gilu Task Manager is a **command-line optimized** task manager with a **Graphical User Interface (GUI)**. It helps users efficiently organize their tasks, supporting **todos, deadlines, and events**.

---

## **Features**

✅ **Add Tasks**: Add **Todos, Deadlines, and Events**  
✅ **View Tasks**:
- `list`: View **all tasks**
- `list YYYY-MM-DD`: View **tasks for a specific date**  
  ✅ **Mark Tasks**: Mark or unmark tasks as done  
  ✅ **Delete Tasks**: Remove tasks from your list  
  ✅ **Find Tasks**: Search for tasks by keyword  
  ✅ **Sort Tasks**: Sort tasks into **Todos, Events, and Deadlines**  
  ✅ **Save Automatically**: All tasks persist across sessions  
  ✅ **User-Friendly GUI**: Simple and clean interface

---

## **Adding Todos**

A **todo task** is a task **without a deadline**.

### **Usage:**
```sh
todo TASK_NAME
```

### **Example:**
```sh
todo Read a book
```

### **Expected Output:**
```
Got it. I've added this task:
[T][ ] Read a book
Now you have 1 task in the list.
```

---

## **Adding Deadlines**

You can add a **deadline task** with a **specific due date**.

### **Usage:**
```sh
deadline TASK_NAME /by YYYY-MM-DD HHmm
```

### **Example:**
```sh
deadline Submit assignment /by 2025-03-10 2359
```

### **Expected Output:**
```
Got it. I've added this task:
[D][ ] Submit assignment (by: Mar 10 2025 23:59)
Now you have 2 tasks in the list.
```

---

## **Adding Events**

Events have a **start and end time**.

### **Usage:**
```sh
event TASK_NAME /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm
```

### **Example:**
```sh
event Project meeting /from 2025-03-10 0900 /to 2025-03-10 1100
```

### **Expected Output:**
```
Got it. I've added this task:
[E][ ] Project meeting (from: Mar 10 2025 09:00 to: Mar 10 2025 11:00)
Now you have 3 tasks in the list.
```

---

## **Listing Tasks**

### **1. List All Tasks**
Displays **all tasks** currently stored.

```sh
list
```

**Example Output:**
```
Here are the tasks in your list:
1. [T][ ] Read a book
2. [D][ ] Submit assignment (by: Mar 10 2025 23:59)
3. [E][ ] Project meeting (from: Mar 10 2025 09:00 to: Mar 10 2025 11:00)
```

### **2. List Tasks for a Specific Date**
Displays **only tasks** that match the given date.

```sh
list YYYY-MM-DD
```

**Example:**
```sh
list 2025-03-10
```

**Expected Output:**
```
Here are the tasks on Mar 10 2025:
1. [D][ ] Submit assignment (by: Mar 10 2025 23:59)
2. [E][ ] Project meeting (from: Mar 10 2025 09:00 to: Mar 10 2025 11:00)
```

---

## **Sorting Tasks**

Sorts and **categorizes** tasks into **Todos, Events, and Deadlines**.

### **Usage:**
```sh
sort
```

### **Expected Output:**
```
Here is your sorted task list:

Events:
1. [E][ ] Project meeting (from: Mar 10 2025 09:00 to: Mar 10 2025 11:00)

Deadlines:
1. [D][ ] Submit assignment (by: Mar 10 2025 23:59)

Todos:
1. [T][ ] Read a book
```

---

## **Marking and Unmarking Tasks**

### **1. Mark a Task as Done**
```sh
mark TASK_INDEX
```

✅ **Example:**
```sh
mark 1
```

✅ **Expected Output:**
```
Cool! I've marked this task as done:
[T][X] Read a book
```

### **2. Unmark a Task**
```sh
unmark TASK_INDEX
```

✅ **Example:**
```sh
unmark 1
```

✅ **Expected Output:**
```
No problem! I've marked this task as not done:
[T][ ] Read a book
```

---

## **Deleting Tasks**

Removes a task from the list.

### **Usage:**
```sh
delete TASK_INDEX
```

✅ **Example:**
```sh
delete 1
```

✅ **Expected Output:**
```
Noted. I've removed this task:
[T][ ] Read a book
Now you have 2 tasks in the list.
```

---

## **Finding Tasks**

Search for tasks **by keyword**.

### **Usage:**
```sh
find KEYWORD
```

✅ **Example:**
```sh
find book
```

✅ **Expected Output:**
```
Here are the matching tasks:
1. [T][ ] Read a book
```

---

## **Exiting Gilu**

### **Usage:**
```sh
bye
```

✅ **Expected Output:**
```
Bye for now! But I hope to see you again soon!
```

All tasks **are automatically saved** before exiting.

---

## **Command Summary**

| **Action**       | **Command Format & Example**                                                                                                      |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------------|
| **Add Todo**    | `todo TASK_NAME` <br> e.g., `todo Read a book`                                                                                    |
| **Add Deadline** | `deadline TASK_NAME /by YYYY-MM-DD HHmm` <br> e.g., `deadline Submit report /by 2025-03-10 2359`                                  |
| **Add Event**   | `event TASK_NAME /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm` <br> e.g., `event Meeting /from 2025-03-10 0900 /to 2025-03-10 1100`  |
| **List All Tasks**  | `list`                                                                                                                          |
| **List Tasks for a Date**  | `list YYYY-MM-DD` <br> e.g., `list 2025-03-10`                                                                          |
| **Sort Tasks**  | `sort`                                                                                                                             |
| **Mark Task**   | `mark TASK_INDEX` <br> e.g., `mark 2`                                                                                              |
| **Unmark Task** | `unmark TASK_INDEX` <br> e.g., `unmark 3`                                                                                          |
| **Delete Task** | `delete TASK_INDEX` <br> e.g., `delete 1`                                                                                          |
| **Find Tasks**  | `find KEYWORD` <br> e.g., `find book`                                                                                              |
| **Exit**        | `bye`                                                                                                                              |

---

### **Happy Task Managing with Gilu!**

---