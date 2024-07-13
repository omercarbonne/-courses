#################################################################
# FILE : hello_turtle.py
# WRITER : Omer Carbonne , omercarbonne , 209193523
# EXERCISE : intro2cs1 ex1 2023
# DESCRIPTION: A simple program that draws a fleet of ships with Turtle
# WEB PAGES I USED: https://docs.python.org/3/library/math.html
#################################################################

import turtle

def draw_triangle():
    """this function draws a triangle"""
    turtle.down()
    turtle.forward(45)
    turtle.right(120)
    turtle.forward(45)
    turtle.right(120)
    turtle.forward(45)
    turtle.right(120)

def draw_sail():
    """This function draws a sail"""
    turtle.left(90)
    turtle.forward(50)
    turtle.right(150)
    draw_triangle()
    turtle.right(30)
    turtle.up()
    turtle.forward(50)
    turtle.down()
    turtle.left(90)

def draw_ship():
    """This function draws a ship"""
    turtle.forward(50)
    draw_sail()
    turtle.forward(50)
    draw_sail()
    turtle.forward(50)
    draw_sail()
    turtle.forward(50)
    turtle.right(120)
    turtle.forward(20)
    turtle.right(60)
    turtle.forward(180)
    turtle.right(60)
    turtle.forward(20)
    turtle.right(30)

def draw_fleet():
    """This function draws a fleet of two ships"""
    draw_ship()
    turtle.up()
    turtle.left(90)
    turtle.forward(300)
    turtle.right(180)
    turtle.down()
    draw_ship()
    turtle.up()
    turtle.right(90)
    turtle.forward(300)

if __name__ == '__main__' :
    draw_fleet()
    turtle.done()




