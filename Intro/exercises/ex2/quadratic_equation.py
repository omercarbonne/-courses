import math


def quadratic_equation(a, b, c):
    """This function gets the values of quadratic equation and returns the results:
    two solutions: (solution1, solution2)
    one solution: (solution1, None)
    none: (None, None)"""
    solution1 = None
    solution2 = None
    if b**2 - 4*a*c > 0:  # Two solutions
        solution1 = (-b+math.sqrt(b**2 - 4*a*c))/2*a
        solution2 = (-b-math.sqrt(b**2 - 4*a*c))/2*a
    if b**2 - 4*a*c == 0:  # One solution
        solution1 = (-b+math.sqrt(b**2 - 4*a*c))/2*a
    return solution1, solution2


def quadratic_equation_user_input():
    """This function gets quadratic equation from the user and returns the results"""

    par_a, par_b, par_c = input("Insert coefficients a, b, and c: ").split(" ")
    par_a = float(par_a)
    par_b = float(par_b)
    par_c = float(par_c)
    if par_a == 0:
        print("The parameter 'a' may not equal 0")
    else:
        solution1, solution2 = quadratic_equation(par_a, par_b, par_c)
        if solution1 is not None and solution2 is not None:
            print("The equation has 2 solutions:", solution1, "and", solution2)
        elif solution1 is not None:
            print("The equation has 1 solution:", solution1)
        else:
            print("The equation has no solutions")
