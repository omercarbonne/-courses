def calculate_mathematical_expression(num1, num2, math_sign):
    """The function returns the result of mathematical expression of two given numbers and given mathematical action"""
    if math_sign == "+":
        return num1+num2
    elif math_sign == "-":
        return num1-num2
    elif math_sign == "*":
        return num1*num2
    elif math_sign == ":":
        if num2 == 0:
            return
        return num1/num2
    else:
        return


def calculate_from_string(math_string):
    """The function returns the result of mathematical expression"""
    num1, math_sign, num2 = math_string.split(" ")
    return calculate_mathematical_expression(float(num1), float(num2), math_sign)

