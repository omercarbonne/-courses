import copy

import helper
from copy import deepcopy

INPUT_MESSAGE = "Please enter top coordinate for ship of size {}: "
NOT_VALID_LOCATION_MESSAGE = "The location is not valid, try again"
FIRING_MESSAGE = "Choose a coordinate to fire at: "
WELCOMING_MESSAGE = """WELCOME TO BATTLESHIP!!!
Please locate your ships on your board: """
STARTING_MESSAGE = "Now it's time to fire!!!"
WINNING_MESSAGE = "CONGRATUKATIONS!!! YOU WON!!!\n"
LOSING_MESSAGE = "YOU LOST :("
NEW_GAME_MESSAGE = "Would you like to play again? type 'y' for yes or 'n' for no"
BOARDS_HEADLINES = "   Your Board                   Rival's Board"
VALID_ANSWERS = ['y', 'Y', 'N', 'n']


def init_board(rows, columns):
    new_board = []
    for i in range(rows):
        new_board.append([])
        for j in range(columns):
            new_board[i].append(helper.WATER)
    return new_board


def is_in_board(board, loc):
    if 0 <= loc[0] < len(board) and 0 <= loc[1] < len(board[0]):
        return True
    else:
        return False


def cell_loc(loc):
    col = ord(loc[0].upper())-64
    if helper.is_int(loc[1:]):
        row = int(loc[1:])
        if  not ((col > helper.NUM_COLUMNS or col < 1) or (row > helper.NUM_ROWS or row < 1)):
            return row-1, col-1
    return None


def valid_ship(board, size, loc):
    if (len(board[0]) <= loc[1]) or loc[1] < 0:
        return False
    elif (len(board) - size < loc[0]) or loc[0] < 0:
        return False
    else:
        for i in range(size):
            if board[loc[0]+i][loc[1]] == helper.SHIP:
                return False


        """for cell in board[loc[0]][loc[1]:loc[1] + 5]:
            if cell == helper.SHIP:
                return False"""
    return True


def update_board(board, loc, size):
    """This function gets a board, a location for a ship and a size, and returns an updated board
    with the new ship in it. The  function assumes the input is valid"""
    new_board = board
    for i in range(size):
        new_board[loc[0] + i][loc[1]] = helper.SHIP
    return new_board


def create_player_board(rows, columns, ship_sizes):
    b = init_board(rows, columns)
    for ship in ship_sizes:
        helper.print_board(b)
        user_input = helper.get_input(INPUT_MESSAGE.format(str(ship)))
        while True:
            if cell_loc(user_input) is not None:
                user_input = cell_loc(user_input)
                if valid_ship(b, ship, user_input):
                    break
            print(NOT_VALID_LOCATION_MESSAGE)
            user_input = helper.get_input(INPUT_MESSAGE.format(str(ship)))
        b = update_board(b,user_input,ship)
    return b


def fire_torpedo(board, loc):
    if not is_in_board(board,loc):
        return board
    if board[loc[0]][loc[1]] == helper.WATER:
        board[loc[0]][loc[1]] = helper.HIT_WATER
    elif board[loc[0]][loc[1]] == helper.SHIP:
        board[loc[0]][loc[1]] = helper.HIT_SHIP
    return board


def valid_locs_ships(board, water, size):
    valid_locs = set()
    for cell in water:
        if valid_ship(board, size, cell):
            valid_locs.add(cell)
    return valid_locs


def water_locs(board):
    water = set()
    for i in range(len(board)):
        for j in range(len(board[0])):
            if board[i][j] == helper.WATER:
                water.add((i,j))
    return water


def create_rival_board():
    b = init_board(helper.NUM_ROWS, helper.NUM_COLUMNS)
    for ship in helper.SHIP_SIZES:
        w = water_locs(b)
        locs = valid_locs_ships(b, w, ship)
        rival_choice = helper.choose_ship_location(b, ship, locs)
        b = update_board(b, rival_choice, ship)
    return b


def hide_board(board):
    hidden_board = copy.deepcopy(board)
    for i in range(len(board)):
        for j in range(len(board[0])):
            if board[i][j] == helper.SHIP:
                hidden_board[i][j] = helper.WATER
    return hidden_board


def user_turn(user_board, rival_board):
    hidden_rival_board = hide_board(rival_board)
    helper.print_board(user_board, hidden_rival_board)
    while True:
        user_input = helper.get_input(FIRING_MESSAGE)
        if cell_loc(user_input) is not None:
            user_input = cell_loc(user_input)
            fire_torpedo(rival_board,user_input)
            return rival_board
        print(NOT_VALID_LOCATION_MESSAGE)


def rival_turn(user_board):
    options = water_locs(hide_board(user_board))
    target = helper.choose_torpedo_target(hide_board(user_board), options)
    return fire_torpedo(user_board ,target)


def is_fleet_destroyed(board):
    for row in board:
        for cell in row:
            if cell == helper.SHIP:
                return False
    return True


def main():
    while True:
        user_board = create_player_board(helper.NUM_ROWS, helper.NUM_COLUMNS, helper.SHIP_SIZES)
        rival_board = create_rival_board()
        result = None
        while True:
            rival_board = user_turn(user_board, rival_board)
            user_board = rival_turn(user_board)
            if is_fleet_destroyed(user_board):
                result = LOSING_MESSAGE
                break
        helper.print_board(user_board, rival_board)
        user_answer = helper.get_input(result+NEW_GAME_MESSAGE)
        while user_answer not in VALID_ANSWERS:
            print("The input is not valid")
            user_answer = helper.get_input(NEW_GAME_MESSAGE)
        if user_answer == 'n' or user_answer == 'N':
            break


if __name__ == "__main__":
    main()




