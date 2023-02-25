# WordUsher Prototype
Represents the original prototype for WordUsher built utilizing the console as the user interface. The project came more as a passion for figuring out how to use the console as a display for more than just text output. You play as the letter "P" tasked with putting, at first glance, a random assortment of letters into a particular word.

# Background
Stages can be as big as you want. All you have to do is modify the code in Main.java. A common problem with this version of the game, however, is that it's unable to solve the word sometimes when letters repeat. The algorithm used here is not as sophisticated as the one in the new version. Still, I think this version has its charm to it. Specifically, I love how it uses text to convey information on the screen and the underlying workings behind that.
```diff
- YOU ARE THE LETTER P
```

# Controls
### Keyboard
To enter an input, please press the Enter key afterward.
- W, UP
- A, LEFT
- S, DOWN
- D, RIGHT

<p align="center">
  <img src="https://filedn.com/lDA0b4tihedFKO8BLVKcEU7/wordusher-prototype-ascii/imgs/MOVEMENT.gif" />
</p>

# Push Objects
Your character can interact with all objects around it! One of these interactions is pushing. To push, move your character into another entity in front of it. Not only that, but when that entity interacts with another entity, it will do the same!
####
<p align="center">
  <img src="https://filedn.com/lDA0b4tihedFKO8BLVKcEU7/wordusher-prototype-ascii/imgs/PUSHING.gif" />
</p>

# Grabbing Objects
- G, GRAB 
####
To choose an object to grab, utilize the grab control and the direction you want to go. For example:

```
g w
```

The object will then stick to your character's letter. You can continue to move around with it, but it will stay in the direction your character held it.
####
<p align="center">
  <img src="https://filedn.com/lDA0b4tihedFKO8BLVKcEU7/wordusher-prototype-ascii/imgs/GRAB.gif" />
</p>

When you no longer want to hold on to an object, let go.
- L, LET GO
```
l
```
<p align="center">
  <img src="https://filedn.com/lDA0b4tihedFKO8BLVKcEU7/wordusher-prototype-ascii/imgs/LETTING_GO.gif" />
</p>

# Solving the Word
Once you figure out the order of the letters and guess the correct word, you'll see a victory prompt!

<p align="center">
  <img src="https://filedn.com/lDA0b4tihedFKO8BLVKcEU7/wordusher-prototype-ascii/imgs/SOLVING_WORD.gif" />
</p>
