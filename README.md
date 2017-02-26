-----------------------
Development environment
-----------------------
Java: Java(TM) SE Runtime Environment
Java Version: 1.8.0_91
OS Name: Mac OS X
OS Version: 10.11.4
OS Architecture: x86_64
Total Memory (MB): 61
Max Memory (MB): 910

----------------------
Command Line Arguments
----------------------
All source code files are in src folder
Compile: javac *.java
Run the game: java Main

Makefile: make - compile all java files
          make run - compile and run the program
          make clean - remove all .class files
--------------
Overall Design
--------------
Main: run the program.
Paint: the whole program’s window
ColorBar: a observer that controlling colour selection
LineThick: a observer that controlling line thickness
Model: the model dealing with notifying observers
View: main canvas that user will draw on
Tool_enum: defining tool type
PaintObject: defining objects that user draws
Dot: coordinate class for position

------------
Enhancements
------------
1. Scale shapes: the ability to change the scale/size of any shape by selecting it, then grabbing a corner of the shape, and dragging to increase/decrease it's size