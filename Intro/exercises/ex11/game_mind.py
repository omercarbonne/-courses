import boggle_board_randomizer
import ex11_utils


def read_dict(path: str):
    """return a set of all the valid words in the given file path"""
    with open(path, "r") as file:
        return {line.strip() for line in file}


BOOGLE_WORD_SET = read_dict("boggle_dict.txt")


class BoggleModel():
    """
    check_word - check if the current word valid
    apply_word - update the game acording to the current word
    get_word - return the current word
    reset_word_and_path - reset the current word and path
    is_neighbour - check if 2 given loc are neighbours
    update_word_and_path - update word and path acoording to given values
    get_char_on_board_lst - return a list with all the chars on the board
    """

    def __init__(self):
        self.__board = boggle_board_randomizer.randomize_board()
        self.__score = 0
        self.__current_word = ""
        self.__current_path = []
        self.__found_words = set()

    def check_word(self):
        """check if the current word valid"""
        if self.__current_word not in self.__found_words:
            if self.__current_word in BOOGLE_WORD_SET:
                return True
        return False

    def apply_word(self):
        self.__update_score(len(self.__current_path))
        self.__found_words.add(self.__current_word)

    def get_word(self):
        return self.__current_word

    def reset_word_and_path(self):
        self.__current_path = []
        self.__current_word = ""

    def is_neighbour(self, loc: tuple[int, int]):
        """check if 2 given loc are neighbours"""
        if self.__current_path:
            current_loc = self.__current_path[-1]
            return ex11_utils.is_neighbour(current_loc, loc)
        return True

    def update_word_and_path(self, char: str, loc: tuple[int, int]):
        self.__current_word += char
        self.__current_path.append(loc)

    def __update_score(self, path_len: int):
        self.__score += path_len**2

    def get_score(self):
        return self.__score

    def get_board(self):
        return self.__board

    def get_char_on_board_lst(self):
        """return a list with all the chars on the board"""
        char_lst = []
        for i in range(len(self.__board)):
            for j in range(len(self.__board[0])):
                char_lst.append(self.__board[i][j])
        return char_lst
