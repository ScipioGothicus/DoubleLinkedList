****************
* Double-Linked List
* CS 221 with Mr. Mick
* 5/3/22
* Preston Hardy
**************** 

OVERVIEW:

 A custom doubly-linked list implementation of the IndexedUnsortedList class, 
 complete with a fully functional list iterator.


INCLUDED FILES:

 IUDoubleLinkedList.java - source file containing the doubly-linked list implementation of the IndexedUnsortedList, also including the list iterator subclass and corresponding constructors.
 ListTester.java - tester file for IUDoubleLinkedList. Tests iterator and doubly-linked list functionality.
 Node.java - source file containing the Node class; Node is the data type used by IUDoubleLinkedList.
 IndexedUnsortedList.java - source file containing the IndexedUnsortedList interface implemented by IUDoubleLinkedList.

COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac IUDoubleLinkedList.java

 Run the compiled class file with the command:
 $ java IUDoubleLinkedList

 Test IUDoubleLinkedList with the commands:
 $ javac ListTester.java
 $ java ListTester

 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 IUDoubleLinkedList is the file containing the doubly-linked list implementation of IndexedUnsortedList.
 It uses the data type Node to contain elements and link, ensuring optimal O(1) runtime when performing tasks such as
 adding or removing Nodes. The file contains the ListIterator subclass, which is a fully functional iterator, complete with
 add, remove, and set methods. IUDoubleLinkedList will function nearly the same as the standard Java doubly-linked list class.
 Nodes can be added or removed from the front, back, or at certain indexes with the provided methods. Certain methods also
 allow for searching elements to add or remove Nodes with matching elements. IUDoubleLinkedList also contains getter methods
 to check for indexes of Nodes or their elements.
 The Node class contains methods to get the next and previous nodes. While getting the previous node with the Node class is
 not necessary, functionality was added for convenience.

TESTING:

 The ListTester source file was used to test IUDoubleLinkedList and ensure complete functionality. Tests were run for both
 the base doubly-linked list along with the list iterator included in IUDoubleLinkedList. These tests would run all methods
 on sample lists and ensure that Nodes were being removed, added, set, and accounted for correctly in the IUDoubleLinkedList
 file. The program can handle concurrency modifications, illegal state exceptions, and out-of-bounds index parameters with
 included exception handlers. The program is designed to be idiot-proof, as all methods include these exceptions to ensure that
 the linked list will function as designed. 
 The removeLast method does not have good runtime, as it has to set the previous Node of the new tail Node. Since the base
 program does not account for previous nodes like the list iterator does, it must search through the entire list, find the
 previous Node of the new tail Node, and set it properly, eating up runtime.

DISCUSSION:

 In all honesty, this project was a total nightmare to complete. Developing this project completely required
 adding code in both the testing file and the source file for the doubly-linked list. During testing, I overlooked
 an error in my testing program which seemed to persist forever with any changes I made. The testing file was
 incorrectly checking the last element for some tests, causing a strange pattern of certain tests to fail. This took
 over two weeks to find and fix, as it was an issue with the previous single-linked list project.
 
 The remove method was also highly confusing to write. I failed to notice a bug within almost all of my methods that
 failed to determine if next or previous was called correctly. Sometimes both of these checks would result in true,
 causing the iterator to remove no elements and bring me great frustration and confusion. Eventually, with a lot of help
 from my instructor, I was able to find both the issues in the testing file and the iterator class and the program now
 passes all tests implemented.