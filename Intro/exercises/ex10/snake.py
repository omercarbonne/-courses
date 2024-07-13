class Snake:
    """
    init- create new snake object:
            size - the snake size(int)
            additional_cells - future cells that need to be added
                               in next move
            body - the snake body represnt by list of location(tuples[int,int])
            future_cut - represent the index that need to be cut in
                        the next move if given
    remove_tail - remove the last index of the snake body
    move_snake - move the snake one cell forword according to a given cell
    future_cut - set the index that need to be cut next move
    __cut_snake - cuts the snake acording to a given index
    add_additional_cells - sets the parameter of new additional cells to be added
    get_snake_loc - return the snake body (list of tuples[int,int])
    get_snake_len - return the snake length
    check_snake_above_tail - check if the head of the snake cross the body
    """

    __DIFAULT_SIZE = 3

    def __init__(self, loc, size: int = __DIFAULT_SIZE):
        self.__size = size
        self.__additional_cells = 0
        self.__body = [(loc[0], loc[1]-i) for i in range(self.__size)]
        self.__future_cut = -1

    def remove_tail(self):
        # delete the snake head
        del self.__body[-1]

    def move_snake(self, new_cell: tuple):
        # move the snake one cell forword according to the given cell
        self.__body[0:0] = [new_cell]
        # check if need to add new body cells to the snake
        if self.__additional_cells <= 0:
            del self.__body[-1]
        else:
            self.__additional_cells -= 1
        # check if need to cut the snake
        if self.__future_cut != -1:
            self.__cut_snake(self.__future_cut)
            self.__future_cut = -1

    def future_cut(self, index):
        self.__future_cut = index

    def __cut_snake(self, index: int):
        self.__body = self.__body[:index]

    def add_additional_cells(self, add_cells: int = 3):
        # adding new cells to the snake in add_cells future turns
        self.__additional_cells += add_cells

    def get_snake_loc(self):
        # return list of tuples with the snake locations
        return self.__body

    def get_snake_len(self):
        return len(self.__body)

    def check_snake_above_tail(self):
        # check if the snake is above itslef
        if self.__body[0] in self.__body[1:]:
            return True
        return False
