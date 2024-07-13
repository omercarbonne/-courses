#################################################################
# FILE : ex3.py
# WRITER : Omer Carbonne , omercarbonne , 209193523
# EXERCISE : intro2cs1 ex1 2023
# DESCRIPTION: EX3
#################################################################


def input_list():
    """ This function gets numbers from the user, until he enter an empty string,
    and returns a list of all the numbers + the sum of them at the end"""
    user_input = input()
    numbers_list = []
    while user_input != "":  # while the user doesn't enter an empty string
        numbers_list.append(float(user_input))
        user_input = input()
    if numbers_list is []:
        numbers_list = [0]
    else:  # sum all the numbers in the list and add it to the end of the list
        numbers_sum = 0
        for num in numbers_list:
            numbers_sum += num
        numbers_list.append(numbers_sum)
    return numbers_list


def inner_product(vec_1, vec_2):
    """This function gets two vectors and returns their inner product"""
    if len(vec_1) != len(vec_2):
        return None
    elif len(vec_1) == 0:
        return 0
    else:
        inner_sum = 0
        for i in range(len(vec_1)):
            inner_sum += (vec_1[i] * vec_2[i])
        return inner_sum


def sequence_monotonicity(sequence):
    """This function checks the monotonicity of a given sequence"""
    answer = [True, True, True, True]
    if len(sequence) > 2:
        num = sequence[0]
        for arg in sequence[1:]:  # starts from the second argument because num is already the first one.
            if arg > num:
                answer[2:] = [False, False]
            elif arg == num:
                answer[1] = False
                answer[3] = False
            elif arg < num:
                answer[0:2] = [False, False]
            if sequence == [False, False, False, False]:
                break
            num = arg
    return answer


def monotonicity_inverse(def_bool):
    """This function gets a describe of a monotonicity sequence and returns an example of one"""
    if def_bool == [False, False, False, False]:
        return [1,2,4,2]
    elif def_bool == [True, False, True, False]:
        return [1, 1, 1, 1]
    elif def_bool == [True, False, False, False]:
        return [1, 2, 2, 4]
    elif def_bool == [True, True, False, False]:
        return [1, 2, 3, 4]
    elif def_bool == [False, False, True, True]:
        return [4, 3, 2, 1]
    elif def_bool == [False, False, True, False]:
        return [4, 3, 3, 1]
    else:
        return None


def calculate_9_indexes(table, start):
    """This function gets a table (list of lists), and calculate the sum of the nine arguments,
    when the starting point is the upper left corner"""
    sum = 0
    for i in range(start[0], start[0]+3):
        for j in range(start[1], start[1]+3):
            sum += table[i][j]
    return sum


def convolve(mat):
    """This function convolve a given matrix and returns the result"""
    if not mat:
        return None
    convolve_answer = []
    for j in range(0, len(mat)-2):
        for i in range(0, len(mat[0])-2):
            if i == 0:
                convolve_answer.append([calculate_9_indexes(mat, [j, i])])
            else:
                convolve_answer[j].append(calculate_9_indexes(mat, [j, i]))
    return convolve_answer


def sum_of_vectors(vec_list):
    """This function gets a list of vectors and returns the sum of them"""
    if not vec_list:
        return None
    if not vec_list[0]:
        return []
    vec_sum = []
    for j in range(len(vec_list[0])):
        temp_sum = 0
        for i in range(len(vec_list)):
            temp_sum += vec_list[i][j]
        vec_sum.append(temp_sum)
    return vec_sum


def num_of_orthogonal(vectors):
    num_of_couples = 0
    for i in range(0, len(vectors)):
        for j in range(i+1, len(vectors)):
            if inner_product(vectors[i], vectors[j]) == 0:
                num_of_couples += 1
    return num_of_couples

