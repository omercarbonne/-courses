import math

# I chose the forth input to be only minuses. to check if the function will work on that case
# The fifth are long numbers that hard to calculate (pi, e, sqrt(2)), to check if the function works with those numbers.


def largest_and_smallest(num1, num2, num3):
    """This function gets 3 numbers and returns a tuple of the biggest one and the smallest one"""
    max_num = num1
    min_num = num1

    if max_num < num2:  # This part checks which is the largest number and insert it into max_num
        max_num = num2
    if max_num < num3:
        max_num = num3

    if num2 < min_num:  # This part checks which is the smallest number and insert it into min_num
        min_num = num2
    if num3 < min_num:
        min_num = num3

    return max_num, min_num


def check_largest_and_smallest():
    """This function runs tests on the function largest_and_smallest() in five cases
    and returns True if all of them passes, else returns False"""

    if largest_and_smallest(17, 1, 6) != (17, 1):
        return False
    if largest_and_smallest(1, 17, 6) != (17, 1):
        return False
    if largest_and_smallest(1, 1, 2) != (2, 1):
        return False
    if largest_and_smallest(-3, -8, -5) != (-3, -8):
        return False
    if largest_and_smallest(math.pi, math.e, math.sqrt(2)) != (math.pi, math.sqrt(2)):
        return False
    return True
