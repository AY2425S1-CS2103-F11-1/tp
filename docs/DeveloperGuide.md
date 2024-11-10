---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TAchy Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**
The following table acknowledges numerous 3rd party libraries, API and documentation that were used in the development of TAchy.

| Name                                                                              | Description                                                                                                              |
|-----------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| [AddressBook-Level 3 (AB-3)](https://se-education.org/addressbook-level3/)        | TAchy is a brownfield project adapted from AB-3, which was created by the [SE-EDU initiative](https://se-education.org). |
| [Jackson](https://github.com/FasterXML/jackson)                                   | Used for parsing JSON files.                                                                                             |
| [Gradle](https://gradle.org/)                                                     | Used for build automation                                                                                                |
| [JavaFX](https://openjfx.io)                                                      | Used in rendering the GUI.                                                                                               |
| [JUnit5](https://junit.org/junit5/)                                               | Used for testing the codebase.                                                                                           |
| [Oracle Java Docs](https://docs.oracle.com/en/java/javase/11/docs/api/index.html) | Used for understanding the default Java API                                                                              |

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete_student 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `StudentListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Student` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete_student 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete_student 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a student).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the currently 'selected' `Student` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Student>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Student` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Student` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**


### Introduction
This section of the developer Guide covers how the features are implemented and executed.

### Features Overview
Listed below are the implemented features, and a brief description of what they do.
* **Adding a new Assignment**: Adds a new Assignment for a particular student
* **Grading an existing Assignment**: Grades an existing assignment belonging to an existing student
* **Deleting an existing Assignment**: Deletes an existing assignment belonging to an existing student
* **Viewing a student's details**: Displays students details along with the assignments associated with the student
* **Adding a remark to a student**: Adds a remark to an existing student

## Adding a new Assignment
This `add_assignment` command is a feature that  allows the user to create a new assignment, based on the student's index. The assignment must have an alphanumeric `Name`, and a valid `Max Score`.

#### Design Considerations:
Previously, AB3 had a single command word for the `add` commands. As TAchy is a brownfield project, additional objects are now allowed to be instantiated via the `add` commmand.
In particular, TAchy has 2 features that "add" items to the app. Firstly, there is an `add_student` feature, and also an `add_assignment` feature.

**Aspect: How to distinguish between the different 'add' commands:**
* Alternative 1 (current choice): Distinguish command word, and thus allow only 1 type of object to be created at a time.
  * Pros: Easy to implement, and less prone to Users making errors in command format
  * Cons: User will only be able to add either students or assignments, one at a time

* Alternative 2: Allow multiple objects to be created concurrently.
  * Pros: Allows adept users to create new objects in the app more quickly
  * Cons: Difficult to implement, and users are prone to making more mistakes in supplying the valid parameters used.


#### Implementation
* LogicManager executes the command "add_assignment".
* LogicManager parses the command via AddressBookParser.
* AddressBookParser creates and delegates parsing to AddAssignmentCommandParser.
* AddAssignmentCommandParser creates an AddAssignmentCommand object.
* AddAssignmentCommand is returned to AddAssignmentCommandParser and back to AddressBookParser.
* LogicManager then executes AddAssignmentCommand, which interacts with the Model.
* AddAssignmentCommand creates a CommandResult object.
* CommandResult is returned to AddAssignmentCommand, which returns the result to LogicManager.
* The sequence concludes with the return to the caller from LogicManager.
* This describes the flow of command execution, parsing, and interaction with the model.

#### Example invocation sequence for AddAssignmentCommand
<puml src="diagrams/add-assignment/AddAssignmentSequenceDiagram.puml" alt="Interactions inside the Logic Component for the `add_assignment` Command" />

#### Example activity diagram for AddAssignmentCommand
<puml src="diagrams/add-assignment/AddAssignmentActivityDiagram.puml" alt="Activities inside the Logic Component for the `add_assignment` Command" />


## Viewing a student's details
This `view_student` command is a feature that allows the user to expand and view all of a student's details in the details panel, based on the student's index.
Additional details such as a student's assignments and remarks, that are not visible in the list panel, will now also be visible in the details panel.

#### Design Considerations:
In order to select a student to be displayed on the detail's panel, the student has to be identified from the list of contacts.

**Aspect: Identifying the student to be displayed on the panel:**
* Alternative 1 (current choice): Identify student by the student index.
    * Pros: Easy to implement, and less prone to Users making errors in command format.
    * Cons: The student index in the list may change every time the list is filtered, which may be confusing for the user as he has to check for the student index every time he calls this command.

* Alternative 2: Identify student by their name.
    * Pros: User will not have to check for the student index in the list every time he wants to call this command.
    * Cons: As TAchy allows multiple contacts to be saved with the same name, additional fields such as phone number will have to be used in conjunction with "name" in order to differentiate between them. This will make the implementation more complex.

#### Implementation
* LogicManager executes the command "view_student".
* LogicManager parses the command via AddressBookParser.
* AddressBookParser creates and delegates parsing to ViewStudentCommandParser.
* ViewStudentCommandParser creates an ViewStudentCommand object.
* ViewStudentCommand is returned to ViewStudentCommandParser and back to AddressBookParser.
* LogicManager then executes ViewStudentCommand, which interacts with the Model.
* ViewStudentCommand creates a CommandResult object.
* CommandResult is returned to ViewStudentCommand, which returns the result to LogicManager.
* The sequence concludes with the return to the caller from LogicManager.
* This describes the flow of command execution, parsing, and interaction with the model.

#### Example invocation sequence for ViewStudentCommand
<puml src="diagrams/ViewStudentSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `view_student` Command" />

## Adding a remark to a student
This `remark` command allows the user to add a remark for a specific student.
The user must specify the student's index from the displayed list, followed by the remark to be added.

#### Design Considerations:
In order to add a remark to a student, the user must provide a number representing the student's index and remark string to get attached to that selected student from the list.

**Aspect: Identifying the student to add a remark to:**
* Alternative 1 (current choice): Identify the student by their index.
    * Pros: Straightforward and easy to implement. Users only need to reference the index in the list to add a remark, which makes it less error-prone.
    * Cons: The student index may change every time the list is filtered, so the user has to check for the student index every time the user before calling this command.

* Alternative 2: Identify student by their name.
    * Pros: Users do not have to worry about filtered indexes, so the user does not have to check the list every time before calling this command.
    * Cons: Since TAchy allows multiple contacts with the same name, the system may encounter ambiguity when handling duplicate names, so additional fields are required to uniquely identify the contacts.

#### Implementation
* LogicManager executes the command "remark".
* LogicManager parses the command via AddressBookParser.
* AddressBookParser creates and delegates parsing to RemarkCommandParser.
* RemarkCommandParser creates a RemarkCommand object.
* RemarkCommand is returned to RemarkCommandParser and back to AddressBookParser.
* LogicManager then executes RemarkCommand, which interacts with the Model.
* RemarkCommand creates a CommandResult object.
* CommandResult is returned to RemarkCommand, which returns the result to LogicManager.
* The sequence concludes with the return to the caller from LogicManager.
* This describes the flow of command execution, parsing, and interaction with the model.

#### Example invocation sequence for RemarkCommand
<puml src="diagrams/RemarkSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `remark` Command" />

#### Example activity diagram for RemarkCommand
<puml src="diagrams/add-remark/RemarkCommandActivityDiagram.puml" alt="Activities inside the Logic Component for the `remark` Command" />

## Planned Enhancements
Team size: 5
1. **Make tag names wrap if it exceeds the UI display:** The current display of tag names results in them exceeding the UI display if they are too long. Although it can be mitigated by enlarging the window size, it relies on using the mouse, which is not CLI-friendly.
2. **Display student information in the window after the Add Student command:** The current implementation does not display the student information in the window after the Add Student command, unlike other commands like the Edit Student command. This should be fixed to maintain consistency.
3. **Display more accurate error messages for unexpected prefixes and '/'s:** The current implementation will display a ```Index should be a non-zero unsigned integer and cannot be blank``` error message for a command that contains prefixes or '/'s that are not expected e.g. ```edit_student si/1 r/math```. However, it should display the command's respective invalid command message to facilitate better understanding.

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Tech-savvy Tuition Teachers

**Value proposition**:

* Provide a fast CLI for tutors to manage and track students' work and submissions.
* Provide a good UI that serves as a dashboard to see existing student details.


### User stories

Priorities: Very high (must have) - `****`,  High (good to have) - `***`, Medium (should have) - `**`, Low (unlikely to have) -  `*`,

| Priority | As a …​                         | I want to …​                          | So that I can…​                                           |
|----------|---------------------------------|---------------------------------------|-----------------------------------------------------------|
| `****`   | teacher with multiple students  | add a new student                     | store important data about my students                    |
| `****`   | teacher with multiple students  | view each student's details           | have a view on each student's progress                    |
| `****`   | teacher                         | delete a student's information        | remove students who are no longer part of my class        |
| `****`   | teacher                         | add assignments                       | track assignments for students                            |
| `****`   | teacher                         | add an assignment score to a student  | grade my students work                                    |
| `****`   | teacher                         | delete assignments                    | remove old assignments                                    |
| `****`   | teacher                         | save data locally                     | access them at a later time                               |
| `****`   | teacher  who grades assignments | edit status of submitted assignments  | keep track if a student has submitted an assignment       |
| `***`    | teacher                         | create remark for individual students | not forget any special consideration for certain students |
| `***`    | teacher                         | edit assignments                      | ensure that the assignments are up to date                |
*{More to be added}*

### Use cases

(For all use cases below, the **System** is the app `TAchy` and the **Actor** is the `tuition teacher`,
unless specified otherwise)

**Use case: Add a student**

**MSS**

1. Teacher requests to add a student
2. TAchy adds the student to the student list

    Use case ends.

**Extensions**

* 1a. The student name is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The student information already exists.
    * 1b1. TAchy an error message of duplicate student.
    * 1b2. Teacher adds the student again by providing a different name.

      Use case resumes at step 2.

---

**Use case: Delete a student**

**MSS**

1. Teacher requests to list the students
2. Teacher requests to delete a student by index
3. TAchy deletes the student from the student list

    Use case ends.

**Extensions**

* 1a. There is no students in the app.
    * 1a1. TAchy displays an error message.

      Use case ends.

* 2a. The student index is invalid.
    * 2a1. TAchy shows an error message.

      Use case ends.

---

**Use case: List students**

**MSS**

1. Teacher requests to list students.
2. TAchy displays the list of students in the current class.

   Use case ends.

**Extensions**

* 1a. There is no students in the app.
    * 1a1. TAchy displays a "no students" message.

      Use case ends.

---

**Use case: Find a student**

**MSS**

1. Teacher requests to search for a student by name.
2. TAchy displays the student(s) matching the search query.

   Use case ends.

**Extensions**

* 1a. No student matches the query.
    * 1a1. TAchy displays a "no students found" message.

      Use case ends.

---

**Use case: Add assignment**

**MSS**

1. Teacher requests to add an assignment to a student by index.
2. TAchy adds the assignment to the student's assignment list.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The assignment name is invalid.
    * 1b1. TAchy shows an error message.

      Use case ends.

* 1c. The assignment already exists under the student.
    * 1c1. TAchy shows a warning message.

      Use case ends.

---

**Use case: Delete assignment**

**MSS**

1. Teacher requests to delete an assignment for a student by index.
2. TAchy removes the assignment from the student's assignment list.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The assignment index is invalid.
    * 1b1. TAchy shows an error message.

      Use case ends.

---

**Use case: Edit assignment**

**MSS**

1. Teacher requests to edit an assignment for a student by index.
2. TAchy modifies the assignment detail in the student's assignment list.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The assignment index is invalid.
    * 1b1. TAchy shows an error message.

      Use case ends.

---


**Use case: Mark assignment**

**MSS**

1. Teacher requests to mark an assignment as submitted for a student by index.
2. TAchy modifies the assignment submission status in the student's assignment list.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The assignment index is invalid.
    * 1b1. TAchy shows an error message.

      Use case ends.

* 1c. The assignment has already been marked as submitted.
    * 1c1. TAchy shows an error message.

      Use case ends.
---


**Use case: Unmark assignment**

**MSS**

1. Teacher requests to mark an assignment as not submitted for a student by index.
2. TAchy modifies the assignment submission status in the student's assignment list and resets the score to 0.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The assignment index is invalid.
    * 1b1. TAchy shows an error message.

      Use case ends.

* 1c. The assignment has already been unmarked, or has not yet been submitted.
    * 1c1. TAchy shows an error message.

      Use case ends.

---

**Use case: Grade assignment**

**MSS**

1. Teacher requests to add a grade for a student’s assignment by index.
2. TAchy records the grade for the student’s assignment and sets the assignment submission status as 'submitted'.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The assignment index is invalid.
    * 1b1. TAchy shows an error message.

      Use case ends.

* 1c. The grade is invalid.
    * 1c1. TAchy shows an error message.

      Use case ends.

* 1d. The student has already been graded for the assignment.
    * 1d1. TAchy shows a warning message.

      Use case resumes at step 2.

---

**Use case: Add remark for an individual student**

**MSS**

1. Teacher requests to add a remark for a student by index.
2. TAchy records the remark in the student’s profile.

   Use case ends.

**Extensions**

* 1a. The student index is invalid.
    * 1a1. TAchy shows an error message.

      Use case ends.

* 1b. The student already has a remark.
    * 1b1. TAchy shows a warning message.
    * 1b2. TAchy asks if the Teacher wants to overwrite the remark.
    * 1b3. Teacher confirms the overwrite.
    * 1b4. TAchy records the new remark.

      Use case resumes at step 2.

### Planned Enhancements:
Team size: 5

1. Update Result display box to enable wrapping, and be of greater vertical length, so that users do not need to scroll in order to read the result display box.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 students without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

- Business/domain rules: Students cannot have duplicate names. If a tutor has 2 or more students with the same name (e.g: Nicholas Tan and Nicholas Tan), the tutor must add an additional word to the name to differentiate the students (e.g: Nicholas Tan RJC and Nicholas Tan HCI).
- Constraints: The system must be backward compatible with data produced by earlier versions of the system, The total project cost should be $0, The project is offered as a free service, TAs are only allowed to store up to 5 GB of data
- Technical requirements: The system should work on both 32-bit and 64-bit environment, The system should be compatible with Windows, macOS and Linux operating systems.
- Performance requirements: The system should respond to user inputs within five seconds, The system should be able to handle a large number of students, classes, and assignments without degradation in performance, Data retrieval should not take longer than 2 seconds.
- Quality requirements: The system should be usable by a novice who has never used AB3 before, The system should have clear user documentation to guide users through its features, Intuitive error messages will be displayed to the user so that they know what is the correct method of using the system
- Process requirements: The project is expected to adhere to the milestones which are added every week
- Notes about project scope: The system is not required to integrate with third-party systems (e.g. Canvas). The system is not required to generate or print detailed reports of class performance or assignment scores. The system will only support English as the user interface language. The system will not be deployed on the cloud and will only run locally.
- Any other noteworthy points: The system must ensure data privacy by adhering to relevant data protection regulations. The system should not use any language or imagery that may be offensive to students or faculty members from different cultural backgrounds.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Duplicate Names**: Names for students are considered to be duplicates even if the capitalisation is different (e.g: 'Nicholas' is a duplicate of 'nicholas')
* **Private student detail**: A student detail that is not meant to be shared with others
* **Teacher** : Interchangeable with Tutor. This app is meant for Private tutors who teach multiple students

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>
**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.
</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a student

1. **Adding a student with valid details**
    1. Test case: `add_student n/John Doe p/98765432 e/johnd@example.com`<br>
       Expected: A new student named "John Doe" with the specified phone number and email is added to the student list. Status message indicates successful addition.
    2. Test case: `add_student n/Betsy Crowe p/91234567 e/betsycrowe@example.com t/friend`<br>
       Expected: A new student named "Betsy Crowe" with a "friend" tag is added. Status message shows the addition success message.

2. **Adding a student with invalid details**
    1. Test case: `add_student n/John Doe p/phone e/johnd@example.com`<br>
       Expected: No student is added. Error message shown, indicating invalid phone number format.
    2. Other incorrect add commands to try: `add_student`, `add_student n/ e/`, `add_student n/John Doe e/invalid-email`<br>
       Expected: No student is added. Error messages shown, indicating which fields are invalid.

### Viewing a student

1. **Viewing a specific student**
    1. Prerequisites: 1 or more students listed using the `list` command.
    2. Test case: `view_student 2`<br>
       Expected: Detailed view of the second student in the list is displayed.
    3. Test case: `view_student 0`<br>
       Expected: No student is displayed. Error message shown in the status bar.
    4. Test case: `view_student x` (where `x` is greater than the list size)<br>
       Expected: No student is displayed. Error message indicating the index is out of bounds.



### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command. Multiple students in the list.

   2. Test case: `delete_student 1`<br>
      Expected: First student is deleted from the list. Details of the deleted student shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete_student 0`<br>
      Expected: No student is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete_student`, `delete_student x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Editing a student

1. **Editing a student’s details with valid inputs**
    1. Prerequisites: A student list containing at least one student.
    2. Test case: `edit_student si/1 p/98765432`<br>
       Expected: The first student's phone number is updated to "98765432". Success message shown in the status bar.
    3. Test case: `edit_student si/1 n/John Smith t/friend`<br>
       Expected: The first student's name is changed to "John Smith", and a "friend" tag is added.

2. **Editing a student’s details with invalid inputs**
    1. Test case: `edit_student si/1 p/invalidphone`<br>
       Expected: No changes made. Error message indicates invalid phone number format.
    2. Test case: `edit_student si/2 n/`<br>
       Expected: No changes made. Error message shown, indicating name cannot be blank.

### Finding students by name

1. **Finding students with existing names**
    1. Prerequisites: The student list has multiple students with varying names.
    2. Test case: `find John`<br>
       Expected: Students with "John" in their names are listed. Status message shows number of matches.
    3. Test case: `find alex david`<br>
       Expected: Students with names containing either "alex" or "david" are listed.

2. **Finding students with no matches**
    1. Test case: `find nonexistent`<br>
       Expected: No students are listed. Status message indicates no matches found.

### Adding an assignment

1. **Adding a valid assignment**
    1. Prerequisites: The student list includes at least one student.
    2. Test case: `add_assignment si/1 an/Assignment 1 ms/100`<br>
       Expected: An assignment named "Assignment 1" with a maximum score of 100 is added to the first student. Success message displayed.

2. **Adding an invalid assignment**
    1. Test case: `add_assignment si/1 an/AssignmentWithInvalidCharacters@ ms/100`<br>
       Expected: No assignment is added. Error message shown, indicating invalid characters in assignment name.
    2. Test case: `add_assignment si/1 an/ValidName ms/-10`<br>
       Expected: No assignment is added. Error message shown, indicating invalid score format.

### Marking and unmarking an assignment

1. **Marking an assignment as submitted**
    1. Prerequisites: The student list includes at least one student with at least one assignment.
    2. Test case: `mark si/1 ai/1`<br>
       Expected: The first assignment for the first student is marked as submitted. Success message displayed.

2. **Unmarking an assignment as submitted**
    1. Test case: `unmark si/1 ai/1`<br>
       Expected: The submission status for the first assignment of the first student is reset. Success message displayed. Note that if there was a grade assigned to the assignment previously, it will be reset. 

### Grading an assignment

1. **Grading an assignment with valid score**
    1. Prerequisites: The student list includes at least one student with at least one assignment.
    2. Test case: `grade si/1 ai/1 s/80`<br>
       Expected: The score of the first assignment for the first student is set to 80. Success message displayed. Note that if there was already an assigned grade to the assignment, it will be overwritten.

2. **Grading an assignment with invalid score**
    1. Test case: `grade si/1 ai/1 s/300` (assuming max score is 100)<br>
       Expected: No changes made. Error message displayed indicating the score is out of bounds.

### Clearing all data

1. **Clearing all entries**
    1. Test case: `clear`<br>
       Expected: All students and assignments are removed from the app. Success message shown, indicating the data has been cleared.

### Viewing help

1. **Displaying help message**
    1. Test case: `help`<br>
       Expected: A help window is displayed, showing a link to the user guide where detailed usage instructions are provided.

2. **Testing help command in different contexts**
    1. Prerequisites: The app is open and a student list is displayed.
    2. Test case: Execute `help` while in the main screen or after performing other commands like `list` or `find`.<br>
       Expected: Help window is shown, and the app remains in the current state without affecting the student list.
