from typing import List, Tuple, Iterable, Optional

Board = List[List[str]]
Path = List[Tuple[int, int]]
Loc = Tuple[int, int]


def is_valid_path(board: Board, path: Path, words: Iterable[str]) -> Optional[str]:
    """check if given path is valid on the board"""
    if valid_path(path) and is_word_exist(board, path, words):
        return get_word_from_path(board, path)
    return None


def is_in_board(loc: Loc, board: Board):
    """check if a given location is on the board"""
    if -1 < loc[0] < len(board) and -1 < loc[1] < len(board[0]):
        return True
    return False


def is_word_exist(board: Board, path: Path, words: Iterable[str]) -> bool:
    """check if the word on board is in the words Iterable"""
    word = get_word_from_path(board, path)
    return word in words


def get_word_from_path(board: Board, path: Path) -> str:
    """return the word represent by the path on board"""
    word = ''
    for loc in path:
        y, x = loc
        word += board[y][x]
    return word


def valid_path(path: Path) -> bool:
    """check if path is valid"""
    loc_set = set()  # set with all the preivious loc of the path
    for index in range(1, len(path)):
        if not is_neighbour(path[index-1], path[index]):
            return False
        loc_set.add(path[index-1])
        if path[index] in loc_set:  # check if the loc is already in the path
            return False
    return True


def is_neighbour(loc1: Loc, loc2: Loc) -> bool:
    """checking if the two locations are neighbours"""
    if loc1 == loc2:
        return False
    for x in range(loc1[1]-1, loc1[1]+2):
        for y in range(loc1[0]-1, loc1[0]+2):
            if (y, x) == loc2:
                return True
    return False


def find_length_n_paths(n: int, board: Board, words: Iterable[str]) -> List[Path]:
    """finding all the paths in size n from the board"""
    all_path = []
    for y in range((len(board))):
        for x in range(len(board[0])):
            path = []
            _helper_find_lenth_n_paths(path, n, board, words, all_path, (y, x))
    return all_path


def get_loc_neighbours(loc: Loc, board: Board):
    """retrun all the neighbours cells in the board"""
    neighbours_lst = []
    for y in range(loc[0]-1, loc[0]+2):
        for x in range(loc[1]-1, loc[1]+2):
            if is_in_board((y, x), board) and (y, x) != loc:
                neighbours_lst.append((y, x))
    return neighbours_lst


def _helper_find_lenth_n_paths(path: Path, n: int, board: Board, words: Iterable[str], all_path: list, loc: Loc):
    path.append(loc)
    # stoping condition
    if len(path) == n:
        if is_word_exist(board, path, words):
            all_path.append(path[:])
    # recorsive loop
    else:
        for neighbour in get_loc_neighbours(loc, board):
            if neighbour not in path:
                _helper_find_lenth_n_paths(
                    path, n, board, words, all_path, neighbour)
                del path[-1]


def find_length_n_words(n: int, board: Board, words: Iterable[str]) -> List[Path]:
    """backtracking to find all the words length n on the board"""
    all_path = []
    for y in range((len(board))):
        for x in range(len(board[0])):
            path = []
            _helper_length_n_words(path, n, board, words, all_path, (y, x))
    return all_path


def _helper_length_n_words(path: Path, n: int, board: Board, words: Iterable[str], all_path: list, loc: Loc):
    path.append(loc)
    # stoping condition
    if len(get_word_from_path(board, path)) == n:
        if is_word_exist(board, path, words):
            all_path.append(path[:])
    # recorsive loop
    else:
        for neighbour in get_loc_neighbours(loc, board):
            if neighbour not in path:
                _helper_length_n_words(
                    path, n, board, words, all_path, neighbour)
                del path[-1]


def max_score_paths(board: Board, words: Iterable[str]) -> List[Path]:
    """return a list of paths that has the longest length for each word"""
    longest_paths = []
    for word in words:
        longest_path = longest_path_for_word(board, word)
        if longest_path is not None:
            longest_paths.append(longest_path)
    return longest_paths


def longest_path_for_word(board: Board, word: str) -> Path:
    """return the longest path for word"""
    start_loc = get_start_word_point(word, board)
    all_path_for_word = get_all_path_for_word(start_loc, board, word)
    if all_path_for_word != []:
        longest_path = []
        for path in all_path_for_word:
            if len(path) > len(longest_path):
                longest_path = path[:]
        return longest_path
    return None


def get_all_path_for_word(loc_lst: Path, board: Board, word: str) -> list:
    """return all paths for a given word"""
    all_path = []
    for loc in loc_lst:
        path = []
        if len(board[loc[0]][loc[1]]) == 2:
            _helper_get_all_path_for_word(path, all_path, board, word[2:], loc)
        else:
            _helper_get_all_path_for_word(path, all_path, board, word[1:], loc)
    return all_path


def _helper_get_all_path_for_word(path: Path, all_path: list, board: Board, word: str, loc: Loc):
    path.append(loc)
    # stop condition
    if word == '':
        all_path.append(path[:])
    else:
        # recorsive loop
        for neighbour in get_loc_neighbours(loc, board):
            if neighbour not in path:
                if len(board[neighbour[0]][neighbour[1]]) == 2:
                    if len(word) > 1:
                        if board[neighbour[0]][neighbour[1]] == word[:2]:
                            _helper_get_all_path_for_word(
                                path, all_path, board, word[2:], neighbour)
                            del path[-1]
                else:
                    if board[neighbour[0]][neighbour[1]] == word[0]:
                        _helper_get_all_path_for_word(
                            path, all_path, board, word[1:], neighbour)
                        del path[-1]


def get_start_word_point(word: str, board: Board):
    """return all the starting point on board for given word"""
    start_loc = []
    for y in range(len(board)):
        for x in range(len(board[0])):
            if len(board[y][x]) == 2:
                if word[:2] == board[y][x]:
                    start_loc.append((y, x))
            else:
                if word[0] == board[y][x]:
                    start_loc.append((y, x))
    return start_loc
