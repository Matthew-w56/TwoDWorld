# TwoDWorld
 This is a project I did in High School where a player can wander and interact with a simple, boundless world.
 
 Quick story about the project.  Feel free to skip to the Notable Classes section.
 
 At the time, I was in High School, but taking a course at my local College for computer science.  Well I found myself with
 a lot of free time after doing the assignments, so I started this project out of curiosity.  My idea was pretty simple, I
 wanted to make a game where the world the player is in isn't limited.  In other words, the player can keep walking in a
 single direction and never hit a wall, or have their computer crash.
 
 So this program renders the world in terms of verticle slices, or sections, called chunks.  As the player walks around, new
 chunks are rendered and added onto the world dynamically.  When the player gets too far from a given chunk, it is assumed
 that that chunk won't be needed for a bit, and is cached in a stack until the player gets closer, allowing the computer to
 not have to worry about events going on in that area.
 
 ****************************************************************************************************************************
 Some notable classes to look at:<br>
 <ul>
 <li>ChunkManager<ul><li>
     The chunk manager is the largest single class, and what took the most creativity to code.  Since I was in High School
     when I wrote it and had little to no coding training beyond one base-level course in Java and a Codecademy.com
     course in Python, I had no idea how a dynamnic rendering system would look like, or how I would solve any problems
  that arose.  But after some time, I came up with the system that you'll see in this class.</li></ul></li>
 <li>Inventory
     <ul><li>Along with having a world and a player, I wanted to go further and so I added the ability to interact with the world.
     The Player can break things, and place things, storing the items temporarily in their inventory that can be
     interacted with, traversed, reordered, or added to.  The Inventory class gives a good bird's-eye view of how that
  sytsem was coded and is handled.</li></ul></li>
 <li>Menu
     <ul><li>I was having a hard time incorporating existing menu frameworks (such as JavaFX or Swing) into my project in a way that 
     seemed natural and that fit the rest of the program. So I coded my own simple framework for menus that I then 
     implimented for a few aspects to the game such as an exit menu when the player clicks escape, and a menu that allows the 
      player to create new items from the items they have already collected.</li></ul></li></ul><br>

Screenshots: 
1) Starting Screen
2) Exit Menu

![WorldStartShot](https://user-images.githubusercontent.com/26423158/161128988-4f7bd9f7-9a92-4f0f-a125-d9fdbce688c8.PNG)
![WorldExitMenuShot](https://user-images.githubusercontent.com/26423158/161129136-f0f21880-632b-4ab2-9e43-0853179a244e.PNG)
