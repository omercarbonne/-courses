import math

def golden_ratio():
    """This function prints the golden ratio"""
    print((math.sqrt(5)+1)/2)

def six_squared():
    """This function prints the value of six squared"""
    print(math.pow(6,2))

def hypotenuse():
    """This function prints the length of the hypotenuse in triangle it's sides lengths are 5,12"""
    print(math.hypot(5,12))

def pi():
    """This function prints the value of pi"""
    print(math.pi)

def e():
    """This function prints the value of e"""
    print(math.e)

def squares_area():
    """This function prints the squares areas from 1-10"""
    print(1**2, 2**2, 3**2, 4**2, 5**2, 6**2, 7**2, 8**2, 9**2, 10**2)


if __name__ == "__main__" :
    golden_ratio()
    six_squared()
    hypotenuse()
    pi()
    e()
    squares_area()