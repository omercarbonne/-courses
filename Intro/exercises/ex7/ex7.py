import ex7_helper as helper
import typing
from typing import Any
N = typing.TypeVar('N', int, float)


def mult(x: N,  y: int):
    if y == 1:
        return x
    else:
        return helper.add(x, mult(x, helper.subtract_1(y)))


def is_even(n: int):
    if n == 2:
        return True
    else:
        return not is_even(helper.subtract_1(n))


def log_mult(x: N, y: int):
    if y == 1:
        return x
    else:
        if helper.is_odd(y):
            y = helper.subtract_1(y)
            y = helper.divide_by_2(y)
            return helper.add(x, helper.add(log_mult(x, y), log_mult(x, y)))
        else:
            y = helper.divide_by_2(y)
            return helper.add(log_mult(x, y), log_mult(x, y))


def recursive_is_power(b: int, base: int, x: int):
    if b == x:
        return True
    if b > x:
        return False
    else:
        b = helper.add(b, base)
        return recursive_is_power(b, base, x)


def is_power(b: int, x: int):
    if b == 0:
        if x == 0:
            return True
        return False
    if x == 0:
        return False
    if x == 1:
        return True
    if b == 1:  # while x doesn't equals to 1
        return False
    return recursive_is_power(b, b, x)


def reverse_helper(s: str, index: int):
    if index == len(s)-1:
        return s[index]
    else:
        return helper.append_to_end(reverse_helper(s, index+1), s[index])


def reverse(s: str):
    return reverse_helper(s, 0)


def play_hanoi(hanoi: Any, n: int, src: Any, dest: Any, temp: Any):
    if n < 1:
        return None
    if n == 1:
        hanoi.move(src, dest)
    if n > 1:
        play_hanoi(hanoi,n-1, src, temp, dest)
        hanoi.move(src, dest)
        play_hanoi(hanoi, n-1, temp, dest, src)


def ones_in_number(n: int):
    if n == 0:
        return 0
    else:
        if n % 10 == 1:
            return 1 + ones_in_number(n//10)
        else:
            return ones_in_number(n//10)


def number_of_ones(n: int):
    if n == 0:
        return 0
    else:
        return ones_in_number(n) + number_of_ones(n-1)


def compare_1d_lists(l1: list[int], l2: list[int], index: int):
    if len(l1) == len(l2):
        if index == len(l1):
            return True
        else:
            if l1[index] == l2[index]:
                return compare_1d_lists(l1, l2, index+1)
    return False


def compare_2d_lists_helper(l1: list[list[int]], l2: list[list[int]], index: int):
    if len(l1) == len(l2):
        if index == len(l1):
            return True
        elif compare_1d_lists(l1[index], l2[index], 0):
            return compare_2d_lists_helper(l1, l2, index+1)
    return False


def compare_2d_lists(l1: list[list[int]], l2: list[list[int]]) -> bool:
    return compare_2d_lists_helper(l1, l2, 0)


def magic_list(n: int) -> list[Any]:
    if n == 0:
        return []
    lst = magic_list(n-1)
    lst.append(magic_list(n-1))
    return lst

