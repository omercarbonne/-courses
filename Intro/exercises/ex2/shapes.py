import math


def circle_area(radius):
    """returns the area of circle with given radius """
    return (radius**2)*math.pi


def rectangle_area(side1, side2):
    """returns the area of rectangle with given sides"""
    return side1*side2


def triangle_area(side):
    """The function returns the area of triangle with given side"""
    return (math.sqrt(3)*(side**2))/4


def shape_area():
    """This function gets a user choice of a shape (1=circle, 2=rectangle, 3=triangle),
     then get the sizes needed for the calculate, and at last return the area of the shape """
    user_choice = input("Choose shape (1=circle, 2=rectangle, 3=triangle): ")
    if user_choice == "1":  # Calculate the area of circle
        return circle_area(float(input()))

    elif user_choice == "2":  # Calculate the area of rectangle
        return rectangle_area(float(input()), float(input()))

    elif user_choice == "3":  # Calculate the area of triangle
        return triangle_area(float(input()))

    else:
        return None

