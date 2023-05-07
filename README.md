
# Sine of Madness with Slick2D

A platformer game built for our 2nd semester Object Oriented Programming course.

## Prerequires (Libraries included in libs folder)

1. Slick2D library
2. Java 8+
3. LWJGL
4. An IDE that supports java

# Include Libraries in Intellij IDEA
1. Go to the File menu
2. Click project structure
3. On the left side of the window click libraries
4. Then click the + button to add a library
5. Choose java
6. A file explorer should open
7. Navigate into the project folder
8. You should see the libs folder, open that
9. It should have two folders, natives and jars
10. Include all four .jar files (simply click them and click OK)
11. From the natives folder choose the folder with the same name as your operating system
12. Click OK and then click apply
13. Finally, right click the Game class and click run

# Include libraries in Eclipse
1. On the left side of the eclipse window there would be the project that you have opened
2. Right click it and then select Properties
3. Under the search box there would be a Java Build Path option
4. Select the option and then click Libraries which is in the middle of the window 
5. Click the LWJGL jar and expand it
6. Select Native library location
7. By default, this option would have "none" written in parentheses. 
8. Click the option and then click "edit" which is on the right side of the window
9. A new window will open asking the location path. Click on workspace which is on the right side of the text field
10. Click on the project name and then click on lib and then click natives
11. The folder should have folders with different operating system names. Choose the one with the same name as your OS
12. Don't expand that folder just click on it and then press ok 
13. The path will be specified so click ok on the previous windows
 
## How to Play
1. Use the left and right arrow keys to move the character
2. Use the up arrow to jump
3. The space bar to use the attack move
4. You can use the escape key to pause the game and resume, go back to the main menu or quit the game

## Screenshots
### Main menu
![image](https://user-images.githubusercontent.com/67051265/168488169-79b6fdec-fae4-42dd-9e0d-e8dadadecd24.png)
### Info
![image](https://user-images.githubusercontent.com/67051265/168488275-c3ec7ea5-93f3-4d41-ab11-952bc5fee127.png)
### Level 1
![image](https://user-images.githubusercontent.com/67051265/168488201-987a20dc-e664-4f41-bc39-13107e67dc04.png)
### Level 2
![image](https://user-images.githubusercontent.com/67051265/168488251-47ecc220-6e2c-4c87-8855-8c1df74d564a.png)
### Level 3 (Boss)
![image](https://user-images.githubusercontent.com/67051265/168488310-49978532-666c-4b3e-8eb5-7dbd7c1752f3.png)

## Notes
1. If the game is too hard for you and you want to complete it still just go to the Bucky class and on line number 73 change the health to a huge number.
2. You can also go into the Game class and on line number 37 change the enter state to whatever you like.The game will start from that state.

## References

Slick2D Home
	
	[http://slick.ninjacave.com/](http://slick.ninjacave.com/)
