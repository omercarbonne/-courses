import game_utils


class Wall:
    """an object of wall:
    init- creating a new object with the direction of the wall
          and the location of the wall(list of tuples)
    get_loc- return a list of tuples representing the wall location
    move_wall- moving the wall one step forword acording to the direction
    delete_cell - delete one cell of the wall in the given index
    """

    def __init__(self):
        x, y, self.__direction = game_utils.get_random_wall_data()
        # create the wall acording to the direction and the center of the wall
        if self.__direction == game_utils.UP or self.__direction == game_utils.DOWN:
            self.__full_wall = [(x, y+1), (x, y), (x, y-1)]
        else:
            self.__full_wall = [(x+1, y), (x, y), (x-1, y)]

    def get_loc(self):
        return self.__full_wall

    def move_wall(self):
        new_loc = []
        if self.__direction == game_utils.UP:
            for cell in self.__full_wall:
                new_loc.append((cell[0], cell[1]+1))
        elif self.__direction == game_utils.DOWN:
            for cell in self.__full_wall:
                new_loc.append((cell[0], cell[1]-1))
        elif self.__direction == game_utils.LEFT:
            for cell in self.__full_wall:
                new_loc.append((cell[0]-1, cell[1]))
        elif self.__direction == game_utils.RIGHT:
            for cell in self.__full_wall:
                new_loc.append((cell[0]+1, cell[1]))
        self.__full_wall = new_loc

    def delete_cell(self, index):
        del self.__full_wall[index]
