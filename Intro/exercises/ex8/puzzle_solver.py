from typing import List, Tuple, Set, Optional


# We define the types of a partial picture and a constraint (for type checking).
Picture = List[List[int]]
Constraint = Tuple[int, int, int]


def max_seen_cells(picture: list[list[int]], row: int, col: int) -> int:
    if picture[row][col] != 0:
        max_seen = 1
    else:
        return 0

    y = row
    x = col
    while True:  # checks under the cell
        y += 1
        if y == len(picture):
            break
        if picture[y][x] == 0:
            break
        max_seen += 1

    y = row
    x = col
    while True:  # checks under the cell
        y -= 1
        if y < 0:
            break
        if picture[y][x] == 0:
            break
        max_seen += 1

    y = row
    x = col
    while True:  # checks the right side of the cell
        x += 1
        if x == len(picture[0]):
            break
        if picture[y][x] == 0:
            break
        max_seen += 1

    y = row
    x = col
    while True:  # checks the left side of the cell
        x -= 1
        if x < 0:
            break
        if picture[y][x] == 0:
            break
        max_seen += 1

    return max_seen


def min_seen_cells(picture: list[list[int]], row: int, col: int) -> int:
    if picture[row][col] == 1:
        min_seen = 1
    else:
        return 0

    y = row
    x = col
    while True:  # checks under the cell
        y += 1
        if y == len(picture):
            break
        if picture[y][x] != 1:
            break
        min_seen += 1

    y = row
    x = col
    while True:  # checks under the cell
        y -= 1
        if y < 0:
            break
        if picture[y][x] != 1:
            break
        min_seen += 1

    y = row
    x = col
    while True:  # checks the right side of the cell
        x += 1
        if x == len(picture[0]):
            break
        if picture[y][x] != 1:
            break
        min_seen += 1

    y = row
    x = col
    while True:  # checks the left side of the cell
        x -= 1
        if x < 0:
            break
        if picture[y][x] != 1:
            break
        min_seen += 1

    return min_seen


def check_one_constraint(picture: list[list[int]], constraint: tuple) -> int:
    """ the function checks only one constraint in a partial picture"""
    y = constraint[0]
    x = constraint[1]
    seen = constraint[2]
    max_seen = max_seen_cells(picture, y, x)
    min_seen = min_seen_cells(picture, y, x)
    if min_seen <= seen <= max_seen:
        if min_seen == seen == max_seen:
            return 1
        return 2
    return 0


def check_constraints(picture: list[list[int]], constraints_set: set[tuple[int, int, int]]) -> int:
    result = 1
    for constraint in constraints_set:
        temp_result = check_one_constraint(picture, constraint)
        if temp_result == 0:
            return 0
        if temp_result == 2 and result == 1:
            result = 2
    return result


def get_relevant_constraints(constraints_set: set[tuple[int, int, int]], y: int, x: int) -> set[tuple[int, int, int]]:
    new_cons = set()
    for con in constraints_set:
        if con[0] == y or con[1] == x:
            new_cons.add(con)
    return new_cons


def ok_to_color(picture: list[list[int]], constraints_set: set[tuple[int, int, int]], y: int, x: int, color: int) -> bool:
    """check if it's ok to color a cell in given color (1: white, 0: black) according to the constraints"""
    picture[y][x] = color
    relevant_constraints = get_relevant_constraints(constraints_set, y, x)
    if len(relevant_constraints) == 0:
        return True
    result = check_constraints(picture, relevant_constraints)
    picture[y][x] = -1  # returns it to the original value
    if result == 0:
        return False
    return True


def create_picture(y: int, x: int) -> [list[list[int]]]:
    new_picture = []  # creates an empty picture
    for i in range(y):
        new_picture.append([])
        for j in range(x):
            new_picture[i].append(-1)
    return new_picture


def solve_puzzle_helper(picture: [list[list[int]]], constraints_set: set[tuple[int, int, int]], index: int, result: [list[list[int]]] ) -> [list[list[int]]]:
    if index == len(picture)*len(picture[0]):
        answer = [x[:] for x in picture]
        result.append(answer)
        return
    if result != [] or result is None:  # if we already have a result (not empty list)
        return

    row, col = index // len(picture[0]), index % len(picture[1])
    if picture[row][col] != -1:
        solve_puzzle_helper(picture, constraints_set, index+1, result)
        return

    for color in range(2):
        if ok_to_color(picture, constraints_set, row, col, color):
            picture[row][col] = color
            solve_puzzle_helper(picture, constraints_set, index+1, result)
    picture[row][col] = -1


def solve_puzzle(constraints_set: set[tuple[int, int, int]], n: int, m: int) -> [list[list[int]]]:
    new_picture = create_picture(n, m)
    result = []
    solve_puzzle_helper(new_picture, constraints_set, 0, result)
    solution = ''
    for i in result:
        solution = i
    if solution == '':
        return None
    return solution


def how_many_solutions_helper(picture: [list[list[int]]], constraints_set: set[tuple[int, int, int]], index: int, result: []) -> [list[list[int]]]:
    if index == len(picture) * len(picture[0]):
        result[0] += 1
        return

    row, col = index // len(picture[0]), index % len(picture[0])
    if picture[row][col] != -1:
        solve_puzzle_helper(picture, constraints_set, index + 1, result)
        return

    for color in range(2):
        if ok_to_color(picture, constraints_set, row, col, color):
            picture[row][col] = color
            how_many_solutions_helper(picture, constraints_set, index + 1, result)
    picture[row][col] = -1


def how_many_solutions(constraints_set: set[tuple[int, int, int]], n: int, m: int) -> int:
    new_picture = create_picture(n, m)
    result = [0]
    how_many_solutions_helper(new_picture, constraints_set, 0, result)
    return result[0]


def generate_puzzle_helper(picture: list[list[int]], index: int, constraints: list[set[tuple]], answer: []):
    if constraints:
        num_of_solutions = how_many_solutions(constraints, len(picture), len(picture[0]))
    else:
        num_of_solutions = -1  # when constraints is empty
    if num_of_solutions == 1:
        temp_answer = set(constraints)
        make_solution_strict(len(picture), len(picture[0]), temp_answer)
        answer.append(temp_answer)
        return
    elif num_of_solutions == 0:
        return
    else:
        row, col = index // len(picture[0]), index % len(picture[0])
        new_constraint = (row, col, max_seen_cells(picture, row, col))
        constraints.add(new_constraint)
        generate_puzzle_helper(picture, index+1, constraints, answer)
        constraints.remove(new_constraint)


def get_one_not_necessary_con(picture: list[list[int]], constraints: list[set[tuple]]):
    """return one not necessary constraint for the solution"""
    row, col = len(picture), len(picture[0])
    for con in constraints:
        temp_cons = set(constraints)
        temp_cons.remove(con)
        if how_many_solutions(temp_cons, row, col) == 1:
            return con
    return None


def make_solution_strict(row: int, col: int, constraints: list[set[tuple]]):
    """makes sure the constraints are strict solution, and removes the not necessary ones"""
    picture = create_picture(row, col)
    not_necessary_con = get_one_not_necessary_con(picture, constraints)
    while not_necessary_con is not None:
        constraints.remove(not_necessary_con)
        not_necessary_con = get_one_not_necessary_con(picture, constraints)
    return constraints


def generate_puzzle(picture: list[list[int]]) -> set[tuple[int, int, int]]:
    constraints = set()
    answer = []
    generate_puzzle_helper(picture, 0, constraints, answer)
    return answer[0]

