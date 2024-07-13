import helper
from car import Car
from board import Board
import sys


class Game:
    """
    Add class description here
    """

    def __init__(self, board):
        """
        Initialize a new Game object.
        :param board: An object of type board
        """
        self.__board = board
        self.__target_location = board.target_location()
        self.__GET_INPUT_MESSAGE = 'PLEASE ENTER A NAME OF A CAR AND A DIRECTION, IN THIS PATTERN: NAME,DIRECTION: '
        self.__NOT_VALID_MOVEMENT_MESSAGE = 'THE MOVEMENT IS NOT POSSIBLE, PLEASE TRY AGAIN'
        self.__NOT_EXISTING_NAME_MESSAGE = 'THE NAME IS NOT EXIST, PLEASE TRY AGAIN: '
        self.__NOT_VALID_PATTERN_MESSAGE = 'YOUR INPUT IS NOT IN THE VALID PATTERN, PLEASE TRY AGAIN: '
        self.__WELCOMING_MESSAGE = "WELCOME TO THE GAME RUSH HOUR!!! ENJOY!"
        self.__EXITING_MESSAGE = 'ARE YOU SURE YOU WANT TO EXIT? y/n: '
        self.__WINNING_MESSAGE = 'YOU WON!!'
        self.__status = 'p'  # p is playing, w is win, e is exit

    def __get_valid_input(self):
        """
        this function asks for input from the user until the input is valid.
        :return: the valid input as a tuple of two parameters (name, direction)
        """
        user_input = input(self.__GET_INPUT_MESSAGE).split(',')
        while True:  # checks the input has 2 parts separated with ','
            if user_input[0] == '!':  # if the user wants to exit
                return None, None  # two Nones because the function supposed to return tuple
            if len(user_input) == 2:
                if user_input[1] in ['u', 'r', 'l', 'd']:
                    break
            user_input = input(self.__NOT_VALID_PATTERN_MESSAGE).split(',')
        return user_input[0], user_input[1]

    def __single_turn(self):
        """
        The function runs one round of the game :
            1. Get user's input of: what color car to move, and what 
                direction to move it.
            2. Check if the input is valid.
            3. Try moving car according to user's input.

        Before and after every stage of a turn, you may print additional 
        information for the user, e.g., printing the board. In particular,
        you may support additional features, (e.g., hints) as long as they
        don't interfere with the API.
        """
        print(self.__board)
        name, move_key = self.__get_valid_input()  # makes sure the input is valid
        while True:  # checks if the car movement is valid
            if name is None:
                self.__status = 'e'
                break
            if self.__board.move_car(name, move_key):
                break
            print(self.__NOT_VALID_MOVEMENT_MESSAGE)
            name, move_key = self.__get_valid_input()

    def __exiting(self):
        user_input = input(self.__EXITING_MESSAGE)
        if user_input == 'y':
            return True
        return False

    def play(self):
        """
        The main driver of the Game. Manages the game until completion.
        :return: None
        """
        print(self.__WELCOMING_MESSAGE)
        while True:  # keeps running till the game is over (winning or exiting)
            self.__single_turn()
            if self.__status == 'e':
                if self.__exiting():
                    break
                self.__status = 'p'  # if the user wants to keep playing
            if self.__board.cell_content(self.__target_location) is not None:  # which means there is a car there
                self.__status = 'w'
                break
        if self.__status == 'w':
           print(self.__WINNING_MESSAGE)
           input('Type anything to exit')


def create_board_with_cars_dict(cars_dict):
    """
    this function creates a board which contains all the cars from the cars_dict.
    if a car is not in valid location it won't enter to the board
    :param cars_dict:
    :return: board with cars in it
    """
    car_list = []
    board = Board()
    for name, details in cars_dict.items():
        length, location, orientation = details[0], details[1], details[2]
        car_list.append(Car(name, length, location, orientation))
    for car in car_list:
        board.add_car(car)
    return board


if __name__ == "__main__":
    #url_path = sys.argv[1]
    url_path = 'car_config.json'
    cars_dict = helper.load_json(url_path)
    new_board = create_board_with_cars_dict(cars_dict)
    new_game = Game(new_board)
    new_game.play()


