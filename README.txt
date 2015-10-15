Hunt the Wumpus
CS664
Team Members: Jan Horjus, Terry O'Neill (ton@bu.edu) ADD YOUR NAMES HERE


 1.) How to build and test
     TODO: Fill in this section. 

 2.) Description of the Algorithm used to solve
     A.) Keep track of the properties of previously visited squares.
     B.) Have a function which can navigate a safe path (action sequence) to an unvisited square which is adjacent 
         to a visited square (hereafter defined as an "edge square") without going through any other unvisited squares.
     C.) Have a function which determines which edge square to explore next. 
         i.) It should prefer the closest available edge square which can be proven safe. 
         ii.)How do you know which squares are safe?
             a.) If any edge square is adjacent to at least one non-breezy and at least one non-smelly square it is 
                 safe.
             b.) If more than one stench square is visited, any square adjacent to at least one non-breezy square 
                 and less than all known stench squares is safe.
             c.) It seems very hard to know much more than this, but you mostly don't need to. 
     D.) Continue exploring safe edge squares until you find the gold or no safe edge squares remain.
     E.) If no safe edge squares remain and the gold is still not found, use the arrow to proceed.  
         i.) If more than one stench square is visited, stand in one of them and fire the arrow into the un-visited 
             square adjacent to the others. Then proceed to explore the dead-wumpus square and keep looking for 
             the gold as before.
         ii.) If only one stench square is visited and it has multiple unvisited squares adjacent to it, pick one at
              random and fire the arrow into it. Then proceed to explore the square you fired the arrow into, and 
              continue exploring as before. 
         iii.) If the wumpus is dead, the "safe square" calculations become a bit simpler.  
     F.) If, after using the arrow, you again encounter a situation in which no safe edge squares remain and you 
         have not yet found the gold, throw an exception! The game is always supposed to be solvable, so either your 
         map is bad or your solving algorithm is.
     G.) Obviously, once you find the gold pick it up. Then stop exploring and back-track to the exit.
     
===
Notes for discussion from Terry on the above:

I think we make the following assumptions:
- the board is setup so that the AI agent can win through some series of actions.

Q: I am unsure what is the difference between and 'edge' square and an 'adjacent' square above? Is an 'edge' square one that is unvisited?
A: Yes, the definition of "edge square" is "an unvisited square which is adjacent to a visited square". Visited squares we know fully, 
   and unvisited squares 

I think there are two goals for the AI Agent in the game:
  1) get the gold and get out safe
  2) maximize points.

Regarding point 'E.i' above, I don't think the AI Agent  would be maximizing points unless all of the smelly squares have been visited 
before firing the arrow at the wumpus. It is -1 point for each action taken, and -10 points for using the arrow, +1000 for climbing 
out alive, and -1000 for falling into the pit or being eaten by the wumpus. Given this, it would take a maximum of 3 moves 
(-3 points) to figure out where the Wumpus is vs shooting for it (-10 points). I think the only instance in which the arrow 
need be used is if you entered the game at some cell that is in a corner and that entry cell is immediately smelly. 
Since the only two moves you can make may contain the Wumpus, you would have to shoot and listen for the scream in order to 
determine the next move.
A: Right again - using the arrow is never considered unless there are no remaining safe edge squares - meaning that using the arrow is 
   the only alternative to entering an unsafe square and risking death.  



